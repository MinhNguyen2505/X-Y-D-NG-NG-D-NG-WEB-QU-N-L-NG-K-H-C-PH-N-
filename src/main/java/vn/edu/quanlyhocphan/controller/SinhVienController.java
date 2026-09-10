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
import vn.edu.quanlyhocphan.exception.*;
import vn.edu.quanlyhocphan.repository.ChuongTrinhDaoTaoRepository;
import vn.edu.quanlyhocphan.repository.DangKyHocPhanRepository;
import vn.edu.quanlyhocphan.service.*;
import vn.edu.quanlyhocphan.service.NguyenVongService;
import vn.edu.quanlyhocphan.service.DinhHuongService;
import vn.edu.quanlyhocphan.service.TinChiTichLuyService;
import vn.edu.quanlyhocphan.repository.LichThiRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller cho role SINH_VIEN.
 * Tat ca URL bat dau bang /sinh-vien/**
 * Chuc nang: xem CTDT, dang ky, huy, xem TKB, xem ket qua.
 */
@Controller
@RequestMapping("/sinh-vien")
@RequiredArgsConstructor
@Slf4j
public class SinhVienController {

    private final SinhVienService sinhVienService;
    private final DangKyHocPhanService dangKyService;
    private final DangKyHocPhanRepository dangKyRepo;
    private final HocKyService hocKyService;
    private final LopHocPhanService lopHocPhanService;
    private final ChuongTrinhDaoTaoRepository chuongTrinhDaoTaoRepo;
    private final NguyenVongService nguyenVongService;
    private final DinhHuongService dinhHuongService;
    private final TinChiTichLuyService tinChiTichLuyService;
    private final LichThiRepository lichThiRepo;

    // -----------------------------------------------------------------
    // Helper: lay SinhVien tu Principal (email la username)
    // -----------------------------------------------------------------
    private SinhVien laySinhVienHienTai(UserDetails principal) {
        return sinhVienService.findByEmail(principal.getUsername());
    }

