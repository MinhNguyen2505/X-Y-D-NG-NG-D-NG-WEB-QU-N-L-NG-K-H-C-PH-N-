package vn.edu.quanlyhocphan.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.quanlyhocphan.entity.DangKyHocPhan;
import vn.edu.quanlyhocphan.entity.GiangVien;
import vn.edu.quanlyhocphan.entity.HocKy;
import vn.edu.quanlyhocphan.entity.LopHocPhan;
import vn.edu.quanlyhocphan.service.DangKyHocPhanService;
import vn.edu.quanlyhocphan.service.GiangVienService;
import vn.edu.quanlyhocphan.service.HocKyService;
import vn.edu.quanlyhocphan.service.LopHocPhanService;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller cho role GIANG_VIEN.
 * Chuc nang: xem danh sach lop, xem danh sach SV trong lop, nhap diem.
 */
@Controller
@RequestMapping("/giang-vien")
@RequiredArgsConstructor
@Slf4j
public class GiangVienController {

    private final GiangVienService giangVienService;
    private final DangKyHocPhanService dangKyService;
    private final HocKyService hocKyService;
    private final LopHocPhanService lopHocPhanService;

    private GiangVien layGiangVienHienTai(UserDetails principal) {
        return giangVienService.findByEmail(principal.getUsername());
    }

    // =================================================================
    // DASHBOARD
    // =================================================================

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails principal, Model model) {
        GiangVien gv = layGiangVienHienTai(principal);
        List<HocKy> danhSachHocKy = hocKyService.findAll();
        model.addAttribute("giangVien", gv);
        model.addAttribute("danhSachHocKy", danhSachHocKy);
        return "giang-vien/dashboard";
    }

    // =================================================================
    // DANH SACH LOP
    // =================================================================

    @GetMapping("/danh-sach-lop")
    public String xemDanhSachLop(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(required = false) Long hocKyId,
            Model model) {

        GiangVien gv = layGiangVienHienTai(principal);
        List<HocKy> danhSachHocKy = hocKyService.findAll();
        model.addAttribute("giangVien", gv);
        model.addAttribute("danhSachHocKy", danhSachHocKy);

        if (hocKyId != null) {
            HocKy hocKy = hocKyService.findById(hocKyId);
            List<LopHocPhan> danhSachLop =
                lopHocPhanService.findByGiangVienIdAndHocKyId(gv.getId(), hocKyId);
            model.addAttribute("hocKyChon", hocKy);
            model.addAttribute("danhSachLop", danhSachLop);
        }
        return "giang-vien/danh-sach-lop";
    }

    // =================================================================
    // NHAP DIEM
    // =================================================================

    /** Trang xem danh sach SV trong 1 lop de nhap diem */
    @GetMapping("/nhap-diem/{lopHocPhanId}")
    public String trangNhapDiem(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long lopHocPhanId,
            Model model) {

        GiangVien gv = layGiangVienHienTai(principal);
        LopHocPhan lhp = lopHocPhanService.findById(lopHocPhanId);

        // Kiem tra lop co thuoc ve giang vien nay khong
        if (lhp.getGiangVien() == null ||
                !lhp.getGiangVien().getId().equals(gv.getId())) {
            return "redirect:/giang-vien/danh-sach-lop?error=khong-co-quyen";
        }

        List<DangKyHocPhan> danhSachSV =
            dangKyService.layDanhSachSinhVienTrongLop(lopHocPhanId);

        model.addAttribute("giangVien", gv);
        model.addAttribute("lopHocPhan", lhp);
        model.addAttribute("danhSachSV", danhSachSV);
        return "giang-vien/nhap-diem";
    }

    /** Xu ly nhap diem cho 1 sinh vien */
    @PostMapping("/nhap-diem/{lopHocPhanId}")
    public String xuLyNhapDiem(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long lopHocPhanId,
            @RequestParam Long dangKyId,
            @RequestParam(required = false) BigDecimal diemGiuaKy,
            @RequestParam(required = false) BigDecimal diemCuoiKy,
            @RequestParam(required = false) BigDecimal diemTongKet,
            RedirectAttributes redirectAttributes) {

        try {
            dangKyService.nhapDiem(dangKyId, diemGiuaKy, diemCuoiKy, diemTongKet);
            redirectAttributes.addFlashAttribute("successMsg", "Nhap diem thanh cong.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        } catch (Exception e) {
            log.error("Loi khi nhap diem cho DangKy [{}]", dangKyId, e);
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi khi nhap diem.");
        }
        return "redirect:/giang-vien/nhap-diem/" + lopHocPhanId;
    }
}
