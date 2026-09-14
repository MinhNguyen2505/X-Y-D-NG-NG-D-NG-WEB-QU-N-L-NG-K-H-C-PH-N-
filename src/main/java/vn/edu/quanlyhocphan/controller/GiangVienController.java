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
import vn.edu.quanlyhocphan.entity.DangKyThiLai;
import vn.edu.quanlyhocphan.entity.DiemDanh;
import vn.edu.quanlyhocphan.entity.GiangVien;
import vn.edu.quanlyhocphan.entity.HocKy;
import vn.edu.quanlyhocphan.entity.LopHocPhan;
import vn.edu.quanlyhocphan.service.DangKyHocPhanService;
import vn.edu.quanlyhocphan.service.DiemDanhService;
import vn.edu.quanlyhocphan.service.GiangVienService;
import vn.edu.quanlyhocphan.service.HocKyService;
import vn.edu.quanlyhocphan.service.LopHocPhanService;
import vn.edu.quanlyhocphan.service.ThiLaiService;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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
    private final ThiLaiService thiLaiService;
    private final DiemDanhService diemDanhService;

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

        // Dem tong so lop GV dang phu trach (tat ca HK)
        long tongSoLop = lopHocPhanService.findAll().stream()
            .filter(l -> l.getGiangVien() != null && l.getGiangVien().getId().equals(gv.getId()))
            .count();

        // Dem so lop dang mo
        long lopDangMo = lopHocPhanService.findAll().stream()
            .filter(l -> l.getGiangVien() != null && l.getGiangVien().getId().equals(gv.getId())
                      && vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan.MO.equals(l.getTrangThai()))
            .count();

        // Dem tong SV chua co diem tong ket trong cac lop GV phu trach
        long svChuaDiem = lopHocPhanService.findAll().stream()
            .filter(l -> l.getGiangVien() != null && l.getGiangVien().getId().equals(gv.getId())
                      && vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan.MO.equals(l.getTrangThai()))
            .flatMap(l -> dangKyService.layDanhSachSinhVienTrongLop(l.getId()).stream())
            .filter(dk -> dk.getDiemTongKet() == null)
            .count();

        model.addAttribute("giangVien", gv);
        model.addAttribute("danhSachHocKy", danhSachHocKy);
        model.addAttribute("tongSoLop", tongSoLop);
        model.addAttribute("lopDangMo", lopDangMo);
        model.addAttribute("svChuaDiem", svChuaDiem);
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

        // Tinh san so SV da co diem (tranh lambda trong Thymeleaf SpEL)
        long soDaCoDiem = danhSachSV.stream()
            .filter(dk -> dk.getDiemTongKet() != null)
            .count();

        // Build thiLaiMap: dkhpId -> DangKyThiLai moi nhat
        // Giang vien xem SV nao da dang ky thi lai de biet
        Map<Long, DangKyThiLai> thiLaiMap = new java.util.LinkedHashMap<>();
        for (DangKyHocPhan dk : danhSachSV) {
            List<DangKyThiLai> dktlList = thiLaiService.layLichSu(dk.getSinhVien().getId());
            dktlList.stream()
                .filter(tl -> tl.getDangKyHocPhan().getId().equals(dk.getId()))
                .findFirst()
                .ifPresent(tl -> thiLaiMap.put(dk.getId(), tl));
        }

        model.addAttribute("giangVien", gv);
        model.addAttribute("lopHocPhan", lhp);
        model.addAttribute("danhSachSV", danhSachSV);
        model.addAttribute("soDaCoDiem", soDaCoDiem);
        model.addAttribute("thiLaiMap", thiLaiMap);
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

    // =================================================================
    // CHI TIET LOP
    // =================================================================

    /** Trang chi tiet 1 lop: thong tin, lich hoc, danh sach SV + diem + so buoi vang */
    @GetMapping("/lop/{lopHocPhanId}")
    public String chiTietLop(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long lopHocPhanId,
            Model model) {

        GiangVien gv = layGiangVienHienTai(principal);
        LopHocPhan lhp = lopHocPhanService.findById(lopHocPhanId);

        if (lhp.getGiangVien() == null || !lhp.getGiangVien().getId().equals(gv.getId())) {
            return "redirect:/giang-vien/danh-sach-lop?error=khong-co-quyen";
        }

        List<DangKyHocPhan> danhSachSV =
            dangKyService.layDanhSachSinhVienTrongLop(lopHocPhanId);

        // Dem so buoi vang tung SV
        java.util.Map<Long, Long> soVangMap = new java.util.LinkedHashMap<>();
        for (DangKyHocPhan dk : danhSachSV) {
            soVangMap.put(dk.getId(), diemDanhService.demSoBuoiVang(dk.getId()));
        }

        long soDaCoDiem = danhSachSV.stream()
            .filter(dk -> dk.getDiemTongKet() != null).count();

        model.addAttribute("giangVien", gv);
        model.addAttribute("lopHocPhan", lhp);
        model.addAttribute("danhSachSV", danhSachSV);
        model.addAttribute("soVangMap", soVangMap);
        model.addAttribute("soDaCoDiem", soDaCoDiem);
        return "giang-vien/chi-tiet-lop";
    }

    // =================================================================
    // DIEM DANH
    // =================================================================

    /** Hien thi trang diem danh theo buoi */
    @GetMapping("/diem-danh/{lopHocPhanId}")
    public String trangDiemDanh(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long lopHocPhanId,
            @RequestParam(required = false) String ngayHoc,
            @RequestParam(required = false) Integer tietBatDau,
            @RequestParam(required = false) Integer tietKetThuc,
            Model model) {

        GiangVien gv = layGiangVienHienTai(principal);
        LopHocPhan lhp = lopHocPhanService.findById(lopHocPhanId);

        if (lhp.getGiangVien() == null || !lhp.getGiangVien().getId().equals(gv.getId())) {
            return "redirect:/giang-vien/danh-sach-lop?error=khong-co-quyen";
        }

        List<DangKyHocPhan> danhSachSV =
            dangKyService.layDanhSachSinhVienTrongLop(lopHocPhanId);

        // Neu co ngay chon, load diem danh buoi do
        LocalDate ngay = null;
        List<DiemDanh> diemDanhList = null;
        java.util.Map<Long, DiemDanh> diemDanhMap = new java.util.LinkedHashMap<>();

        if (ngayHoc != null && !ngayHoc.isBlank()) {
            try {
                ngay = LocalDate.parse(ngayHoc, DateTimeFormatter.ISO_LOCAL_DATE);
                diemDanhList = diemDanhService.layDiemDanhTheoNgay(lopHocPhanId, ngay);
                for (DiemDanh dd : diemDanhList) {
                    diemDanhMap.put(dd.getDangKyHocPhan().getId(), dd);
                }
            } catch (Exception e) {
                log.warn("Ngay hoc khong hop le: {}", ngayHoc);
            }
        }

        model.addAttribute("giangVien", gv);
        model.addAttribute("lopHocPhan", lhp);
        model.addAttribute("danhSachSV", danhSachSV);
        model.addAttribute("ngayChon", ngay);
        model.addAttribute("tietBatDau", tietBatDau != null ? tietBatDau : 1);
        model.addAttribute("tietKetThuc", tietKetThuc != null ? tietKetThuc : 3);
        model.addAttribute("diemDanhMap", diemDanhMap);
        return "giang-vien/diem-danh";
    }

    /** Xu ly luu diem danh */
    @PostMapping("/diem-danh/{lopHocPhanId}")
    public String luuDiemDanh(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long lopHocPhanId,
            @RequestParam String ngayHoc,
            @RequestParam Integer tietBatDau,
            @RequestParam Integer tietKetThuc,
            @RequestParam Map<String, String> allParams,
            RedirectAttributes ra) {

        GiangVien gv = layGiangVienHienTai(principal);
        LopHocPhan lhp = lopHocPhanService.findById(lopHocPhanId);

        if (lhp.getGiangVien() == null || !lhp.getGiangVien().getId().equals(gv.getId())) {
            return "redirect:/giang-vien/danh-sach-lop?error=khong-co-quyen";
        }

        try {
            LocalDate ngay = LocalDate.parse(ngayHoc, DateTimeFormatter.ISO_LOCAL_DATE);
            // Parse tung trang thai tu form: ten field = "dd_{dangKyId}"
            java.util.Map<Long, String> diemDanhData = new java.util.LinkedHashMap<>();
            for (Map.Entry<String, String> e : allParams.entrySet()) {
                if (e.getKey().startsWith("dd_")) {
                    Long dkId = Long.parseLong(e.getKey().substring(3));
                    diemDanhData.put(dkId, e.getValue());
                }
            }
            diemDanhService.luuDiemDanh(lopHocPhanId, ngay, tietBatDau, tietKetThuc, diemDanhData);
            ra.addFlashAttribute("successMsg", "Lưu điểm danh thành công!");
        } catch (Exception e) {
            log.error("Loi luu diem danh LHP [{}]", lopHocPhanId, e);
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/giang-vien/diem-danh/" + lopHocPhanId
               + "?ngayHoc=" + ngayHoc
               + "&tietBatDau=" + tietBatDau
               + "&tietKetThuc=" + tietKetThuc;
    }

    // =================================================================
    // XUAT EXCEL DIEM
    // =================================================================

    /** Xuat file Excel diem cua lop */
    @GetMapping("/xuat-diem/{lopHocPhanId}")
    public void xuatExcelDiem(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long lopHocPhanId,
            HttpServletResponse response) throws IOException {

        GiangVien gv = layGiangVienHienTai(principal);
        LopHocPhan lhp = lopHocPhanService.findById(lopHocPhanId);

        if (lhp.getGiangVien() == null || !lhp.getGiangVien().getId().equals(gv.getId())) {
            response.sendError(403, "Khong co quyen xuat diem lop nay");
            return;
        }

        List<DangKyHocPhan> danhSachSV =
            dangKyService.layDanhSachSinhVienTrongLop(lopHocPhanId);

        // Tao workbook Excel
        try (var wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            var sheet = wb.createSheet("Diem_" + lhp.getMaLopHp());

            // Style header
            var headerStyle = wb.createCellStyle();
            var headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(
                new org.apache.poi.xssf.usermodel.XSSFColor(
                    new byte[]{(byte)0x1a,(byte)0x56,(byte)0xdb}, null));
            headerStyle.setFillPattern(
                org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
            headerFont.setColor(org.apache.poi.ss.usermodel.IndexedColors.WHITE.getIndex());

            // Style fail (diem < 5)
            var failStyle = wb.createCellStyle();
            var failFont = wb.createFont();
            failFont.setColor(org.apache.poi.ss.usermodel.IndexedColors.RED.getIndex());
            failFont.setBold(true);
            failStyle.setFont(failFont);

            // Tieu de bảng
            var titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("BẢNG ĐIỂM LỚP: " + lhp.getMaLopHp());
            titleRow.createCell(1).setCellValue("Môn: " + lhp.getMonHoc().getTenMon());
            titleRow.createCell(3).setCellValue("Học kỳ: " + lhp.getHocKy().getTenHocKy());

            // Header cột
            var hRow = sheet.createRow(2);
            String[] headers = {"STT","MSSV","Họ và tên","Điểm GK","Điểm CK","Điểm TK","Kết quả","Số buổi vắng"};
            for (int i = 0; i < headers.length; i++) {
                var cell = hRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, i == 2 ? 8000 : 4000);
            }

            // Du lieu
            int rowNum = 3;
            int stt = 1;
            for (DangKyHocPhan dk : danhSachSV) {
                var row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(stt++);
                row.createCell(1).setCellValue(dk.getSinhVien().getMssv());
                row.createCell(2).setCellValue(dk.getSinhVien().getHoTen());

                // Diem GK
                if (dk.getDiemGiuaKy() != null) {
                    row.createCell(3).setCellValue(dk.getDiemGiuaKy().doubleValue());
                } else {
                    row.createCell(3).setCellValue("");
                }
                // Diem CK
                if (dk.getDiemCuoiKy() != null) {
                    row.createCell(4).setCellValue(dk.getDiemCuoiKy().doubleValue());
                } else {
                    row.createCell(4).setCellValue("");
                }
                // Diem TK
                if (dk.getDiemTongKet() != null) {
                    var cell = row.createCell(5);
                    cell.setCellValue(dk.getDiemTongKet().doubleValue());
                    if (dk.getDiemTongKet().compareTo(new BigDecimal("5.0")) < 0) {
                        cell.setCellStyle(failStyle);
                    }
                } else {
                    row.createCell(5).setCellValue("");
                }
                // Ket qua
                if (dk.getDiemTongKet() == null) {
                    row.createCell(6).setCellValue("Chưa có điểm");
                } else if (dk.getDiemTongKet().compareTo(new BigDecimal("5.0")) >= 0) {
                    row.createCell(6).setCellValue("Đạt");
                } else {
                    var cell = row.createCell(6);
                    cell.setCellValue("Không đạt");
                    cell.setCellStyle(failStyle);
                }
                // So buoi vang
                long soVang = diemDanhService.demSoBuoiVang(dk.getId());
                row.createCell(7).setCellValue(soVang > 0 ? soVang : 0);
            }

            // Dong tong ket
            var sumRow = sheet.createRow(rowNum + 1);
            sumRow.createCell(0).setCellValue("Tổng SV: " + danhSachSV.size());
            long daDat = danhSachSV.stream()
                .filter(d -> d.getDiemTongKet() != null
                          && d.getDiemTongKet().compareTo(new BigDecimal("5.0")) >= 0)
                .count();
            sumRow.createCell(2).setCellValue("Đạt: " + daDat + " / Không đạt: "
                + (danhSachSV.size() - daDat));

            // Xuat file
            String tenFile = "Diem_" + lhp.getMaLopHp() + "_"
                + java.time.LocalDate.now().toString().replace("-","") + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + tenFile + "\"");
            wb.write(response.getOutputStream());
        }
        log.info("GV [{}] xuat Excel diem LHP [{}]", gv.getMaGv(), lhp.getMaLopHp());
    }
}
