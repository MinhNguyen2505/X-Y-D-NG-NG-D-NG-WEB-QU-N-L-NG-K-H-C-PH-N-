package vn.edu.quanlyhocphan.service;

import vn.edu.quanlyhocphan.entity.DangKyHocPhan;

import java.math.BigDecimal;
import java.util.List;

public interface DangKyHocPhanService {

    /**
     * Dang ky hoc phan — ap dung du 7 rang buoc nghiep vu.
     * @param sinhVienId  id cua sinh vien dang ky
     * @param lopHocPhanId id cua lop hoc phan muon dang ky
     * @return ban ghi DangKyHocPhan vua tao
     */
    DangKyHocPhan dangKy(Long sinhVienId, Long lopHocPhanId);

    /**
     * Huy dang ky hoc phan.
     * Doi trang_thai = DA_HUY, giam si_so_hien_tai - 1 trong cung transaction.
     * Kiem tra thoi gian huy cung phai trong khung dk.
     */
    void huyDangKy(Long sinhVienId, Long lopHocPhanId);

    /** Lay danh sach dang ky hien tai cua SV trong 1 hoc ky (trang thai DA_DANG_KY). */
    List<DangKyHocPhan> layDangKyHienTai(Long sinhVienId, Long hocKyId);

    /** Lay tat ca dang ky cua SV trong 1 hoc ky (DA_DANG_KY + HOAN_THANH) — dung cho TKB. */
    List<DangKyHocPhan> layDangKyThoiKhoaBieu(Long sinhVienId, Long hocKyId);

    /** Lay toan bo lich su dang ky + ket qua cua SV (tat ca hoc ky). */
    List<DangKyHocPhan> layLichSuDangKy(Long sinhVienId);

    /** GV nhap diem cho 1 ban ghi dang ky (goi sau khi hoc ky ket thuc). */
    DangKyHocPhan nhapDiem(Long dangKyId, BigDecimal diemGiuaKy,
                           BigDecimal diemCuoiKy, BigDecimal diemTongKet);

    /** Lay danh sach SV trong 1 lop (GV dung de nhap diem). */
    List<DangKyHocPhan> layDanhSachSinhVienTrongLop(Long lopHocPhanId);
}
