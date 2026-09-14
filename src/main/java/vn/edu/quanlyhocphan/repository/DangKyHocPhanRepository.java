package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.quanlyhocphan.entity.DangKyHocPhan;
import vn.edu.quanlyhocphan.enums.TrangThaiDangKy;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DangKyHocPhanRepository extends JpaRepository<DangKyHocPhan, Long> {

    Optional<DangKyHocPhan> findBySinhVienIdAndLopHocPhanId(
            Long sinhVienId, Long lopHocPhanId);

    /**
     * Lay dang ky theo HK + trang thai — dung cho trang dang ky LHP.
     * Fetch day du: monHoc + hocKy + giangVien + lichHocs.
     */
    @Query("SELECT dkhp FROM DangKyHocPhan dkhp " +
           "JOIN FETCH dkhp.lopHocPhan lhp " +
           "JOIN FETCH lhp.monHoc " +
           "JOIN FETCH lhp.hocKy " +
           "LEFT JOIN FETCH lhp.giangVien " +
           "LEFT JOIN FETCH lhp.lichHocs " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "AND lhp.hocKy.id = :hocKyId " +
           "AND dkhp.trangThai = :trangThai")
    List<DangKyHocPhan> findBySinhVienIdAndHocKyIdAndTrangThai(
            @Param("sinhVienId") Long sinhVienId,
            @Param("hocKyId") Long hocKyId,
            @Param("trangThai") TrangThaiDangKy trangThai);

    /**
     * Lay tat ca dang ky DA_DANG_KY + HOAN_THANH cua SV trong 1 HK.
     * Dung cho TKB va trang dang ky LHP (layDangKyThoiKhoaBieu).
     */
    @Query("SELECT dkhp FROM DangKyHocPhan dkhp " +
           "JOIN FETCH dkhp.lopHocPhan lhp " +
           "JOIN FETCH lhp.monHoc " +
           "JOIN FETCH lhp.hocKy " +
           "LEFT JOIN FETCH lhp.giangVien " +
           "LEFT JOIN FETCH lhp.lichHocs " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "AND lhp.hocKy.id = :hocKyId " +
           "AND dkhp.trangThai IN ('DA_DANG_KY', 'HOAN_THANH') " +
           "ORDER BY lhp.monHoc.tenMon ASC")
    List<DangKyHocPhan> findDangKyThoiKhoaBieu(
            @Param("sinhVienId") Long sinhVienId,
            @Param("hocKyId") Long hocKyId);

    @Query("SELECT COALESCE(SUM(lhp.monHoc.soTinChi), 0) " +
           "FROM DangKyHocPhan dkhp " +
           "JOIN dkhp.lopHocPhan lhp " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "AND lhp.hocKy.id = :hocKyId " +
           "AND dkhp.trangThai = 'DA_DANG_KY'")
    Integer tinhTongTinChiDaDangKy(
            @Param("sinhVienId") Long sinhVienId,
            @Param("hocKyId") Long hocKyId);

    @Query("SELECT COUNT(dkhp) > 0 FROM DangKyHocPhan dkhp " +
           "JOIN dkhp.lopHocPhan lhp " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "AND lhp.monHoc.id = :monHocId " +
           "AND dkhp.trangThai = 'HOAN_THANH' " +
           "AND dkhp.diemTongKet >= 5.0")
    boolean kiemTraDaHoanThanhMon(
            @Param("sinhVienId") Long sinhVienId,
            @Param("monHocId") Long monHocId);

    /**
     * Toan bo lich su dang ky (tat ca trang thai, tat ca HK).
     * Fetch day du de dung cho ket-qua-hoc-tap, chuong-trinh-hoc, tin-chi-tich-luy.
     */
    @Query("SELECT dkhp FROM DangKyHocPhan dkhp " +
           "JOIN FETCH dkhp.lopHocPhan lhp " +
           "JOIN FETCH lhp.monHoc " +
           "JOIN FETCH lhp.hocKy " +
           "LEFT JOIN FETCH lhp.giangVien " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "ORDER BY lhp.hocKy.namHoc DESC, lhp.hocKy.hocKyThu DESC")
    List<DangKyHocPhan> findLichSuDangKy(@Param("sinhVienId") Long sinhVienId);

    /**
     * Lay danh sach SV trong lop (GV nhap diem, Admin xem).
     * Fetch sinhVien + lopHocPhan.monHoc de tranh LazyInit khi render.
     */
    @Query("SELECT dkhp FROM DangKyHocPhan dkhp " +
           "JOIN FETCH dkhp.sinhVien sv " +
           "JOIN FETCH dkhp.lopHocPhan lhp " +
           "JOIN FETCH lhp.monHoc " +
           "JOIN FETCH lhp.hocKy " +
           "WHERE dkhp.lopHocPhan.id = :lopHocPhanId " +
           "AND dkhp.trangThai <> 'DA_HUY' " +
           "ORDER BY sv.mssv ASC")
    List<DangKyHocPhan> findDanhSachSinhVienTrongLop(
            @Param("lopHocPhanId") Long lopHocPhanId);

    /**
     * Kiem tra SV da dang ky mon nay trong HK nay chua.
     * Chặn đăng ký 2 lớp khác nhau của cùng môn trong cùng HK.
     */
    @Query("SELECT COUNT(dkhp) > 0 FROM DangKyHocPhan dkhp " +
           "JOIN dkhp.lopHocPhan lhp " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "AND lhp.monHoc.id = :monHocId " +
           "AND lhp.hocKy.id = :hocKyId " +
           "AND dkhp.trangThai IN ('DA_DANG_KY', 'HOAN_THANH')")
    boolean kiemTraDaDangKyMonTrongHocKy(
            @Param("sinhVienId") Long sinhVienId,
            @Param("monHocId") Long monHocId,
            @Param("hocKyId") Long hocKyId);

    /** Lay Set<monHocId> da HOAN_THANH >= 5 — chặn đăng ký lại. */
    @Query("SELECT lhp.monHoc.id FROM DangKyHocPhan dkhp " +
           "JOIN dkhp.lopHocPhan lhp " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "AND dkhp.trangThai = 'HOAN_THANH' " +
           "AND dkhp.diemTongKet >= 5.0")
    Set<Long> findMonDaHoanThanhIds(@Param("sinhVienId") Long sinhVienId);

    /** Dem tong tin chi tich luy (HOAN_THANH + diem >= 5). */
    @Query("SELECT COALESCE(SUM(lhp.monHoc.soTinChi), 0) FROM DangKyHocPhan dkhp " +
           "JOIN dkhp.lopHocPhan lhp " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "AND dkhp.trangThai = 'HOAN_THANH' " +
           "AND dkhp.diemTongKet >= 5.0")
    Integer tinhTongTinChiTichLuy(@Param("sinhVienId") Long sinhVienId);
}
