package vn.edu.quanlyhocphan.service;

import vn.edu.quanlyhocphan.dto.BaoCaoThongKeDTO;

public interface BaoCaoService {
    BaoCaoThongKeDTO thongKeTheoHocKy(Long hocKyId);
    BaoCaoThongKeDTO thongKeTongQuan();
}
