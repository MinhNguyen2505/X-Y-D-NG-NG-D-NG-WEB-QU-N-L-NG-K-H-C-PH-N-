package vn.edu.quanlyhocphan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.quanlyhocphan.entity.*;
import vn.edu.quanlyhocphan.enums.TrangThaiDangKy;
import vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan;
import vn.edu.quanlyhocphan.exception.*;
import vn.edu.quanlyhocphan.repository.*;
import vn.edu.quanlyhocphan.service.DangKyHocPhanService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * =============================================================
 * DANG KY HOC PHAN SERVICE — Trien khai 7 rang buoc nghiep vu
 * =============================================================
 *
 * Thu tu kiem tra trong method dangKy():
 *   1. Tim SV + LopHocPhan (kem HocKy)
 *   2. Kiem tra thoi gian dang ky  [Rang buoc #6]
 *   3. Kiem tra trang thai lop (MO) [Rang buoc #2 - phan 1]
 *   4. Kiem tra trung lop           [Rang buoc #5]
 *   5. Kiem tra mon tien quyet      [Rang buoc #3]
 *   6. Kiem tra tong tin chi        [Rang buoc #4]
 *   7. Kiem tra trung lich          [Rang buoc #1]
 *   8. Kiem tra si so + Optimistic Locking [Rang buoc #2 - chinh]
 *   9. Luu DangKyHocPhan + tang si_so_hien_tai [Rang buoc #7]
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DangKyHocPhanServiceImpl implements DangKyHocPhanService {

    private final SinhVienRepository sinhVienRepo;
    private final LopHocPhanRepository lopHocPhanRepo;
    private final DangKyHocPhanRepository dangKyRepo;
    private final LichHocRepository lichHocRepo;
    private final MonTienQuyetRepository monTienQuyetRepo;
    private final ChuongTrinhDaoTaoRepository chuongTrinhDaoTaoRepo;
    private final LichThiRepository lichThiRepo;

    // =================================================================
    // DANG KY — Toan bo cac rang buoc nghiep vu
    // =================================================================

    @Override
    @Transactional
    public DangKyHocPhan dangKy(Long sinhVienId, Long lopHocPhanId) {
        log.info("SV [{}] bat dau dang ky LHP [{}]", sinhVienId, lopHocPhanId);

        // --- Load entities ---
        SinhVien sinhVien = sinhVienRepo.findById(sinhVienId)
            .orElseThrow(() -> new ResourceNotFoundException("Sinh vien", sinhVienId));

        // [Mục 2] Kiem tra trang thai sinh vien (chi DANG_HOC moi duoc dang ky)
        kiemTraTrangThaiSinhVien(sinhVien);

        // Fetch kem HocKy + MonHoc trong 1 query
        LopHocPhan lhp = lopHocPhanRepo.findByIdWithLichHoc(lopHocPhanId)
            .orElseThrow(() -> new ResourceNotFoundException("Lop hoc phan", lopHocPhanId));

        HocKy hocKy = lhp.getHocKy();

        // [#6 + Mục 7] Kiem tra thoi gian dang ky va chot dot dang ky
        kiemTraThoiGianDangKy(hocKy);

        // [#2-part1] Kiem tra trang thai lop con mo
        kiemTraTrangThaiLop(lhp);

        // [Mục 1] Kiem tra doi tuong sinh vien (mon hoc thuoc CTDT nganh)
        kiemTraDoiTuongDangKy(sinhVien, lhp.getMonHoc());

        // [Mục 6] Kiem tra hoc cai thien / mon da dat (diem >= 8.0 thi khong cho hoc cai thien)
        kiemTraDaDatMon(sinhVienId, lhp.getMonHoc());

        // [#5 + Mục 3] Kiem tra trung lop (cho phep dang ky lai neu truoc do DA_HUY)
        kiemTraTrungLop(sinhVienId, lopHocPhanId, lhp.getMaLopHp());

        // [#5b] Kiem tra da dang ky mon nay trong HK nay chua (lop khac cung mon)
        kiemTraTrungMonTrongHocKy(sinhVienId, lhp.getMonHoc().getId(), hocKy.getId());

        // [#3] Kiem tra mon tien quyet
        kiemTraMonTienQuyet(sinhVienId, lhp.getMonHoc().getId());

        // [#4] Kiem tra tong tin chi (chi tinh cac LHP chinh thuc, khong tinh nguyen vong)
        kiemTraTongTinChi(sinhVienId, hocKy, lhp.getMonHoc().getSoTinChi());

        // [#1] Kiem tra trung lich hoc
        kiemTraTrungLich(sinhVienId, hocKy.getId(), lhp);

        // [Mục 5] Kiem tra trung lich thi
        kiemTraTrungLichThi(sinhVienId, hocKy.getId(), lhp);

        // [#2-chinh + #7 + Mục 8] Kiem tra si so (Optimistic Locking) + luu trong cung transaction
        return dangKyVaCapNhatSiSo(sinhVien, lhp);
    }

    // =================================================================
    // MỤC 2 — Kiem tra trang thai sinh vien
    // =================================================================

    private void kiemTraTrangThaiSinhVien(SinhVien sinhVien) {
        if (sinhVien.getTrangThai() != vn.edu.quanlyhocphan.enums.TrangThaiSinhVien.DANG_HOC) {
            String tenTrangThai = switch (sinhVien.getTrangThai()) {
                case BAO_LUU -> "Bảo lưu";
                case DINH_CHI -> "Đình chỉ học tập";
                case TOT_NGHIEP -> "Đã tốt nghiệp";
                default -> sinhVien.getTrangThai().name();
            };
            log.warn("SV [{}] khong the dang ky vi trang thai [{}]", sinhVien.getMssv(), sinhVien.getTrangThai());
            throw new NghiepVuException("Sinh viên đang ở trạng thái [" + tenTrangThai + "], không được phép thực hiện đăng ký.");
        }
    }

    // =================================================================
    // MỤC 1 — Kiem tra doi tuong duoc phep dang ky (thuoc CTDT nganh)
    // =================================================================

    private void kiemTraDoiTuongDangKy(SinhVien sinhVien, MonHoc monHoc) {
        if (sinhVien.getNganh() != null) {
            boolean thuocCTDT = chuongTrinhDaoTaoRepo.existsByNganhIdAndMonHocId(sinhVien.getNganh().getId(), monHoc.getId());
            if (!thuocCTDT) {
                log.warn("SV [{}] khong thuoc CTDT mon [{}] nganh [{}]",
                    sinhVien.getMssv(), monHoc.getMaMon(), sinhVien.getNganh().getTenNganh());
                throw new NghiepVuException(
                    "Môn học [" + monHoc.getTenMon() + "] không thuộc Chương trình đào tạo ngành "
                    + sinhVien.getNganh().getTenNganh() + ".");
            }
        }
    }

    // =================================================================
    // RANG BUOC #6 + MỤC 7 — Kiem tra thoi gian dang ky & da chot dang ky
    // =================================================================

    private void kiemTraThoiGianDangKy(HocKy hocKy) {
        if (Boolean.TRUE.equals(hocKy.getDaChotDangKy())) {
            log.warn("Hoc ky [{}] da chot dang ky", hocKy.getTenHocKy());
            throw new NgoaiThoiGianDangKyException(
                "Học kỳ [" + hocKy.getTenHocKy() + "] đã chốt đợt đăng ký. Không thể thực hiện thao tác.");
        }
        LocalDate hom_nay = LocalDate.now();
        boolean trongThoiGian = !hom_nay.isBefore(hocKy.getNgayBatDauDk())
                             && !hom_nay.isAfter(hocKy.getNgayKetThucDk());
        if (!trongThoiGian) {
            log.warn("Dang ky ngoai thoi gian: hom nay={}, dk={} -> {}",
                hom_nay, hocKy.getNgayBatDauDk(), hocKy.getNgayKetThucDk());
            throw new NgoaiThoiGianDangKyException(
                hocKy.getNgayBatDauDk(), hocKy.getNgayKetThucDk());
        }
    }

    // =================================================================
    // RANG BUOC #2 (phan 1) — Trang thai lop phai la MO
    // =================================================================

    private void kiemTraTrangThaiLop(LopHocPhan lhp) {
        if (lhp.getTrangThai() != TrangThaiLopHocPhan.MO) {
            throw new HetChoException(lhp.getMaLopHp());
        }
    }

    // =================================================================
    // MỤC 6 — Kiem tra hoc cai thien / mon da dat (diem >= 8.0 thi chan)
    // =================================================================

    private void kiemTraDaDatMon(Long sinhVienId, MonHoc monHoc) {
        List<DangKyHocPhan> lichSu = dangKyRepo.findLichSuDangKy(sinhVienId);
        for (DangKyHocPhan dk : lichSu) {
            if (dk.getLopHocPhan().getMonHoc().getId().equals(monHoc.getId())
                && dk.getTrangThai() == TrangThaiDangKy.HOAN_THANH
                && dk.getDiemTongKet() != null) {
                if (dk.getDiemTongKet().compareTo(new BigDecimal("8.0")) >= 0) {
                    log.warn("SV [{}] da dat diem cao mon [{}] diem={}", sinhVienId, monHoc.getTenMon(), dk.getDiemTongKet());
                    throw new NghiepVuException(
                        "Bạn đã hoàn thành môn [" + monHoc.getTenMon() + "] với điểm " + dk.getDiemTongKet() + " (Giỏi/Xuất sắc). Không thể đăng ký học cải thiện.");
                }
                log.info("SV [{}] dang ky hoc cai thien mon [{}] (diem cu = {})", sinhVienId, monHoc.getTenMon(), dk.getDiemTongKet());
            }
        }
    }

    // =================================================================
    // RANG BUOC #5 + MỤC 3 — Kiem tra trung lop (Cho phep dang ky lai neu DA_HUY)
    // =================================================================

    private void kiemTraTrungLop(Long sinhVienId, Long lopHocPhanId, String maLopHp) {
        dangKyRepo.findBySinhVienIdAndLopHocPhanId(sinhVienId, lopHocPhanId)
            .ifPresent(dk -> {
                if (dk.getTrangThai() == TrangThaiDangKy.DA_DANG_KY) {
                    throw new TrungLopException(maLopHp);
                }
                // Neu trang_thai = DA_HUY: cho phep dang ky lai (se update ban ghi o buoc dangKyVaCapNhatSiSo)
            });
    }

    // =================================================================
    // RANG BUOC #5b — Kiem tra trung mon trong cung hoc ky
    // =================================================================

    private void kiemTraTrungMonTrongHocKy(Long sinhVienId, Long monHocId, Long hocKyId) {
        boolean daDangKy = dangKyRepo.kiemTraDaDangKyMonTrongHocKy(sinhVienId, monHocId, hocKyId);
        if (daDangKy) {
            throw new TrungLopException(
                "Bạn đã đăng ký lớp khác của môn học này trong học kỳ này rồi.");
        }
    }

    // =================================================================
    // RANG BUOC #3 — Kiem tra mon tien quyet
    // =================================================================

    private void kiemTraMonTienQuyet(Long sinhVienId, Long monHocId) {
        List<MonTienQuyet> danhSachTienQuyet =
            monTienQuyetRepo.findByMonHocIdWithTienQuyet(monHocId);

        for (MonTienQuyet mtq : danhSachTienQuyet) {
            MonHoc monTq = mtq.getMonTienQuyet();
            boolean daHoanThanh = dangKyRepo.kiemTraDaHoanThanhMon(sinhVienId, monTq.getId());
            if (!daHoanThanh) {
                log.warn("SV [{}] chua hoan thanh mon tien quyet [{}]",
                    sinhVienId, monTq.getTenMon());
                throw new ThieuTienQuyetException(monTq.getTenMon());
            }
        }
    }

    // =================================================================
    // RANG BUOC #4 — Kiem tra tong tin chi (khong tinh nguyen vong)
    // =================================================================

    private void kiemTraTongTinChi(Long sinhVienId, HocKy hocKy, int soTinChiThem) {
        int tinChiHienTaiLHP = dangKyRepo.tinhTongTinChiDaDangKy(sinhVienId, hocKy.getId());
        if (tinChiHienTaiLHP + soTinChiThem > hocKy.getTinChiToiDa()) {
            log.warn("SV [{}] vuot tin chi: LHP={} + them={} > max={}",
                sinhVienId, tinChiHienTaiLHP, soTinChiThem, hocKy.getTinChiToiDa());
            throw new VuotTinChiException(tinChiHienTaiLHP, soTinChiThem, hocKy.getTinChiToiDa());
        }
    }

    // =================================================================
    // RANG BUOC #1 — Kiem tra trung lich hoc
    // =================================================================

    private void kiemTraTrungLich(Long sinhVienId, Long hocKyId, LopHocPhan lhpMoi) {
        // Lay lich cua cac lop SV DA dang ky trong hoc ky nay
        List<LichHoc> lichDaDangKy =
            lichHocRepo.findLichHocDaDangKyCuaSinhVien(sinhVienId, hocKyId);

        // Lay lich cua lop MUON dang ky
        List<LichHoc> lichMoi = lhpMoi.getLichHocs();

        for (LichHoc cu : lichDaDangKy) {
            for (LichHoc moi : lichMoi) {
                if (isXungDotLich(cu, moi)) {
                    String maLopCu = cu.getLopHocPhan().getMaLopHp();
                    log.warn("Trung lich hoc: Thu {} Tiet {}-{} voi lop [{}]",
                        moi.getThu(), moi.getTietBatDau(), moi.getTietKetThuc(), maLopCu);
                    throw new TrungLichException(maLopCu);
                }
            }
        }
    }

    // =================================================================
    // MỤC 5 — Kiem tra trung lich thi
    // =================================================================

    private void kiemTraTrungLichThi(Long sinhVienId, Long hocKyId, LopHocPhan lhpMoi) {
        List<LichThi> lichThiMoi = lichThiRepo.findByLopHocPhan_IdOrderByNgayThiAscGioBatDauAsc(lhpMoi.getId());
        if (lichThiMoi == null || lichThiMoi.isEmpty()) {
            return;
        }

        List<LichThi> lichThiDaDangKy = lichThiRepo.findBySinhVienAndHocKy(sinhVienId, hocKyId);
        for (LichThi cu : lichThiDaDangKy) {
            for (LichThi moi : lichThiMoi) {
                if (cu.getNgayThi() != null && cu.getNgayThi().equals(moi.getNgayThi())) {
                    if (cu.getGioBatDau() != null && cu.getGioKetThuc() != null &&
                        moi.getGioBatDau() != null && moi.getGioKetThuc() != null) {
                        boolean overlap = cu.getGioBatDau().isBefore(moi.getGioKetThuc())
                                       && cu.getGioKetThuc().isAfter(moi.getGioBatDau());
                        if (overlap) {
                            String tenMonCu = cu.getLopHocPhan().getMonHoc().getTenMon();
                            log.warn("Trung lich thi: ngay {} ({} - {}) voi mon [{}]",
                                moi.getNgayThi(), moi.getGioBatDau(), moi.getGioKetThuc(), tenMonCu);
                            throw new TrungLichException(
                                "Trùng lịch thi ngày " + moi.getNgayThi() + " (" + moi.getGioBatDau() + " - " + moi.getGioKetThuc() + ") với môn [" + tenMonCu + "]");
                        }
                    }
                }
            }
        }
    }

    /**
     * Kiem tra hai buoi hoc co xung dot tiet khong.
     * Overlap: cung thu VA tiet giao nhau.
     * Cong thuc overlap: A.start <= B.end AND A.end >= B.start
     */
    private boolean isXungDotLich(LichHoc a, LichHoc b) {
        if (!a.getThu().equals(b.getThu())) return false;
        return a.getTietBatDau() <= b.getTietKetThuc()
            && a.getTietKetThuc() >= b.getTietBatDau();
    }

    // =================================================================
    // RANG BUOC #2 (chinh) + #7
    // Optimistic Locking: kiem tra si so + luu/cap nhat DangKy + cap nhat si_so
    // Tat ca trong cung @Transactional -> commit hoac rollback toan bo
    // =================================================================

    private DangKyHocPhan dangKyVaCapNhatSiSo(SinhVien sinhVien, LopHocPhan lhp) {
        try {
            // [#2] Kiem tra con cho (lan cuoi, truoc khi ghi DB)
            if (!lhp.conCho()) {
                throw new HetChoException(lhp.getMaLopHp());
            }

            // [#7] Tang si_so_hien_tai + 1
            // @Version tren LopHocPhan dam bao neu 2 transaction cung tang:
            //   transaction sau se bi OptimisticLockException -> nem HetChoException
            lhp.setSiSoHienTai(lhp.getSiSoHienTai() + 1);
            lopHocPhanRepo.save(lhp); // Hibernate kiem tra version o day

            // [#7] Tao moi hoac cap nhat ban ghi DangKyHocPhan (neu truoc do DA_HUY)
            DangKyHocPhan dangKy = dangKyRepo
                .findBySinhVienIdAndLopHocPhanId(sinhVien.getId(), lhp.getId())
                .orElse(null);

            if (dangKy != null) {
                dangKy.setTrangThai(TrangThaiDangKy.DA_DANG_KY);
                dangKy.setNgayDangKy(LocalDateTime.now());
                dangKy.setDiemGiuaKy(null);
                dangKy.setDiemCuoiKy(null);
                dangKy.setDiemTongKet(null);
            } else {
                dangKy = DangKyHocPhan.builder()
                    .sinhVien(sinhVien)
                    .lopHocPhan(lhp)
                    .ngayDangKy(LocalDateTime.now())
                    .trangThai(TrangThaiDangKy.DA_DANG_KY)
                    .build();
            }

            DangKyHocPhan saved = dangKyRepo.save(dangKy);
            log.info("Dang ky thanh cong: SV [{}] - LHP [{}]",
                sinhVien.getMssv(), lhp.getMaLopHp());
            return saved;

        } catch (ObjectOptimisticLockingFailureException e) {
            // 2 SV tranh 1 cho cuoi: transaction thu 2 bi bat exception nay
            log.warn("Race condition: LHP [{}] het cho do Optimistic Lock", lhp.getMaLopHp());
            throw new HetChoException(lhp.getMaLopHp());
        }
    }

    // =================================================================
    // HUY DANG KY
    // =================================================================

    @Override
    @Transactional
    public void huyDangKy(Long sinhVienId, Long lopHocPhanId) {
        log.info("SV [{}] huy dang ky LHP [{}]", sinhVienId, lopHocPhanId);

        DangKyHocPhan dangKy = dangKyRepo
            .findBySinhVienIdAndLopHocPhanId(sinhVienId, lopHocPhanId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Khong tim thay ban ghi dang ky de huy."));

        if (dangKy.getTrangThai() != TrangThaiDangKy.DA_DANG_KY) {
            throw new NghiepVuException(
                "Chi co the huy cac dang ky o trang thai DA_DANG_KY.");
        }

        LopHocPhan lhp = dangKy.getLopHocPhan();
        HocKy hocKy = lhp.getHocKy();

        // Kiem tra thoi gian huy (cung trong khung dang ky)
        kiemTraThoiGianDangKy(hocKy);

        // [#7] Doi trang_thai -> DA_HUY + giam si_so trong cung transaction
        dangKy.setTrangThai(TrangThaiDangKy.DA_HUY);
        dangKyRepo.save(dangKy);

        lhp.setSiSoHienTai(Math.max(0, lhp.getSiSoHienTai() - 1));
        lopHocPhanRepo.save(lhp);

        log.info("Huy dang ky thanh cong: SV [{}] - LHP [{}]",
            sinhVienId, lhp.getMaLopHp());
    }

    // =================================================================
    // QUERY METHODS
    // =================================================================

    @Override
    @Transactional(readOnly = true)
    public List<DangKyHocPhan> layDangKyHienTai(Long sinhVienId, Long hocKyId) {
        return dangKyRepo.findBySinhVienIdAndHocKyIdAndTrangThai(
            sinhVienId, hocKyId, TrangThaiDangKy.DA_DANG_KY);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DangKyHocPhan> layDangKyThoiKhoaBieu(Long sinhVienId, Long hocKyId) {
        return dangKyRepo.findDangKyThoiKhoaBieu(sinhVienId, hocKyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DangKyHocPhan> layLichSuDangKy(Long sinhVienId) {
        return dangKyRepo.findLichSuDangKy(sinhVienId);
    }

    @Override
    @Transactional
    public DangKyHocPhan nhapDiem(Long dangKyId, BigDecimal diemGiuaKy,
                                   BigDecimal diemCuoiKy, BigDecimal diemTongKet) {
        DangKyHocPhan dk = dangKyRepo.findById(dangKyId)
            .orElseThrow(() -> new ResourceNotFoundException("DangKyHocPhan", dangKyId));

        // Validate diem
        validateDiem(diemGiuaKy, "Diem giua ky");
        validateDiem(diemCuoiKy, "Diem cuoi ky");
        validateDiem(diemTongKet, "Diem tong ket");

        dk.setDiemGiuaKy(diemGiuaKy);
        dk.setDiemCuoiKy(diemCuoiKy);
        dk.setDiemTongKet(diemTongKet);
        dk.setTrangThai(TrangThaiDangKy.HOAN_THANH);

        return dangKyRepo.save(dk);
    }

    private void validateDiem(BigDecimal diem, String tenDiem) {
        if (diem != null && (diem.compareTo(BigDecimal.ZERO) < 0
                || diem.compareTo(new BigDecimal("10")) > 0)) {
            throw new IllegalArgumentException(tenDiem + " phai trong khoang 0-10.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DangKyHocPhan> layDanhSachSinhVienTrongLop(Long lopHocPhanId) {
        return dangKyRepo.findDanhSachSinhVienTrongLop(lopHocPhanId);
    }
}
