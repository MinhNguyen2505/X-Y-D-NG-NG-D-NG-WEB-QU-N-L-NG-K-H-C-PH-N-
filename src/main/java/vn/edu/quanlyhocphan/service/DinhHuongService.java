package vn.edu.quanlyhocphan.service;

import vn.edu.quanlyhocphan.entity.DangKyDinhHuong;
import vn.edu.quanlyhocphan.entity.DinhHuong;

import java.util.List;
import java.util.Optional;

public interface DinhHuongService {

    // Admin
    List<DinhHuong> findAll();
    DinhHuong findById(Long id);
    DinhHuong save(DinhHuong dinhHuong);
    void dong(Long id);
    void mo(Long id);
    List<DangKyDinhHuong> findDangKyByDinhHuong(Long dinhHuongId);

    // Sinh vien
    List<DinhHuong> findDangMoByNganh(Long nganhId);
    List<DangKyDinhHuong> findDangKyCuaSinhVien(Long svId);
    Optional<DangKyDinhHuong> findActiveByNganh(Long svId, Long nganhId);
    void dangKy(Long svId, Long dinhHuongId);
    void huy(Long svId, Long dinhHuongId);
}