    // =================================================================
    // DASHBOARD
    // =================================================================

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails principal, Model model) {
        SinhVien sv = laySinhVienHienTai(principal);
        List<HocKy> hocKyDangMo = hocKyService.findHocKyDangMoDangKy();

        // Loi chao theo gio trong ngay
        int hour = java.time.LocalTime.now().getHour();
        String greeting = hour < 12 ? "Chào buổi sáng,"
                        : hour < 18 ? "Chào buổi chiều,"
                        : "Chào buổi tối,";

        // Hoc ky dang mo dang ky (lay cai dau tien neu co)
        HocKy hocKyHienTai = hocKyDangMo.isEmpty() ? null : hocKyDangMo.get(0);

        // Tong tin chi da dang ky trong hoc ky hien tai
        int tinChiDaDangKy = 0;
        if (hocKyHienTai != null) {
            tinChiDaDangKy = dangKyRepo.tinhTongTinChiDaDangKy(sv.getId(), hocKyHienTai.getId());
        }

        // Dang ky gan day (tat ca hoc ky, lay 5 ban ghi moi nhat)
        List<DangKyHocPhan> dangKyGanDay = dangKyService.layLichSuDangKy(sv.getId());

        model.addAttribute("sinhVien", sv);
        model.addAttribute("hocKyDangMo", hocKyDangMo);
        model.addAttribute("hocKyHienTai", hocKyHienTai);
        model.addAttribute("tinChiDaDangKy", tinChiDaDangKy);
        model.addAttribute("dangKyGanDay", dangKyGanDay);
        model.addAttribute("greeting", greeting);
        return "sinh-vien/dashboard";
    }

    // =================================================================
    // CHUONG TRINH DAO TAO
    // =================================================================

    @GetMapping("/chuong-trinh-dao-tao")
    public String xemChuongTrinhDaoTao(
            @AuthenticationPrincipal UserDetails principal, Model model) {
        SinhVien sv = laySinhVienHienTai(principal);
        model.addAttribute("sinhVien", sv);

        if (sv.getNganh() == null) {
            model.addAttribute("error", "Ban chua duoc gan nganh hoc.");
            return "sinh-vien/chuong-trinh-dao-tao";
        }

        List<ChuongTrinhDaoTao> ctdt =
            chuongTrinhDaoTaoRepo.findByNganhIdWithMonHoc(sv.getNganh().getId());
        model.addAttribute("danhSachCTDT", ctdt);
        return "sinh-vien/chuong-trinh-dao-tao";
    }

    // =================================================================
    // DANG KY HOC PHAN
    // =================================================================

    /** Trang chon hoc ky va xem danh sach lop mo dang ky */
    @GetMapping("/dang-ky")
    public String trangDangKy(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(required = false) Long hocKyId,
            Model model) {

        SinhVien sv = laySinhVienHienTai(principal);
        List<HocKy> danhSachHocKy = hocKyService.findHocKyDangMoDangKy();
        model.addAttribute("sinhVien", sv);
        model.addAttribute("danhSachHocKy", danhSachHocKy);

        if (hocKyId != null) {
            HocKy hocKy = hocKyService.findById(hocKyId);
            List<LopHocPhan> danhSachLop =
                lopHocPhanService.findByHocKyId(hocKyId, TrangThaiLopHocPhan.MO);
            List<DangKyHocPhan> daDangKy =
                dangKyService.layDangKyHienTai(sv.getId(), hocKyId);

            // Set id cac lop da dang ky (de template kiem tra nhanh)
            Set<Long> daDangKyLopIds = daDangKy.stream()
                .map(dk -> dk.getLopHocPhan().getId())
                .collect(Collectors.toSet());

            model.addAttribute("hocKyChon", hocKy);
            model.addAttribute("danhSachLop", danhSachLop);
            model.addAttribute("daDangKy", daDangKy);
            model.addAttribute("daDangKyLopIds", daDangKyLopIds);
        }
        return "sinh-vien/dang-ky";
    }

    /** Thuc hien dang ky hoc phan — bat tat ca 6 exception rieng biet */
    @PostMapping("/dang-ky")
    public String thucHienDangKy(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam Long lopHocPhanId,
            @RequestParam Long hocKyId,
            RedirectAttributes redirectAttributes) {

        SinhVien sv = laySinhVienHienTai(principal);
        try {
            dangKyService.dangKy(sv.getId(), lopHocPhanId);
            redirectAttributes.addFlashAttribute("successMsg", "Dang ky hoc phan thanh cong!");

        } catch (HetChoException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        } catch (TrungLichException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        } catch (ThieuTienQuyetException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        } catch (VuotTinChiException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        } catch (TrungLopException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        } catch (NgoaiThoiGianDangKyException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        } catch (Exception e) {
            log.error("Loi khong xac dinh khi dang ky LHP [{}]", lopHocPhanId, e);
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi xay ra. Vui long thu lai sau.");
        }
        return "redirect:/sinh-vien/dang-ky?hocKyId=" + hocKyId;
    }

    /** Huy dang ky hoc phan */
    @PostMapping("/huy-dang-ky")
    public String huyDangKy(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam Long lopHocPhanId,
            @RequestParam Long hocKyId,
            RedirectAttributes redirectAttributes) {

        SinhVien sv = laySinhVienHienTai(principal);
        try {
            dangKyService.huyDangKy(sv.getId(), lopHocPhanId);
            redirectAttributes.addFlashAttribute("successMsg", "Huy dang ky thanh cong.");
        } catch (NgoaiThoiGianDangKyException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        } catch (Exception e) {
            log.error("Loi khi huy dang ky LHP [{}]", lopHocPhanId, e);
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi xay ra khi huy dang ky.");
        }
        return "redirect:/sinh-vien/dang-ky?hocKyId=" + hocKyId;
    }

    // =================================================================
    // THOI KHOA BIEU (lich hoc + lich thi)
    // =================================================================

    @GetMapping("/thoi-khoa-bieu")
    public String xemThoiKhoaBieu(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(required = false) Long hocKyId,
            @RequestParam(defaultValue = "lich-hoc") String tab,
            Model model) {

        SinhVien sv = laySinhVienHienTai(principal);
        List<HocKy> danhSachHocKy = hocKyService.findAll();
        model.addAttribute("sinhVien", sv);
        model.addAttribute("danhSachHocKy", danhSachHocKy);
        model.addAttribute("tab", tab);

        if (hocKyId != null) {
            HocKy hocKy = hocKyService.findById(hocKyId);
            // Lich hoc: DangKyHocPhan kem LopHocPhan + LichHoc
            List<DangKyHocPhan> danhSachDangKy =
                dangKyService.layDangKyHienTai(sv.getId(), hocKyId);
            // Lich thi: tat ca lich thi cua cac lop SV dang hoc trong hoc ky nay
            List<LichThi> danhSachLichThi =
                lichThiRepo.findBySinhVienAndHocKy(sv.getId(), hocKyId);

            model.addAttribute("hocKyChon", hocKy);
            model.addAttribute("danhSachDangKy", danhSachDangKy);
            model.addAttribute("danhSachLichThi", danhSachLichThi);
        }
        return "sinh-vien/thoi-khoa-bieu";
    }

    // =================================================================
    // DANG KY NGUYEN VONG
    // =================================================================
    @GetMapping("/dang-ky-nguyen-vong")
    public String dangKyNguyenVong(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(required = false) Long keHoachId,
            Model model) {
        SinhVien sv = laySinhVienHienTai(principal);
        model.addAttribute("sinhVien", sv);
        model.addAttribute("danhSachKeHoach", nguyenVongService.findKeHoachDangMo());

        if (keHoachId != null) {
            var keHoach = nguyenVongService.findKeHoachById(keHoachId);
            var daDangKyIds = nguyenVongService.findDaDangKyIds(sv.getId(), keHoachId);
            var daDangKy = nguyenVongService.findDangKyCuaSinhVien(sv.getId(), keHoachId);
            model.addAttribute("keHoachChon", keHoach);
            model.addAttribute("daDangKyIds", daDangKyIds);
            model.addAttribute("daDangKy", daDangKy);
        }
        return "sinh-vien/dang-ky-nguyen-vong";
    }

    @PostMapping("/dang-ky-nguyen-vong")
    public String thucHienDangKyNguyenVong(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam Long keHoachId,
            @RequestParam List<Long> nguyenVongMonHocIds,
            RedirectAttributes redirectAttributes) {
        SinhVien sv = laySinhVienHienTai(principal);
        int thanhCong = 0;
        int loi = 0;
        for (Long nvMonHocId : nguyenVongMonHocIds) {
            try {
                nguyenVongService.dangKy(sv.getId(), nvMonHocId);
                thanhCong++;
            } catch (Exception e) {
                log.warn("Loi dang ky nguyen vong: {}", e.getMessage());
                loi++;
            }
        }
        if (thanhCong > 0)
            redirectAttributes.addFlashAttribute("successMsg",
                "Dang ky thanh cong " + thanhCong + " nguyen vong.");
        if (loi > 0)
            redirectAttributes.addFlashAttribute("errorMsg",
                loi + " nguyen vong bi loi (co the da dang ky truoc do).");
        return "redirect:/sinh-vien/dang-ky-nguyen-vong?keHoachId=" + keHoachId;
    }

    @PostMapping("/huy-nguyen-vong")
    public String huyNguyenVong(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam Long nguyenVongMonHocId,
            @RequestParam Long keHoachId,
            RedirectAttributes redirectAttributes) {
        SinhVien sv = laySinhVienHienTai(principal);
        try {
            nguyenVongService.huyDangKy(sv.getId(), nguyenVongMonHocId);
            redirectAttributes.addFlashAttribute("successMsg", "Da huy dang ky nguyen vong.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Co loi: " + e.getMessage());
        }
        return "redirect:/sinh-vien/dang-ky-nguyen-vong?keHoachId=" + keHoachId;
    }

    // =================================================================
    // TRA CUU KET QUA DANG KY (ket qua dang ky nguyen vong)
    // =================================================================
    @GetMapping("/tra-cuu-ket-qua")
    public String traCuuKetQua(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(required = false) Long keHoachId,
            Model model) {
        SinhVien sv = laySinhVienHienTai(principal);

        // Lay tat ca ke hoach (ca mo lan dong) de SV chon xem
        List<vn.edu.quanlyhocphan.entity.KeHoachNguyenVong> danhSachKeHoach =
            nguyenVongService.findAllKeHoach();

        model.addAttribute("sinhVien", sv);
        model.addAttribute("danhSachKeHoach", danhSachKeHoach);

        if (keHoachId != null) {
            var keHoach = nguyenVongService.findKeHoachById(keHoachId);
            var danhSachDangKy = nguyenVongService.findDangKyCuaSinhVien(sv.getId(), keHoachId);

            // Thong ke
            long choDuyet = danhSachDangKy.stream().filter(d -> "CHO_DUYET".equals(d.getTrangThai())).count();
            long daDuyet  = danhSachDangKy.stream().filter(d -> "DA_DUYET".equals(d.getTrangThai())).count();

            model.addAttribute("keHoachChon", keHoach);
            model.addAttribute("danhSachDangKy", danhSachDangKy);
            model.addAttribute("choDuyet", choDuyet);
            model.addAttribute("daDuyet",  daDuyet);
        }
        return "sinh-vien/tra-cuu-ket-qua";
    }

    // =================================================================
    // DINH HUONG HOC TAP
    // =================================================================
    @GetMapping("/dinh-huong-hoc-tap")
    public String dinhHuongHocTap(
            @AuthenticationPrincipal UserDetails principal,
            Model model) {
        SinhVien sv = laySinhVienHienTai(principal);
        model.addAttribute("sinhVien", sv);

        // Lay dinh huong dang mo theo nganh cua SV
        List<DinhHuong> danhSachDinhHuong = new java.util.ArrayList<>();
        DangKyDinhHuong daDangKy = null;

        if (sv.getNganh() != null) {
            danhSachDinhHuong = dinhHuongService.findDangMoByNganh(sv.getNganh().getId());
            daDangKy = dinhHuongService.findActiveByNganh(sv.getId(), sv.getNganh().getId())
                .orElse(null);
        }

        model.addAttribute("danhSachDinhHuong", danhSachDinhHuong);
        model.addAttribute("daDangKy", daDangKy);
        model.addAttribute("lichSuDangKy", dinhHuongService.findDangKyCuaSinhVien(sv.getId()));
        return "sinh-vien/dinh-huong-hoc-tap";
    }

    @PostMapping("/dang-ky-dinh-huong")
    public String dangKyDinhHuong(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam Long dinhHuongId,
            RedirectAttributes ra) {
        SinhVien sv = laySinhVienHienTai(principal);
        try {
            dinhHuongService.dangKy(sv.getId(), dinhHuongId);
            ra.addFlashAttribute("successMsg", "Dang ky dinh huong hoc tap thanh cong!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/sinh-vien/dinh-huong-hoc-tap";
    }

    @PostMapping("/huy-dinh-huong")
    public String huyDinhHuong(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam Long dinhHuongId,
            RedirectAttributes ra) {
        SinhVien sv = laySinhVienHienTai(principal);
        try {
            dinhHuongService.huy(sv.getId(), dinhHuongId);
            ra.addFlashAttribute("successMsg", "Da huy dang ky dinh huong.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/sinh-vien/dinh-huong-hoc-tap";
    }

    // =================================================================
    // DANG KY THI LAI (placeholder)
    // =================================================================
    @GetMapping("/dang-ky-thi-lai")
    public String dangKyThiLai(@AuthenticationPrincipal UserDetails principal, Model model) {
        SinhVien sv = laySinhVienHienTai(principal);
        // Hiển thị các môn có điểm < 5 để SV đăng ký thi lại
        List<DangKyHocPhan> monKhongDat = dangKyService.layLichSuDangKy(sv.getId())
            .stream()
            .filter(dk -> dk.getDiemTongKet() != null && dk.getDiemTongKet().doubleValue() < 5.0)
            .collect(Collectors.toList());
        model.addAttribute("sinhVien", sv);
        model.addAttribute("monKhongDat", monKhongDat);
        return "sinh-vien/dang-ky-thi-lai";
    }

    // =================================================================
    // TIN CHI TICH LUY
    // =================================================================

    @GetMapping("/tin-chi-tich-luy")
    public String xemTinChiTichLuy(
            @AuthenticationPrincipal UserDetails principal,
            Model model) {
        SinhVien sv = laySinhVienHienTai(principal);
        model.addAttribute("sinhVien", sv);

        if (sv.getNganh() == null) {
            model.addAttribute("errorMsg", "Ban chua duoc gan nganh hoc.");
            return "sinh-vien/tin-chi-tich-luy";
        }

        var danhSachKhoi = tinChiTichLuyService.tinhTinChiTheoKhoi(
                sv.getId(), sv.getNganh().getId());
        int tongTichLuy  = tinChiTichLuyService.tongTinChiDaTichLuy(
                sv.getId(), sv.getNganh().getId());

        // Tong so tin chi yeu cau trong CTDT
        int tongYeuCau = danhSachKhoi.stream()
                .mapToInt(k -> k.getTongSoTinChi()).sum();
        int tongBatBuoc = danhSachKhoi.stream()
                .mapToInt(k -> k.getTinChiBatBuoc()).sum();

        model.addAttribute("danhSachKhoi", danhSachKhoi);
        model.addAttribute("tongTichLuy",  tongTichLuy);
        model.addAttribute("tongYeuCau",   tongYeuCau);
        model.addAttribute("tongBatBuoc",  tongBatBuoc);
        return "sinh-vien/tin-chi-tich-luy";
    }

    // =================================================================
    // KET QUA HOC TAP
    // =================================================================

    @GetMapping("/ket-qua-hoc-tap")
    public String xemKetQuaHocTap(
            @AuthenticationPrincipal UserDetails principal, Model model) {

        SinhVien sv = laySinhVienHienTai(principal);
        List<DangKyHocPhan> lichSu = dangKyService.layLichSuDangKy(sv.getId());

        // Tinh GPA trung binh diem tong ket cac mon co diem
        double gpa = lichSu.stream()
            .filter(dk -> dk.getDiemTongKet() != null)
            .mapToDouble(dk -> dk.getDiemTongKet().doubleValue())
            .average()
            .orElse(0.0);

        model.addAttribute("sinhVien", sv);
        model.addAttribute("lichSu", lichSu);
        model.addAttribute("gpa", String.format("%.2f", gpa));
        return "sinh-vien/ket-qua-hoc-tap";
    }
}
