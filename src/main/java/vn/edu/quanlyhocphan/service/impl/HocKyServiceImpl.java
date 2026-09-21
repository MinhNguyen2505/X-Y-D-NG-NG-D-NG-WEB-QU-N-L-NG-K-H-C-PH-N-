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
    }

    @Override @Transactional
    public void moLaiDangKy(Long hocKyId) {
        HocKy hk = findById(hocKyId);
        hk.setDaChotDangKy(false);
        hocKyRepo.save(hk);
    }
}
