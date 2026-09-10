package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Bang NGUYEN_VONG_MON_HOC.
 * Cau noi giua KeHoachNguyenVong va MonHoc.
 * Admin them mon vao ke hoach, SV dang ky theo tung dong nay.
 */
@Entity
@Table(name = "nguyen_vong_mon_hoc",
       uniqueConstraints = @UniqueConstraint(name = "uq_nvmh",
           columnNames = {"ke_hoach_nguyen_vong_id", "mon_hoc_id"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class NguyenVongMonHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ke_hoach_nguyen_vong_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_nvmh_ke_hoach"))
    private KeHoachNguyenVong keHoachNguyenVong;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mon_hoc_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_nvmh_mon_hoc"))
    private MonHoc monHoc;

    @OneToMany(mappedBy = "nguyenVongMonHoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DangKyNguyenVong> danhSachDangKy = new ArrayList<>();
}
