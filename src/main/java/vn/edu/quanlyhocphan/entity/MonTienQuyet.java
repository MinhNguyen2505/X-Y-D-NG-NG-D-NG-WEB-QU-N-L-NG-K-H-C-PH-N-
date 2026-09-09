package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Bang MON_TIEN_QUYET — luu cac cap (mon_hoc_id, mon_tien_quyet_id).
 *
 * Annotation quan trong:
 * - @UniqueConstraint(columnNames = {"mon_hoc_id","mon_tien_quyet_id"}):
 *   dam bao khong co ban ghi trung lap — 1 mon chi co 1 mon tien quyet
 *   cu the, khong them 2 lan.
 * - Hai @ManyToOne cung tro vao bang MON_HOC nhung y nghia khac nhau:
 *     + monHoc      : mon can hoc (mon "chinh" — phai co tien quyet)
 *     + monTienQuyet: mon phai hoan thanh TRUOC (mon "dieu kien")
 * - CHECK(mon_hoc_id <> mon_tien_quyet_id) duoc khai bao o DB (schema.sql),
 *   Service cung phai kiem tra truoc khi luu de thong bao loi ro rang.
 */
@Entity
@Table(
    name = "mon_tien_quyet",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_mtq",
            columnNames = {"mon_hoc_id", "mon_tien_quyet_id"}
        )
    }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonTienQuyet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Mon hoc can co tien quyet (mon "chinh").
     * VD: CTDL can hoc OOP truoc.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "mon_hoc_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_mtq_mon_hoc")
    )
    private MonHoc monHoc;

    /**
     * Mon phai hoan thanh truoc (mon "dieu kien").
     * VD: OOP phai dat truoc khi dang ky CTDL.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "mon_tien_quyet_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_mtq_tien_quyet")
    )
    private MonHoc monTienQuyet;
}
