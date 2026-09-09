package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.quanlyhocphan.entity.GiangVien;

import java.util.Optional;

public interface GiangVienRepository extends JpaRepository<GiangVien, Long> {

    Optional<GiangVien> findByMaGv(String maGv);

    Optional<GiangVien> findByEmail(String email);

    boolean existsByMaGv(String maGv);

    boolean existsByEmail(String email);
}
