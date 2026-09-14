package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.quanlyhocphan.entity.DiemDanh;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiemDanhRepository extends JpaRepository<DiemDanh, Long> {

    /** Lay toan bo diem danh cua 1 lop theo ngay hoc */
    @Query("SELECT d FROM DiemDanh d " +
           "JOIN FETCH d.dangKyHocPhan dk " +
           "JOIN FETCH dk.sinhVien sv " +
           "WHERE dk.lopHocPhan.id = :lopHocPhanId " +
           "AND d.ngayHoc = :ngayHoc " +
           "ORDER BY sv.mssv ASC")
    List<DiemDanh> findByLopAndNgay(
            @Param("lopHocPhanId") Long lopHocPhanId,
            @Param("ngayHoc") LocalDate ngayHoc);

    /** Lay toan bo diem danh cua 1 lop (tat ca ngay) */
    @Query("SELECT d FROM DiemDanh d " +
           "JOIN FETCH d.dangKyHocPhan dk " +
           "JOIN FETCH dk.sinhVien sv " +
           "WHERE dk.lopHocPhan.id = :lopHocPhanId " +
           "ORDER BY d.ngayHoc ASC, sv.mssv ASC")
    List<DiemDanh> findByLop(@Param("lopHocPhanId") Long lopHocPhanId);

    /** Tim diem danh cu the cua 1 SV trong 1 buoi */
    Optional<DiemDanh> findByDangKyHocPhanIdAndNgayHocAndTietBatDau(
            Long dangKyId, LocalDate ngayHoc, Integer tietBatDau);

    /** Dem so buoi vang cua 1 SV trong lop */
    @Query("SELECT COUNT(d) FROM DiemDanh d " +
           "WHERE d.dangKyHocPhan.id = :dangKyId " +
           "AND d.trangThai <> 'CO_MAT'")
    long demSoBuoiVang(@Param("dangKyId") Long dangKyId);
}
