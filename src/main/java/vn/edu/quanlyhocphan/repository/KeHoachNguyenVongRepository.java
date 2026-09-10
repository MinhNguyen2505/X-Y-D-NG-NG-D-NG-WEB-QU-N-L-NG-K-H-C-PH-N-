package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.quanlyhocphan.entity.KeHoachNguyenVong;

import java.util.List;

public interface KeHoachNguyenVongRepository extends JpaRepository<KeHoachNguyenVong, Long> {

    /** Lay tat ca ke hoach kem HocKy (tranh lazy) */
    @Query("SELECT k FROM KeHoachNguyenVong k JOIN FETCH k.hocKy ORDER BY k.id DESC")
    List<KeHoachNguyenVong> findAllWithHocKy();

    /** Lay ke hoach dang mo (SV co the dang ky) */
    @Query("SELECT k FROM KeHoachNguyenVong k JOIN FETCH k.hocKy " +
           "WHERE k.trangThai = 'DANG_MO' " +
           "AND CURRENT_DATE >= k.ngayBatDau AND CURRENT_DATE <= k.ngayKetThuc")
    List<KeHoachNguyenVong> findDangMo();

    /** Lay ke hoach kem danh sach mon va mon hoc chi tiet */
    @Query("SELECT DISTINCT k FROM KeHoachNguyenVong k " +
           "JOIN FETCH k.hocKy " +
           "LEFT JOIN FETCH k.danhSachMon m " +
           "LEFT JOIN FETCH m.monHoc " +
           "WHERE k.id = :id")
    java.util.Optional<KeHoachNguyenVong> findByIdWithMon(@Param("id") Long id);
}
