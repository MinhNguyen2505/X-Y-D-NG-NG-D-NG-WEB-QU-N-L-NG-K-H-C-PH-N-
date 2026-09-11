package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.quanlyhocphan.entity.DangKyNguyenVong;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DangKyNguyenVongRepository extends JpaRepository<DangKyNguyenVong, Long> {

    /**
     * Lay cac dang ky con hieu luc (loai DA_HUY) cua 1 SV trong 1 ke hoach.
     * Dung cho: hien bảng "đã đăng ký" va tinh daDangKyIds.
     */
    @Query("SELECT d FROM DangKyNguyenVong d " +
           "JOIN FETCH d.nguyenVongMonHoc m " +
           "JOIN FETCH m.monHoc " +
           "WHERE d.sinhVien.id = :sinhVienId " +
           "AND m.keHoachNguyenVong.id = :keHoachId " +
           "AND d.trangThai <> 'DA_HUY'")
    List<DangKyNguyenVong> findBySinhVienAndKeHoach(
            @Param("sinhVienId") Long sinhVienId,
            @Param("keHoachId") Long keHoachId);

    /**
     * Lay id cac NguyenVongMonHoc ma SV da dang ky (loai DA_HUY).
     * Dung de an checkbox cua mon da dang ky trong bang dang ky moi.
     */
    @Query("SELECT d.nguyenVongMonHoc.id FROM DangKyNguyenVong d " +
           "WHERE d.sinhVien.id = :sinhVienId " +
           "AND d.nguyenVongMonHoc.keHoachNguyenVong.id = :keHoachId " +
           "AND d.trangThai <> 'DA_HUY'")
    Set<Long> findDaDangKyIds(
            @Param("sinhVienId") Long sinhVienId,
            @Param("keHoachId") Long keHoachId);

    /**
     * Lay TOAN BO lich su dang ky (ke ca DA_HUY) cua 1 SV trong 1 ke hoach.
     * Dung cho: trang tra cuu ket qua va bang lich su trong dang ky nguyen vong.
     */
    @Query("SELECT d FROM DangKyNguyenVong d " +
           "JOIN FETCH d.nguyenVongMonHoc m " +
           "JOIN FETCH m.monHoc " +
           "WHERE d.sinhVien.id = :sinhVienId " +
           "AND m.keHoachNguyenVong.id = :keHoachId " +
           "ORDER BY d.ngayDangKy DESC")
    List<DangKyNguyenVong> findAllBySinhVienAndKeHoach(
            @Param("sinhVienId") Long sinhVienId,
            @Param("keHoachId") Long keHoachId);

    /** Dem so dang ky nguyen vong CHO_DUYET cua 1 SV (dung cho badge sidebar). */
    @Query("SELECT COUNT(d) FROM DangKyNguyenVong d " +
           "WHERE d.sinhVien.id = :sinhVienId AND d.trangThai = 'CHO_DUYET'")
    long countChoDuyetBySinhVienId(@Param("sinhVienId") Long sinhVienId);

    /**
     * Lay Set<monHocId> cua cac mon co nguyen vong DA_DUYET trong 1 hoc ky.
     * Dung cho trang dang ky LHP de danh dau lop "goi y theo nguyen vong".
     */
    @Query("SELECT d.nguyenVongMonHoc.monHoc.id FROM DangKyNguyenVong d " +
           "WHERE d.sinhVien.id = :sinhVienId " +
           "AND d.nguyenVongMonHoc.keHoachNguyenVong.hocKy.id = :hocKyId " +
           "AND d.trangThai = 'DA_DUYET'")
    Set<Long> findMonNguyenVongDuyetIds(
            @Param("sinhVienId") Long sinhVienId,
            @Param("hocKyId") Long hocKyId);

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
