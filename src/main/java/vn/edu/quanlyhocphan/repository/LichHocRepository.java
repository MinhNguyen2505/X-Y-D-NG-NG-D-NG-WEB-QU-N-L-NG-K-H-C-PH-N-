package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.quanlyhocphan.entity.LichHoc;

import java.util.List;

public interface LichHocRepository extends JpaRepository<LichHoc, Long> {

    /** Lay lich hoc cua 1 lop hoc phan */
    List<LichHoc> findByLopHocPhanId(Long lopHocPhanId);

    /**
     * Lay tat ca lich hoc cua cac lop ma SV DA DANG KY trong 1 hoc ky.
     *
     * Dung cho rang buoc nghiep vu #1: kiem tra trung lich.
     * Service goi method nay voi sinhVienId + hocKyId,
     * sau do kiem tra overlap voi lich cua lop SV muon dang ky.
     *
     * JOIN path: DangKyHocPhan -> LopHocPhan -> LichHoc
     *            dang_ky.lop_hoc_phan.hoc_ky_id = :hocKyId
     *            dang_ky.trang_thai = 'DA_DANG_KY' (khong tinh ban ghi huy)
     *
     * NOTE: dang_ky_hoc_phan KHONG co hoc_ky_id, phai di qua lop_hoc_phan.
     */
    @Query("SELECT lh FROM LichHoc lh " +
           "JOIN lh.lopHocPhan lhp " +
           "JOIN DangKyHocPhan dkhp ON dkhp.lopHocPhan = lhp " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "AND lhp.hocKy.id = :hocKyId " +
           "AND dkhp.trangThai = 'DA_DANG_KY'")
    List<LichHoc> findLichHocDaDangKyCuaSinhVien(
            @Param("sinhVienId") Long sinhVienId,
            @Param("hocKyId") Long hocKyId);

    /**
     * Kiem tra nhanh xem lich cua 1 lop co xung dot voi lich da dang ky cua SV khong.
     * Overlap khi: cung thu VA (tiet_bat_dau_moi <= tiet_ket_thuc_cu
     *                              AND tiet_ket_thuc_moi >= tiet_bat_dau_cu)
     *
     * Tra ve danh sach cac LichHoc xung dot (rong = khong xung dot).
     *
     * Rewrote: dung subquery trong WHERE thay vi JOIN...IN (HQL khong con ho tro).
     */
    @Query("SELECT lh FROM LichHoc lh " +
           "JOIN lh.lopHocPhan lhp " +
           "JOIN DangKyHocPhan dkhp ON dkhp.lopHocPhan = lhp " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "AND lhp.hocKy.id = :hocKyId " +
           "AND dkhp.trangThai = 'DA_DANG_KY' " +
           "AND EXISTS (" +
           "    SELECT 1 FROM LichHoc lh2 " +
           "    WHERE lh2.lopHocPhan.id = :lopHocPhanMoiId " +
           "    AND lh.thu = lh2.thu " +
           "    AND lh.tietBatDau <= lh2.tietKetThuc " +
           "    AND lh.tietKetThuc >= lh2.tietBatDau" +
           ")")
    List<LichHoc> findLichXungDot(
            @Param("sinhVienId") Long sinhVienId,
            @Param("hocKyId") Long hocKyId,
            @Param("lopHocPhanMoiId") Long lopHocPhanMoiId);
}
