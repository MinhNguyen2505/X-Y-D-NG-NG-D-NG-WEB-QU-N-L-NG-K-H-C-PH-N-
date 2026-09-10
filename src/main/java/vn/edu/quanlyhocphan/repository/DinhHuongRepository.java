package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.quanlyhocphan.entity.DinhHuong;

import java.util.List;

public interface DinhHuongRepository extends JpaRepository<DinhHuong, Long> {

    @Query("SELECT d FROM DinhHuong d JOIN FETCH d.nganh ORDER BY d.nganh.id, d.id")
    List<DinhHuong> findAllWithNganh();

    @Query("SELECT d FROM DinhHuong d JOIN FETCH d.nganh WHERE d.nganh.id = :nganhId")
    List<DinhHuong> findByNganhId(@Param("nganhId") Long nganhId);

    @Query("SELECT d FROM DinhHuong d JOIN FETCH d.nganh " +
           "WHERE d.nganh.id = :nganhId AND d.trangThai = 'DANG_MO'")
    List<DinhHuong> findDangMoByNganhId(@Param("nganhId") Long nganhId);
}
