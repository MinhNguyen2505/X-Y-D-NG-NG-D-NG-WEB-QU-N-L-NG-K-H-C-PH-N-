package vn.edu.quanlyhocphan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.quanlyhocphan.entity.SinhVien;
import vn.edu.quanlyhocphan.exception.ResourceNotFoundException;
import vn.edu.quanlyhocphan.repository.SinhVienRepository;
import vn.edu.quanlyhocphan.service.SinhVienService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SinhVienServiceImpl implements SinhVienService {

    private final SinhVienRepository sinhVienRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public SinhVien findById(Long id) {
        return sinhVienRepo.findByIdWithNganh(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sinh vien", id));
    }

    @Override
    @Transactional(readOnly = true)
    public SinhVien findByMssv(String mssv) {
        return sinhVienRepo.findByMssv(mssv)
            .orElseThrow(() -> new ResourceNotFoundException("Sinh vien voi MSSV: " + mssv));
    }

    @Override
    @Transactional(readOnly = true)
    public SinhVien findByEmail(String email) {
        return sinhVienRepo.findByEmailWithNganh(email)
            .orElseThrow(() -> new ResourceNotFoundException("Sinh vien voi email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SinhVien> findAll() {
        return sinhVienRepo.findAllWithNganh();
    }

    @Override
    @Transactional
    public SinhVien save(SinhVien sinhVien) {
        if (sinhVien.getId() == null) {
            if (sinhVien.getMatKhau() != null && !sinhVien.getMatKhau().isBlank()) {
                sinhVien.setMatKhau(passwordEncoder.encode(sinhVien.getMatKhau()));
            }
        } else {
            SinhVien old = sinhVienRepo.findById(sinhVien.getId()).orElse(null);
            if (sinhVien.getMatKhau() == null || sinhVien.getMatKhau().isBlank()) {
                if (old != null) sinhVien.setMatKhau(old.getMatKhau());
            } else {
                if (old == null || !sinhVien.getMatKhau().equals(old.getMatKhau())) {
                    sinhVien.setMatKhau(passwordEncoder.encode(sinhVien.getMatKhau()));
                }
            }
        }
        return sinhVienRepo.save(sinhVien);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!sinhVienRepo.existsById(id)) {
            throw new ResourceNotFoundException("Sinh vien", id);
        }
        sinhVienRepo.deleteById(id);
    }

    @Override
    public boolean existsByMssv(String mssv) {
        return sinhVienRepo.existsByMssv(mssv);
    }

    @Override
    public boolean existsByEmail(String email) {
        return sinhVienRepo.existsByEmail(email);
    }
}
