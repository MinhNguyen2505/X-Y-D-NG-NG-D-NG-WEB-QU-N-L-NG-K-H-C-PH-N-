package vn.edu.quanlyhocphan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.quanlyhocphan.entity.GiangVien;
import vn.edu.quanlyhocphan.exception.ResourceNotFoundException;
import vn.edu.quanlyhocphan.repository.GiangVienRepository;
import vn.edu.quanlyhocphan.service.GiangVienService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GiangVienServiceImpl implements GiangVienService {

    private final GiangVienRepository giangVienRepo;
    private final PasswordEncoder passwordEncoder;

    @Override @Transactional(readOnly = true)
    public GiangVien findById(Long id) {
        return giangVienRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Giang vien", id));
    }

    @Override @Transactional(readOnly = true)
    public GiangVien findByMaGv(String maGv) {
        return giangVienRepo.findByMaGv(maGv)
            .orElseThrow(() -> new ResourceNotFoundException("Giang vien voi ma: " + maGv));
    }

    @Override @Transactional(readOnly = true)
    public GiangVien findByEmail(String email) {
        return giangVienRepo.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Giang vien voi email: " + email));
    }

    @Override @Transactional(readOnly = true)
    public List<GiangVien> findAll() {
        return giangVienRepo.findAll();
    }

    @Override @Transactional
    public GiangVien save(GiangVien giangVien) {
        if (giangVien.getId() == null) {
            giangVien.setMatKhau(passwordEncoder.encode(giangVien.getMatKhau()));
        }
        return giangVienRepo.save(giangVien);
    }

    @Override @Transactional
    public void deleteById(Long id) {
        if (!giangVienRepo.existsById(id)) throw new ResourceNotFoundException("Giang vien", id);
        giangVienRepo.deleteById(id);
    }
}
