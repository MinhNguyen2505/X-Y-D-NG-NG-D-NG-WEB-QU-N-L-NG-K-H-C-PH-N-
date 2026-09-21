package vn.edu.quanlyhocphan.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.quanlyhocphan.entity.*;
import vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan;
import vn.edu.quanlyhocphan.repository.NganhRepository;
import vn.edu.quanlyhocphan.repository.QuanLyRepository;
import vn.edu.quanlyhocphan.service.*;
/**
 * Controller cho role QUAN_LY (Phong Dao Tao).
 * Nhiem vu: dieu hanh hoc vu hang ngay
 *   - Mo/dong lop hoc phan
 *   - Quan ly ke hoach nguyen vong + duyet NV
 *   - Duyet thi lai
 *   - Quan ly dinh huong hoc tap
 *   - Xem bao cao thong ke
 *   - Xem SV/GV (readonly)
 */
@Controller
@RequestMapping("/quan-ly")
@RequiredArgsConstructor
@Slf4j
public class QuanLyController {

    private final SinhVienService      sinhVienService;
    private final GiangVienService     giangVienService;
    private final MonHocService        monHocService;
    private final HocKyService         hocKyService;
    private final LopHocPhanService    lopHocPhanService;
    private final NguyenVongService    nguyenVongService;
    private final DinhHuongService     dinhHuongService;
    private final ThiLaiService        thiLaiService;
    private final DangKyHocPhanService dangKyHocPhanService;
    private final BaoCaoService        baoCaoService;
    private final NganhRepository      nganhRepo;
    private final QuanLyRepository     quanLyRepo;

