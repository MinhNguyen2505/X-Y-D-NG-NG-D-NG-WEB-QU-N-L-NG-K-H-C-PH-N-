package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Bang NGANH — luu thong tin nganh hoc.
 *
 * Annotation quan trong:
 * - @Table(uniqueConstraints): khai bao UNIQUE(ma_nganh) o cap bang,
 *   cho phep Hibernate validate schema va tao DDL dung.
 * - @OneToMany(mappedBy, cascade): day la phia "mot" cua quan he 1-N voi
 *   SinhVien. mappedBy chi ten field ben phia "nhieu" (SinhVien.nganh),
 *   khong phat sinh cot FK them — FK nam o bang sinh_vien.
 */
@Entity
@Table(
    name = "nganh",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_nganh_ma_nganh", columnNames = "ma_nganh")
    }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nganh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_nganh", nullable = false, length = 20)
    private String maNganh;

    @Column(name = "ten_nganh", nullable = false)
    private String tenNganh;

    // ---- Quan he 1-N (chi dung khi can load danh sach SV theo nganh) ----
    @OneToMany(mappedBy = "nganh", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<SinhVien> sinhViens = new ArrayList<>();

    @OneToMany(mappedBy = "nganh", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ChuongTrinhDaoTao> chuongTrinhDaoTaos = new ArrayList<>();
}
