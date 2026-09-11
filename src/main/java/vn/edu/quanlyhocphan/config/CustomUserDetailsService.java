package vn.edu.quanlyhocphan.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.quanlyhocphan.entity.Admin;
import vn.edu.quanlyhocphan.entity.GiangVien;
import vn.edu.quanlyhocphan.entity.SinhVien;
import vn.edu.quanlyhocphan.repository.AdminRepository;
import vn.edu.quanlyhocphan.repository.GiangVienRepository;
import vn.edu.quanlyhocphan.repository.SinhVienRepository;

import java.util.Optional;

/**
 * Xac thuc nguoi dung tu 3 nguon trong DB:
 * Admin, SinhVien, GiangVien.
 * KHONG con hardcode — tat ca lay tu bang admin trong DB.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository     adminRepository;
    private final SinhVienRepository  sinhVienRepository;
    private final GiangVienRepository giangVienRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1. Kiem tra trong bang Admin
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return User.builder()
                    .username(admin.getEmail())
                    .password(admin.getMatKhau())
                    .roles("ADMIN")
                    .build();
        }

        // 2. Kiem tra trong bang SinhVien
        Optional<SinhVien> svOpt = sinhVienRepository.findByEmail(email);
        if (svOpt.isPresent()) {
            SinhVien sv = svOpt.get();
            return User.builder()
                    .username(sv.getEmail())
                    .password(sv.getMatKhau())
                    .roles("SINH_VIEN")
                    .build();
        }

        // 3. Kiem tra trong bang GiangVien
        Optional<GiangVien> gvOpt = giangVienRepository.findByEmail(email);
        if (gvOpt.isPresent()) {
            GiangVien gv = gvOpt.get();
            return User.builder()
                    .username(gv.getEmail())
                    .password(gv.getMatKhau())
                    .roles("GIANG_VIEN")
                    .build();
        }

        throw new UsernameNotFoundException("Khong tim thay tai khoan: " + email);
    }
}
