package vn.edu.quanlyhocphan.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.quanlyhocphan.entity.LopHocPhan;
import vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan;

import java.util.List;
import java.util.Optional;

public interface LopHocPhanRepository extends JpaRepository<LopHocPhan, Long> {

    Optional<LopHocPhan> findByMaLopHp(String maLopHp);

    boolean existsByMaLopHp(String maLopHp);

    /**
     * Lay danh sach lop hoc phan theo hoc ky va trang thai.
     * Dung cho trang Admin quan ly lop / SV xem lich.
     */
    @Query("SELECT lhp FROM LopHocPhan lhp " +
           "JOIN FETCH lhp.monHoc " +
           "LEFT JOIN FETCH lhp.giangVien " +
           "LEFT JOIN FETCH lhp.lichHocs " +
           "WHERE lhp.hocKy.id = :hocKyId " +
           "AND (:trangThai IS NULL OR lhp.trangThai = :trangThai)")
    List<LopHocPhan> findByHocKyIdAndTrangThai(
            @Param("hocKyId") Long hocKyId,
            @Param("trangThai") TrangThaiLopHocPhan trangThai);

    /**
     * Lay danh sach lop hoc phan cua 1 giang vien trong 1 hoc ky.
     * Dung cho trang GiangVien xem danh sach lop day.
     */
    @Query("SELECT lhp FROM LopHocPhan lhp " +
           "JOIN FETCH lhp.monHoc " +
           "WHERE lhp.giangVien.id = :giangVienId " +
           "AND lhp.hocKy.id = :hocKyId")
    List<LopHocPhan> findByGiangVienIdAndHocKyId(
            @Param("giangVienId") Long giangVienId,
            @Param("hocKyId") Long hocKyId);

    /**
     * Lay danh sach lop hoc phan cua 1 mon hoc trong 1 hoc ky.
     * Dung cho SV xem cac lop co the dang ky cua 1 mon.
     */
    @Query("SELECT lhp FROM LopHocPhan lhp " +
           "JOIN FETCH lhp.monHoc " +
           "LEFT JOIN FETCH lhp.giangVien " +
           "WHERE lhp.monHoc.id = :monHocId " +
           "AND lhp.hocKy.id = :hocKyId " +
           "AND lhp.trangThai = 'MO'")
    List<LopHocPhan> findLopMoByMonHocIdAndHocKyId(
            @Param("monHocId") Long monHocId,
            @Param("hocKyId") Long hocKyId);

    /**
     * Lay lop hoc phan kem lich hoc (tranh N+1 khi can hien thi TKB).
     */
    @Query("SELECT DISTINCT lhp FROM LopHocPhan lhp " +
           "JOIN FETCH lhp.monHoc " +
           "LEFT JOIN FETCH lhp.lichHocs " +
           "WHERE lhp.id = :id")
    Optional<LopHocPhan> findByIdWithLichHoc(@Param("id") Long id);
}
