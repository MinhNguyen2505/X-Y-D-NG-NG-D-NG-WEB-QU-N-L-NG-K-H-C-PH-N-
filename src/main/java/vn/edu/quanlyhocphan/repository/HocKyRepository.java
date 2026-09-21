package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.quanlyhocphan.entity.HocKy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HocKyRepository extends JpaRepository<HocKy, Long> {

    Optional<HocKy> findByNamHocAndHocKyThu(String namHoc, Integer hocKyThu);

    /**
     * Tim hoc ky dang trong thoi gian nhan dang ky (rang buoc nghiep vu #6).
     * Service goi voi LocalDate.now() de kiem tra SV co duoc dang ky khong.
     *
     * Dieu kien: ngayBatDauDk <= ngayHienTai <= ngayKetThucDk
     */
    @Query("SELECT hk FROM HocKy hk " +
           "WHERE :ngayHienTai BETWEEN hk.ngayBatDauDk AND hk.ngayKetThucDk " +
           "AND (hk.daChotDangKy IS NULL OR hk.daChotDangKy = false)")
    List<HocKy> findHocKyDangMoDangKy(@Param("ngayHienTai") LocalDate ngayHienTai);

    /**
     * Tim hoc ky cu the theo id, dung ket hop voi kiem tra thoi gian dang ky.
     * Service can query nay de xac nhan hoc_ky cua lop dang ky con han dang ky.
     */
    @Query("SELECT hk FROM HocKy hk WHERE hk.id = :hocKyId " +
           "AND :ngayHienTai BETWEEN hk.ngayBatDauDk AND hk.ngayKetThucDk " +
           "AND (hk.daChotDangKy IS NULL OR hk.daChotDangKy = false)")
    Optional<HocKy> findByIdAndDangMoDangKy(
            @Param("hocKyId") Long hocKyId,
            @Param("ngayHienTai") LocalDate ngayHienTai);

    /** Lay tat ca hoc ky sap xep giam dan (hoc ky moi nhat dau tien) */
    List<HocKy> findAllByOrderByNamHocDescHocKyThuDesc();
}
