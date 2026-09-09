package vn.edu.quanlyhocphan.service;

import vn.edu.quanlyhocphan.entity.GiangVien;
import java.util.List;

public interface GiangVienService {
    GiangVien findById(Long id);
    GiangVien findByMaGv(String maGv);
    GiangVien findByEmail(String email);
    List<GiangVien> findAll();
    GiangVien save(GiangVien giangVien);
    void deleteById(Long id);
}
