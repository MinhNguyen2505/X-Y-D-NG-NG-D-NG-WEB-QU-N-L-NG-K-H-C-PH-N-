package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Bang LICH_HOC — lich hoc cu the cua 1 lop hoc phan.
 *
 * Mot lop hoc phan co the co NHIEU ban ghi lich_hoc (nhieu buoi/tuan).
 * VD: LHP "CTDL-01" hoc:
 *   - Thu 2, tiet 1-3, phong A101 (ly thuyet)
 *   - Thu 5, tiet 7-9, phong B204 (thuc hanh)
 *
 * => Cot PHONG nam o day (khong phai lop_hoc_phan) vi ly do tren.
 *
 * Service check trung lich bang cach lay tat ca LichHoc cua SV
 * trong hoc ky va kiem tra overlap voi LichHoc cua lop muon dang ky:
 *   - Cung thu
 *   - Tiet overlap: tiet_bat_dau_moi <= tiet_ket_thuc_cu
 *                   AND tiet_ket_thuc_moi >= tiet_bat_dau_cu
 */
@Entity
@Table(name = "lich_hoc")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @ManyToOne: nhieu buoi hoc thuoc 1 lop hoc phan.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "lop_hoc_phan_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_lh_lop_hoc_phan")
    )
    private LopHocPhan lopHocPhan;

    /**
     * Thu trong tuan: 2 = Thu Hai, 3 = Thu Ba, ..., 8 = Chu Nhat.
     * CHECK(thu BETWEEN 2 AND 8) da co o DB.
     */
    @Column(name = "thu", nullable = false)
    private Integer thu;

    /** Tiet bat dau (1-15) */
    @Column(name = "tiet_bat_dau", nullable = false)
    private Integer tietBatDau;

    /** Tiet ket thuc (>= tiet_bat_dau) */
    @Column(name = "tiet_ket_thuc", nullable = false)
    private Integer tietKetThuc;

    /** Phong hoc (co the null neu chua xep phong) */
    @Column(name = "phong", length = 50)
    private String phong;
}
