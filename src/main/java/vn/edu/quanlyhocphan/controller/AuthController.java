package vn.edu.quanlyhocphan.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Xu ly dang nhap / dang xuat / redirect sau login theo role.
 * Spring Security tu xu ly POST /login, controller chi can GET.
 */
@Controller
public class AuthController {

    /** Trang dang nhap */
    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "Sai email hoac mat khau. Vui long thu lai.");
        }
        if (logout != null) {
            model.addAttribute("logoutMsg", "Ban da dang xuat thanh cong.");
        }
        return "auth/login";
    }

    /**
     * Redirect den trang tuong ung theo role sau khi dang nhap.
     * Spring Security chuyen den /dashboard sau login thanh cong.
     */
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        }
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_QUAN_LY"))) {
            return "redirect:/quan-ly/dashboard";
        }
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_GIANG_VIEN"))) {
            return "redirect:/giang-vien/dashboard";
        }
        return "redirect:/sinh-vien/dashboard";
    }

    @GetMapping("/")
    public String home(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }
}
