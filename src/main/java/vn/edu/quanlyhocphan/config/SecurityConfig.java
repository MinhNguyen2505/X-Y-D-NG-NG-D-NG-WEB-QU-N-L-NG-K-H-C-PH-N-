package vn.edu.quanlyhocphan.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Cau hinh Spring Security.
 *
 * - PasswordEncoder: dung BCrypt ma hoa mat khau SV va GV.
 * - SecurityFilterChain: phan quyen theo 3 role (SINH_VIEN, GIANG_VIEN, ADMIN).
 *   URL pattern ro rang de Controller de biet path nao cho role nao.
 *
 * NOTE: UserDetailsService se duoc cau hinh o buoc 8 (Bước 8 - Bảo mật).
 * Hien tai dung cau hinh HTTP security de app co the chay truoc.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * BCryptPasswordEncoder la chuan de ma hoa mat khau.
     * Duoc inject vao SinhVienServiceImpl va GiangVienServiceImpl.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Cac URL public (dang nhap, tai nguyen tinh)
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()

                // Admin: quan ly toan bo he thong
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Giang vien: xem lop, nhap diem
                .requestMatchers("/giang-vien/**").hasRole("GIANG_VIEN")

                // Sinh vien: dang ky, xem TKB, xem ket qua
                .requestMatchers("/sinh-vien/**").hasRole("SINH_VIEN")

                // Con lai phai dang nhap
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
}