    // ===== helper =====
    private QuanLy layQuanLyHienTai(UserDetails principal) {
        return quanLyRepo.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("Khong tim thay Quan Ly"));
    }

    @ModelAttribute
    public void themThongTinChung(@AuthenticationPrincipal UserDetails principal, Model model) {
        if (principal != null) {
            try {
                quanLyRepo.findByEmail(principal.getUsername()).ifPresent(ql -> {
                    model.addAttribute("quanLy", ql);
                });
                long nvChoDuyet = nguyenVongService.findAllKeHoach().stream()
                        .mapToLong(kh -> nguyenVongService.findDangKyByKeHoach(kh.getId()).stream()
                                .filter(d -> "CHO_DUYET".equals(d.getTrangThai())).count())
                        .sum();
                long thiLaiChoDuyet = thiLaiService.findByTrangThai("CHO_DUYET").size();
                model.addAttribute("qlNvChoDuyet", nvChoDuyet);
                model.addAttribute("qlThiLaiChoDuyet", thiLaiChoDuyet);
            } catch (Exception ignored) {}
        }
    }

    // =================================================================
    // DASHBOARD
    // =================================================================

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails principal, Model model) {
        QuanLy ql = layQuanLyHienTai(principal);
        model.addAttribute("quanLy", ql);

        long lopDangMo = lopHocPhanService.findAll().stream()
                .filter(l -> TrangThaiLopHocPhan.MO.equals(l.getTrangThai())).count();
        long thiLaiChoDuyet = thiLaiService.findByTrangThai("CHO_DUYET").size();
        long nvChoDuyet = nguyenVongService.findAllKeHoach().stream()
                .mapToLong(kh -> nguyenVongService.findDangKyByKeHoach(kh.getId()).stream()
                        .filter(d -> "CHO_DUYET".equals(d.getTrangThai())).count())
                .sum();

        model.addAttribute("tongSinhVien", sinhVienService.findAll().size());
        model.addAttribute("tongGiangVien", giangVienService.findAll().size());
        model.addAttribute("lopDangMo", lopDangMo);
        model.addAttribute("thiLaiChoDuyet", thiLaiChoDuyet);
        model.addAttribute("nvChoDuyet", nvChoDuyet);
        model.addAttribute("danhSachHocKy", hocKyService.findAll());
        return "quan-ly/dashboard";
    }

    // =================================================================
    // XEM SINH VIEN (readonly)
    // =================================================================

    @GetMapping("/sinh-vien")
    public String danhSachSinhVien(Model model) {
        model.addAttribute("danhSach", sinhVienService.findAll());
        return "quan-ly/sinh-vien";
    }

    // =================================================================
    // XEM GIANG VIEN (readonly)
    // =================================================================

    @GetMapping("/giang-vien")
    public String danhSachGiangVien(Model model) {
        model.addAttribute("danhSach", giangVienService.findAll());
        return "quan-ly/giang-vien";
    }

    // =================================================================
    // LOP HOC PHAN
    // =================================================================

    @GetMapping("/lop-hoc-phan")
    public String danhSachLop(@RequestParam(required = false) Long hocKyId, Model model) {
        model.addAttribute("danhSachHocKy", hocKyService.findAll());
        if (hocKyId != null) {
            model.addAttribute("hocKyChon", hocKyService.findById(hocKyId));
            model.addAttribute("danhSachLop", lopHocPhanService.findByHocKyId(hocKyId, null));
        }
        return "quan-ly/lop-hoc-phan/danh-sach";
    }

    @GetMapping("/lop-hoc-phan/them")
    public String formThemLop(Model model) {
        model.addAttribute("lopHocPhan", new LopHocPhan());
        model.addAttribute("danhSachHocKy", hocKyService.findAll());
        model.addAttribute("danhSachMonHoc", monHocService.findAll());
        model.addAttribute("danhSachGiangVien", giangVienService.findAll());
        return "quan-ly/lop-hoc-phan/form";
    }

    @PostMapping("/lop-hoc-phan/them")
    public String themLop(@ModelAttribute LopHocPhan lopHocPhan, RedirectAttributes ra) {
        try {
            lopHocPhan.setTrangThai(TrangThaiLopHocPhan.MO);
            lopHocPhanService.save(lopHocPhan);
            ra.addFlashAttribute("successMsg", "Mở lớp học phần thành công.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/quan-ly/lop-hoc-phan";
    }

    @GetMapping("/lop-hoc-phan/sua/{id}")
    public String formSuaLop(@PathVariable Long id, Model model) {
        model.addAttribute("lopHocPhan", lopHocPhanService.findById(id));
        model.addAttribute("danhSachHocKy", hocKyService.findAll());
        model.addAttribute("danhSachMonHoc", monHocService.findAll());
        model.addAttribute("danhSachGiangVien", giangVienService.findAll());
        model.addAttribute("danhSachSV", dangKyHocPhanService.layDanhSachSinhVienTrongLop(id));
        return "quan-ly/lop-hoc-phan/form";
    }

    @PostMapping("/lop-hoc-phan/sua/{id}")
    public String suaLop(@PathVariable Long id, @ModelAttribute LopHocPhan lopHocPhan, RedirectAttributes ra) {
        try {
            lopHocPhan.setId(id);
            lopHocPhanService.save(lopHocPhan);
            ra.addFlashAttribute("successMsg", "Cập nhật lớp học phần thành công.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/quan-ly/lop-hoc-phan";
    }

    @PostMapping("/lop-hoc-phan/{id}/lich-hoc")
    public String themLichHoc(@PathVariable Long id, @ModelAttribute LichHoc lichHoc, RedirectAttributes ra) {
        try {
            lopHocPhanService.themLichHoc(id, lichHoc);
            ra.addFlashAttribute("successMsg", "Thêm lịch học thành công.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/quan-ly/lop-hoc-phan/sua/" + id;
    }

    @PostMapping("/lop-hoc-phan/{id}/dong")
    public String dongLop(@PathVariable Long id, @RequestParam(required = false) Long hocKyId, RedirectAttributes ra) {
        try {
            LopHocPhan lhp = lopHocPhanService.findById(id);
            lhp.setTrangThai(TrangThaiLopHocPhan.DONG);
            lopHocPhanService.save(lhp);
            ra.addFlashAttribute("successMsg", "Đã đóng lớp " + lhp.getMaLopHp() + ".");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/quan-ly/lop-hoc-phan" + (hocKyId != null ? "?hocKyId=" + hocKyId : "");
    }

    @PostMapping("/lop-hoc-phan/{id}/mo")
    public String moLaiLop(@PathVariable Long id, @RequestParam(required = false) Long hocKyId, RedirectAttributes ra) {
        try {
            LopHocPhan lhp = lopHocPhanService.findById(id);
            lhp.setTrangThai(TrangThaiLopHocPhan.MO);
            lopHocPhanService.save(lhp);
            ra.addFlashAttribute("successMsg", "Đã mở lại lớp " + lhp.getMaLopHp() + ".");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/quan-ly/lop-hoc-phan" + (hocKyId != null ? "?hocKyId=" + hocKyId : "");
    }

    // =================================================================
    // NGUYEN VONG
    // =================================================================

    @GetMapping("/nguyen-vong")
    public String danhSachNV(Model model) {
        var ds = nguyenVongService.findAllKeHoach();
        java.util.Map<Long, Long> soChoDuyetMap = ds.stream().collect(java.util.stream.Collectors.toMap(
                kh -> kh.getId(),
                kh -> nguyenVongService.findDangKyByKeHoach(kh.getId()).stream()
                        .filter(d -> "CHO_DUYET".equals(d.getTrangThai())).count()
        ));
        model.addAttribute("danhSach", ds);
        model.addAttribute("danhSachKeHoach", ds);
        model.addAttribute("soChoDuyetMap", soChoDuyetMap);
        return "quan-ly/nguyen-vong/danh-sach";
    }

    @GetMapping("/nguyen-vong/them")
    public String formThemNV(Model model) {
        model.addAttribute("keHoach", new KeHoachNguyenVong());
        model.addAttribute("danhSachHocKy", hocKyService.findAll());
        return "quan-ly/nguyen-vong/form";
    }

    @PostMapping("/nguyen-vong/them")
    public String themNV(@ModelAttribute KeHoachNguyenVong keHoach, RedirectAttributes ra) {
        try {
            nguyenVongService.saveKeHoach(keHoach);
            ra.addFlashAttribute("successMsg", "Tạo kế hoạch nguyện vọng thành công.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/quan-ly/nguyen-vong";
    }

    @GetMapping("/nguyen-vong/{id}")
    public String chiTietNV(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            var keHoach = nguyenVongService.findKeHoachById(id);
            var monDaTrongKeHoach = keHoach.getDanhSachMon().stream()
                    .map(m -> m.getMonHoc().getId()).toList();
            model.addAttribute("keHoach", keHoach);
            model.addAttribute("danhSachMonHoc", monHocService.findAll());
            model.addAttribute("monDaTrongKeHoach", monDaTrongKeHoach);
            model.addAttribute("danhSachDangKy", nguyenVongService.findDangKyByKeHoach(id));
            return "quan-ly/nguyen-vong/chi-tiet";
        } catch (vn.edu.quanlyhocphan.exception.ResourceNotFoundException e) {
            ra.addFlashAttribute("errorMsg", "Kế hoạch nguyện vọng #" + id + " không tồn tại.");
            return "redirect:/quan-ly/nguyen-vong";
        }
    }

    @PostMapping("/nguyen-vong/{id}/dong")
    public String dongNV(@PathVariable Long id, RedirectAttributes ra) {
        try { nguyenVongService.dongKeHoach(id); ra.addFlashAttribute("successMsg", "Đã đóng kế hoạch."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/quan-ly/nguyen-vong/" + id;
    }

    @PostMapping("/nguyen-vong/{id}/mo")
    public String moNV(@PathVariable Long id, RedirectAttributes ra) {
        try { nguyenVongService.moKeHoach(id); ra.addFlashAttribute("successMsg", "Đã mở lại kế hoạch."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/quan-ly/nguyen-vong/" + id;
    }

    @PostMapping("/nguyen-vong/{id}/them-mon")
    public String themMonNV(@PathVariable Long id, @RequestParam Long monHocId, RedirectAttributes ra) {
        try { nguyenVongService.themMonVaoKeHoach(id, monHocId); ra.addFlashAttribute("successMsg", "Đã thêm môn."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/quan-ly/nguyen-vong/" + id;
    }

    @PostMapping("/nguyen-vong/{keHoachId}/xoa-mon/{nvMonHocId}")
    public String xoaMonNV(@PathVariable Long keHoachId, @PathVariable Long nvMonHocId, RedirectAttributes ra) {
        try { nguyenVongService.xoaMonKhoiKeHoach(nvMonHocId); ra.addFlashAttribute("successMsg", "Đã xóa môn."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/quan-ly/nguyen-vong/" + keHoachId;
    }

    @PostMapping("/nguyen-vong/{keHoachId}/duyet/{dangKyId}")
    public String duyetNV(@PathVariable Long keHoachId, @PathVariable Long dangKyId, RedirectAttributes ra) {
        try { nguyenVongService.duyetDangKy(dangKyId); ra.addFlashAttribute("successMsg", "Đã duyệt nguyện vọng."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/quan-ly/nguyen-vong/" + keHoachId;
    }

    @PostMapping("/nguyen-vong/{keHoachId}/tu-choi/{dangKyId}")
    public String tuChoiNV(@PathVariable Long keHoachId, @PathVariable Long dangKyId, RedirectAttributes ra) {
        try { nguyenVongService.tuChoiDangKy(dangKyId); ra.addFlashAttribute("successMsg", "Đã từ chối nguyện vọng."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/quan-ly/nguyen-vong/" + keHoachId;
    }

    @PostMapping("/nguyen-vong/{id}/duyet-tat-ca")
    public String duyetTatCaNV(@PathVariable Long id, RedirectAttributes ra) {
        try {
            int so = nguyenVongService.duyetTatCa(id);
            ra.addFlashAttribute("successMsg", "Đã duyệt " + so + " nguyện vọng.");
        } catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/quan-ly/nguyen-vong/" + id;
    }

    // =================================================================
    // THI LAI
    // =================================================================

    @GetMapping("/thi-lai")
    public String danhSachThiLai(@RequestParam(defaultValue = "CHO_DUYET") String trangThai, Model model) {
        model.addAttribute("danhSach", thiLaiService.findByTrangThai(trangThai));
        model.addAttribute("trangThaiFilter", trangThai);
        return "quan-ly/thi-lai/danh-sach";
    }

    @PostMapping("/thi-lai/{id}/duyet")
    public String duyetThiLai(@PathVariable Long id, RedirectAttributes ra) {
        try { thiLaiService.duyet(id); ra.addFlashAttribute("successMsg", "Đã duyệt đăng ký thi lại."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/quan-ly/thi-lai";
    }

    @PostMapping("/thi-lai/{id}/tu-choi")
    public String tuChoiThiLai(@PathVariable Long id, RedirectAttributes ra) {
        try { thiLaiService.tuChoi(id); ra.addFlashAttribute("successMsg", "Đã từ chối đăng ký thi lại."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/quan-ly/thi-lai";
    }

    @PostMapping("/thi-lai/duyet-tat-ca")
    public String duyetTatCaThiLai(RedirectAttributes ra) {
        int so = thiLaiService.duyetTatCa();
        ra.addFlashAttribute("successMsg", "Đã duyệt " + so + " đăng ký thi lại.");
        return "redirect:/quan-ly/thi-lai";
    }

    // =================================================================
    // DINH HUONG
    // =================================================================

    @GetMapping("/dinh-huong")
    public String danhSachDinhHuong(Model model) {
        model.addAttribute("danhSach", dinhHuongService.findAll());
        return "quan-ly/dinh-huong/danh-sach";
    }

    @GetMapping("/dinh-huong/them")
    public String formThemDinhHuong(Model model) {
        model.addAttribute("dinhHuong", new DinhHuong());
        model.addAttribute("danhSachNganh", nganhRepo.findAll());
        return "quan-ly/dinh-huong/form";
    }

    @PostMapping("/dinh-huong/them")
    public String themDinhHuong(@ModelAttribute DinhHuong dinhHuong, RedirectAttributes ra) {
        try { dinhHuongService.save(dinhHuong); ra.addFlashAttribute("successMsg", "Đã tạo định hướng."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/quan-ly/dinh-huong";
    }

    @GetMapping("/dinh-huong/sua/{id}")
    public String formSuaDinhHuong(@PathVariable Long id, Model model) {
        model.addAttribute("dinhHuong", dinhHuongService.findById(id));
        model.addAttribute("danhSachNganh", nganhRepo.findAll());
        model.addAttribute("danhSachDangKy", dinhHuongService.findDangKyByDinhHuong(id));
        return "quan-ly/dinh-huong/form";
    }

    @PostMapping("/dinh-huong/sua/{id}")
    public String suaDinhHuong(@PathVariable Long id, @ModelAttribute DinhHuong dinhHuong, RedirectAttributes ra) {
        try { dinhHuong.setId(id); dinhHuongService.save(dinhHuong); ra.addFlashAttribute("successMsg", "Đã cập nhật."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/quan-ly/dinh-huong";
    }

    @PostMapping("/dinh-huong/{id}/dong")
    public String dongDinhHuong(@PathVariable Long id, RedirectAttributes ra) {
        try { dinhHuongService.dong(id); ra.addFlashAttribute("successMsg", "Đã đóng định hướng."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/quan-ly/dinh-huong";
    }

    @PostMapping("/dinh-huong/{id}/mo")
    public String moDinhHuong(@PathVariable Long id, RedirectAttributes ra) {
        try { dinhHuongService.mo(id); ra.addFlashAttribute("successMsg", "Đã mở lại định hướng."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/quan-ly/dinh-huong";
    }

    // =================================================================
    // BAO CAO THONG KE
    // =================================================================

    @GetMapping("/bao-cao")
    public String baoCao(@RequestParam(required = false) Long hocKyId, Model model) {
        model.addAttribute("danhSachHocKy", hocKyService.findAll());
        if (hocKyId != null) {
            model.addAttribute("hocKyChon", hocKyService.findById(hocKyId));
            model.addAttribute("baoCao", baoCaoService.thongKeTheoHocKy(hocKyId));
        } else {
            model.addAttribute("baoCao", baoCaoService.thongKeTongQuan());
        }
        return "quan-ly/bao-cao";
    }
}
