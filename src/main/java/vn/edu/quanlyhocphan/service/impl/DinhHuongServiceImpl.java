package vn.edu.quanlyhocphan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.quanlyhocphan.entity.*;
import vn.edu.quanlyhocphan.exception.NghiepVuException;
import vn.edu.quanlyhocphan.exception.ResourceNotFoundException;
import vn.edu.quanlyhocphan.repository.*;
import vn.edu.quanlyhocphan.service.DinhHuongService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DinhHuongServiceImpl implements DinhHuongService {

    private final DinhHuongRepository dinhHuongRepo;
    private final DangKyDinhHuongRepository dangKyRepo;
    private final SinhVienRepository sinhVienRepo;

    // ================================================================
    // ADMIN
    // ================================================================

    @Override @Transactional(readOnly = true)
    public List<DinhHuong> findAll() {
        return dinhHuongRepo.findAllWithNganh();
    }

    @Override @Transactional(readOnly = true)
    public DinhHuong findById(Long id) {
        return dinhHuongRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dinh huong", id));
    }

    @Override @Transactional
    public DinhHuong save(DinhHuong dinhHuong) {
        return dinhHuongRepo.save(dinhHuong);
    }

    @Override @Transactional
    public void dong(Long id) {
        DinhHuong dh = findById(id);
        dh.setTrangThai("DA_DONG");
        dinhHuongRepo.save(dh);
    }

    @Override @Transactional
    public void mo(Long id) {
        DinhHuong dh = findById(id);
        dh.setTrangThai("DANG_MO");
        dinhHuongRepo.save(dh);
    }

    @Override @Transactional(readOnly = true)
    public List<DangKyDinhHuong> findDangKyByDinhHuong(Long dinhHuongId) {
        return dangKyRepo.findByDinhHuongId(dinhHuongId);
    }

    // ================================================================
    // SINH VIEN
    // ================================================================

    @Override @Transactional(readOnly = true)
    public List<DinhHuong> findDangMoByNganh(Long nganhId) {
        return dinhHuongRepo.findDangMoByNganhId(nganhId);
    }

    @Override @Transactional(readOnly = true)
    public List<DangKyDinhHuong> findDangKyCuaSinhVien(Long svId) {
        return dangKyRepo.findBySinhVienId(svId);
    }

    @Override @Transactional(readOnly = true)
    public Optional<DangKyDinhHuong> findActiveByNganh(Long svId, Long nganhId) {
        return dangKyRepo.findActiveBySinhVienAndNganh(svId, nganhId);
    }

    @Override @Transactional
    public void dangKy(Long svId, Long dinhHuongId) {
        DinhHuong dh = findById(dinhHuongId);

        if (!dh.isDangMo()) {
            throw new NghiepVuException("Dinh huong nay hien khong con trong thoi gian dang ky.");
        }

        // Kiem tra SV da co dinh huong active trong nganh nay chua
        Optional<DangKyDinhHuong> existing =
            dangKyRepo.findActiveBySinhVienAndNganh(svId, dh.getNganh().getId());
        if (existing.isPresent()) {
            throw new NghiepVuException(
                "Ban da dang ky dinh huong [" + existing.get().getDinhHuong().getTenDinhHuong() +
                "] trong nganh nay. Vui long huy truoc khi chon dinh huong khac.");
        }

        // Kiem tra da co ban ghi cu (DA_HUY) thi cap nhat lai
        Optional<DangKyDinhHuong> old =
            dangKyRepo.findBySinhVienIdAndDinhHuongId(svId, dinhHuongId);
        if (old.isPresent()) {
            old.get().setTrangThai("DA_DANG_KY");
            old.get().setNgayDangKy(java.time.LocalDateTime.now());
            dangKyRepo.save(old.get());
            return;
        }

        SinhVien sv = sinhVienRepo.findById(svId)
            .orElseThrow(() -> new ResourceNotFoundException("Sinh vien", svId));

        DangKyDinhHuong dk = DangKyDinhHuong.builder()
            .sinhVien(sv)
            .dinhHuong(dh)
            .trangThai("DA_DANG_KY")
            .build();
        dangKyRepo.save(dk);
        log.info("SV [{}] dang ky dinh huong [{}]", svId, dh.getTenDinhHuong());
    }

    @Override @Transactional
    public void huy(Long svId, Long dinhHuongId) {
        DangKyDinhHuong dk = dangKyRepo
            .findBySinhVienIdAndDinhHuongId(svId, dinhHuongId)
            .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay ban ghi dang ky dinh huong."));
        dk.setTrangThai("DA_HUY");
        dangKyRepo.save(dk);
        log.info("SV [{}] huy dinh huong [{}]", svId, dinhHuongId);
    }
}
