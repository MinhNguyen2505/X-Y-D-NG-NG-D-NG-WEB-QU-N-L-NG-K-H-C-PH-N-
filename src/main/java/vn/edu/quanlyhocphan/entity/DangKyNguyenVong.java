package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Bang DANG_KY_NGUYEN_VONG.
 * SV dang ky nguyen vong theo tung mon trong ke hoach.
 * trang_thai: CHO_DUYET / DA_DUYET / DA_HUY
 */
@Entity
@Table(name = "dang_ky_nguyen_vong",
       uniqueConstraints = @UniqueConstraint(name = "uq_dknv",
           columnNames = {"sinh_vien_id", "nguyen_vong_mon_hoc_id"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class DangKyNguyenVong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sinh_vien_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_dknv_sinh_vien"))
    private SinhVien sinhVien;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nguyen_vong_mon_hoc_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_dknv_nguyen_vong_mh"))
    private NguyenVongMonHoc nguyenVongMonHoc;

    @Column(name = "ngay_dang_ky", nullable = false)
    @Builder.Default
    private LocalDateTime ngayDangKy = LocalDateTime.now();

    @Column(name = "trang_thai", nullable = false, length = 20)
    @Builder.Default
    private String trangThai = "CHO_DUYET";
}
