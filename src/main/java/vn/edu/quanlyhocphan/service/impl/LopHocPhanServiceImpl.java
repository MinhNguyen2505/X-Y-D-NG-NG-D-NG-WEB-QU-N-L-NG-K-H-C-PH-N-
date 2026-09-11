package vn.edu.quanlyhocphan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.quanlyhocphan.entity.LichHoc;
import vn.edu.quanlyhocphan.entity.LopHocPhan;
import vn.edu.quanlyhocphan.enums.TrangThaiLopHocPhan;
import vn.edu.quanlyhocphan.exception.ResourceNotFoundException;
import vn.edu.quanlyhocphan.repository.LichHocRepository;
import vn.edu.quanlyhocphan.repository.LopHocPhanRepository;
import vn.edu.quanlyhocphan.service.LopHocPhanService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LopHocPhanServiceImpl implements LopHocPhanService {

    private final LopHocPhanRepository lopHocPhanRepo;
    private final LichHocRepository lichHocRepo;

    @Override @Transactional(readOnly = true)
    public LopHocPhan findById(Long id) {
        return lopHocPhanRepo.findByIdWithLichHoc(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lop hoc phan", id));
    }

    @Override @Transactional(readOnly = true)
    public List<LopHocPhan> findAll() {
        return lopHocPhanRepo.findAll();
    }

    @Override @Transactional(readOnly = true)
    public LopHocPhan findByMaLopHp(String maLopHp) {
        return lopHocPhanRepo.findByMaLopHp(maLopHp)
            .orElseThrow(() -> new ResourceNotFoundException("Lop hoc phan: " + maLopHp));
    }

    @Override @Transactional(readOnly = true)
    public List<LopHocPhan> findByHocKyId(Long hocKyId, TrangThaiLopHocPhan trangThai) {
        return lopHocPhanRepo.findByHocKyIdAndTrangThai(hocKyId, trangThai);
    }

    @Override @Transactional(readOnly = true)
    public List<LopHocPhan> findByGiangVienIdAndHocKyId(Long giangVienId, Long hocKyId) {
        return lopHocPhanRepo.findByGiangVienIdAndHocKyId(giangVienId, hocKyId);
    }

    @Override @Transactional
    public LopHocPhan save(LopHocPhan lopHocPhan) {
        return lopHocPhanRepo.save(lopHocPhan);
    }

    @Override @Transactional
    public void deleteById(Long id) {
        if (!lopHocPhanRepo.existsById(id))
            throw new ResourceNotFoundException("Lop hoc phan", id);
        lopHocPhanRepo.deleteById(id);
    }

    @Override @Transactional
    public LichHoc themLichHoc(Long lopHocPhanId, LichHoc lichHoc) {
        LopHocPhan lhp = lopHocPhanRepo.findById(lopHocPhanId)
            .orElseThrow(() -> new ResourceNotFoundException("Lop hoc phan", lopHocPhanId));
        lichHoc.setLopHocPhan(lhp);
        return lichHocRepo.save(lichHoc);
    }

    @Override @Transactional
    public void xoaLichHoc(Long lichHocId) {
        lichHocRepo.deleteById(lichHocId);
    }
}
