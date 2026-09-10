package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.quanlyhocphan.entity.DangKyNguyenVong;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DangKyNguyenVongRepository extends JpaRepository<DangKyNguyenVong, Long> {

    /** Lay tat ca dang ky nguyen vong cua 1 SV trong 1 ke hoach */
    @Query("SELECT d FROM DangKyNguyenVong d " +
           "JOIN FETCH d.nguyenVongMonHoc m " +
           "JOIN FETCH m.monHoc " +
           "WHERE d.sinhVien.id = :sinhVienId " +
           "AND m.keHoachNguyenVong.id = :keHoachId " +
           "AND d.trangThai <> 'DA_HUY'")
    List<DangKyNguyenVong> findBySinhVienAndKeHoach(
            @Param("sinhVienId") Long sinhVienId,
            @Param("keHoachId") Long keHoachId);

    /** Lay id cac NguyenVongMonHoc ma SV da dang ky (de check checkbox) */
    @Query("SELECT d.nguyenVongMonHoc.id FROM DangKyNguyenVong d " +
           "WHERE d.sinhVien.id = :sinhVienId " +
           "AND d.nguyenVongMonHoc.keHoachNguyenVong.id = :keHoachId " +
           "AND d.trangThai <> 'DA_HUY'")
    Set<Long> findDaDangKyIds(
            @Param("sinhVienId") Long sinhVienId,
            @Param("keHoachId") Long keHoachId);

    Optional<DangKyNguyenVong> findBySinhVienIdAndNguyenVongMonHocId(
            Long sinhVienId, Long nguyenVongMonHocId);

    /** Lay tat ca dang ky cua 1 ke hoach (admin xem) */
    @Query("SELECT d FROM DangKyNguyenVong d " +
           "JOIN FETCH d.sinhVien " +
           "JOIN FETCH d.nguyenVongMonHoc m " +
           "JOIN FETCH m.monHoc " +
           "WHERE m.keHoachNguyenVong.id = :keHoachId " +
           "ORDER BY d.sinhVien.mssv, m.monHoc.maMon")
    List<DangKyNguyenVong> findByKeHoach(@Param("keHoachId") Long keHoachId);
}
