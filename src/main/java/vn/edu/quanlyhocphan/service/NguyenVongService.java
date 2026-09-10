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

    // ---- Sinh vien ----
    List<KeHoachNguyenVong> findKeHoachDangMo();
    List<DangKyNguyenVong> findDangKyCuaSinhVien(Long sinhVienId, Long keHoachId);
    Set<Long> findDaDangKyIds(Long sinhVienId, Long keHoachId);
    void dangKy(Long sinhVienId, Long nguyenVongMonHocId);
    void huyDangKy(Long sinhVienId, Long nguyenVongMonHocId);
}
