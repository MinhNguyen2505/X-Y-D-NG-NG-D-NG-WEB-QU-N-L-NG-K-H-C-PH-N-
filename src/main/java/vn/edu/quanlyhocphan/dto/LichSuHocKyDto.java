package vn.edu.quanlyhocphan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO cho tab "Lich su hoc tap" trang chuong-trinh-hoc.
 * Tinh san diem chu, mau, ket qua o controller de template khong can goi method Java.
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LichSuHocKyDto {

    private String tenHocKy;
    private int    soMon;
    private int    tongTinChi;
    private String gpa; // "7.50" hoac "—"

    private List<MonHocKyDto> danhSachMon;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class MonHocKyDto {
        private int    stt;
        private String maMon;
        private String tenMon;
        private int    soTinChi;
        private String diemGiuaKy;  // "8.0" hoac "—"
        private String diemCuoiKy;
        private String diemTongKet;
        private String diemChu;     // "A+", "B", "F", "—"
        private String mauDiem;     // "d-a", "d-b", "d-c", "d-d", "d-f", "d-null"
        private String ketQua;      // "Hoàn thành", "Không đạt", "Đang học"
        private String mauKetQua;   // CSS inline style background
    }

    // ---- Helper static methods ----
    public static String tinhDiemChu(BigDecimal d) {
        if (d == null) return "—";
        double v = d.doubleValue();
        if (v >= 9.0) return "A+";
        if (v >= 8.5) return "A";
        if (v >= 8.0) return "B+";
        if (v >= 7.0) return "B";
        if (v >= 6.5) return "C+";
        if (v >= 6.0) return "C";
        if (v >= 5.5) return "D+";
        if (v >= 5.0) return "D";
        return "F";
    }

    public static String tinhMauDiem(BigDecimal d) {
        if (d == null) return "d-null";
        double v = d.doubleValue();
        if (v >= 8.5) return "d-a";
        if (v >= 7.0) return "d-b";
        if (v >= 6.0) return "d-c";
        if (v >= 5.0) return "d-d";
        return "d-f";
    }

    public static String tinhKetQua(BigDecimal d) {
        if (d == null) return "Đang học";
        return d.doubleValue() >= 5.0 ? "Hoàn thành" : "Không đạt";
    }

    public static String tinhMauKetQua(BigDecimal d) {
        if (d == null) return "background:#e0f2fe;color:#0369a1;";
        return d.doubleValue() >= 5.0
            ? "background:#dcfce7;color:#16a34a;"
            : "background:#fee2e2;color:#dc2626;";
    }
}
