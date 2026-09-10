package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.quanlyhocphan.entity.KhoiKienThuc;

import java.util.List;

@Repository
public interface KhoiKienThucRepository extends JpaRepository<KhoiKienThuc, Long> {

    /** Lay tat ca khoi cua 1 nganh (+ cac khoi chung cho moi nganh) */
    List<KhoiKienThuc> findByNganh_IdOrNganhIsNullOrderByMaKhoiAsc(Long nganhId);

    List<KhoiKienThuc> findAllByOrderByMaKhoiAsc();
}
