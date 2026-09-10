package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Bang KHOI_KIEN_THUC — nhom cac mon hoc thanh tung khoi.
 * VD: KT1.1 = Khoi kien thuc giao duc dai cuong,
 *     KT3.1 = Khoi kien thuc chuyen nganh, GDTC, QPAN...
 */
@Entity
@Table(name = "khoi_kien_thuc")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhoiKienThuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_khoi", nullable = false, length = 30)
    private String maKhoi;

    @Column(name = "ten_khoi", nullable = false)
    private String tenKhoi;

    /** Null = ap dung cho moi nganh; co gia tri = rieng cho nganh do */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nganh_id", foreignKey = @ForeignKey(name = "fk_khoi_nganh"))
    private Nganh nganh;

    @OneToMany(mappedBy = "khoiKienThuc", fetch = FetchType.LAZY)
    @Builder.Default
    private List<ChuongTrinhDaoTao> chuongTrinhDaoTaos = new ArrayList<>();
}
