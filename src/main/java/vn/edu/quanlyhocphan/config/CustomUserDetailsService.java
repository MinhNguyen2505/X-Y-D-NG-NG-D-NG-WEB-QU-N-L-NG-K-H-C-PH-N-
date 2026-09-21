package vn.edu.quanlyhocphan.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.quanlyhocphan.entity.Admin;
import vn.edu.quanlyhocphan.entity.GiangVien;
import vn.edu.quanlyhocphan.entity.QuanLy;
import vn.edu.quanlyhocphan.entity.SinhVien;
import vn.edu.quanlyhocphan.repository.AdminRepository;
import vn.edu.quanlyhocphan.repository.GiangVienRepository;
import vn.edu.quanlyhocphan.repository.QuanLyRepository;
import vn.edu.quanlyhocphan.repository.SinhVienRepository;

import java.util.Optional;

/**
 * Xac thuc nguoi dung tu 4 nguon trong DB:
 * Admin, QuanLy, SinhVien, GiangVien.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository     adminRepository;
    private final QuanLyRepository    quanLyRepository;
    private final SinhVienRepository  sinhVienRepository;
    private final GiangVienRepository giangVienRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1. Admin — quan tri he thong
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return User.builder()
                    .username(admin.getEmail())
                    .password(admin.getMatKhau())
                    .roles("ADMIN")
                    .build();
        }

        // 2. Quan Ly — phong dao tao
        Optional<QuanLy> qlOpt = quanLyRepository.findByEmail(email);
        if (qlOpt.isPresent()) {
            QuanLy ql = qlOpt.get();
            return User.builder()
                    .username(ql.getEmail())
                    .password(ql.getMatKhau())
                    .roles("QUAN_LY")
                    .build();
        }

        // 3. Sinh vien
        Optional<SinhVien> svOpt = sinhVienRepository.findByEmail(email);
        if (svOpt.isPresent()) {
            SinhVien sv = svOpt.get();
            return User.builder()
                    .username(sv.getEmail())
                    .password(sv.getMatKhau())
                    .roles("SINH_VIEN")
                    .build();
        }

        // 4. Giang vien
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
