package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Bang DIEM_DANH — ghi nhan tinh trang diem danh tung buoi hoc cua SV.
 * trang_thai: CO_MAT | VANG_CO_PHEP | VANG_KHONG_PHEP
 */
@Entity
@Table(
    name = "diem_danh",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_dd",
        columnNames = {"dang_ky_id", "ngay_hoc", "tiet_bat_dau"}
    )
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DiemDanh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dang_ky_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_dd_dkhp"))
    private DangKyHocPhan dangKyHocPhan;

    @Column(name = "ngay_hoc", nullable = false)
    private LocalDate ngayHoc;

    @Column(name = "tiet_bat_dau", nullable = false)
    private Integer tietBatDau;

    @Column(name = "tiet_ket_thuc", nullable = false)
    private Integer tietKetThuc;

    /** CO_MAT | VANG_CO_PHEP | VANG_KHONG_PHEP */
    @Column(name = "trang_thai", nullable = false, length = 30)
    @Builder.Default
    private String trangThai = "CO_MAT";

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;
}
