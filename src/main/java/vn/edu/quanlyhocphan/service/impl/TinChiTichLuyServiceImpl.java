package vn.edu.quanlyhocphan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.quanlyhocphan.dto.TinChiKhoiDto;
import vn.edu.quanlyhocphan.dto.TinChiKhoiDto.MonTrongKhoiDto;
import vn.edu.quanlyhocphan.entity.ChuongTrinhDaoTao;
import vn.edu.quanlyhocphan.entity.DangKyHocPhan;
import vn.edu.quanlyhocphan.entity.KhoiKienThuc;
import vn.edu.quanlyhocphan.repository.ChuongTrinhDaoTaoRepository;
import vn.edu.quanlyhocphan.repository.DangKyHocPhanRepository;
import vn.edu.quanlyhocphan.repository.KhoiKienThucRepository;
import vn.edu.quanlyhocphan.service.TinChiTichLuyService;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TinChiTichLuyServiceImpl implements TinChiTichLuyService {

    private final ChuongTrinhDaoTaoRepository ctdtRepo;
    private final DangKyHocPhanRepository     dkhpRepo;
    private final KhoiKienThucRepository      khoiRepo;

    @Override
    public List<TinChiKhoiDto> tinhTinChiTheoKhoi(Long sinhVienId, Long nganhId) {

        // Query 1: toan bo CTDT cua nganh (JOIN FETCH MonHoc + KhoiKienThuc)
        List<ChuongTrinhDaoTao> ctdtList =
            ctdtRepo.findByNganhIdWithMonHocAndKhoi(nganhId);

        // Query 2: tat ca khoi cua nganh (ca khoi chung nganh_id=null)
        List<KhoiKienThuc> danhSachKhoi =
            khoiRepo.findByNganh_IdOrNganhIsNullOrderByMaKhoiAsc(nganhId);

        // Query 3: lich su dang ky cua SV
        List<DangKyHocPhan> lichSu = dkhpRepo.findLichSuDangKy(sinhVienId);

        // Build map: monHocId -> diem tong ket tot nhat (chi tinh HOAN_THANH, loai DA_HUY)
        Map<Long, BigDecimal> diemMap = new HashMap<>();
        Map<Long, String>     trangThaiMap = new HashMap<>();
        for (DangKyHocPhan dk : lichSu) {
            // Bỏ qua bản ghi DA_HUY — không tính vào tích lũy
            if (dk.getTrangThai() == vn.edu.quanlyhocphan.enums.TrangThaiDangKy.DA_HUY) continue;

            Long monId = dk.getLopHocPhan().getMonHoc().getId();
            BigDecimal diem = dk.getDiemTongKet();
            // Giu diem cao nhat
            if (!diemMap.containsKey(monId)) {
                diemMap.put(monId, diem);
                trangThaiMap.put(monId, dk.getTrangThai().name());
            } else {
                BigDecimal cur = diemMap.get(monId);
                if (diem != null && (cur == null || diem.compareTo(cur) > 0)) {
                    diemMap.put(monId, diem);
                    trangThaiMap.put(monId, dk.getTrangThai().name());
                }
            }
        }

        // Build map: maKhoi -> list CTDT (theo thu tu khoi)
        // Khoi chung (GDTC, QPAN) co nganh_id=null nen dung id de match
        Map<Long, KhoiKienThuc>            khoiById  = new LinkedHashMap<>();
        Map<String, List<ChuongTrinhDaoTao>> byMaKhoi = new LinkedHashMap<>();

        for (KhoiKienThuc k : danhSachKhoi) {
            khoiById.put(k.getId(), k);
            byMaKhoi.put(k.getMaKhoi(), new ArrayList<>());
        }
        // Fallback cho mon chua co khoi
        byMaKhoi.put("KHAC", new ArrayList<>());

        for (ChuongTrinhDaoTao c : ctdtList) {
            if (c.getKhoiKienThuc() != null) {
                String maKhoi = c.getKhoiKienThuc().getMaKhoi();
                byMaKhoi.computeIfAbsent(maKhoi, k -> new ArrayList<>()).add(c);
            } else {
                byMaKhoi.get("KHAC").add(c);
            }
        }

        // Build result
        List<TinChiKhoiDto> result = new ArrayList<>();
        for (Map.Entry<String, List<ChuongTrinhDaoTao>> entry : byMaKhoi.entrySet()) {
            List<ChuongTrinhDaoTao> monList = entry.getValue();
            // Bo qua nhom "KHAC" neu trong
            if (monList.isEmpty() && entry.getKey().equals("KHAC")) continue;

            String maKhoi = entry.getKey();
            KhoiKienThuc khoi = danhSachKhoi.stream()
                .filter(k -> k.getMaKhoi().equals(maKhoi))
                .findFirst().orElse(null);

            String tenKhoi = khoi != null ? khoi.getTenKhoi() : "Khác";
            Long   khoiId  = khoi != null ? khoi.getId() : null;

            int tongTC = 0, batBuocTC = 0, tichLuyTC = 0;
            List<MonTrongKhoiDto> dsDto = new ArrayList<>();
            int stt = 1;

            for (ChuongTrinhDaoTao c : monList) {
                int tc = c.getMonHoc().getSoTinChi();
                tongTC += tc;
                boolean batBuoc = Boolean.TRUE.equals(c.getBatBuoc());
                if (batBuoc) batBuocTC += tc;

                Long       monId    = c.getMonHoc().getId();
                BigDecimal diem     = diemMap.get(monId);
                // Dung nguong 5.0 co dinh — dong nhat voi tinhTongTinChiTichLuy() trong DB
                boolean    dat      = diem != null && diem.compareTo(new BigDecimal("5.0")) >= 0;

                if (dat) tichLuyTC += tc;

                String ketQua;
                String trangThai = trangThaiMap.get(monId);
                if (trangThai == null)                        ketQua = "Chưa học";
                else if (dat)                                 ketQua = "Hoàn thành";
                else if (diem != null)                        ketQua = "Không đạt";
                else                                          ketQua = "Đang học";

                dsDto.add(MonTrongKhoiDto.builder()
                    .stt(stt++)
                    .maMon(c.getMonHoc().getMaMon())
                    .tenMon(c.getMonHoc().getTenMon())
                    .soTinChi(tc)
                    .diem(diem)
                    .danhGia(diem == null ? "" : (dat ? "Đạt" : "Không đạt"))
                    .diemQuyDoi(diemQuyDoi(diem))
                    .diemChu(diemChu(diem))
                    .ketQua(ketQua)
                    .batBuoc(batBuoc)
                    .build());
            }

            result.add(TinChiKhoiDto.builder()
                .khoiId(khoiId)
                .maKhoi(maKhoi)
                .tenKhoi(tenKhoi)
                .tongSoTinChi(tongTC)
                .tinChiBatBuoc(batBuocTC)
                .tinChiDaTichLuy(tichLuyTC)
                .danhSachMon(dsDto)
                .build());
        }

        return result;
    }

    @Override
    public int tongTinChiDaTichLuy(Long sinhVienId, Long nganhId) {
        // Lay tu DB truc tiep, khong goi lai tinhTinChiTheoKhoi de tranh double query
        return dkhpRepo.tinhTongTinChiTichLuy(sinhVienId);
    }

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
