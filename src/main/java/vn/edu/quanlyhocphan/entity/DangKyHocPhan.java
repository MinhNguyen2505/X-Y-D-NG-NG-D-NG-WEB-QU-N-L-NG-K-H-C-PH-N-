package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.quanlyhocphan.enums.TrangThaiDangKy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Bang DANG_KY_HOC_PHAN — ghi nhan SV dang ky lop hoc phan nao.
 *
 * THIET KE QUAN TRONG:
 * 1. KHONG CO HOC_KY_ID:
 *    - Hoc ky duoc suy ra qua: dang_ky -> lop_hoc_phan -> hoc_ky
 *    - Tranh transitive dependency: neu co hoc_ky_id o day, khi lop
 *      hoc phan doi hoc ky (hy huu), 2 gia tri se khong dong bo.
 *    - Khi can query theo hoc ky: JOIN sang lop_hoc_phan.hoc_ky_id.
 *
 * 2. @UniqueConstraint(sinh_vien_id, lop_hoc_phan_id):
 *    - 1 SV chi duoc dang ky 1 lop hoc phan dung 1 lan.
 *    - Khi SV huy (trang_thai = DA_HUY), ban ghi van con -> khong the
 *      dang ky lai cung lop (Service can check & xu ly nghiep vu nay).
 *
 * 3. Diem: DECIMAL(4,2) de luu chinh xac VD: 8.50, 10.00.
 *    - nullable = true vi diem chi co sau khi hoc xong.
 *    - CHECK(0-10) da co o DB; Service cung nen validate truoc khi luu.
 */
@Entity
@Table(
    name = "dang_ky_hoc_phan",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_dkhp",
            columnNames = {"sinh_vien_id", "lop_hoc_phan_id"}
        )
    }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DangKyHocPhan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @ManyToOne: nhieu ban ghi dang ky thuoc 1 sinh vien.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "sinh_vien_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_dkhp_sinh_vien")
    )
    private SinhVien sinhVien;

    /**
     * @ManyToOne: nhieu ban ghi dang ky thuoc 1 lop hoc phan.
     * Tu day suy ra duoc hoc ky (lop_hoc_phan.hoc_ky_id).
     * KHONG luu them hoc_ky_id o day.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "lop_hoc_phan_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_dkhp_lop_hoc_phan")
    )
    private LopHocPhan lopHocPhan;

    @Column(name = "ngay_dang_ky", nullable = false)
    @Builder.Default
    private LocalDateTime ngayDangKy = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, length = 30)
    @Builder.Default
    private TrangThaiDangKy trangThai = TrangThaiDangKy.DA_DANG_KY;

    /** Diem giua ky: 0.00 - 10.00, null neu chua co diem */
    @Column(name = "diem_giua_ky", precision = 4, scale = 2)
    private BigDecimal diemGiuaKy;

    /** Diem cuoi ky: 0.00 - 10.00, null neu chua co diem */
    @Column(name = "diem_cuoi_ky", precision = 4, scale = 2)
    private BigDecimal diemCuoiKy;

    /** Diem tong ket: 0.00 - 10.00, null neu chua tinh */
    @Column(name = "diem_tong_ket", precision = 4, scale = 2)
    private BigDecimal diemTongKet;

    // ---- Helper: kiem tra mon dat (diem tong ket >= 5.0) ----
    public boolean isDat() {
        return diemTongKet != null
            && diemTongKet.compareTo(new BigDecimal("5.0")) >= 0;
    }

    // ---- Helper: kiem tra dang con dang ky hop le ----
    public boolean isDaDangKy() {
        return TrangThaiDangKy.DA_DANG_KY.equals(trangThai);
    }
}
