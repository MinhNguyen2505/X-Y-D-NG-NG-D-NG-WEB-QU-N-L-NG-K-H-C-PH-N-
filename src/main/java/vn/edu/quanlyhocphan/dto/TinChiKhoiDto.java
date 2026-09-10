package vn.edu.quanlyhocphan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO tong hop tin chi theo khoi kien thuc.
 * Dung cho trang "Tin chi tich luy".
 *
 * Tuong ung voi 2 bang trong anh:
 *  1. "Tong diem theo khoi"      -> TinChiKhoiDto
 *  2. "Tong hop chi tiet..."     -> danhSachMonTrongKhoi (List<MonTrongKhoiDto>)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TinChiKhoiDto {

    private Long   khoiId;
    private String maKhoi;
    private String tenKhoi;

    /** Tong so tin chi theo CTDT cua khoi */
    private int tongSoTinChi;

    /** So tin chi bat buoc trong khoi */
    private int tinChiBatBuoc;

    /** So tin chi SV da tich luy (dat >= diem_dat) trong khoi */
    private int tinChiDaTichLuy;

    @Builder.Default
    private List<MonTrongKhoiDto> danhSachMon = new ArrayList<>();

    // ----------------------------------------------------------------
    /** Chi tiet tung mon trong khoi */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonTrongKhoiDto {
        private int    stt;
        private String maMon;
        private String tenMon;
        private int    soTinChi;

        /** Diem tong ket (null = chua co diem) */
        private java.math.BigDecimal diem;

        /** "Dat" / "Khong dat" / "" */
        private String danhGia;

        /** Diem quy doi (0-4) */
        private java.math.BigDecimal diemQuyDoi;

        /** Diem chu: A+, A, B+, B, C+, C, D+, D, F */
        private String diemChu;

        /** Ket qua: "Hoan thanh" / "Chua hoc" / "Khong dat" */
        private String ketQua;

        private boolean batBuoc;
    }
}
