package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.quanlyhocphan.entity.QuanLy;

import java.util.List;
import java.util.Optional;

public interface QuanLyRepository extends JpaRepository<QuanLy, Long> {
    Optional<QuanLy> findByEmail(String email);
    boolean existsByEmail(String email);
}
