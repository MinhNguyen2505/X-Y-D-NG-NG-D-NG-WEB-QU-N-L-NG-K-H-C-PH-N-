package vn.edu.quanlyhocphan.service;

import vn.edu.quanlyhocphan.entity.DiemDanh;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DiemDanhService {

    /** Lay diem danh cua 1 lop theo ngay */
    List<DiemDanh> layDiemDanhTheoNgay(Long lopHocPhanId, LocalDate ngayHoc);

    /** Lay toan bo diem danh cua lop */
    List<DiemDanh> layToanBoDiemDanh(Long lopHocPhanId);

    /**
     * Luu diem danh theo buoi: map<dangKyId, trangThai>.
     * Neu chua co thi tao moi, da co thi update.
     */
    void luuDiemDanh(Long lopHocPhanId, LocalDate ngayHoc,
                     Integer tietBatDau, Integer tietKetThuc,
                     Map<Long, String> diemDanhMap);

    /** Dem so buoi vang cua 1 dang ky */
    long demSoBuoiVang(Long dangKyId);
}
