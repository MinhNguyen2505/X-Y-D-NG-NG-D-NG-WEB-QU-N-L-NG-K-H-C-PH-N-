package vn.edu.quanlyhocphan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.quanlyhocphan.dto.TinChiKhoiDto;
import vn.edu.quanlyhocphan.dto.TinChiKhoiDto.MonTrongKhoiDto;
import vn.edu.quanlyhocphan.entity.ChuongTrinhDaoTao;
import vn.edu.quanlyhocphan.entity.DangKyHocPhan;
import vn.edu.quanlyhocphan.repository.ChuongTrinhDaoTaoRepository;
import vn.edu.quanlyhocphan.repository.DangKyHocPhanRepository;
import vn.edu.quanlyhocphan.service.TinChiTichLuyService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TinChiTichLuyServiceImpl implements TinChiTichLuyService {

    private final ChuongTrinhDaoTaoRepository ctdtRepo;
    private final DangKyHocPhanRepository     dkhpRepo;

    @Override
    public List<TinChiKhoiDto> tinhTinChiTheoKhoi(Long sinhVienId, Long nganhId) {
        // 1. Lay toan bo CTDT cua nganh (kem khoi)
        List<ChuongTrinhDaoTao> ctdt = ctdtRepo.findByNganhIdWithMonHocAndKhoi(nganhId);

        // 2. Lay toan bo lich su dang ky cua SV (tat ca hoc ky)
        List<DangKyHocPhan> lichSu = dkhpRepo.findLichSuDangKy(sinhVienId);

        // 3. Map: monHocId -> dang ky tot nhat (co diem tong ket cao nhat)
        Map<Long, DangKyHocPhan> bestDangKy = new HashMap<>();
        for (DangKyHocPhan dk : lichSu) {
            Long monId = dk.getLopHocPhan().getMonHoc().getId();
            if (!bestDangKy.containsKey(monId)) {
                bestDangKy.put(monId, dk);
            } else {
                DangKyHocPhan existing = bestDangKy.get(monId);
                if (dk.getDiemTongKet() != null) {
                    if (existing.getDiemTongKet() == null ||
                        dk.getDiemTongKet().compareTo(existing.getDiemTongKet()) > 0) {
                        bestDangKy.put(monId, dk);
                    }
                }
            }
        }

        // 4. Nhom CTDT theo khoi
        // Cac mon chua co khoi gom vao 1 nhom "Khac"
        Map<String, List<ChuongTrinhDaoTao>> byKhoi = new LinkedHashMap<>();

        for (ChuongTrinhDaoTao c : ctdt) {
            String key = c.getKhoiKienThuc() != null
                    ? c.getKhoiKienThuc().getMaKhoi()
                    : "KHAC";
            byKhoi.computeIfAbsent(key, k -> new ArrayList<>()).add(c);
        }

        // 5. Build DTO cho tung khoi
        List<TinChiKhoiDto> result = new ArrayList<>();
        for (Map.Entry<String, List<ChuongTrinhDaoTao>> entry : byKhoi.entrySet()) {
            List<ChuongTrinhDaoTao> monTrongKhoi = entry.getValue();
            ChuongTrinhDaoTao first = monTrongKhoi.get(0);

            String maKhoi   = entry.getKey();
            String tenKhoi  = (first.getKhoiKienThuc() != null)
                    ? first.getKhoiKienThuc().getTenKhoi()
                    : "Khác";
            Long   khoiId   = (first.getKhoiKienThuc() != null)
                    ? first.getKhoiKienThuc().getId()
                    : null;

            int tongTC    = 0;
            int batBuocTC = 0;
            int tichLuyTC = 0;

            List<MonTrongKhoiDto> dsMon = new ArrayList<>();
            int stt = 1;

            for (ChuongTrinhDaoTao c : monTrongKhoi) {
                int tc = c.getMonHoc().getSoTinChi();
                tongTC += tc;
                if (Boolean.TRUE.equals(c.getBatBuoc())) batBuocTC += tc;

                DangKyHocPhan dk  = bestDangKy.get(c.getMonHoc().getId());
                BigDecimal diem   = (dk != null) ? dk.getDiemTongKet() : null;
                boolean dat       = diem != null && diem.compareTo(
                        c.getDiemDat() != null ? c.getDiemDat() : new BigDecimal("5.0")) >= 0;

                if (dat) tichLuyTC += tc;

                String danhGia   = diem == null ? "" : (dat ? "Đạt" : "Không đạt");
                BigDecimal quyDoi = diemQuyDoi(diem);
                String chu        = diemChu(diem);
                String ketQua;
                if (dk == null) {
                    ketQua = "Chưa học";
                } else if (dat) {
                    ketQua = "Hoàn thành";
                } else if (diem != null) {
                    ketQua = "Không đạt";
                } else {
                    ketQua = "Đang học";
                }

                dsMon.add(MonTrongKhoiDto.builder()
                        .stt(stt++)
                        .maMon(c.getMonHoc().getMaMon())
                        .tenMon(c.getMonHoc().getTenMon())
                        .soTinChi(tc)
                        .diem(diem)
                        .danhGia(danhGia)
                        .diemQuyDoi(quyDoi)
                        .diemChu(chu)
                        .ketQua(ketQua)
                        .batBuoc(Boolean.TRUE.equals(c.getBatBuoc()))
                        .build());
            }

            result.add(TinChiKhoiDto.builder()
                    .khoiId(khoiId)
                    .maKhoi(maKhoi)
                    .tenKhoi(tenKhoi)
                    .tongSoTinChi(tongTC)
                    .tinChiBatBuoc(batBuocTC)
                    .tinChiDaTichLuy(tichLuyTC)
                    .danhSachMon(dsMon)
                    .build());
        }

        return result;
    }

    @Override
    public int tongTinChiDaTichLuy(Long sinhVienId, Long nganhId) {
        return tinhTinChiTheoKhoi(sinhVienId, nganhId)
                .stream()
                .mapToInt(TinChiKhoiDto::getTinChiDaTichLuy)
                .sum();
    }

    // ----------------------------------------------------------------
    // Quy doi diem he 10 -> he 4 (theo thang diem 10-4 pho bien VN)
    // ----------------------------------------------------------------
    private static BigDecimal diemQuyDoi(BigDecimal d) {
        if (d == null) return null;
        double v = d.doubleValue();
        if (v >= 9.0) return new BigDecimal("4.0");
        if (v >= 8.5) return new BigDecimal("3.7");
        if (v >= 8.0) return new BigDecimal("3.5");
        if (v >= 7.0) return new BigDecimal("3.0");
        if (v >= 6.5) return new BigDecimal("2.5");
        if (v >= 6.0) return new BigDecimal("2.0");
        if (v >= 5.5) return new BigDecimal("1.5");
        if (v >= 5.0) return new BigDecimal("1.0");
        return BigDecimal.ZERO;
    }

    private static String diemChu(BigDecimal d) {
        if (d == null) return "";
        double v = d.doubleValue();
        if (v >= 9.0) return "A+";
        if (v >= 8.5) return "A";
        if (v >= 8.0) return "B+";
        if (v >= 7.0) return "B";
        if (v >= 6.5) return "C+";
        if (v >= 6.0) return "C";
        if (v >= 5.5) return "D+";
        if (v >= 5.0) return "D";
        return "F";
    }
}
