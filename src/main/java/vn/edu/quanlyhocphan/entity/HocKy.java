package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Bang HOC_KY — quan ly thoi gian hoc va thoi gian cho phep dang ky.
 *
 * @UniqueConstraint(nam_hoc, hoc_ky_thu): chi co 1 hoc ky 1/2/3
 * trong moi nam hoc — tranh tao trung.
 *
 * Cac cot ngay su dung LocalDate (khong co gio, phu hop voi DATE cua MySQL).
 * Service se so sanh LocalDate.now() voi ngayBatDauDk / ngayKetThucDk
 * de kiem tra khoang thoi gian dang ky hop le (rang buoc nghiep vu #6).
 */
@Entity
@Table(
    name = "hoc_ky",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_hk",
            columnNames = {"nam_hoc", "hoc_ky_thu"}
        )
    }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HocKy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_hoc_ky", nullable = false, length = 100)
    private String tenHocKy;

    /** VD: "2024-2025" */
    @Column(name = "nam_hoc", nullable = false, length = 20)
    private String namHoc;

    /** 1, 2, hoac 3 (hoc ky he) */
    @Column(name = "hoc_ky_thu", nullable = false)
    private Integer hocKyThu;

    /** Ngay bat dau nhan dang ky hoc phan */
    @Column(name = "ngay_bat_dau_dk", nullable = false)
    private LocalDate ngayBatDauDk;

    /** Ngay ket thuc nhan dang ky hoc phan */
    @Column(name = "ngay_ket_thuc_dk", nullable = false)
    private LocalDate ngayKetThucDk;

    @Column(name = "ngay_bat_dau_hoc", nullable = false)
    private LocalDate ngayBatDauHoc;

    @Column(name = "ngay_ket_thuc_hoc", nullable = false)
    private LocalDate ngayKetThucHoc;

    /** So tin chi toi thieu SV phai dang ky trong hoc ky nay */
    @Column(name = "tin_chi_toi_thieu", nullable = false)
    @Builder.Default
    private Integer tinChiToiThieu = 0;

    /** So tin chi toi da SV duoc phep dang ky trong hoc ky nay */
    @Column(name = "tin_chi_toi_da", nullable = false)
    @Builder.Default
    private Integer tinChiToiDa = 25;

    // ---- Quan he 1-N voi LopHocPhan (cac lop mo trong hoc ky nay) ----
    @OneToMany(mappedBy = "hocKy", fetch = FetchType.LAZY)
    @Builder.Default
    private List<LopHocPhan> lopHocPhans = new ArrayList<>();
}
