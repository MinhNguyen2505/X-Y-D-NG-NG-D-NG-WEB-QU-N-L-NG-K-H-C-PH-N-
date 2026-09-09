package vn.edu.quanlyhocphan.service;

import vn.edu.quanlyhocphan.entity.MonHoc;
import vn.edu.quanlyhocphan.entity.MonTienQuyet;
import java.util.List;

public interface MonHocService {
    MonHoc findById(Long id);
    MonHoc findByMaMon(String maMon);
    List<MonHoc> findAll();
    MonHoc save(MonHoc monHoc);
    void deleteById(Long id);
    MonTienQuyet themTienQuyet(Long monHocId, Long monTienQuyetId);
    void xoaTienQuyet(Long monTienQuyetId);
    List<MonTienQuyet> layDanhSachTienQuyet(Long monHocId);
}
