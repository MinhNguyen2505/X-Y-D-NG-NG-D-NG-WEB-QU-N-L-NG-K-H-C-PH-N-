package vn.edu.quanlyhocphan.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.quanlyhocphan.entity.*;
import vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan;
import vn.edu.quanlyhocphan.repository.NganhRepository;
import vn.edu.quanlyhocphan.service.*;
import vn.edu.quanlyhocphan.service.NguyenVongService;

import java.util.List;

/**
 * Controller cho role ADMIN (Phong dao tao).
 * Chuc nang:
 *   - Quan ly mon hoc (CRUD)
 *   - Mo lop hoc phan theo hoc ky
 *   - Quan ly sinh vien (CRUD)
 *   - Quan ly giang vien (CRUD)
 *   - Quan ly hoc ky
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final SinhVienService sinhVienService;
    private final GiangVienService giangVienService;
    private final MonHocService monHocService;
    private final HocKyService hocKyService;
    private final LopHocPhanService lopHocPhanService;
    private final NganhRepository nganhRepo;
    private final NguyenVongService nguyenVongService;

    // =================================================================
    // DASHBOARD
    // =================================================================

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("tongSinhVien", sinhVienService.findAll().size());
        model.addAttribute("tongGiangVien", giangVienService.findAll().size());
        model.addAttribute("tongMonHoc", monHocService.findAll().size());
        model.addAttribute("danhSachHocKy", hocKyService.findAll());
        return "admin/dashboard";
    }

    // =================================================================
    // QUAN LY SINH VIEN
    // =================================================================

    @GetMapping("/sinh-vien")
    public String danhSachSinhVien(Model model) {
        model.addAttribute("danhSach", sinhVienService.findAll());
        return "admin/sinh-vien/danh-sach";
    }

    @GetMapping("/sinh-vien/them")
    public String formThemSinhVien(Model model) {
        model.addAttribute("sinhVien", new SinhVien());
        model.addAttribute("danhSachNganh", nganhRepo.findAll());
        return "admin/sinh-vien/form";
    }

    @PostMapping("/sinh-vien/them")
    public String themSinhVien(@ModelAttribute SinhVien sinhVien,
                               RedirectAttributes redirectAttributes) {
        try {
            if (sinhVienService.existsByMssv(sinhVien.getMssv())) {
                redirectAttributes.addFlashAttribute("errorMsg",
                    "MSSV [" + sinhVien.getMssv() + "] da ton tai.");
                return "redirect:/admin/sinh-vien/them";
            }
            sinhVienService.save(sinhVien);
            redirectAttributes.addFlashAttribute("successMsg", "Them sinh vien thanh cong.");
        } catch (Exception e) {
            log.error("Loi them sinh vien", e);
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/sinh-vien";
    }

    @GetMapping("/sinh-vien/sua/{id}")
    public String formSuaSinhVien(@PathVariable Long id, Model model) {
        model.addAttribute("sinhVien", sinhVienService.findById(id));
        model.addAttribute("danhSachNganh", nganhRepo.findAll());
        return "admin/sinh-vien/form";
    }

    @PostMapping("/sinh-vien/sua/{id}")
    public String suaSinhVien(@PathVariable Long id,
                              @ModelAttribute SinhVien sinhVien,
                              RedirectAttributes redirectAttributes) {
        try {
            sinhVien.setId(id);
            sinhVienService.save(sinhVien);
            redirectAttributes.addFlashAttribute("successMsg", "Cap nhat sinh vien thanh cong.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/sinh-vien";
    }

    @PostMapping("/sinh-vien/xoa/{id}")
    public String xoaSinhVien(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            sinhVienService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMsg", "Xoa sinh vien thanh cong.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/sinh-vien";
    }

    // =================================================================
    // QUAN LY GIANG VIEN
    // =================================================================

    @GetMapping("/giang-vien")
    public String danhSachGiangVien(Model model) {
        model.addAttribute("danhSach", giangVienService.findAll());
        return "admin/giang-vien/danh-sach";
    }

    @GetMapping("/giang-vien/them")
    public String formThemGiangVien(Model model) {
        model.addAttribute("giangVien", new GiangVien());
        return "admin/giang-vien/form";
    }

    @PostMapping("/giang-vien/them")
    public String themGiangVien(@ModelAttribute GiangVien giangVien,
                                RedirectAttributes redirectAttributes) {
        try {
            giangVienService.save(giangVien);
            redirectAttributes.addFlashAttribute("successMsg", "Them giang vien thanh cong.");
        } catch (Exception e) {
            log.error("Loi them giang vien", e);
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/giang-vien";
    }

    @GetMapping("/giang-vien/sua/{id}")
    public String formSuaGiangVien(@PathVariable Long id, Model model) {
        model.addAttribute("giangVien", giangVienService.findById(id));
        return "admin/giang-vien/form";
    }

    @PostMapping("/giang-vien/sua/{id}")
    public String suaGiangVien(@PathVariable Long id,
                               @ModelAttribute GiangVien giangVien,
                               RedirectAttributes redirectAttributes) {
        try {
            giangVien.setId(id);
            giangVienService.save(giangVien);
            redirectAttributes.addFlashAttribute("successMsg", "Cap nhat giang vien thanh cong.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/giang-vien";
    }

    @PostMapping("/giang-vien/xoa/{id}")
    public String xoaGiangVien(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            giangVienService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMsg", "Xoa giang vien thanh cong.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/giang-vien";
    }

    // =================================================================
    // QUAN LY MON HOC
    // =================================================================

    @GetMapping("/mon-hoc")
    public String danhSachMonHoc(Model model) {
        model.addAttribute("danhSach", monHocService.findAll());
        return "admin/mon-hoc/danh-sach";
    }

    @GetMapping("/mon-hoc/them")
    public String formThemMonHoc(Model model) {
        model.addAttribute("monHoc", new MonHoc());
        return "admin/mon-hoc/form";
    }

    @PostMapping("/mon-hoc/them")
    public String themMonHoc(@ModelAttribute MonHoc monHoc,
                             RedirectAttributes redirectAttributes) {
        try {
            monHocService.save(monHoc);
            redirectAttributes.addFlashAttribute("successMsg", "Them mon hoc thanh cong.");
        } catch (Exception e) {
            log.error("Loi them mon hoc", e);
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/mon-hoc";
    }

    @GetMapping("/mon-hoc/sua/{id}")
    public String formSuaMonHoc(@PathVariable Long id, Model model) {
        MonHoc monHoc = monHocService.findById(id);
        model.addAttribute("monHoc", monHoc);
        model.addAttribute("danhSachTienQuyet", monHocService.layDanhSachTienQuyet(id));
        model.addAttribute("tatCaMonHoc", monHocService.findAll());
        return "admin/mon-hoc/form";
    }

    @PostMapping("/mon-hoc/sua/{id}")
    public String suaMonHoc(@PathVariable Long id, @ModelAttribute MonHoc monHoc,
                            RedirectAttributes redirectAttributes) {
        try {
            monHoc.setId(id);
            monHocService.save(monHoc);
            redirectAttributes.addFlashAttribute("successMsg", "Cap nhat mon hoc thanh cong.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/mon-hoc";
    }

    @PostMapping("/mon-hoc/xoa/{id}")
    public String xoaMonHoc(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            monHocService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMsg", "Xoa mon hoc thanh cong.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/mon-hoc";
    }

    /** Them mon tien quyet */
    @PostMapping("/mon-hoc/{id}/tien-quyet")
    public String themTienQuyet(@PathVariable Long id,
                                @RequestParam Long monTienQuyetId,
                                RedirectAttributes redirectAttributes) {
        try {
            monHocService.themTienQuyet(id, monTienQuyetId);
            redirectAttributes.addFlashAttribute("successMsg", "Them mon tien quyet thanh cong.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/mon-hoc/sua/" + id;
    }

    // =================================================================
    // QUAN LY HOC KY
    // =================================================================

    @GetMapping("/hoc-ky")
    public String danhSachHocKy(Model model) {
        model.addAttribute("danhSach", hocKyService.findAll());
        return "admin/hoc-ky/danh-sach";
    }

    @GetMapping("/hoc-ky/them")
    public String formThemHocKy(Model model) {
        model.addAttribute("hocKy", new HocKy());
        return "admin/hoc-ky/form";
    }

    @PostMapping("/hoc-ky/them")
    public String themHocKy(@ModelAttribute HocKy hocKy,
                            RedirectAttributes redirectAttributes) {
        try {
            hocKyService.save(hocKy);
            redirectAttributes.addFlashAttribute("successMsg", "Tao hoc ky thanh cong.");
        } catch (Exception e) {
            log.error("Loi tao hoc ky", e);
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/hoc-ky";
    }

    @GetMapping("/hoc-ky/sua/{id}")
    public String formSuaHocKy(@PathVariable Long id, Model model) {
        model.addAttribute("hocKy", hocKyService.findById(id));
        return "admin/hoc-ky/form";
    }

    @PostMapping("/hoc-ky/sua/{id}")
    public String suaHocKy(@PathVariable Long id, @ModelAttribute HocKy hocKy,
                           RedirectAttributes redirectAttributes) {
        try {
            hocKy.setId(id);
            hocKyService.save(hocKy);
            redirectAttributes.addFlashAttribute("successMsg", "Cap nhat hoc ky thanh cong.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/hoc-ky";
    }

    // =================================================================
    // MO LOP HOC PHAN
    // =================================================================

    @GetMapping("/lop-hoc-phan")
    public String danhSachLop(@RequestParam(required = false) Long hocKyId, Model model) {
        model.addAttribute("danhSachHocKy", hocKyService.findAll());
        if (hocKyId != null) {
            model.addAttribute("hocKyChon", hocKyService.findById(hocKyId));
            model.addAttribute("danhSachLop",
                lopHocPhanService.findByHocKyId(hocKyId, null));
        }
        return "admin/lop-hoc-phan/danh-sach";
    }

    @GetMapping("/lop-hoc-phan/them")
    public String formThemLop(Model model) {
        model.addAttribute("lopHocPhan", new LopHocPhan());
        model.addAttribute("danhSachHocKy", hocKyService.findAll());
        model.addAttribute("danhSachMonHoc", monHocService.findAll());
        model.addAttribute("danhSachGiangVien", giangVienService.findAll());
        return "admin/lop-hoc-phan/form";
    }

    @PostMapping("/lop-hoc-phan/them")
    public String themLop(@ModelAttribute LopHocPhan lopHocPhan,
                          RedirectAttributes redirectAttributes) {
        try {
            lopHocPhan.setTrangThai(TrangThaiLopHocPhan.MO);
            lopHocPhanService.save(lopHocPhan);
            redirectAttributes.addFlashAttribute("successMsg", "Mo lop hoc phan thanh cong.");
        } catch (Exception e) {
            log.error("Loi mo lop hoc phan", e);
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/lop-hoc-phan";
    }

    @GetMapping("/lop-hoc-phan/sua/{id}")
    public String formSuaLop(@PathVariable Long id, Model model) {
        model.addAttribute("lopHocPhan", lopHocPhanService.findById(id));
        model.addAttribute("danhSachHocKy", hocKyService.findAll());
        model.addAttribute("danhSachMonHoc", monHocService.findAll());
        model.addAttribute("danhSachGiangVien", giangVienService.findAll());
        return "admin/lop-hoc-phan/form";
    }

    @PostMapping("/lop-hoc-phan/sua/{id}")
    public String suaLop(@PathVariable Long id, @ModelAttribute LopHocPhan lopHocPhan,
                         RedirectAttributes redirectAttributes) {
        try {
            lopHocPhan.setId(id);
            lopHocPhanService.save(lopHocPhan);
            redirectAttributes.addFlashAttribute("successMsg", "Cap nhat lop hoc phan thanh cong.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/lop-hoc-phan";
    }

    /** Them lich hoc cho lop */
    @PostMapping("/lop-hoc-phan/{id}/lich-hoc")
    public String themLichHoc(@PathVariable Long id,
                              @ModelAttribute LichHoc lichHoc,
                              RedirectAttributes redirectAttributes) {
        try {
            lopHocPhanService.themLichHoc(id, lichHoc);
            redirectAttributes.addFlashAttribute("successMsg", "Them lich hoc thanh cong.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/lop-hoc-phan/sua/" + id;
    }

    // =================================================================
    // QUAN LY KE HOACH NGUYEN VONG
    // =================================================================

    @GetMapping("/nguyen-vong")
    public String danhSachKeHoach(Model model) {
        model.addAttribute("danhSach", nguyenVongService.findAllKeHoach());
        return "admin/nguyen-vong/danh-sach";
    }

    @GetMapping("/nguyen-vong/them")
    public String formThemKeHoach(Model model) {
        model.addAttribute("keHoach", new vn.edu.quanlyhocphan.entity.KeHoachNguyenVong());
        model.addAttribute("danhSachHocKy", hocKyService.findAll());
        return "admin/nguyen-vong/form";
    }

    @PostMapping("/nguyen-vong/them")
    public String themKeHoach(@ModelAttribute vn.edu.quanlyhocphan.entity.KeHoachNguyenVong keHoach,
                              RedirectAttributes redirectAttributes) {
        try {
            nguyenVongService.saveKeHoach(keHoach);
            redirectAttributes.addFlashAttribute("successMsg", "Tao ke hoach nguyen vong thanh cong.");
        } catch (Exception e) {
            log.error("Loi tao ke hoach nguyen vong", e);
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/nguyen-vong";
    }

    @GetMapping("/nguyen-vong/{id}")
    public String chiTietKeHoach(@PathVariable Long id, Model model) {
        model.addAttribute("keHoach", nguyenVongService.findKeHoachById(id));
        model.addAttribute("tatCaMonHoc", monHocService.findAll());
        model.addAttribute("danhSachDangKy", nguyenVongService.findDangKyByKeHoach(id));
        return "admin/nguyen-vong/chi-tiet";
    }

    @PostMapping("/nguyen-vong/{id}/dong")
    public String dongKeHoach(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            nguyenVongService.dongKeHoach(id);
            redirectAttributes.addFlashAttribute("successMsg", "Da dong ke hoach dang ky nguyen vong.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/nguyen-vong/" + id;
    }

    @PostMapping("/nguyen-vong/{id}/mo")
    public String moKeHoach(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            nguyenVongService.moKeHoach(id);
            redirectAttributes.addFlashAttribute("successMsg", "Da mo lai ke hoach dang ky nguyen vong.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/nguyen-vong/" + id;
    }

    @PostMapping("/nguyen-vong/{id}/them-mon")
    public String themMonVaoKeHoach(@PathVariable Long id,
                                    @RequestParam Long monHocId,
                                    RedirectAttributes redirectAttributes) {
        try {
            nguyenVongService.themMonVaoKeHoach(id, monHocId);
            redirectAttributes.addFlashAttribute("successMsg", "Da them mon hoc vao ke hoach.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/nguyen-vong/" + id;
    }

    @PostMapping("/nguyen-vong/{keHoachId}/xoa-mon/{nvMonHocId}")
    public String xoaMonKhoiKeHoach(@PathVariable Long keHoachId,
                                    @PathVariable Long nvMonHocId,
                                    RedirectAttributes redirectAttributes) {
        try {
            nguyenVongService.xoaMonKhoiKeHoach(nvMonHocId);
            redirectAttributes.addFlashAttribute("successMsg", "Da xoa mon hoc khoi ke hoach.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/nguyen-vong/" + keHoachId;
    }
}
