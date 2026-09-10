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
}
