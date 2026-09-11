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
     * Lay CTDT cho trang Thong tin Chuong trinh hoc.
     * Chi fetch MonHoc + KhoiKienThuc — KHONG fetch cacMonTienQuyet o day
     * (tranh cartesian product lam cham query).
     * Tien quyet se duoc lay rieng boi queryTienQuyet() neu can.
     */
    @Query("SELECT ctdt FROM ChuongTrinhDaoTao ctdt " +
           "JOIN FETCH ctdt.monHoc mh " +
           "LEFT JOIN FETCH ctdt.khoiKienThuc " +
           "WHERE ctdt.nganh.id = :nganhId " +
           "AND (:keyword IS NULL OR :keyword = '' " +
           "     OR LOWER(mh.maMon) LIKE LOWER(CONCAT('%',:keyword,'%')) " +
           "     OR LOWER(mh.tenMon) LIKE LOWER(CONCAT('%',:keyword,'%'))) " +
           "ORDER BY ctdt.hocKyThu ASC, mh.maMon ASC")
    List<ChuongTrinhDaoTao> searchByNganhId(
            @Param("nganhId") Long nganhId,
            @Param("keyword") String keyword);

    /**
     * Lay tien quyet cua nhieu mon hoc mot luc (batch load).
     * Tranh N+1: goi 1 query cho tat ca mon thay vi goi tung mon.
     */
    @Query("SELECT DISTINCT mh FROM MonHoc mh " +
           "JOIN FETCH mh.cacMonTienQuyet mtq " +
           "JOIN FETCH mtq.monTienQuyet " +
           "WHERE mh.id IN :monHocIds")
    List<vn.edu.quanlyhocphan.entity.MonHoc> findMonHocWithTienQuyet(
            @Param("monHocIds") List<Long> monHocIds);

    /**
     * Kiem tra mon hoc co thuoc chuong trinh dao tao cua nganh khong.
     */
    Optional<ChuongTrinhDaoTao> findByNganhIdAndMonHocId(Long nganhId, Long monHocId);

    boolean existsByNganhIdAndMonHocId(Long nganhId, Long monHocId);
}
