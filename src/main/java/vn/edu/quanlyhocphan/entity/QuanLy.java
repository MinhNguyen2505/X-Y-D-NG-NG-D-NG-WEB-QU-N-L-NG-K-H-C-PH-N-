package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quan_ly",
       uniqueConstraints = @UniqueConstraint(name = "uq_ql_email", columnNames = "email"))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class QuanLy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "mat_khau", nullable = false, length = 255)
    private String matKhau;

    @Column(name = "ho_ten", nullable = false, length = 255)
    private String hoTen;

    @Column(name = "don_vi", length = 255)
    private String donVi;

    @Column(name = "so_dien_thoai", length = 50)
    private String soDienThoai;
}
