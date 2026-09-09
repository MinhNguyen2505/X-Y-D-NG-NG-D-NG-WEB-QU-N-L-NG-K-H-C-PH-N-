package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.quanlyhocphan.enums.TrangThaiSinhVien;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Bang SINH_VIEN.
 *
 * Annotation quan trong:
 * - @ManyToOne(fetch = LAZY): day la phia "nhieu" cua quan he N-1 voi Nganh.
 *   @JoinColumn(name = "nganh_id") chi ro cot FK trong bang sinh_vien.
 *   LAZY: chi load Nganh khi goi nganh.getXxx(), tranh N+1 query.
 * - @Enumerated(EnumType.STRING): luu enum theo ten (VD: "DANG_HOC") thay vi
 *   so nguyen — giup DB de doc, khong bi mat nghia khi them enum moi.
 */
@Entity
@Table(
    name = "sinh_vien",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_sv_mssv",  columnNames = "mssv"),
        @UniqueConstraint(name = "uq_sv_email", columnNames = "email")
    }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinhVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mssv", nullable = false, length = 20)
    private String mssv;

    @Column(name = "ho_ten", nullable = false)
    private String hoTen;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "lop_sinh_hoat", length = 50)
    private String lopSinhHoat;

    @Column(name = "khoa_hoc", length = 20)
    private String khoaHoc;

    /**
     * @ManyToOne(fetch = LAZY): nhieu SinhVien thuoc 1 Nganh.
     * @JoinColumn: cot FK "nganh_id" nam trong bang sinh_vien.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nganh_id", foreignKey = @ForeignKey(name = "fk_sv_nganh"))
    private Nganh nganh;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, length = 30)
    @Builder.Default
    private TrangThaiSinhVien trangThai = TrangThaiSinhVien.DANG_HOC;

    // ---- Quan he 1-N voi DangKyHocPhan ----
    @OneToMany(mappedBy = "sinhVien", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DangKyHocPhan> dangKyHocPhans = new ArrayList<>();
}
