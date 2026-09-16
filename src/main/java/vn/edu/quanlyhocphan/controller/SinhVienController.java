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
import vn.edu.quanlyhocphan.repository.NganhRepository;
import vn.edu.quanlyhocphan.service.*;
import vn.edu.quanlyhocphan.service.NguyenVongService;
import vn.edu.quanlyhocphan.service.DinhHuongService;
import vn.edu.quanlyhocphan.service.TinChiTichLuyService;
import vn.edu.quanlyhocphan.service.ThiLaiService;
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
    private final NganhRepository nganhRepo;
    private final ThiLaiService thiLaiService;
    private final vn.edu.quanlyhocphan.repository.DangKyNguyenVongRepository dangKyNvRepo;
    private final vn.edu.quanlyhocphan.repository.DangKyThiLaiRepository thiLaiRepo;

    // -----------------------------------------------------------------
    // Helper: lay SinhVien tu Principal (email la username)
    // -----------------------------------------------------------------
    private SinhVien laySinhVienHienTai(UserDetails principal) {
        return sinhVienService.findByEmail(principal.getUsername());
    }

    private int tinhTinChiTichLuyChinhXac(SinhVien sv) {
        if (sv.getNganh() == null) return 0;
        return tinChiTichLuyService.tinhTinChiTheoKhoi(sv.getId(), sv.getNganh().getId())
                .stream().mapToInt(k -> k.getTinChiDaTichLuy()).sum();
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

        // Dang ky gan day (hien thi bảng)
        List<DangKyHocPhan> dangKyGanDay = dangKyService.layLichSuDangKy(sv.getId());

        // Tin chi tich luy — dung DB query (HOAN_THANH + diem >= 5), dong nhat voi trang Tin chi
        int tinChiTichLuy = tinhTinChiTichLuyChinhXac(sv);

        // Nguyen vong CHO_DUYET — 1 COUNT query
        long nguyenVongChoDuyet = dangKyNvRepo.countChoDuyetBySinhVienId(sv.getId());

        // Thi lai CHO_DUYET — 1 COUNT query
        long thiLaiChoDuyet = thiLaiRepo.countChoDuyetBySinhVienId(sv.getId());

        // Dinh huong dang active
        DangKyDinhHuong dinhHuongActive = null;
        if (sv.getNganh() != null) {
            dinhHuongActive = dinhHuongService
                .findActiveByNganh(sv.getId(), sv.getNganh().getId())
                .orElse(null);
        }

        model.addAttribute("sinhVien", sv);
        model.addAttribute("hocKyDangMo", hocKyDangMo);
        model.addAttribute("hocKyHienTai", hocKyHienTai);
        model.addAttribute("tinChiDaDangKy", tinChiDaDangKy);
        model.addAttribute("tinChiTichLuy", tinChiTichLuy);
        model.addAttribute("dangKyGanDay", dangKyGanDay);
        model.addAttribute("greeting", greeting);
        model.addAttribute("nguyenVongChoDuyet", nguyenVongChoDuyet);
        model.addAttribute("thiLaiChoDuyet", thiLaiChoDuyet);
        model.addAttribute("dinhHuongActive", dinhHuongActive);
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

    /** Trang dang ky tin chi: tab LHP + tab Nguyen vong + tab Ket qua NV */
    @GetMapping("/dang-ky")
    public String trangDangKy(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(required = false) Long hocKyId,
            @RequestParam(defaultValue = "lhp") String tab,
            @RequestParam(required = false) Long keHoachId,
            Model model) {

        SinhVien sv = laySinhVienHienTai(principal);
        List<HocKy> danhSachHocKy = hocKyService.findHocKyDangMoDangKy();
        model.addAttribute("sinhVien", sv);
        model.addAttribute("danhSachHocKy", danhSachHocKy);

        // % hoan thanh CTDT = tinChiTichLuy / tongTCCTDT — hien thi bat ke chon HK hay chua
        int tinChiTichLuy = tinhTinChiTichLuyChinhXac(sv);
        int tongTCCtdt = 0;
        if (sv.getNganh() != null) {
            tongTCCtdt = chuongTrinhDaoTaoRepo.searchByNganhId(sv.getNganh().getId(), "")
                .stream().mapToInt(c -> c.getMonHoc().getSoTinChi()).sum();
        }
        int phanTramCtdt = tongTCCtdt > 0 ? tinChiTichLuy * 100 / tongTCCtdt : 0;
        String mauCtdt = phanTramCtdt >= 75 ? "#15803d"
                       : phanTramCtdt >= 40 ? "#1a56db"
                       : "#6d28d9";
        model.addAttribute("tinChiTichLuy", tinChiTichLuy);
        model.addAttribute("tongTCCtdt",    tongTCCtdt);
        model.addAttribute("phanTramCtdt",  phanTramCtdt);
        model.addAttribute("mauCtdt",       mauCtdt);

        if (hocKyId != null) {
            HocKy hocKy = hocKyService.findById(hocKyId);
            List<LopHocPhan> danhSachLop =
                lopHocPhanService.findByHocKyId(hocKyId, TrangThaiLopHocPhan.MO);
            // Lay ca DA_DANG_KY lan HOAN_THANH trong HK nay
            // -> daDangKyLopIds block nút Hủy, tinChiDaChonHK tính đúng dù đã có điểm
            List<DangKyHocPhan> daDangKy =
                dangKyService.layDangKyThoiKhoaBieu(sv.getId(), hocKyId);

            // Set id cac lop da dang ky (de template kiem tra nhanh)
            Set<Long> daDangKyLopIds = daDangKy.stream()
                .map(dk -> dk.getLopHocPhan().getId())
                .collect(Collectors.toSet());

            // Set monHocId cua mon da HOAN_THANH — 1 COUNT query thay vi stream toan bo lich su
            Set<Long> monDaHoanThanhIds = dangKyRepo.findMonDaHoanThanhIds(sv.getId());

            // Set monHocId co nguyen vong DA_DUYET trong hoc ky nay — 1 query thay vi loop
            Set<Long> monNguyenVongDuyetIds = dangKyNvRepo
                .findMonNguyenVongDuyetIds(sv.getId(), hocKyId);

            model.addAttribute("hocKyChon", hocKy);
            model.addAttribute("danhSachLop", danhSachLop);
            model.addAttribute("daDangKy", daDangKy);
            model.addAttribute("daDangKyLopIds", daDangKyLopIds);
            model.addAttribute("monDaHoanThanhIds", monDaHoanThanhIds);
            model.addAttribute("monNguyenVongDuyetIds", monNguyenVongDuyetIds);

            // Tinh tong TC da dang ky trong HK hien tai (LHP) + Nguyen vong
            int tinChiDaChonLHP = daDangKy.stream()
                .mapToInt(dk -> dk.getLopHocPhan().getMonHoc().getSoTinChi())
                .sum();
            int tinChiNV = dangKyNvRepo.tinhTongTinChiNguyenVong(sv.getId(), hocKyId);
            int tinChiDaChonHK = tinChiDaChonLHP + tinChiNV;
            
            model.addAttribute("tinChiDaChonHK", tinChiDaChonHK);
            model.addAttribute("tinChiDaChonLHP", tinChiDaChonLHP);
            model.addAttribute("tinChiNV", tinChiNV);

            // % cai thien = tinChiDaChonHK / tinChiToiDa * 100
            int phanTramDK = hocKy.getTinChiToiDa() > 0
                ? tinChiDaChonHK * 100 / hocKy.getTinChiToiDa() : 0;
            model.addAttribute("phanTramDK", phanTramDK);

            // Mau progress bar tinh san (tranh expression phuc tap trong Thymeleaf)
            String mauProgress = phanTramDK >= 100 ? "#dc2626"
                               : phanTramDK >= 75  ? "#f59e0b"
                               : "#1a56db";
            model.addAttribute("mauProgress", mauProgress);

            // Con co the dang ky them bao nhieu TC
            int conLaiTC = Math.max(0, hocKy.getTinChiToiDa() - tinChiDaChonHK);
            model.addAttribute("conLaiTC", conLaiTC);

            // List thu va tiet de render grid lich hoc (tranh array literal trong Thymeleaf)
            model.addAttribute("danhSachThu",  java.util.List.of(2,3,4,5,6,7,8));
            model.addAttribute("danhSachTiet", java.util.List.of(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15));
        }

        // === TAB NGUYEN VONG ===
        model.addAttribute("tab", tab);

        // Danh sach ke hoach nguyen vong dang mo
        var danhSachKeHoach = nguyenVongService.findKeHoachDangMo();
        model.addAttribute("danhSachKeHoach", danhSachKeHoach);

        if (keHoachId != null) {
            var keHoachChon = nguyenVongService.findKeHoachById(keHoachId);
            var daDangKyIds  = nguyenVongService.findDaDangKyIds(sv.getId(), keHoachId);
            var daDangKyNV   = nguyenVongService.findLichSuDangKy(sv.getId(), keHoachId);
            model.addAttribute("keHoachChon",  keHoachChon);
            model.addAttribute("daDangKyIds",  daDangKyIds);
            model.addAttribute("daDangKyNV",   daDangKyNV);
        }

        // Ket qua: lay tat ca ke hoach (ca dong) de xem lich su
        model.addAttribute("tatCaKeHoach", nguyenVongService.findAllKeHoach());

        // Tab ket-qua-nv: neu co keHoachId thi load data ket qua
        if (keHoachId != null && ("ket-qua-nv".equals(tab) || "nguyen-vong".equals(tab))) {
            var danhSachKQ = nguyenVongService.findLichSuDangKy(sv.getId(), keHoachId);
            long choDuyet  = danhSachKQ.stream().filter(d -> "CHO_DUYET".equals(d.getTrangThai())).count();
            long daDuyet   = danhSachKQ.stream().filter(d -> "DA_DUYET".equals(d.getTrangThai())).count();
            long daHuy     = danhSachKQ.stream().filter(d -> "DA_HUY".equals(d.getTrangThai())).count();
            model.addAttribute("danhSachKetQua", danhSachKQ);
            model.addAttribute("choDuyet", choDuyet);
            model.addAttribute("daDuyet",  daDuyet);
            model.addAttribute("daHuy",    daHuy);
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
            // Lay ca DA_DANG_KY lan HOAN_THANH de hien ca HK cu
            List<DangKyHocPhan> danhSachDangKy =
                dangKyService.layDangKyThoiKhoaBieu(sv.getId(), hocKyId);
            List<LichThi> danhSachLichThi =
                lichThiRepo.findBySinhVienAndHocKy(sv.getId(), hocKyId);

            model.addAttribute("hocKyChon", hocKy);
            model.addAttribute("danhSachDangKy", danhSachDangKy);
            model.addAttribute("danhSachLichThi", danhSachLichThi);
            model.addAttribute("danhSachThu",  java.util.List.of(2,3,4,5,6,7,8));
            model.addAttribute("danhSachTiet", java.util.List.of(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15));
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
            // Bảng "đã đăng ký": hiện TẤT CẢ lịch sử kể cả DA_HUY
            var daDangKy = nguyenVongService.findLichSuDangKy(sv.getId(), keHoachId);
            model.addAttribute("keHoachChon", keHoach);
            model.addAttribute("daDangKyIds", daDangKyIds);
            model.addAttribute("daDangKy", daDangKy);

            // Tín chỉ đã đăng ký nguyện vọng (CHO_DUYET + DA_DUYET) trong học kỳ này
            Long hocKyId = keHoach.getHocKy().getId();
            int tinChiNV = dangKyNvRepo.tinhTongTinChiNguyenVong(sv.getId(), hocKyId);
            // Tín chỉ đã đăng ký học phần chính thức trong học kỳ này
            int tinChiLHP = dangKyRepo.tinhTongTinChiDaDangKy(sv.getId(), hocKyId);
            int tinChiToiDa = keHoach.getHocKy().getTinChiToiDa();
            int tinChiDaChon = tinChiNV + tinChiLHP;
            model.addAttribute("tinChiDaChon", tinChiDaChon);
            model.addAttribute("tinChiNV", tinChiNV);
            model.addAttribute("tinChiLHP", tinChiLHP);
            model.addAttribute("tinChiToiDa", tinChiToiDa);

            // Danh sách các học phần đã đăng ký trong học kỳ này (để SV xem trước khi đăng ký NV)
            var danhSachLHPDaDangKy = dangKyRepo.findDangKyThoiKhoaBieu(sv.getId(), hocKyId);
            model.addAttribute("danhSachLHPDaDangKy", danhSachLHPDaDangKy);
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
        return "redirect:/sinh-vien/dang-ky?tab=nguyen-vong&keHoachId=" + keHoachId;
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
        return "redirect:/sinh-vien/dang-ky?tab=nguyen-vong&keHoachId=" + keHoachId;
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
            // Tra cuu: lay TOAN BO lich su (ca DA_HUY) de SV biet toan trang
            var danhSachDangKy = nguyenVongService.findLichSuDangKy(sv.getId(), keHoachId);

            // Thong ke (chi dem cac ban ghi con hieu luc)
            long choDuyet = danhSachDangKy.stream().filter(d -> "CHO_DUYET".equals(d.getTrangThai())).count();
            long daDuyet  = danhSachDangKy.stream().filter(d -> "DA_DUYET".equals(d.getTrangThai())).count();
            long daHuy    = danhSachDangKy.stream().filter(d -> "DA_HUY".equals(d.getTrangThai())).count();

            model.addAttribute("keHoachChon", keHoach);
            model.addAttribute("danhSachDangKy", danhSachDangKy);
            model.addAttribute("choDuyet", choDuyet);
            model.addAttribute("daDuyet",  daDuyet);
            model.addAttribute("daHuy",    daHuy);
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
    // DANG KY THI LAI
    // =================================================================
    @GetMapping("/dang-ky-thi-lai")
    public String dangKyThiLai(@AuthenticationPrincipal UserDetails principal, Model model) {
        SinhVien sv = laySinhVienHienTai(principal);
        List<DangKyHocPhan> monChuaDat = thiLaiService.layMonChuaDat(sv.getId());
        List<vn.edu.quanlyhocphan.entity.DangKyThiLai> lichSuThiLai = thiLaiService.layLichSu(sv.getId());

        // Build map: dangKyHocPhanId -> DangKyThiLai (bản ghi mới nhất)
        // Dùng trong template để kiểm tra từng môn đã đăng ký thi lại chưa
        java.util.Map<Long, vn.edu.quanlyhocphan.entity.DangKyThiLai> thiLaiMap = new java.util.LinkedHashMap<>();
        for (vn.edu.quanlyhocphan.entity.DangKyThiLai tl : lichSuThiLai) {
            Long dkhpId = tl.getDangKyHocPhan().getId();
            // Giữ bản ghi mới nhất (list đã được sort ngayDangKy DESC từ repo)
            thiLaiMap.putIfAbsent(dkhpId, tl);
        }

        model.addAttribute("sinhVien", sv);
        model.addAttribute("monChuaDat",   monChuaDat);
        model.addAttribute("lichSuThiLai", lichSuThiLai);
        model.addAttribute("thiLaiMap",    thiLaiMap);
        return "sinh-vien/dang-ky-thi-lai";
    }

    @PostMapping("/dang-ky-thi-lai")
    public String thucHienDangKyThiLai(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam Long dangKyHocPhanId,
            RedirectAttributes ra) {
        SinhVien sv = laySinhVienHienTai(principal);
        try {
            thiLaiService.dangKy(sv.getId(), dangKyHocPhanId);
            ra.addFlashAttribute("successMsg", "Đăng ký thi lại thành công! Chờ Phòng Đào tạo xác nhận.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/sinh-vien/dang-ky-thi-lai";
    }

    @PostMapping("/huy-thi-lai")
    public String huyThiLai(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam Long dangKyHocPhanId,
            RedirectAttributes ra) {
        SinhVien sv = laySinhVienHienTai(principal);
        try {
            thiLaiService.huy(sv.getId(), dangKyHocPhanId);
            ra.addFlashAttribute("successMsg", "Đã hủy đăng ký thi lại.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/sinh-vien/dang-ky-thi-lai";
    }

    // =================================================================
    // THONG TIN CHUONG TRINH HOC
    // =================================================================

    /** Tab CTDT — chi load chuong trinh dao tao */
    @GetMapping("/chuong-trinh-hoc")
    public String xemChuongTrinhHoc(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(required = false, defaultValue = "") String keyword,
            Model model) {

        SinhVien sv = laySinhVienHienTai(principal);
        model.addAttribute("sinhVien", sv);
        model.addAttribute("keyword", keyword);
        model.addAttribute("tab", "ctdt");

        if (sv.getNganh() == null) {
            model.addAttribute("errorMsg", "Bạn chưa được gán ngành học.");
            return "sinh-vien/chuong-trinh-hoc";
        }

        Long nganhId = sv.getNganh().getId();
        model.addAttribute("nganhChon", sv.getNganh());

        // 1 query: fetch CTDT + MonHoc + KhoiKienThuc (không fetch tiền quyết ở đây)
        List<ChuongTrinhDaoTao> danhSachCTDT =
            chuongTrinhDaoTaoRepo.searchByNganhId(nganhId, keyword);

        // Batch-load tiền quyết cho tất cả môn trong 1 query riêng
        // rồi nhét vào Map để template lookup nhanh
        java.util.Map<Long, List<vn.edu.quanlyhocphan.entity.MonTienQuyet>> tienQuyetMap =
            new java.util.HashMap<>();
        if (!danhSachCTDT.isEmpty()) {
            List<Long> monHocIds = danhSachCTDT.stream()
                .map(c -> c.getMonHoc().getId())
                .distinct()
                .collect(Collectors.toList());
            List<vn.edu.quanlyhocphan.entity.MonHoc> monCoTq =
                chuongTrinhDaoTaoRepo.findMonHocWithTienQuyet(monHocIds);
            for (vn.edu.quanlyhocphan.entity.MonHoc mh : monCoTq) {
                tienQuyetMap.put(mh.getId(), mh.getCacMonTienQuyet());
            }
        }

        int  tongTC    = danhSachCTDT.stream().mapToInt(c -> c.getMonHoc().getSoTinChi()).sum();
        long soBatBuoc = danhSachCTDT.stream().filter(c -> Boolean.TRUE.equals(c.getBatBuoc())).count();
        long soTuChon  = danhSachCTDT.stream().filter(c -> !Boolean.TRUE.equals(c.getBatBuoc())).count();

        // Tinh san set id cua ban ghi dau tien moi hoc ky
        // -> template chi can check hkSeparatorIds.contains(c.id) thay vi dung index trick
        java.util.Set<Long> hkSeparatorIds = new java.util.LinkedHashSet<>();
        int prevHk = -1;
        for (ChuongTrinhDaoTao c : danhSachCTDT) {
            if (c.getHocKyThu() != prevHk) {
                hkSeparatorIds.add(c.getId());
                prevHk = c.getHocKyThu();
            }
        }

        model.addAttribute("danhSachCTDT",   danhSachCTDT);
        model.addAttribute("tienQuyetMap",   tienQuyetMap);
        model.addAttribute("hkSeparatorIds", hkSeparatorIds);
        model.addAttribute("tongTC",         tongTC);
        model.addAttribute("soBatBuoc",      soBatBuoc);
        model.addAttribute("soTuChon",       soTuChon);

        return "sinh-vien/chuong-trinh-hoc";
    }

    /** Tab Lịch sử học tập — load riêng, chỉ khi SV click sang tab */
    @GetMapping("/chuong-trinh-hoc/lich-su")
    public String xemLichSuHocTap(
            @AuthenticationPrincipal UserDetails principal,
            Model model) {

        SinhVien sv = laySinhVienHienTai(principal);
        model.addAttribute("sinhVien", sv);
        model.addAttribute("tab", "lich-su");

        if (sv.getNganh() == null) {
            model.addAttribute("errorMsg", "Bạn chưa được gán ngành học.");
            return "sinh-vien/chuong-trinh-hoc";
        }
        model.addAttribute("nganhChon", sv.getNganh());

        // 1 query: toàn bộ lịch sử đăng ký
        List<DangKyHocPhan> lichSu = dangKyService.layLichSuDangKy(sv.getId());

        // Nhóm theo học kỳ rồi build DTO
        java.util.Map<String, List<DangKyHocPhan>> rawMap = new java.util.LinkedHashMap<>();
        for (DangKyHocPhan dk : lichSu) {
            String hkKey = dk.getLopHocPhan().getHocKy().getTenHocKy();
            rawMap.computeIfAbsent(hkKey, k -> new java.util.ArrayList<>()).add(dk);
        }

        List<vn.edu.quanlyhocphan.dto.LichSuHocKyDto> danhSachHocKy = new java.util.ArrayList<>();
        for (java.util.Map.Entry<String, List<DangKyHocPhan>> e : rawMap.entrySet()) {
            List<DangKyHocPhan> dkList = e.getValue();
            int    tongTCKy  = dkList.stream().mapToInt(dk -> dk.getLopHocPhan().getMonHoc().getSoTinChi()).sum();
            double tongDiem  = dkList.stream()
                .filter(dk -> dk.getDiemTongKet() != null)
                .mapToDouble(dk -> dk.getDiemTongKet().doubleValue()
                                 * dk.getLopHocPhan().getMonHoc().getSoTinChi()).sum();
            int    tcCoDiem  = dkList.stream()
                .filter(dk -> dk.getDiemTongKet() != null)
                .mapToInt(dk -> dk.getLopHocPhan().getMonHoc().getSoTinChi()).sum();
            String gpa = tcCoDiem > 0 ? String.format("%.2f", tongDiem / tcCoDiem) : "—";

            List<vn.edu.quanlyhocphan.dto.LichSuHocKyDto.MonHocKyDto> dsMon = new java.util.ArrayList<>();
            int stt = 1;
            for (DangKyHocPhan dk : dkList) {
                java.math.BigDecimal dtk = dk.getDiemTongKet();
                dsMon.add(vn.edu.quanlyhocphan.dto.LichSuHocKyDto.MonHocKyDto.builder()
                    .stt(stt++)
                    .maMon(dk.getLopHocPhan().getMonHoc().getMaMon())
                    .tenMon(dk.getLopHocPhan().getMonHoc().getTenMon())
                    .soTinChi(dk.getLopHocPhan().getMonHoc().getSoTinChi())
                    .diemGiuaKy(dk.getDiemGiuaKy()  != null
                        ? String.format("%.1f", dk.getDiemGiuaKy().doubleValue())  : "—")
                    .diemCuoiKy(dk.getDiemCuoiKy()  != null
                        ? String.format("%.1f", dk.getDiemCuoiKy().doubleValue())  : "—")
                    .diemTongKet(dtk != null
                        ? String.format("%.1f", dtk.doubleValue()) : "—")
                    .diemChu(vn.edu.quanlyhocphan.dto.LichSuHocKyDto.tinhDiemChu(dtk))
                    .mauDiem(vn.edu.quanlyhocphan.dto.LichSuHocKyDto.tinhMauDiem(dtk))
                    .ketQua(vn.edu.quanlyhocphan.dto.LichSuHocKyDto.tinhKetQua(dtk))
                    .mauKetQua(vn.edu.quanlyhocphan.dto.LichSuHocKyDto.tinhMauKetQua(dtk))
                    .build());
            }

            danhSachHocKy.add(vn.edu.quanlyhocphan.dto.LichSuHocKyDto.builder()
                .tenHocKy(e.getKey())
                .soMon(dkList.size())
                .tongTinChi(tongTCKy)
                .gpa(gpa)
                .danhSachMon(dsMon)
                .build());
        }

        model.addAttribute("danhSachHocKy", danhSachHocKy);
        return "sinh-vien/chuong-trinh-hoc";
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

        // Dung tong tich luy tinh tu CTDT (tranh dem ca mon ngoai chuong trinh)
        int tongTichLuy = danhSachKhoi.stream()
                .mapToInt(k -> k.getTinChiDaTichLuy())
                .sum();

        // Tong so tin chi yeu cau trong CTDT
        int tongYeuCau = danhSachKhoi.stream()
                .mapToInt(k -> k.getTongSoTinChi()).sum();
        int tongBatBuoc = danhSachKhoi.stream()
                .mapToInt(k -> k.getTinChiBatBuoc()).sum();

        model.addAttribute("danhSachKhoi", danhSachKhoi);
        model.addAttribute("tongTichLuy",  tongTichLuy);
        model.addAttribute("tongYeuCau",   tongYeuCau);
        model.addAttribute("tongBatBuoc",  tongBatBuoc);

        int tongMon = danhSachKhoi.stream().mapToInt(k -> k.getDanhSachMon().size()).sum();
        model.addAttribute("tongMon", tongMon);

        // Tinh san phan tram de tranh #numbers.formatDecimal trong Thymeleaf SpEL
        String phanTramTichLuy = tongYeuCau > 0
            ? String.format("%.1f%%", tongTichLuy * 100.0 / tongYeuCau)
            : "0%";
        int phanTramInt = tongYeuCau > 0 ? tongTichLuy * 100 / tongYeuCau : 0;
        model.addAttribute("phanTramTichLuy", phanTramTichLuy);
        model.addAttribute("phanTramInt",     phanTramInt);
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

        // Chỉ tính GPA trên môn HOAN_THANH có điểm (loại DA_HUY và DA_DANG_KY chưa có điểm)
        double tongDiemNhanTC = lichSu.stream()
            .filter(dk -> dk.getTrangThai() == vn.edu.quanlyhocphan.enums.TrangThaiDangKy.HOAN_THANH
                       && dk.getDiemTongKet() != null)
            .mapToDouble(dk -> dk.getDiemTongKet().doubleValue()
                             * dk.getLopHocPhan().getMonHoc().getSoTinChi())
            .sum();
        int tongTinChi = lichSu.stream()
            .filter(dk -> dk.getTrangThai() == vn.edu.quanlyhocphan.enums.TrangThaiDangKy.HOAN_THANH
                       && dk.getDiemTongKet() != null)
            .mapToInt(dk -> dk.getLopHocPhan().getMonHoc().getSoTinChi())
            .sum();
        double gpa = tongTinChi > 0 ? tongDiemNhanTC / tongTinChi : 0.0;

        // Tổng tín chỉ tích lũy từ DB — đồng nhất với trang Tín chỉ tích lũy
        int tinChiTichLuy = tinhTinChiTichLuyChinhXac(sv);

        // Build thiLaiMap: dangKyHocPhanId -> DangKyThiLai (ban ghi moi nhat)
        // De template kiem tra moi mon co dang ky thi lai chua
        List<vn.edu.quanlyhocphan.entity.DangKyThiLai> lichSuThiLai =
            thiLaiService.layLichSu(sv.getId());
        java.util.Map<Long, vn.edu.quanlyhocphan.entity.DangKyThiLai> thiLaiMap =
            new java.util.LinkedHashMap<>();
        for (vn.edu.quanlyhocphan.entity.DangKyThiLai tl : lichSuThiLai) {
            thiLaiMap.putIfAbsent(tl.getDangKyHocPhan().getId(), tl);
        }

        model.addAttribute("sinhVien", sv);
        model.addAttribute("lichSu", lichSu);
        model.addAttribute("gpa", String.format("%.2f", gpa));
        model.addAttribute("tinChiTichLuy", tinChiTichLuy);
        model.addAttribute("thiLaiMap", thiLaiMap);
        return "sinh-vien/ket-qua-hoc-tap";
    }
}


