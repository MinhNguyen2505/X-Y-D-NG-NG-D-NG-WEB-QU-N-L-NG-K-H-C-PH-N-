package vn.edu.quanlyhocphan.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin",
       uniqueConstraints = @UniqueConstraint(name = "uq_admin_email", columnNames = "email"))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "mat_khau", nullable = false, length = 255)
    private String matKhau;

    @Column(name = "ho_ten", nullable = false, length = 255)
    private String hoTen;
}
