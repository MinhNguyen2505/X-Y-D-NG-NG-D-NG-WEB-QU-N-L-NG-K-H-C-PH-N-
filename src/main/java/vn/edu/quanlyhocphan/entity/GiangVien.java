package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Bang GIANG_VIEN.
 * Khong co quan he ke thua hay role phuc tap —
 * phan quyen xu ly o tang Security (UserDetailsService).
 */
@Entity
@Table(
    name = "giang_vien",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_gv_ma_gv",  columnNames = "ma_gv"),
        @UniqueConstraint(name = "uq_gv_email",  columnNames = "email")
    }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiangVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_gv", nullable = false, length = 20)
    private String maGv;

    @Column(name = "ho_ten", nullable = false)
    private String hoTen;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Column(name = "khoa_bo_mon")
    private String khoaBoMon;

    // ---- Quan he 1-N: 1 giang vien day nhieu lop hoc phan ----
    @OneToMany(mappedBy = "giangVien", fetch = FetchType.LAZY)
    @Builder.Default
    private List<LopHocPhan> lopHocPhans = new ArrayList<>();
}
