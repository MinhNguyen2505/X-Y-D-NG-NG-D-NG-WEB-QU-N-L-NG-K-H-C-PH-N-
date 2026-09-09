package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Bang MON_HOC — chua thong tin mon hoc (khong phai lop cu the).
 * Lop cu the duoc mo trong bang lop_hoc_phan theo tung hoc ky.
 */
@Entity
@Table(
    name = "mon_hoc",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_mh_ma_mon", columnNames = "ma_mon")
    }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_mon", nullable = false, length = 20)
    private String maMon;

    @Column(name = "ten_mon", nullable = false)
    private String tenMon;

    /** So tin chi phai > 0 (CHECK constraint da co o DB) */
    @Column(name = "so_tin_chi", nullable = false)
    private Integer soTinChi;

    @Column(name = "so_tiet_ly_thuyet", nullable = false)
    @Builder.Default
    private Integer soTietLyThuyet = 0;

    @Column(name = "so_tiet_thuc_hanh", nullable = false)
    @Builder.Default
    private Integer soTietThucHanh = 0;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    // ---- Quan he 1-N voi cac LopHocPhan mo tu mon nay ----
    @OneToMany(mappedBy = "monHoc", fetch = FetchType.LAZY)
    @Builder.Default
    private List<LopHocPhan> lopHocPhans = new ArrayList<>();

    // ---- Quan he 1-N voi ChuongTrinhDaoTao ----
    @OneToMany(mappedBy = "monHoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ChuongTrinhDaoTao> chuongTrinhDaoTaos = new ArrayList<>();

    /**
     * Cac mon can HOC TRUOC mon nay (mon nay la mon "chinh").
     * VD: monHoc = "CTDL", tienQuyet = [OOP]
     * => phai hoc OOP truoc khi hoc CTDL.
     */
    @OneToMany(mappedBy = "monHoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MonTienQuyet> cacMonTienQuyet = new ArrayList<>();

    /**
     * Cac mon ma mon nay la TIEN QUYET cua (mon nay la mon "phu").
     * VD: monHoc = "OOP", laMonTienQuyetCua = [CTDL]
     */
    @OneToMany(mappedBy = "monTienQuyet", fetch = FetchType.LAZY)
    @Builder.Default
    private List<MonTienQuyet> laMonTienQuyetCua = new ArrayList<>();
}
