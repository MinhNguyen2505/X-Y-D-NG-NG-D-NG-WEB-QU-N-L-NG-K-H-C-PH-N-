package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.quanlyhocphan.entity.NguyenVongMonHoc;

import java.util.List;

public interface NguyenVongMonHocRepository extends JpaRepository<NguyenVongMonHoc, Long> {

    @Query("SELECT m FROM NguyenVongMonHoc m JOIN FETCH m.monHoc WHERE m.keHoachNguyenVong.id = :keHoachId")
    List<NguyenVongMonHoc> findByKeHoachId(@Param("keHoachId") Long keHoachId);

    boolean existsByKeHoachNguyenVongIdAndMonHocId(Long keHoachId, Long monHocId);
}
