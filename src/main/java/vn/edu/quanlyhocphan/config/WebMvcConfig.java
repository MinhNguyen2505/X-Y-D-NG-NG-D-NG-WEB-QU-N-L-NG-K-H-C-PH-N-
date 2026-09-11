package vn.edu.quanlyhocphan.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Dang ky interceptor:
 * - SinhVienBadgeInterceptor: inject badge counts (thi lai + nguyen vong cho duyet)
 *   vao moi response cua sinh vien de sidebar hien thi so luong.
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final SinhVienBadgeInterceptor sinhVienBadgeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sinhVienBadgeInterceptor)
                .addPathPatterns("/sinh-vien/**");
    }
}
