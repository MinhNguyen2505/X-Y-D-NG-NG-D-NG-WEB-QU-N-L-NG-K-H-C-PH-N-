package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan;

import java.util.ArrayList;
import java.util.List;

/**
 * Bang LOP_HOC_PHAN — cac lop hoc cu the duoc mo theo tung hoc ky.
 *
 * Annotation quan trong:
 *
 * 1. @Version (truong "version"):
 *    - Hibernate tu dong quan ly cot version (BIGINT).
 *    - Khi UPDATE, Hibernate them dieu kien: WHERE version = <gia_tri_cu>
 *    - Neu 2 transaction doc cung 1 ban ghi va cung UPDATE:
 *        + Transaction 1 update thanh cong -> version tang len 1.
 *        + Transaction 2 update voi version cu -> WHERE khong khop ->
 *          nem OptimisticLockException.
 *    - Service bat OptimisticLockException va nem lai HetChoException
 *      -> tranh race condition khi nhieu SV tranh 1 cho cuoi.
 *
 * 2. KHONG CO COT PHONG:
 *    - Phong hoc chi nam o bang lich_hoc.phong.
 *    - Mot lop co the hoc o nhieu phong khac nhau (ly thuyet/thuc hanh).
 *
 * 3. @ManyToOne(fetch = LAZY) cho ca 3 FK (mon_hoc, hoc_ky, giang_vien):
 *    - Tranh load du lieu thua khi chi can ma lop hoac si so.
 */
@Entity
@Table(
    name = "lop_hoc_phan",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_lhp_ma_lop_hp", columnNames = "ma_lop_hp")
    }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LopHocPhan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_lop_hp", nullable = false, length = 50)
    private String maLopHp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "mon_hoc_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_lhp_mon_hoc")
    )
    private MonHoc monHoc;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "hoc_ky_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_lhp_hoc_ky")
    )
    private HocKy hocKy;

    /** Giang vien co the null neu chua phan cong */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "giang_vien_id",
        foreignKey = @ForeignKey(name = "fk_lhp_giang_vien")
    )
    private GiangVien giangVien;

    @Column(name = "si_so_toi_thieu")
    @Builder.Default
    private Integer siSoToiThieu = 15;

    @Column(name = "si_so_toi_da", nullable = false)
    @Builder.Default
    private Integer siSoToiDa = 50;

    /** Tang/giam trong cung transaction voi DangKyHocPhan (rang buoc nghiep vu #7) */
    @Column(name = "si_so_hien_tai", nullable = false)
    @Builder.Default
    private Integer siSoHienTai = 0;

    /** Doi tuong mo lop: VD "K21", "CNTT", "K21_CNTT" hoac null/"TAT_CA" */
    @Column(name = "doi_tuong", length = 100)
    private String doiTuong;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, length = 30)
    @Builder.Default
    private TrangThaiLopHocPhan trangThai = TrangThaiLopHocPhan.MO;

    /**
     * @Version: Hibernate tu dong tang sau moi UPDATE.
     * Bat buoc de xu ly Optimistic Locking khi nhieu SV dang ky dong thoi.
     * KHONG duoc set thu cong — de Hibernate quan ly.
     */
    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    // ---- Quan he 1-N voi LichHoc ----
    @OneToMany(mappedBy = "lopHocPhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<LichHoc> lichHocs = new ArrayList<>();

    // ---- Quan he 1-N voi LichThi ----
    @OneToMany(mappedBy = "lopHocPhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<LichThi> lichThis = new ArrayList<>();

    // ---- Quan he 1-N voi DangKyHocPhan ----
    @OneToMany(mappedBy = "lopHocPhan", fetch = FetchType.LAZY)
    @Builder.Default
    private List<DangKyHocPhan> dangKyHocPhans = new ArrayList<>();

    // ---- Helper method kiem tra con cho ----
    public boolean conCho() {
        return siSoHienTai < siSoToiDa;
    }
}
