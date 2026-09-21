package vn.edu.quanlyhocphan.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.quanlyhocphan.exception.NghiepVuException;
import vn.edu.quanlyhocphan.repository.AdminRepository;
import vn.edu.quanlyhocphan.repository.DangKyNguyenVongRepository;
import vn.edu.quanlyhocphan.repository.DangKyThiLaiRepository;
import vn.edu.quanlyhocphan.repository.GiangVienRepository;
import vn.edu.quanlyhocphan.repository.QuanLyRepository;
import vn.edu.quanlyhocphan.repository.SinhVienRepository;
import vn.edu.quanlyhocphan.service.TaiKhoanService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TaiKhoanController {

    private final TaiKhoanService taiKhoanService;
    private final SinhVienRepository sinhVienRepository;
    private final GiangVienRepository giangVienRepository;
    private final QuanLyRepository quanLyRepository;
    private final AdminRepository adminRepository;
    private final DangKyNguyenVongRepository dangKyNvRepo;
    private final DangKyThiLaiRepository thiLaiRepo;

    @GetMapping("/doi-mat-khau")
    public String formDoiMatKhau(@AuthenticationPrincipal UserDetails principal, Authentication auth, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getUsername();
        String role = "SINH_VIEN";

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            role = "ADMIN";
            adminRepository.findByEmail(email).ifPresent(ad -> model.addAttribute("admin", ad));
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_QUAN_LY"))) {
            role = "QUAN_LY";
            quanLyRepository.findByEmail(email).ifPresent(ql -> model.addAttribute("quanLy", ql));
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GIANG_VIEN"))) {
            role = "GIANG_VIEN";
            giangVienRepository.findByEmail(email).ifPresent(gv -> model.addAttribute("giangVien", gv));
        } else {
            sinhVienRepository.findByEmail(email).ifPresent(sv -> {
                model.addAttribute("sinhVien", sv);
                model.addAttribute("badgeNguyenVong", dangKyNvRepo.countChoDuyetBySinhVienId(sv.getId()));
                model.addAttribute("badgeThiLai", thiLaiRepo.countChoDuyetBySinhVienId(sv.getId()));
            });
        }

        model.addAttribute("userRole", role);
        model.addAttribute("email", email);
        return "auth/doi-mat-khau";
    }

    @PostMapping("/doi-mat-khau")
    public String xuLyDoiMatKhau(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam("matKhauCu") String matKhauCu,
            @RequestParam("matKhauMoi") String matKhauMoi,
            @RequestParam("xacNhanMatKhauMoi") String xacNhanMatKhauMoi,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        try {
            taiKhoanService.doiMatKhau(principal.getUsername(), matKhauCu, matKhauMoi, xacNhanMatKhauMoi);
            redirectAttributes.addFlashAttribute("successMsg", "Đổi mật khẩu thành công! Mật khẩu mới của bạn đã có hiệu lực.");
        } catch (NghiepVuException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi khi đổi mật khẩu", e);
            redirectAttributes.addFlashAttribute("errorMsg", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/doi-mat-khau";
    }

    // Role-specific redirect shortcuts
    @GetMapping({"/sinh-vien/doi-mat-khau", "/giang-vien/doi-mat-khau", "/quan-ly/doi-mat-khau", "/admin/doi-mat-khau"})
    public String redirectDoiMatKhau() {
        return "redirect:/doi-mat-khau";
    }

    // ===== ADMIN RESET PASSWORD CHO TỪNG LOẠI TÀI KHOẢN =====

    @PostMapping("/admin/sinh-vien/reset-mat-khau/{id}")
    public String adminResetMatKhauSinhVien(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            taiKhoanService.resetMatKhau("sinh-vien", id, "123456");
            redirectAttributes.addFlashAttribute("successMsg",
                    "Đã đặt lại mật khẩu cho sinh viên về mặc định: [123456]. Sinh viên có thể đăng nhập và tự đổi mật khẩu.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi đặt lại mật khẩu: " + e.getMessage());
        }
        return "redirect:/admin/sinh-vien";
    }

    @PostMapping("/admin/giang-vien/reset-mat-khau/{id}")
    public String adminResetMatKhauGiangVien(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            taiKhoanService.resetMatKhau("giang-vien", id, "123456");
            redirectAttributes.addFlashAttribute("successMsg",
                    "Đã đặt lại mật khẩu cho giảng viên về mặc định: [123456]. Giảng viên có thể đăng nhập và tự đổi mật khẩu.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi đặt lại mật khẩu: " + e.getMessage());
        }
        return "redirect:/admin/giang-vien";
    }

    @PostMapping("/admin/quan-ly/reset-mat-khau/{id}")
    public String adminResetMatKhauQuanLy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            taiKhoanService.resetMatKhau("quan-ly", id, "123456");
            redirectAttributes.addFlashAttribute("successMsg",
                    "Đã đặt lại mật khẩu cho cán bộ quản lý về mặc định: [123456]. Cán bộ có thể đăng nhập và tự đổi mật khẩu.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi đặt lại mật khẩu: " + e.getMessage());
        }
        return "redirect:/admin/quan-ly";
    }
}
