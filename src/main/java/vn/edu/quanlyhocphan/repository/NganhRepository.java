package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.quanlyhocphan.entity.Nganh;

import java.util.Optional;

public interface NganhRepository extends JpaRepository<Nganh, Long> {

    Optional<Nganh> findByMaNganh(String maNganh);

    boolean existsByMaNganh(String maNganh);
}
