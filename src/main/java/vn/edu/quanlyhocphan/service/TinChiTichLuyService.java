package vn.edu.quanlyhocphan.service;

import vn.edu.quanlyhocphan.dto.TinChiKhoiDto;

import java.util.List;

public interface TinChiTichLuyService {

    /**
     * Tinh toan tin chi tich luy cua sinh vien theo tung khoi kien thuc.
     *
     * @param sinhVienId ID sinh vien
     * @param nganhId    ID nganh cua sinh vien
     * @return danh sach TinChiKhoiDto (1 phan tu = 1 khoi)
     */
    List<TinChiKhoiDto> tinhTinChiTheoKhoi(Long sinhVienId, Long nganhId);

    /** Tong so tin chi da tich luy (tat ca cac khoi) */
    int tongTinChiDaTichLuy(Long sinhVienId, Long nganhId);
}
