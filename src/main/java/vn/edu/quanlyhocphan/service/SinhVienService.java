package vn.edu.quanlyhocphan.service;

import vn.edu.quanlyhocphan.entity.SinhVien;
import java.util.List;

public interface SinhVienService {
    SinhVien findById(Long id);
    SinhVien findByMssv(String mssv);
    SinhVien findByEmail(String email);
    List<SinhVien> findAll();
    SinhVien save(SinhVien sinhVien);
    void deleteById(Long id);
    boolean existsByMssv(String mssv);
    boolean existsByEmail(String email);
}
