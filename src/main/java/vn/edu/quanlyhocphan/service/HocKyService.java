package vn.edu.quanlyhocphan.service;

import vn.edu.quanlyhocphan.entity.HocKy;
import java.util.List;

public interface HocKyService {
    HocKy findById(Long id);
    List<HocKy> findAll();
    HocKy save(HocKy hocKy);
    void deleteById(Long id);
    /** Lay hoc ky hien tai dang mo dang ky (co the co nhieu hon 1 neu overlap). */
    List<HocKy> findHocKyDangMoDangKy();
}
