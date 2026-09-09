package vn.edu.quanlyhocphan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.quanlyhocphan.entity.MonHoc;
import vn.edu.quanlyhocphan.entity.MonTienQuyet;
import vn.edu.quanlyhocphan.exception.ResourceNotFoundException;
import vn.edu.quanlyhocphan.repository.MonHocRepository;
import vn.edu.quanlyhocphan.repository.MonTienQuyetRepository;
import vn.edu.quanlyhocphan.service.MonHocService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonHocServiceImpl implements MonHocService {

    private final MonHocRepository monHocRepo;
    private final MonTienQuyetRepository monTienQuyetRepo;

    @Override @Transactional(readOnly = true)
    public MonHoc findById(Long id) {
        return monHocRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Mon hoc", id));
    }

    @Override @Transactional(readOnly = true)
    public MonHoc findByMaMon(String maMon) {
        return monHocRepo.findByMaMon(maMon)
            .orElseThrow(() -> new ResourceNotFoundException("Mon hoc voi ma: " + maMon));
    }

    @Override @Transactional(readOnly = true)
    public List<MonHoc> findAll() { return monHocRepo.findAll(); }

    @Override @Transactional
    public MonHoc save(MonHoc monHoc) { return monHocRepo.save(monHoc); }

    @Override @Transactional
    public void deleteById(Long id) {
        if (!monHocRepo.existsById(id)) throw new ResourceNotFoundException("Mon hoc", id);
        monHocRepo.deleteById(id);
    }

    @Override @Transactional
    public MonTienQuyet themTienQuyet(Long monHocId, Long monTienQuyetId) {
        if (monHocId.equals(monTienQuyetId)) {
            throw new IllegalArgumentException("Mon hoc khong the la tien quyet cua chinh no.");
        }
        MonHoc monHoc = findById(monHocId);
        MonHoc tienQuyet = findById(monTienQuyetId);

        MonTienQuyet mtq = MonTienQuyet.builder()
            .monHoc(monHoc)
            .monTienQuyet(tienQuyet)
            .build();
        return monTienQuyetRepo.save(mtq);
    }

    @Override @Transactional
    public void xoaTienQuyet(Long monTienQuyetId) {
        monTienQuyetRepo.deleteById(monTienQuyetId);
    }

    @Override @Transactional(readOnly = true)
    public List<MonTienQuyet> layDanhSachTienQuyet(Long monHocId) {
        return monTienQuyetRepo.findByMonHocIdWithTienQuyet(monHocId);
    }
}
