package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.quanlyhocphan.entity.DangKyHocPhan;
import vn.edu.quanlyhocphan.enums.TrangThaiDangKy;

import java.util.List;
import java.util.Optional;

public interface DangKyHocPhanRepository extends JpaRepository<DangKyHocPhan, Long> {

    /**
     * Tim ban ghi dang ky theo cap (SinhVien, LopHocPhan).
     * Dung cho:
     * - Rang buoc #5: kiem tra trung lop (da dang ky chua)
     * - Huy dang ky: can lay ban ghi de doi trang_thai
     */
    Optional<DangKyHocPhan> findBySinhVienIdAndLopHocPhanId(
            Long sinhVienId, Long lopHocPhanId);

    /**
     * Lay tat ca dang ky cua SV trong 1 hoc ky (theo trang thai).
     *
     * NOTE: KHONG co hoc_ky_id o dang_ky_hoc_phan,
     *       phai join qua lop_hoc_phan de lay hoc_ky_id.
     *
     * Dung cho:
     * - Rang buoc #4: tinh tong tin chi dang ky trong hoc ky
     * - Rang buoc #1: lay danh sach lop SV da dang ky de check lich
     * - Trang thai thoi khoa bieu cua SV
     */
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
     * Tinh tong so tin chi SV da dang ky (trang thai DA_DANG_KY) trong 1 hoc ky.
     *
     * Dung cho rang buoc nghiep vu #4: kiem tra vuot gioi han tin chi.
     * JOIN path: dang_ky -> lop_hoc_phan -> mon_hoc.so_tin_chi
     *                    -> lop_hoc_phan -> hoc_ky (lay hoc_ky_id)
     */
    @Query("SELECT COALESCE(SUM(lhp.monHoc.soTinChi), 0) " +
           "FROM DangKyHocPhan dkhp " +
           "JOIN dkhp.lopHocPhan lhp " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "AND lhp.hocKy.id = :hocKyId " +
           "AND dkhp.trangThai = 'DA_DANG_KY'")
    Integer tinhTongTinChiDaDangKy(
            @Param("sinhVienId") Long sinhVienId,
            @Param("hocKyId") Long hocKyId);

    /**
     * Kiem tra SV da hoan thanh mon hoc nao do chua (rang buoc nghiep vu #3).
     *
     * "Hoan thanh" = co dang ky voi trang_thai HOAN_THANH VA diem_tong_ket >= 5.0
     * Dung de kiem tra mon tien quyet: SV phai dat mon tien quyet moi duoc dang ky.
     *
     * JOIN path: dang_ky -> lop_hoc_phan -> mon_hoc
     */
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
     * Lay toan bo lich su dang ky cua SV (tat ca hoc ky, tat ca trang thai).
     * Dung cho trang "Xem ket qua hoc tap" cua SinhVien.
     */
    @Query("SELECT dkhp FROM DangKyHocPhan dkhp " +
           "JOIN FETCH dkhp.lopHocPhan lhp " +
           "JOIN FETCH lhp.monHoc " +
           "JOIN FETCH lhp.hocKy " +
           "WHERE dkhp.sinhVien.id = :sinhVienId " +
           "ORDER BY lhp.hocKy.namHoc DESC, lhp.hocKy.hocKyThu DESC")
    List<DangKyHocPhan> findLichSuDangKy(@Param("sinhVienId") Long sinhVienId);

    /**
     * Lay danh sach SV dang ky 1 lop hoc phan (GV dung de nhap diem).
     * Tra ve cac ban ghi co trang thai DA_DANG_KY hoac HOAN_THANH.
     */
    @Query("SELECT dkhp FROM DangKyHocPhan dkhp " +
           "JOIN FETCH dkhp.sinhVien " +
           "WHERE dkhp.lopHocPhan.id = :lopHocPhanId " +
           "AND dkhp.trangThai <> 'DA_HUY' " +
           "ORDER BY dkhp.sinhVien.mssv ASC")
    List<DangKyHocPhan> findDanhSachSinhVienTrongLop(
            @Param("lopHocPhanId") Long lopHocPhanId);
}
