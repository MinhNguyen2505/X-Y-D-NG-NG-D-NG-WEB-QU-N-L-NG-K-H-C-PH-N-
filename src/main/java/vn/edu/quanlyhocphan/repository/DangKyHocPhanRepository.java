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

    @Query("SELECT dkhp FROM DangKyHocPhan dkhp " +
           "JOIN FETCH dkhp.lopHocPhan lhp " +
           "JOIN FETCH lhp.monHoc " +
           "LEFT JOIN FETCH lhp.lichHocs " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "AND lhp.hocKy.id = :hocKyId " +
           "AND dkhp.trangThai = :trangThai")
    List<DangKyHocPhan> findBySinhVienIdAndHocKyIdAndTrangThai(
            @Param("sinhVienId") Long sinhVienId,
            @Param("hocKyId") Long hocKyId,
            @Param("trangThai") TrangThaiDangKy trangThai);

    /**
     * Lay dang ky cua SV trong 1 hoc ky — ca DA_DANG_KY lan HOAN_THANH.
     * Dung cho Thoi khoa bieu: HK cu da HOAN_THANH van can hien.
     */
    @Query("SELECT dkhp FROM DangKyHocPhan dkhp " +
           "JOIN FETCH dkhp.lopHocPhan lhp " +
           "JOIN FETCH lhp.monHoc " +
           "LEFT JOIN FETCH lhp.lichHocs " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "AND lhp.hocKy.id = :hocKyId " +
           "AND dkhp.trangThai IN ('DA_DANG_KY','HOAN_THANH')")
    List<DangKyHocPhan> findDangKyTheoHocKy(
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

    @Query("SELECT dkhp FROM DangKyHocPhan dkhp " +
           "JOIN FETCH dkhp.lopHocPhan lhp " +
           "JOIN FETCH lhp.monHoc " +
           "JOIN FETCH lhp.hocKy " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "ORDER BY lhp.hocKy.namHoc DESC, lhp.hocKy.hocKyThu DESC")
    List<DangKyHocPhan> findLichSuDangKy(@Param("sinhVienId") Long sinhVienId);

    @Query("SELECT dkhp FROM DangKyHocPhan dkhp " +
           "JOIN FETCH dkhp.sinhVien " +
           "WHERE dkhp.lopHocPhan.id = :lopHocPhanId " +
           "AND dkhp.trangThai <> 'DA_HUY' " +
           "ORDER BY dkhp.sinhVien.mssv ASC")
    List<DangKyHocPhan> findDanhSachSinhVienTrongLop(
            @Param("lopHocPhanId") Long lopHocPhanId);

    /**
     * Lay Set<monHocId> cua cac mon SV da HOAN_THANH va dat diem >= 5.
     * Dung de chặn đăng ký lại môn đã hoàn thành — 1 query nhẹ thay vì
     * load toàn bộ lịch sử rồi stream/filter.
     */
    @Query("SELECT lhp.monHoc.id FROM DangKyHocPhan dkhp " +
           "JOIN dkhp.lopHocPhan lhp " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "AND dkhp.trangThai = 'HOAN_THANH' " +
           "AND dkhp.diemTongKet >= 5.0")
    Set<Long> findMonDaHoanThanhIds(@Param("sinhVienId") Long sinhVienId);

    /**
     * Đếm tổng tín chỉ tích lũy (môn đạt >= 5, trạng thái HOAN_THANH).
     * Dùng cho Dashboard và Kết quả học tập — 1 SUM query thay vì stream.
     */
    @Query("SELECT COALESCE(SUM(lhp.monHoc.soTinChi), 0) FROM DangKyHocPhan dkhp " +
           "JOIN dkhp.lopHocPhan lhp " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "AND dkhp.trangThai = 'HOAN_THANH' " +
           "AND dkhp.diemTongKet >= 5.0")
    Integer tinhTongTinChiTichLuy(@Param("sinhVienId") Long sinhVienId);
}
