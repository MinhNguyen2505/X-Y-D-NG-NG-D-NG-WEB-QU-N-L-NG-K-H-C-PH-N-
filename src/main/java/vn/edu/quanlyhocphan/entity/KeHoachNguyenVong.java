package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Bang KE_HOACH_NGUYEN_VONG.
 * Admin tao ke hoach, chua cac mon hoc de SV dang ky nguyen vong.
 * trang_thai: DANG_MO (SV co the dang ky) / DA_DONG (het han)
 */
@Entity
@Table(name = "ke_hoach_nguyen_vong")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class KeHoachNguyenVong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_ke_hoach", nullable = false)
    private String tenKeHoach;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hoc_ky_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_khnv_hoc_ky"))
    private HocKy hocKy;

    @Column(name = "ngay_bat_dau", nullable = false)
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc", nullable = false)
    private LocalDate ngayKetThuc;

    @Column(name = "trang_thai", nullable = false, length = 20)
    @Builder.Default
    private String trangThai = "DANG_MO";

    @OneToMany(mappedBy = "keHoachNguyenVong", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<NguyenVongMonHoc> danhSachMon = new ArrayList<>();

    public boolean isDangMo() {
        LocalDate today = LocalDate.now();
        return "DANG_MO".equals(trangThai)
            && !today.isBefore(ngayBatDau)
            && !today.isAfter(ngayKetThuc);
    }
}
