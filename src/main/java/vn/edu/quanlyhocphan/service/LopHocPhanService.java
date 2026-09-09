package vn.edu.quanlyhocphan.service;

import vn.edu.quanlyhocphan.entity.LopHocPhan;
import vn.edu.quanlyhocphan.entity.LichHoc;
import vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan;

import java.util.List;

public interface LopHocPhanService {
    LopHocPhan findById(Long id);
    LopHocPhan findByMaLopHp(String maLopHp);
    List<LopHocPhan> findByHocKyId(Long hocKyId, TrangThaiLopHocPhan trangThai);
    List<LopHocPhan> findByGiangVienIdAndHocKyId(Long giangVienId, Long hocKyId);
    LopHocPhan save(LopHocPhan lopHocPhan);
    void deleteById(Long id);
    LichHoc themLichHoc(Long lopHocPhanId, LichHoc lichHoc);
    void xoaLichHoc(Long lichHocId);
}
