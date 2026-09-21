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
import vn.edu.quanlyhocphan.service.DinhHuongService;

import java.util.List;import java.util.List;

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
    private final DinhHuongService dinhHuongService;
    private final vn.edu.quanlyhocphan.service.ThiLaiService thiLaiService;
    private final vn.edu.quanlyhocphan.service.DangKyHocPhanService dangKyHocPhanService;
    private final vn.edu.quanlyhocphan.service.BaoCaoService baoCaoService;
    private final vn.edu.quanlyhocphan.repository.QuanLyRepository quanLyRepo;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    // =================================================================
    // DASHBOARD
    // =================================================================

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("tongSinhVien",  sinhVienService.findAll().size());
        model.addAttribute("tongGiangVien", giangVienService.findAll().size());
        model.addAttribute("tongQuanLy",    quanLyRepo.count());
        model.addAttribute("tongMonHoc",    monHocService.findAll().size());
        model.addAttribute("danhSachHocKy", hocKyService.findAll());

        // Stat lien ket thuc te
        long lopDangMo = lopHocPhanService.findAll().stream()
            .filter(l -> vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan.MO
                             .equals(l.getTrangThai()))
            .count();
        long thiLaiChoDuyet   = thiLaiService.findByTrangThai("CHO_DUYET").size();
        long nguyenVongChoDuyet = nguyenVongService.findAllKeHoach().stream()
            .mapToLong(kh -> nguyenVongService.findDangKyByKeHoach(kh.getId()).stream()
                .filter(d -> "CHO_DUYET".equals(d.getTrangThai())).count())
            .sum();

        model.addAttribute("lopDangMo",          lopDangMo);
        model.addAttribute("thiLaiChoDuyet",      thiLaiChoDuyet);
        model.addAttribute("nguyenVongChoDuyet",  nguyenVongChoDuyet);
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
        SinhVien sv = sinhVienService.findById(id);
        sv.setMatKhau(""); // Bao mat: xoa hash mat khau de khong bao gio bi lo ra view
        model.addAttribute("sinhVien", sv);
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
        GiangVien gv = giangVienService.findById(id);
        gv.setMatKhau(""); // Bao mat: xoa hash mat khau de khong bao gio bi lo ra view
        model.addAttribute("giangVien", gv);
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
        // Danh sach SV da dang ky lop nay (hien de admin kiem tra)
        model.addAttribute("danhSachSV",
            dangKyHocPhanService.layDanhSachSinhVienTrongLop(id));
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

    /** Dong lop hoc phan (MO -> DONG) */
    @PostMapping("/lop-hoc-phan/{id}/dong")
    public String dongLop(@PathVariable Long id,
                          @RequestParam(required = false) Long hocKyId,
                          RedirectAttributes redirectAttributes) {
        try {
            LopHocPhan lhp = lopHocPhanService.findById(id);
            lhp.setTrangThai(TrangThaiLopHocPhan.DONG);
            lopHocPhanService.save(lhp);
            redirectAttributes.addFlashAttribute("successMsg", "Đã đóng lớp " + lhp.getMaLopHp() + ".");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/lop-hoc-phan" + (hocKyId != null ? "?hocKyId=" + hocKyId : "");
    }

    /** Mo lai lop hoc phan (DONG -> MO) */
    @PostMapping("/lop-hoc-phan/{id}/mo")
    public String moLaiLop(@PathVariable Long id,
                           @RequestParam(required = false) Long hocKyId,
                           RedirectAttributes redirectAttributes) {
        try {
            LopHocPhan lhp = lopHocPhanService.findById(id);
            lhp.setTrangThai(TrangThaiLopHocPhan.MO);
            lopHocPhanService.save(lhp);
            redirectAttributes.addFlashAttribute("successMsg", "Đã mở lại lớp " + lhp.getMaLopHp() + ".");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/lop-hoc-phan" + (hocKyId != null ? "?hocKyId=" + hocKyId : "");
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
    public String chiTietKeHoach(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            var keHoach = nguyenVongService.findKeHoachById(id);
            // Lay id cac mon da co trong ke hoach de loc khoi dropdown
            java.util.Set<Long> monDaTrongKeHoach = keHoach.getDanhSachMon().stream()
                .map(m -> m.getMonHoc().getId())
                .collect(java.util.stream.Collectors.toSet());
            model.addAttribute("keHoach", keHoach);
            model.addAttribute("tatCaMonHoc", monHocService.findAll());
            model.addAttribute("monDaTrongKeHoach", monDaTrongKeHoach);
            model.addAttribute("danhSachDangKy", nguyenVongService.findDangKyByKeHoach(id));
            return "admin/nguyen-vong/chi-tiet";
        } catch (vn.edu.quanlyhocphan.exception.ResourceNotFoundException e) {
            ra.addFlashAttribute("errorMsg", "Kế hoạch nguyện vọng #" + id + " không tồn tại.");
            return "redirect:/admin/nguyen-vong";
        }
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

    /** Duyet 1 dang ky nguyen vong */
    @PostMapping("/nguyen-vong/{keHoachId}/duyet/{dangKyId}")
    public String duyetDangKy(@PathVariable Long keHoachId,
                               @PathVariable Long dangKyId,
                               RedirectAttributes ra) {
        try {
            nguyenVongService.duyetDangKy(dangKyId);
            ra.addFlashAttribute("successMsg", "Da duyet dang ky nguyen vong.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/nguyen-vong/" + keHoachId;
    }

    /** Tu choi 1 dang ky nguyen vong */
    @PostMapping("/nguyen-vong/{keHoachId}/tu-choi/{dangKyId}")
    public String tuChoiDangKy(@PathVariable Long keHoachId,
                                @PathVariable Long dangKyId,
                                RedirectAttributes ra) {
        try {
            nguyenVongService.tuChoiDangKy(dangKyId);
            ra.addFlashAttribute("successMsg", "Da tu choi dang ky nguyen vong.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/nguyen-vong/" + keHoachId;
    }

    /** Duyet tat ca CHO_DUYET trong ke hoach */
    @PostMapping("/nguyen-vong/{id}/duyet-tat-ca")
    public String duyetTatCa(@PathVariable Long id, RedirectAttributes ra) {
        try {
            int so = nguyenVongService.duyetTatCa(id);
            ra.addFlashAttribute("successMsg", "Da duyet " + so + " dang ky nguyen vong.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/nguyen-vong/" + id;
    }

    // =================================================================
    // QUAN LY DANG KY THI LAI
    // =================================================================

    @GetMapping("/thi-lai")
    public String danhSachThiLai(
            @RequestParam(defaultValue = "CHO_DUYET") String trangThai,
            Model model) {
        model.addAttribute("danhSach", thiLaiService.findByTrangThai(trangThai));
        model.addAttribute("trangThaiFilter", trangThai);
        return "admin/thi-lai/danh-sach";
    }

    @PostMapping("/thi-lai/{id}/duyet")
    public String duyetThiLai(@PathVariable Long id, RedirectAttributes ra) {
        try {
            thiLaiService.duyet(id);
            ra.addFlashAttribute("successMsg", "Đã duyệt đăng ký thi lại.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/thi-lai";
    }

    @PostMapping("/thi-lai/{id}/tu-choi")
    public String tuChoiThiLai(@PathVariable Long id, RedirectAttributes ra) {
        try {
            thiLaiService.tuChoi(id);
            ra.addFlashAttribute("successMsg", "Đã từ chối đăng ký thi lại.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/thi-lai";
    }

    @PostMapping("/thi-lai/duyet-tat-ca")
    public String duyetTatCaThiLai(RedirectAttributes ra) {
        int so = thiLaiService.duyetTatCa();
        ra.addFlashAttribute("successMsg", "Đã duyệt " + so + " đăng ký thi lại.");
        return "redirect:/admin/thi-lai";
    }

    // =================================================================
    // QUAN LY DINH HUONG HOC TAP
    // =================================================================

    @GetMapping("/dinh-huong")
    public String danhSachDinhHuong(Model model) {
        model.addAttribute("danhSach", dinhHuongService.findAll());
        return "admin/dinh-huong/danh-sach";
    }

    @GetMapping("/dinh-huong/them")
    public String formThemDinhHuong(Model model) {
        model.addAttribute("dinhHuong", new vn.edu.quanlyhocphan.entity.DinhHuong());
        model.addAttribute("danhSachNganh", nganhRepo.findAll());
        return "admin/dinh-huong/form";
    }

    @PostMapping("/dinh-huong/them")
    public String themDinhHuong(
            @ModelAttribute vn.edu.quanlyhocphan.entity.DinhHuong dinhHuong,
            RedirectAttributes ra) {
        try {
            dinhHuongService.save(dinhHuong);
            ra.addFlashAttribute("successMsg", "Tao dinh huong thanh cong.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/dinh-huong";
    }

    @GetMapping("/dinh-huong/sua/{id}")
    public String formSuaDinhHuong(@PathVariable Long id, Model model) {
        model.addAttribute("dinhHuong", dinhHuongService.findById(id));
        model.addAttribute("danhSachNganh", nganhRepo.findAll());
        model.addAttribute("danhSachDangKy", dinhHuongService.findDangKyByDinhHuong(id));
        return "admin/dinh-huong/form";
    }

    @PostMapping("/dinh-huong/sua/{id}")
    public String suaDinhHuong(@PathVariable Long id,
                               @ModelAttribute vn.edu.quanlyhocphan.entity.DinhHuong dinhHuong,
                               RedirectAttributes ra) {
        try {
            dinhHuong.setId(id);
            dinhHuongService.save(dinhHuong);
            ra.addFlashAttribute("successMsg", "Cap nhat dinh huong thanh cong.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/admin/dinh-huong";
    }

    @PostMapping("/dinh-huong/{id}/dong")
    public String dongDinhHuong(@PathVariable Long id, RedirectAttributes ra) {
        try { dinhHuongService.dong(id); ra.addFlashAttribute("successMsg", "Da dong dinh huong."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/admin/dinh-huong";
    }

    @PostMapping("/dinh-huong/{id}/mo")
    public String moDinhHuong(@PathVariable Long id, RedirectAttributes ra) {
        try { dinhHuongService.mo(id); ra.addFlashAttribute("successMsg", "Da mo lai dinh huong."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/admin/dinh-huong";
    }

    // =================================================================
    // QUAN LY CAN BO PHONG DAO TAO (ROLE_QUAN_LY)
    // =================================================================

    @GetMapping("/quan-ly")
    public String danhSachQuanLy(Model model) {
        model.addAttribute("danhSach", quanLyRepo.findAll());
        return "admin/quan-ly/danh-sach";
    }

    @GetMapping("/quan-ly/them")
    public String formThemQuanLy(Model model) {
        model.addAttribute("quanLy", new QuanLy());
        return "admin/quan-ly/form";
    }

    @PostMapping("/quan-ly/them")
    public String themQuanLy(@ModelAttribute QuanLy quanLy, RedirectAttributes ra) {
        try {
            if (quanLyRepo.existsByEmail(quanLy.getEmail())) {
                ra.addFlashAttribute("errorMsg", "Email [" + quanLy.getEmail() + "] đã tồn tại.");
                return "redirect:/admin/quan-ly/them";
            }
            if (quanLy.getMatKhau() != null && !quanLy.getMatKhau().isBlank()) {
                quanLy.setMatKhau(passwordEncoder.encode(quanLy.getMatKhau()));
            }
            quanLyRepo.save(quanLy);
            ra.addFlashAttribute("successMsg", "Thêm cán bộ quản lý thành công.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/quan-ly";
    }

    @GetMapping("/quan-ly/sua/{id}")
    public String formSuaQuanLy(@PathVariable Long id, Model model) {
        QuanLy ql = quanLyRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy Quản lý"));
        ql.setMatKhau(""); // Bao mat: xoa hash mat khau de khong bao gio bi lo ra view
        model.addAttribute("quanLy", ql);
        return "admin/quan-ly/form";
    }

    @PostMapping("/quan-ly/sua/{id}")
    public String suaQuanLy(@PathVariable Long id, @ModelAttribute QuanLy quanLy, RedirectAttributes ra) {
        try {
            QuanLy old = quanLyRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy Quản lý"));
            old.setHoTen(quanLy.getHoTen());
            old.setEmail(quanLy.getEmail());
            old.setSoDienThoai(quanLy.getSoDienThoai());
            old.setDonVi(quanLy.getDonVi());
            if (quanLy.getMatKhau() != null && !quanLy.getMatKhau().isBlank()) {
                old.setMatKhau(passwordEncoder.encode(quanLy.getMatKhau()));
            }
            quanLyRepo.save(old);
            ra.addFlashAttribute("successMsg", "Cập nhật cán bộ quản lý thành công.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/quan-ly";
    }

    @PostMapping("/quan-ly/xoa/{id}")
    public String xoaQuanLy(@PathVariable Long id, RedirectAttributes ra) {
        try {
            quanLyRepo.deleteById(id);
            ra.addFlashAttribute("successMsg", "Đã xóa cán bộ quản lý.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/quan-ly";
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
        return "admin/bao-cao";
    }
}
