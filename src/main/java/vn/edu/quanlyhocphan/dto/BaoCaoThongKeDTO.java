package vn.edu.quanlyhocphan.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaoCaoThongKeDTO {

    // === TONG QUAN ===
    private int tongSinhVien;
    private int tongLopHocPhan;
    private int tongDangKyHocPhan;

    // === DANG KY LHP THEO HOC KY ===
    private int svDaDangKy;           // SV da dang ky >= 1 mon
    private int svChuaDangKy;         // SV chua dang ky mon nao
    private double tiLeDangKy;        // % SV da dang ky
    private double trungBinhTCperSV;  // Trung binh TC moi SV

    // === PHAN BO TIN CHI ===
    // key: "0-5TC", "6-10TC", ... value: so SV
    private Map<String, Long> phanBoTinChi;

    // === TOP MON HOC ===
    private List<TopMonDTO> topMonHot;  // top 5 mon duoc dang ky nhieu nhat

    // === NGUYEN VONG ===
    private long nvChoDuyet;
    private long nvDaDuyet;
    private long nvDaHuy;
    private double tiLeNVDuyet;

    // === THI LAI ===
    private long thiLaiChoDuyet;
    private long thiLaiDaDuyet;

    // === TRANG THAI LOP ===
    private long lopDangMo;
    private long lopDaDong;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TopMonDTO {
        private String maMon;
        private String tenMon;
        private long soLuongDangKy;
        private int soTinChi;
    }
}
