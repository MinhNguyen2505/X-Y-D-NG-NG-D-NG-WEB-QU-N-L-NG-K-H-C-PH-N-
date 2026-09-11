package vn.edu.quanlyhocphan.service;

import vn.edu.quanlyhocphan.entity.DangKyNguyenVong;
import vn.edu.quanlyhocphan.entity.KeHoachNguyenVong;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface NguyenVongService {

    // ---- Admin ----
    List<KeHoachNguyenVong> findAllKeHoach();
    KeHoachNguyenVong findKeHoachById(Long id);
    KeHoachNguyenVong saveKeHoach(KeHoachNguyenVong keHoach);
    void dongKeHoach(Long id);
    void moKeHoach(Long id);
    void themMonVaoKeHoach(Long keHoachId, Long monHocId);
    void xoaMonKhoiKeHoach(Long nguyenVongMonHocId);
    List<DangKyNguyenVong> findDangKyByKeHoach(Long keHoachId);

    /** Duyet 1 dang ky nguyen vong (CHO_DUYET -> DA_DUYET) */
    void duyetDangKy(Long dangKyId);

    /** Tu choi 1 dang ky nguyen vong (CHO_DUYET/DA_DUYET -> DA_HUY) */
    void tuChoiDangKy(Long dangKyId);

    /** Duyet tat ca CHO_DUYET trong 1 ke hoach */
    int duyetTatCa(Long keHoachId);

    // ---- Sinh vien ----
    List<KeHoachNguyenVong> findKeHoachDangMo();
    /** Lay lich su dang ky con hieu luc (loai DA_HUY) — dung cho trang dang ky moi */
    List<DangKyNguyenVong> findDangKyCuaSinhVien(Long sinhVienId, Long keHoachId);
    /** Lay TOAN BO lich su dang ky (ke ca DA_HUY) — dung cho trang tra cuu */
    List<DangKyNguyenVong> findLichSuDangKy(Long sinhVienId, Long keHoachId);
    Set<Long> findDaDangKyIds(Long sinhVienId, Long keHoachId);
    void dangKy(Long sinhVienId, Long nguyenVongMonHocId);
    void huyDangKy(Long sinhVienId, Long nguyenVongMonHocId);
}
