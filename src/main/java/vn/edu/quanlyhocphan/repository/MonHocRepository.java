package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.quanlyhocphan.entity.MonHoc;

import java.util.Optional;

public interface MonHocRepository extends JpaRepository<MonHoc, Long> {

    Optional<MonHoc> findByMaMon(String maMon);

    boolean existsByMaMon(String maMon);
}
