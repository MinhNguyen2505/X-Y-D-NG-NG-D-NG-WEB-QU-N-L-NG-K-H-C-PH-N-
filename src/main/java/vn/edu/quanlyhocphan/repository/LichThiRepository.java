package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.quanlyhocphan.entity.LichThi;

import java.util.List;

@Repository
public interface LichThiRepository extends JpaRepository<LichThi, Long> {

    /**
     * Lay lich thi cua tat ca lop hoc phan ma SV da dang ky
     * trong 1 hoc ky cu the.
     */
    @Query("""
        SELECT lt FROM LichThi lt
        JOIN FETCH lt.lopHocPhan lhp
        JOIN FETCH lhp.monHoc mh
        JOIN lhp.dangKyHocPhans dk
        WHERE dk.sinhVien.id = :svId
          AND lhp.hocKy.id   = :hocKyId
          AND dk.trangThai   = 'DA_DANG_KY'
        ORDER BY lt.ngayThi ASC, lt.gioBatDau ASC
        """)
    List<LichThi> findBySinhVienAndHocKy(
            @Param("svId")    Long svId,
            @Param("hocKyId") Long hocKyId);

    /** Lich thi cua 1 lop hoc phan */
    List<LichThi> findByLopHocPhan_IdOrderByNgayThiAscGioBatDauAsc(Long lopHocPhanId);
}
