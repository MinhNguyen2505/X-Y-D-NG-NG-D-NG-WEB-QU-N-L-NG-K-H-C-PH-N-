package vn.edu.quanlyhocphan.service;

import vn.edu.quanlyhocphan.entity.DangKyThiLai;

import java.util.List;

public interface ThiLaiService {

    // ---- Sinh vien ----
    /** Lay danh sach mon chua dat cua SV (de hien thi form dang ky thi lai) */
    List<vn.edu.quanlyhocphan.entity.DangKyHocPhan> layMonChuaDat(Long sinhVienId);

    /** Dang ky thi lai 1 mon */
    void dangKy(Long sinhVienId, Long dangKyHocPhanId);

    /** Huy dang ky thi lai */
    void huy(Long sinhVienId, Long dangKyHocPhanId);

    /** Lay lich su dang ky thi lai cua SV */
    List<DangKyThiLai> layLichSu(Long sinhVienId);

    // ---- Admin ----
    /** Lay tat ca dang ky thi lai */
    List<DangKyThiLai> findAll();

    /** Lay theo trang thai */
    List<DangKyThiLai> findByTrangThai(String trangThai);

    /** Duyet 1 dang ky thi lai */
    void duyet(Long dangKyThiLaiId);

    /** Tu choi 1 dang ky thi lai */
    void tuChoi(Long dangKyThiLaiId);

    /** Duyet tat ca CHO_DUYET */
    int duyetTatCa();
}
