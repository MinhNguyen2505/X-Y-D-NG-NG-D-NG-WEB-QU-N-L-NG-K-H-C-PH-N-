package vn.edu.quanlyhocphan.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.quanlyhocphan.entity.GiangVien;
import vn.edu.quanlyhocphan.entity.SinhVien;
import vn.edu.quanlyhocphan.repository.GiangVienRepository;
import vn.edu.quanlyhocphan.repository.SinhVienRepository;

import java.util.Optional;

/**
 * Trien khai UserDetailsService de Spring Security co the xac thuc
 * nguoi dung tu 3 nguon khac nhau: Admin (hardcode/cấu hình), SinhVien (DB), GiangVien (DB).
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SinhVienRepository sinhVienRepository;
    private final GiangVienRepository giangVienRepository;
    // Bỏ inject PasswordEncoder ở đây để tránh circular dependency. PasswordEncoder được gọi ở config

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Kiem tra tai khoan Admin (Trong thuc te co the query tu bang Admin, o day dung hardcode cho don gian)
        if ("admin@email.com".equalsIgnoreCase(email)) {
            return User.builder()
                    .username("admin@email.com")
                    // pass là: 123456 đã được mã hoá BCrypt
                    .password("$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny")
                    .roles("ADMIN")
                    .build();
        }

        // 2. Kiem tra trong bang SinhVien
        Optional<SinhVien> sinhVienOpt = sinhVienRepository.findByEmail(email);
        if (sinhVienOpt.isPresent()) {
            SinhVien sv = sinhVienOpt.get();
            return User.builder()
                    .username(sv.getEmail())
                    .password(sv.getMatKhau())
                    .roles("SINH_VIEN")
                    .build();
        }

        // 3. Kiem tra trong bang GiangVien
        Optional<GiangVien> giangVienOpt = giangVienRepository.findByEmail(email);
        if (giangVienOpt.isPresent()) {
            GiangVien gv = giangVienOpt.get();
            return User.builder()
                    .username(gv.getEmail())
                    .password(gv.getMatKhau())
                    .roles("GIANG_VIEN")
                    .build();
        }

        // Neu khong tim thay
        throw new UsernameNotFoundException("Không tìm thấy tài khoản với email: " + email);
    }
}
