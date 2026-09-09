package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.quanlyhocphan.entity.ChuongTrinhDaoTao;

import java.util.List;
import java.util.Optional;

public interface ChuongTrinhDaoTaoRepository extends JpaRepository<ChuongTrinhDaoTao, Long> {

    /**
     * Lay chuong trinh dao tao theo nganh, kem thong tin MonHoc.
     * Dung cho trang "Xem chuong trinh dao tao" cua SinhVien.
     */
    @Query("SELECT ctdt FROM ChuongTrinhDaoTao ctdt " +
           "JOIN FETCH ctdt.monHoc " +
           "WHERE ctdt.nganh.id = :nganhId " +
           "ORDER BY ctdt.hocKyThu ASC")
    List<ChuongTrinhDaoTao> findByNganhIdWithMonHoc(@Param("nganhId") Long nganhId);

    /**
     * Kiem tra mon hoc co thuoc chuong trinh dao tao cua nganh khong.
     */
    Optional<ChuongTrinhDaoTao> findByNganhIdAndMonHocId(Long nganhId, Long monHocId);

    boolean existsByNganhIdAndMonHocId(Long nganhId, Long monHocId);
}
