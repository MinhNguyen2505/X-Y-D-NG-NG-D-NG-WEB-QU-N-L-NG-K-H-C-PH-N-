package vn.edu.quanlyhocphan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.quanlyhocphan.dto.BaoCaoThongKeDTO;
import vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan;
import vn.edu.quanlyhocphan.repository.DangKyHocPhanRepository;
import vn.edu.quanlyhocphan.repository.DangKyNguyenVongRepository;
import vn.edu.quanlyhocphan.repository.DangKyThiLaiRepository;
import vn.edu.quanlyhocphan.repository.LopHocPhanRepository;
import vn.edu.quanlyhocphan.repository.SinhVienRepository;
import vn.edu.quanlyhocphan.service.BaoCaoService;
import vn.edu.quanlyhocphan.service.ThiLaiService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BaoCaoServiceImpl implements BaoCaoService {

    private final DangKyHocPhanRepository  dangKyRepo;
    private final DangKyNguyenVongRepository dangKyNvRepo;
    private final DangKyThiLaiRepository   thiLaiRepo;
    private final LopHocPhanRepository     lopHocPhanRepo;
    private final SinhVienRepository       sinhVienRepo;
    private final ThiLaiService            thiLaiService;

    @Override
    public BaoCaoThongKeDTO thongKeTheoHocKy(Long hocKyId) {
        long tongSV = sinhVienRepo.count();
        long svDaDangKy = dangKyRepo.demSVDaDangKyTrongHK(hocKyId);
        long svChuaDangKy = tongSV - svDaDangKy;
        double tiLe = tongSV > 0 ? svDaDangKy * 100.0 / tongSV : 0;

        // Trung binh TC / SV
        long tongTC = dangKyRepo.tongTinChiDangKyTrongHK(hocKyId);
        double trungBinhTC = svDaDangKy > 0 ? (double) tongTC / svDaDangKy : 0;

        // Phan bo tin chi
        Map<String, Long> phanBo = tinhPhanBoTinChi(hocKyId);

        // Top mon hot
        List<BaoCaoThongKeDTO.TopMonDTO> topMon = layTopMonHot(5);

        // Nguyen vong
        long nvChoDuyet = dangKyNvRepo.countByTrangThai("CHO_DUYET");
        long nvDaDuyet  = dangKyNvRepo.countByTrangThai("DA_DUYET");
        long nvDaHuy    = dangKyNvRepo.countByTrangThai("DA_HUY");
        double tiLeNV   = (nvChoDuyet + nvDaDuyet + nvDaHuy) > 0
                ? nvDaDuyet * 100.0 / (nvChoDuyet + nvDaDuyet + nvDaHuy) : 0;

        // Thi lai
        long thiLaiChoDuyet = thiLaiService.findByTrangThai("CHO_DUYET").size();
        long thiLaiDaDuyet  = thiLaiService.findByTrangThai("DA_DUYET").size();

        // Lop hoc phan
        long lopMo   = lopHocPhanRepo.findAll().stream()
                .filter(l -> TrangThaiLopHocPhan.MO.equals(l.getTrangThai())).count();
        long lopDong = lopHocPhanRepo.findAll().stream()
                .filter(l -> TrangThaiLopHocPhan.DONG.equals(l.getTrangThai())).count();

        return BaoCaoThongKeDTO.builder()
                .tongSinhVien((int) tongSV)
                .tongLopHocPhan((int) lopHocPhanRepo.count())
                .tongDangKyHocPhan((int) dangKyRepo.count())
                .svDaDangKy((int) svDaDangKy)
                .svChuaDangKy((int) svChuaDangKy)
                .tiLeDangKy(Math.round(tiLe * 10.0) / 10.0)
                .trungBinhTCperSV(Math.round(trungBinhTC * 10.0) / 10.0)
                .phanBoTinChi(phanBo)
                .topMonHot(topMon)
                .nvChoDuyet(nvChoDuyet)
                .nvDaDuyet(nvDaDuyet)
                .nvDaHuy(nvDaHuy)
                .tiLeNVDuyet(Math.round(tiLeNV * 10.0) / 10.0)
                .thiLaiChoDuyet(thiLaiChoDuyet)
                .thiLaiDaDuyet(thiLaiDaDuyet)
                .lopDangMo(lopMo)
                .lopDaDong(lopDong)
                .build();
    }

    @Override
    public BaoCaoThongKeDTO thongKeTongQuan() {
        long tongSV = sinhVienRepo.count();
        List<BaoCaoThongKeDTO.TopMonDTO> topMon = layTopMonHot(5);

        long nvChoDuyet = dangKyNvRepo.countByTrangThai("CHO_DUYET");
        long nvDaDuyet  = dangKyNvRepo.countByTrangThai("DA_DUYET");
        long nvDaHuy    = dangKyNvRepo.countByTrangThai("DA_HUY");
        double tiLeNV   = (nvChoDuyet + nvDaDuyet + nvDaHuy) > 0
                ? nvDaDuyet * 100.0 / (nvChoDuyet + nvDaDuyet + nvDaHuy) : 0;

        long thiLaiChoDuyet = thiLaiService.findByTrangThai("CHO_DUYET").size();
        long thiLaiDaDuyet  = thiLaiService.findByTrangThai("DA_DUYET").size();

        long lopMo   = lopHocPhanRepo.findAll().stream()
                .filter(l -> TrangThaiLopHocPhan.MO.equals(l.getTrangThai())).count();
        long lopDong = lopHocPhanRepo.findAll().stream()
                .filter(l -> TrangThaiLopHocPhan.DONG.equals(l.getTrangThai())).count();

        return BaoCaoThongKeDTO.builder()
                .tongSinhVien((int) tongSV)
                .tongLopHocPhan((int) lopHocPhanRepo.count())
                .tongDangKyHocPhan((int) dangKyRepo.count())
                .topMonHot(topMon)
                .nvChoDuyet(nvChoDuyet)
                .nvDaDuyet(nvDaDuyet)
                .nvDaHuy(nvDaHuy)
                .tiLeNVDuyet(Math.round(tiLeNV * 10.0) / 10.0)
                .thiLaiChoDuyet(thiLaiChoDuyet)
                .thiLaiDaDuyet(thiLaiDaDuyet)
                .lopDangMo(lopMo)
                .lopDaDong(lopDong)
                .build();
    }

    private List<BaoCaoThongKeDTO.TopMonDTO> layTopMonHot(int limit) {
        List<Object[]> rows = dangKyRepo.findTopMonHot();
        List<BaoCaoThongKeDTO.TopMonDTO> result = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, rows.size()); i++) {
            Object[] r = rows.get(i);
            result.add(new BaoCaoThongKeDTO.TopMonDTO(
                    (String) r[0],
                    (String) r[1],
                    ((Number) r[3]).longValue(),
                    ((Number) r[2]).intValue()
            ));
        }
        return result;
    }

    private Map<String, Long> tinhPhanBoTinChi(Long hocKyId) {
        List<Object[]> rows = dangKyRepo.findTCPerSVTrongHK(hocKyId);
        Map<String, Long> phanBo = new LinkedHashMap<>();
        phanBo.put("0 TC", 0L);
        phanBo.put("1-5 TC", 0L);
        phanBo.put("6-10 TC", 0L);
        phanBo.put("11-15 TC", 0L);
        phanBo.put("16-20 TC", 0L);
        phanBo.put(">20 TC", 0L);

        for (Object[] row : rows) {
            long tc = ((Number) row[1]).longValue();
            if (tc == 0) phanBo.merge("0 TC", 1L, Long::sum);
            else if (tc <= 5) phanBo.merge("1-5 TC", 1L, Long::sum);
            else if (tc <= 10) phanBo.merge("6-10 TC", 1L, Long::sum);
            else if (tc <= 15) phanBo.merge("11-15 TC", 1L, Long::sum);
            else if (tc <= 20) phanBo.merge("16-20 TC", 1L, Long::sum);
            else phanBo.merge(">20 TC", 1L, Long::sum);
        }
        return phanBo;
    }
}
