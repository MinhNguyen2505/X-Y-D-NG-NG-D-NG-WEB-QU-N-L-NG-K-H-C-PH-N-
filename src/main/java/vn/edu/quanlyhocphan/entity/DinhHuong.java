package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Bang DINH_HUONG.
 * Admin tao dinh huong hoc tap cho tung nganh.
 * SV chon 1 dinh huong trong nganh cua minh.
 * trang_thai: DANG_MO / DA_DONG
 */
@Entity
@Table(name = "dinh_huong")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class DinhHuong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_dinh_huong", nullable = false)
    private String tenDinhHuong;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nganh_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_dh_nganh"))
    private Nganh nganh;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    /** BAT_BUOC / TU_CHON */
    @Column(name = "che_do_dang_ky", length = 50)
    @Builder.Default
    private String cheDoDangKy = "BAT_BUOC";

    @Column(name = "trang_thai", nullable = false, length = 20)
    @Builder.Default
    private String trangThai = "DANG_MO";

    @OneToMany(mappedBy = "dinhHuong", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DangKyDinhHuong> danhSachDangKy = new ArrayList<>();

    public boolean isDangMo() {
        if (!"DANG_MO".equals(trangThai)) return false;
        LocalDate today = LocalDate.now();
        if (ngayBatDau != null && today.isBefore(ngayBatDau)) return false;
        if (ngayKetThuc != null && today.isAfter(ngayKetThuc)) return false;
        return true;
    }
}
