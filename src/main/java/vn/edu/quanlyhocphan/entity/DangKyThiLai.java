package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Bang DANG_KY_THI_LAI.
 * SV dang ky thi lai cac mon co diem tong ket < 5.
 * Lien ket voi dang_ky_hoc_phan goc de biet SV hoc lop nao, mon nao.
 */
@Entity
@Table(name = "dang_ky_thi_lai",
       uniqueConstraints = @UniqueConstraint(name = "uq_dktl",
           columnNames = {"sinh_vien_id", "dang_ky_hp_id"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class DangKyThiLai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sinh_vien_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_dktl_sv"))
    private SinhVien sinhVien;

    /** Dang ky hoc phan goc (mon thi lai) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dang_ky_hp_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_dktl_dkhp"))
    private DangKyHocPhan dangKyHocPhan;

    @Column(name = "ngay_dang_ky", nullable = false)
    @Builder.Default
    private LocalDateTime ngayDangKy = LocalDateTime.now();

    /** CHO_DUYET / DA_DUYET / DA_HUY */
    @Column(name = "trang_thai", nullable = false, length = 20)
    @Builder.Default
    private String trangThai = "CHO_DUYET";

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;
}
