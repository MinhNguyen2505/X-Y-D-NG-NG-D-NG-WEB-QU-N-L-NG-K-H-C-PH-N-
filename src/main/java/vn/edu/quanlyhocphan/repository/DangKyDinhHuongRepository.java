package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.quanlyhocphan.entity.DangKyDinhHuong;

import java.util.List;
import java.util.Optional;

public interface DangKyDinhHuongRepository extends JpaRepository<DangKyDinhHuong, Long> {

    /** Dinh huong SV dang active (chua huy) trong 1 nganh */
    @Query("SELECT d FROM DangKyDinhHuong d " +
           "JOIN FETCH d.dinhHuong dh " +
           "JOIN FETCH dh.nganh " +
           "WHERE d.sinhVien.id = :svId " +
           "AND dh.nganh.id = :nganhId " +
           "AND d.trangThai = 'DA_DANG_KY'")
    Optional<DangKyDinhHuong> findActiveBySinhVienAndNganh(
            @Param("svId") Long svId,
            @Param("nganhId") Long nganhId);

    /** Lich su dang ky dinh huong cua SV */
    @Query("SELECT d FROM DangKyDinhHuong d " +
           "JOIN FETCH d.dinhHuong dh JOIN FETCH dh.nganh " +
           "WHERE d.sinhVien.id = :svId ORDER BY d.ngayDangKy DESC")
    List<DangKyDinhHuong> findBySinhVienId(@Param("svId") Long svId);

    Optional<DangKyDinhHuong> findBySinhVienIdAndDinhHuongId(Long svId, Long dhId);

    /** Tat ca dang ky cua 1 dinh huong (admin xem) */
    @Query("SELECT d FROM DangKyDinhHuong d JOIN FETCH d.sinhVien " +
           "WHERE d.dinhHuong.id = :dhId ORDER BY d.sinhVien.mssv")
    List<DangKyDinhHuong> findByDinhHuongId(@Param("dhId") Long dhId);
}
