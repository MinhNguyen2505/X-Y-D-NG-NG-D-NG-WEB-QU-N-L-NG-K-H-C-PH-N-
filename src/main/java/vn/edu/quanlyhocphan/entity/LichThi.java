package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Bang LICH_THI — lich thi giua ky / cuoi ky cua tung lop hoc phan.
 * loai_thi : GIUA_KY | CUOI_KY
 * hinh_thuc: TU_LUAN | TRAC_NGHIEM | THUC_HANH | VAN_DAP
 */
@Entity
@Table(name = "lich_thi")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichThi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "lop_hoc_phan_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_lt_lop_hoc_phan")
    )
    private LopHocPhan lopHocPhan;

    /** GIUA_KY hoac CUOI_KY */
    @Column(name = "loai_thi", nullable = false, length = 20)
    @Builder.Default
    private String loaiThi = "CUOI_KY";

    @Column(name = "ngay_thi", nullable = false)
    private LocalDate ngayThi;

    @Column(name = "gio_bat_dau", nullable = false)
    private LocalTime gioBatDau;

    @Column(name = "gio_ket_thuc", nullable = false)
    private LocalTime gioKetThuc;

    @Column(name = "phong_thi", length = 50)
    private String phongThi;

    /** TU_LUAN / TRAC_NGHIEM / THUC_HANH / VAN_DAP */
    @Column(name = "hinh_thuc", nullable = false, length = 30)
    @Builder.Default
    private String hinhThuc = "TU_LUAN";

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    // Helper
    public boolean isCuoiKy() {
        return "CUOI_KY".equalsIgnoreCase(loaiThi);
    }
}
