package vn.edu.quanlyhocphan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.quanlyhocphan.entity.DangKyHocPhan;
import vn.edu.quanlyhocphan.entity.DangKyThiLai;
import vn.edu.quanlyhocphan.entity.SinhVien;
import vn.edu.quanlyhocphan.exception.NghiepVuException;
import vn.edu.quanlyhocphan.exception.ResourceNotFoundException;
import vn.edu.quanlyhocphan.repository.DangKyHocPhanRepository;
import vn.edu.quanlyhocphan.repository.DangKyThiLaiRepository;
import vn.edu.quanlyhocphan.repository.SinhVienRepository;
import vn.edu.quanlyhocphan.service.ThiLaiService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ThiLaiServiceImpl implements ThiLaiService {

    private final DangKyThiLaiRepository thiLaiRepo;
    private final DangKyHocPhanRepository dkhpRepo;
    private final SinhVienRepository svRepo;

    private static final BigDecimal DIEM_DAT = new BigDecimal("5.0");

    @Override
    public List<DangKyHocPhan> layMonChuaDat(Long sinhVienId) {
        // Lay tat ca dang ky da co diem va diem < 5
        return dkhpRepo.findLichSuDangKy(sinhVienId).stream()
            .filter(dk -> dk.getDiemTongKet() != null
                       && dk.getDiemTongKet().compareTo(DIEM_DAT) < 0)
            .toList();
    }

    @Override
    @Transactional
    public void dangKy(Long sinhVienId, Long dangKyHocPhanId) {
        // Kiem tra mon chua dat
        DangKyHocPhan dkhp = dkhpRepo.findById(dangKyHocPhanId)
            .orElseThrow(() -> new ResourceNotFoundException("DangKyHocPhan", dangKyHocPhanId));

        if (dkhp.getDiemTongKet() == null || dkhp.getDiemTongKet().compareTo(DIEM_DAT) >= 0) {
            throw new NghiepVuException("Mon nay khong du dieu kien thi lai (diem >= 5 hoac chua co diem).");
        }

        // Kiem tra da dang ky thi lai chua
        thiLaiRepo.findBySinhVienIdAndDangKyHocPhanId(sinhVienId, dangKyHocPhanId)
            .ifPresent(dk -> {
                if (!"DA_HUY".equals(dk.getTrangThai())) {
                    throw new NghiepVuException("Ban da dang ky thi lai mon nay roi (trang thai: "
                        + dk.getTrangThai() + ").");
                }
                // Neu da huy thi cho dang ky lai
                dk.setTrangThai("CHO_DUYET");
                thiLaiRepo.save(dk);
                log.info("SV [{}] dang ky lai thi lai dkhp [{}]", sinhVienId, dangKyHocPhanId);
            });

        if (thiLaiRepo.findBySinhVienIdAndDangKyHocPhanId(sinhVienId, dangKyHocPhanId).isEmpty()) {
            SinhVien sv = svRepo.findById(sinhVienId)
                .orElseThrow(() -> new ResourceNotFoundException("SinhVien", sinhVienId));
            DangKyThiLai dktl = DangKyThiLai.builder()
                .sinhVien(sv)
                .dangKyHocPhan(dkhp)
                .trangThai("CHO_DUYET")
                .build();
            thiLaiRepo.save(dktl);
            log.info("SV [{}] dang ky thi lai mon dkhp [{}]", sinhVienId, dangKyHocPhanId);
        }
    }

    @Override
    @Transactional
    public void huy(Long sinhVienId, Long dangKyHocPhanId) {
        DangKyThiLai dk = thiLaiRepo
            .findBySinhVienIdAndDangKyHocPhanId(sinhVienId, dangKyHocPhanId)
            .orElseThrow(() -> new NghiepVuException("Khong tim thay dang ky thi lai."));
        if ("DA_DUYET".equals(dk.getTrangThai())) {
            throw new NghiepVuException("Khong the huy sau khi da duyet. Vui long lien he Phong Dao tao.");
        }
        dk.setTrangThai("DA_HUY");
        thiLaiRepo.save(dk);
        log.info("SV [{}] huy dang ky thi lai dkhp [{}]", sinhVienId, dangKyHocPhanId);
    }

    @Override
    public List<DangKyThiLai> layLichSu(Long sinhVienId) {
        return thiLaiRepo.findBySinhVienId(sinhVienId);
    }

    @Override
    public List<DangKyThiLai> findAll() {
        return thiLaiRepo.findAllWithDetails();
    }

    @Override
    public List<DangKyThiLai> findByTrangThai(String trangThai) {
        return thiLaiRepo.findByTrangThai(trangThai);
    }

    @Override
    @Transactional
    public void duyet(Long dangKyThiLaiId) {
        DangKyThiLai dk = thiLaiRepo.findById(dangKyThiLaiId)
            .orElseThrow(() -> new ResourceNotFoundException("DangKyThiLai", dangKyThiLaiId));
        if ("DA_HUY".equals(dk.getTrangThai())) {
            throw new NghiepVuException("Khong the duyet ban ghi da huy.");
        }
        dk.setTrangThai("DA_DUYET");
        thiLaiRepo.save(dk);
        log.info("Admin duyet dang ky thi lai id={}", dangKyThiLaiId);
    }

    @Override
    @Transactional
    public void tuChoi(Long dangKyThiLaiId) {
        DangKyThiLai dk = thiLaiRepo.findById(dangKyThiLaiId)
            .orElseThrow(() -> new ResourceNotFoundException("DangKyThiLai", dangKyThiLaiId));
        dk.setTrangThai("DA_HUY");
        thiLaiRepo.save(dk);
        log.info("Admin tu choi dang ky thi lai id={}", dangKyThiLaiId);
    }

    @Override
    @Transactional
    public int duyetTatCa() {
        List<DangKyThiLai> choDuyet = thiLaiRepo.findByTrangThai("CHO_DUYET");
        choDuyet.forEach(dk -> dk.setTrangThai("DA_DUYET"));
        thiLaiRepo.saveAll(choDuyet);
        log.info("Admin duyet tat ca {} dang ky thi lai", choDuyet.size());
        return choDuyet.size();
    }
}
