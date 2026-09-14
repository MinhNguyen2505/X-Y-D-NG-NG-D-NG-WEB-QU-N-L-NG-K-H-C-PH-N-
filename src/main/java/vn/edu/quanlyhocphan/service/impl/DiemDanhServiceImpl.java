package vn.edu.quanlyhocphan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.quanlyhocphan.entity.DangKyHocPhan;
import vn.edu.quanlyhocphan.entity.DiemDanh;
import vn.edu.quanlyhocphan.exception.ResourceNotFoundException;
import vn.edu.quanlyhocphan.repository.DangKyHocPhanRepository;
import vn.edu.quanlyhocphan.repository.DiemDanhRepository;
import vn.edu.quanlyhocphan.service.DiemDanhService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiemDanhServiceImpl implements DiemDanhService {

    private final DiemDanhRepository diemDanhRepo;
    private final DangKyHocPhanRepository dkhpRepo;

    @Override
    @Transactional(readOnly = true)
    public List<DiemDanh> layDiemDanhTheoNgay(Long lopHocPhanId, LocalDate ngayHoc) {
        return diemDanhRepo.findByLopAndNgay(lopHocPhanId, ngayHoc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiemDanh> layToanBoDiemDanh(Long lopHocPhanId) {
        return diemDanhRepo.findByLop(lopHocPhanId);
    }

    @Override
    @Transactional
    public void luuDiemDanh(Long lopHocPhanId, LocalDate ngayHoc,
                             Integer tietBatDau, Integer tietKetThuc,
                             Map<Long, String> diemDanhMap) {
        for (Map.Entry<Long, String> entry : diemDanhMap.entrySet()) {
            Long dangKyId = entry.getKey();
            String trangThai = entry.getValue();
            if (trangThai == null) trangThai = "CO_MAT";

            Optional<DiemDanh> existing = diemDanhRepo
                .findByDangKyHocPhanIdAndNgayHocAndTietBatDau(dangKyId, ngayHoc, tietBatDau);

            if (existing.isPresent()) {
                existing.get().setTrangThai(trangThai);
                diemDanhRepo.save(existing.get());
            } else {
                DangKyHocPhan dkhp = dkhpRepo.findById(dangKyId)
                    .orElseThrow(() -> new ResourceNotFoundException("DangKyHocPhan", dangKyId));
                DiemDanh dd = DiemDanh.builder()
                    .dangKyHocPhan(dkhp)
                    .ngayHoc(ngayHoc)
                    .tietBatDau(tietBatDau)
                    .tietKetThuc(tietKetThuc)
                    .trangThai(trangThai)
                    .build();
                diemDanhRepo.save(dd);
            }
        }
        log.info("Luu diem danh LHP [{}] ngay [{}] tiet [{}-{}]: {} SV",
            lopHocPhanId, ngayHoc, tietBatDau, tietKetThuc, diemDanhMap.size());
    }

    @Override
    @Transactional(readOnly = true)
    public long demSoBuoiVang(Long dangKyId) {
        return diemDanhRepo.demSoBuoiVang(dangKyId);
    }
}
