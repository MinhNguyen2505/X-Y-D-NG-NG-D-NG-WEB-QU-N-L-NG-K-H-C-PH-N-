package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Bang CHUONG_TRINH_DAO_TAO — ke hoach hoc tap cua 1 nganh.
 * Moi ban ghi = 1 mon hoc thuoc 1 nganh, goi y hoc o hoc ky thu may.
 *
 * @UniqueConstraint(nganh_id, mon_hoc_id): moi nganh chi co 1 mon hoc
 * cu the trong chuong trinh dao tao (khong lap mon).
 */
@Entity
@Table(
    name = "chuong_trinh_dao_tao",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_ctdt",
            columnNames = {"nganh_id", "mon_hoc_id"}
        )
    }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhDaoTao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "nganh_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_ctdt_nganh")
    )
    private Nganh nganh;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "mon_hoc_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_ctdt_mon_hoc")
    )
    private MonHoc monHoc;

    /** Hoc ky thu may trong chuong trinh (VD: 1, 2, 3...) */
    @Column(name = "hoc_ky_thu", nullable = false)
    private Integer hocKyThu;

    /** true = mon bat buoc; false = mon tu chon */
    @Column(name = "bat_buoc", nullable = false)
    @Builder.Default
    private Boolean batBuoc = true;

    /** Khoi kien thuc cua mon hoc nay trong CTDT (co the null) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khoi_id", foreignKey = @ForeignKey(name = "fk_ctdt_khoi"))
    private KhoiKienThuc khoiKienThuc;

    /** Diem dat toi thieu cho mon nay (default 5.00) */
    @Column(name = "diem_dat", precision = 4, scale = 2)
    @Builder.Default
    private BigDecimal diemDat = new BigDecimal("5.00");

    // ================================================================
    // CAC COT BO SUNG CHO TRANG THONG TIN CHUONG TRINH HOC
    // ================================================================

    /**
     * So tiet hoc phi (so tiet tinh phi, co the khac so tin chi).
     * VD: mon 3TC nhung tinh 3 tiet phi => so_tiet_phi = 3
     */
    @Column(name = "so_tiet_phi")
    private Integer soTietPhi;

    /**
     * Hoc ky du kien (label hien thi), VD: "2023_2024_1(1)"
     * Dinh dang: <nam_hoc_bat_dau>_<nam_hoc_ket_thuc>_<hk>(<hk_thu>)
     */
    @Column(name = "hoc_ky_du_kien_label", length = 50)
    private String hocKyDuKienLabel;

    /**
     * Hoc ky thuc te da hoc (neu SV da hoan thanh mon nay).
     * Co the null neu chua hoc.
     */
    @Column(name = "hoc_ky_thuc_te_label", length = 50)
    private String hocKyThucTeLabel;

    /** So tiet Ly thuyet / Bai tap (LT_BT) */
    @Column(name = "lt_bt")
    @Builder.Default
    private Integer ltBt = 0;

    /** So tiet Thuc hanh (TH) */
    @Column(name = "so_tiet_th")
    @Builder.Default
    private Integer soTietTh = 0;

    /** So tiet Bai tap lon / Do an mon hoc (BTL_DAMH) */
    @Column(name = "btl_damh")
    @Builder.Default
    private Integer btlDamh = 0;

    /** So tiet Tieu thuyet / Do an / Khoa luan tot nghiep (TT_DA_KLTN) */
    @Column(name = "tt_da_kltn")
    @Builder.Default
    private Integer ttDaKltn = 0;
}
