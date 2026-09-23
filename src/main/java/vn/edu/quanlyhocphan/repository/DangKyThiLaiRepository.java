package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.quanlyhocphan.entity.DangKyThiLai;

import java.util.List;
import java.util.Optional;

@Repository
public interface DangKyThiLaiRepository extends JpaRepository<DangKyThiLai, Long> {

    /** Lay tat ca dang ky thi lai cua 1 SV kem thong tin mon hoc */
    @Query("""
        SELECT d FROM DangKyThiLai d
        JOIN FETCH d.dangKyHocPhan dk
        JOIN FETCH dk.lopHocPhan lhp
        JOIN FETCH lhp.monHoc
        JOIN FETCH lhp.hocKy
        WHERE d.sinhVien.id = :svId
        ORDER BY d.ngayDangKy DESC
        """)
    List<DangKyThiLai> findBySinhVienId(@Param("svId") Long svId);

    /** Kiem tra da dang ky thi lai mon nay chua */
    Optional<DangKyThiLai> findBySinhVienIdAndDangKyHocPhanId(
            Long sinhVienId, Long dangKyHocPhanId);

    /** Admin: lay tat ca dang ky thi lai chua duyet */
    @Query("""
        SELECT d FROM DangKyThiLai d
        JOIN FETCH d.sinhVien sv
        JOIN FETCH d.dangKyHocPhan dk
        JOIN FETCH dk.lopHocPhan lhp
        JOIN FETCH lhp.monHoc
        JOIN FETCH lhp.hocKy
        ORDER BY d.ngayDangKy DESC
        """)
    List<DangKyThiLai> findAllWithDetails();

    /** Admin: lay theo trang thai */
    @Query("""
        SELECT d FROM DangKyThiLai d
        JOIN FETCH d.sinhVien sv
        JOIN FETCH d.dangKyHocPhan dk
        JOIN FETCH dk.lopHocPhan lhp
        JOIN FETCH lhp.monHoc
        JOIN FETCH lhp.hocKy
        WHERE d.trangThai = :trangThai
        ORDER BY d.ngayDangKy DESC
        """)
    List<DangKyThiLai> findByTrangThai(@Param("trangThai") String trangThai);

    /** Dem so dang ky thi lai CHO_DUYET cua 1 SV (dung cho badge sidebar). */
    @Query("SELECT COUNT(d) FROM DangKyThiLai d " +
           "WHERE d.sinhVien.id = :svId AND d.trangThai = 'CHO_DUYET'")
    long countChoDuyetBySinhVienId(@Param("svId") Long svId);
}
