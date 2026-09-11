package vn.edu.quanlyhocphan.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import vn.edu.quanlyhocphan.entity.SinhVien;
import vn.edu.quanlyhocphan.repository.DangKyNguyenVongRepository;
import vn.edu.quanlyhocphan.repository.DangKyThiLaiRepository;
import vn.edu.quanlyhocphan.service.SinhVienService;

/**
 * Inject badge counts vao model cho tat ca request /sinh-vien/**
 * Dung 2 COUNT query nhe thay vi load toan bo list roi dem.
 */
@Component
@RequiredArgsConstructor
public class SinhVienBadgeInterceptor implements HandlerInterceptor {

    private final SinhVienService            sinhVienService;
    private final DangKyNguyenVongRepository dangKyNvRepo;
    private final DangKyThiLaiRepository     thiLaiRepo;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView mav) {
        if (mav == null) return;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) return;

        String uri = request.getRequestURI();
        if (!uri.startsWith("/sinh-vien")) return;

        try {
            SinhVien sv = sinhVienService.findByEmail(auth.getName());

            // 2 COUNT queries — nhe hon nhieu so voi flatMap loop truoc day
            long badgeThiLai     = thiLaiRepo.countChoDuyetBySinhVienId(sv.getId());
            long badgeNguyenVong = dangKyNvRepo.countChoDuyetBySinhVienId(sv.getId());

            mav.addObject("badgeThiLai",     badgeThiLai);
            mav.addObject("badgeNguyenVong", badgeNguyenVong);

            // Neu sinhVien chua duoc set boi controller thi set o day
            if (!mav.getModel().containsKey("sinhVien")) {
                mav.addObject("sinhVien", sv);
            }
        } catch (Exception ignored) {
            // SV chua ton tai hoac loi khac -> bo qua, khong lam hong request
        }
    }
}
