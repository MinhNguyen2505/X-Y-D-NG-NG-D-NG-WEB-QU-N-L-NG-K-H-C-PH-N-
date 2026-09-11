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
import vn.edu.quanlyhocphan.repository.DangKyThiLaiRepository;
import vn.edu.quanlyhocphan.repository.DangKyNguyenVongRepository;
import vn.edu.quanlyhocphan.service.SinhVienService;
import vn.edu.quanlyhocphan.service.NguyenVongService;

/**
 * Interceptor: inject badge counts vao model cho tat ca request /sinh-vien/**
 * De sidebar hien duoc so luong nguyen vong + thi lai cho duyet.
 */
@Component
@RequiredArgsConstructor
public class SinhVienBadgeInterceptor implements HandlerInterceptor {

    private final SinhVienService sinhVienService;
    private final vn.edu.quanlyhocphan.service.ThiLaiService thiLaiService;
    private final NguyenVongService nguyenVongService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView mav) {
        if (mav == null) return;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) return;

        String uri = request.getRequestURI();
        if (!uri.startsWith("/sinh-vien")) return;

        try {
            SinhVien sv = sinhVienService.findByEmail(auth.getName());

            // Dem thi lai cho duyet
            long badgeThiLai = thiLaiService.layLichSu(sv.getId()).stream()
                .filter(t -> "CHO_DUYET".equals(t.getTrangThai()))
                .count();

            // Dem nguyen vong cho duyet (across all ke hoach)
            long badgeNguyenVong = nguyenVongService.findAllKeHoach().stream()
                .flatMap(kh -> nguyenVongService.findLichSuDangKy(sv.getId(), kh.getId()).stream())
                .filter(d -> "CHO_DUYET".equals(d.getTrangThai()))
                .count();

            mav.addObject("badgeThiLai",     badgeThiLai);
            mav.addObject("badgeNguyenVong", badgeNguyenVong);
            mav.addObject("sinhVien",        mav.getModel().containsKey("sinhVien")
                                             ? mav.getModel().get("sinhVien") : sv);
        } catch (Exception ignored) {
            // SV chua ton tai hoac loi khac -> bo qua, khong lam hong request
        }
    }
}
