package vn.edu.quanlyhocphan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.quanlyhocphan.entity.HocKy;
import vn.edu.quanlyhocphan.exception.ResourceNotFoundException;
import vn.edu.quanlyhocphan.repository.HocKyRepository;
import vn.edu.quanlyhocphan.service.HocKyService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HocKyServiceImpl implements HocKyService {

    private final HocKyRepository hocKyRepo;
    private final vn.edu.quanlyhocphan.repository.LopHocPhanRepository lopHocPhanRepo;
    private final vn.edu.quanlyhocphan.repository.DangKyHocPhanRepository dangKyHocPhanRepo;

    @Override @Transactional(readOnly = true)
    public HocKy findById(Long id) {
        return hocKyRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Hoc ky", id));
    }

    @Override @Transactional(readOnly = true)
    public List<HocKy> findAll() {
        return hocKyRepo.findAllByOrderByNamHocDescHocKyThuDesc();
    }

    @Override @Transactional
    public HocKy save(HocKy hocKy) { return hocKyRepo.save(hocKy); }

    @Override @Transactional
    public void deleteById(Long id) {
        if (!hocKyRepo.existsById(id)) throw new ResourceNotFoundException("Hoc ky", id);
        hocKyRepo.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public List<HocKy> findHocKyDangMoDangKy() {
        return hocKyRepo.findHocKyDangMoDangKy(LocalDate.now());
    }

    @Override @Transactional
    public void chotDangKy(Long hocKyId) {
        HocKy hk = findById(hocKyId);
        hk.setDaChotDangKy(true);
        hocKyRepo.save(hk);

        // Tu dong huy cac lop khong dat si so toi thieu, chot cac lop dat si so
        List<vn.edu.quanlyhocphan.entity.LopHocPhan> dsLop = lopHocPhanRepo.findByHocKyIdAndTrangThai(hocKyId, null);
        for (vn.edu.quanlyhocphan.entity.LopHocPhan lhp : dsLop) {
            int min = lhp.getSiSoToiThieu() != null ? lhp.getSiSoToiThieu() : 15;
            if (lhp.getSiSoHienTai() < min) {
                lhp.setTrangThai(vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan.HUY);
                lhp.setSiSoHienTai(0);
                lopHocPhanRepo.save(lhp);

                // Cap nhat ban ghi dang ky cua SV trong lop bi huy ve DA_HUY
                List<vn.edu.quanlyhocphan.entity.DangKyHocPhan> dsDk =
                    dangKyHocPhanRepo.findDanhSachSinhVienTrongLop(lhp.getId());
                for (vn.edu.quanlyhocphan.entity.DangKyHocPhan dk : dsDk) {
                    dk.setTrangThai(vn.edu.quanlyhocphan.enums.TrangThaiDangKy.DA_HUY);
                    dangKyHocPhanRepo.save(dk);
                }
            } else if (lhp.getTrangThai() == vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan.MO) {
                lhp.setTrangThai(vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan.DONG);
                lopHocPhanRepo.save(lhp);
            }
        }
    }

    @Override @Transactional
    public void moLaiDangKy(Long hocKyId) {
        HocKy hk = findById(hocKyId);
        hk.setDaChotDangKy(false);
        hocKyRepo.save(hk);

        // Mo lai cac lop sang trang thai MO neu truoc do bi DONG
        List<vn.edu.quanlyhocphan.entity.LopHocPhan> dsLop = lopHocPhanRepo.findByHocKyIdAndTrangThai(hocKyId, null);
        for (vn.edu.quanlyhocphan.entity.LopHocPhan lhp : dsLop) {
            if (lhp.getTrangThai() == vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan.DONG) {
                lhp.setTrangThai(vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan.MO);
                lopHocPhanRepo.save(lhp);
            }
        }
    }
}
