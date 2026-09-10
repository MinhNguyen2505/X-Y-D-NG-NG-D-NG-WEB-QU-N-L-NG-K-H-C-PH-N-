package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Bang DANG_KY_DINH_HUONG.
 * SV dang ky 1 dinh huong hoc tap.
 * trang_thai: DA_DANG_KY / DA_HUY
 */
@Entity
@Table(name = "dang_ky_dinh_huong",
       uniqueConstraints = @UniqueConstraint(name = "uq_dkdh",
           columnNames = {"sinh_vien_id", "dinh_huong_id"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class DangKyDinhHuong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sinh_vien_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_dkdh_sv"))
    private SinhVien sinhVien;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dinh_huong_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_dkdh_dh"))
    private DinhHuong dinhHuong;

    @Column(name = "ngay_dang_ky", nullable = false)
    @Builder.Default
    private LocalDateTime ngayDangKy = LocalDateTime.now();

    @Column(name = "trang_thai", nullable = false, length = 20)
    @Builder.Default
    private String trangThai = "DA_DANG_KY";
}
