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
     * Lay CTDT day du: fetch MonHoc + KhoiKienThuc trong 1 query.
     * Tranh N+1 problem khi duyet danh sach.
     */
    @Query("SELECT DISTINCT ctdt FROM ChuongTrinhDaoTao ctdt " +
           "JOIN FETCH ctdt.monHoc mh " +
           "LEFT JOIN FETCH ctdt.khoiKienThuc " +
           "WHERE ctdt.nganh.id = :nganhId " +
           "ORDER BY ctdt.hocKyThu ASC, mh.maMon ASC")
    List<ChuongTrinhDaoTao> findByNganhIdWithMonHocAndKhoi(@Param("nganhId") Long nganhId);

    /**
     * Lay CTDT day du cho trang Thong tin Chuong trinh hoc.
     * Fetch kem MonHoc (voi cac MonTienQuyet) va KhoiKienThuc.
     * Ho tro tim kiem theo ma mon hoac ten mon (case-insensitive).
     */
    @Query("SELECT DISTINCT ctdt FROM ChuongTrinhDaoTao ctdt " +
           "JOIN FETCH ctdt.monHoc mh " +
           "LEFT JOIN FETCH ctdt.khoiKienThuc " +
           "LEFT JOIN FETCH mh.cacMonTienQuyet mtq " +
           "LEFT JOIN FETCH mtq.monTienQuyet " +
           "WHERE ctdt.nganh.id = :nganhId " +
           "AND (:keyword IS NULL OR :keyword = '' " +
           "     OR LOWER(mh.maMon) LIKE LOWER(CONCAT('%',:keyword,'%')) " +
           "     OR LOWER(mh.tenMon) LIKE LOWER(CONCAT('%',:keyword,'%'))) " +
           "ORDER BY ctdt.hocKyThu ASC, mh.maMon ASC")
    List<ChuongTrinhDaoTao> searchByNganhId(
            @Param("nganhId") Long nganhId,
            @Param("keyword") String keyword);

    /**
     * Kiem tra mon hoc co thuoc chuong trinh dao tao cua nganh khong.
     */
    Optional<ChuongTrinhDaoTao> findByNganhIdAndMonHocId(Long nganhId, Long monHocId);

    boolean existsByNganhIdAndMonHocId(Long nganhId, Long monHocId);
}
