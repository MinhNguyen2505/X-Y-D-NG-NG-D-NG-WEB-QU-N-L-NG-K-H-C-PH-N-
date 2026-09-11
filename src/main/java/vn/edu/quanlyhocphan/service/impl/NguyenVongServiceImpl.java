package vn.edu.quanlyhocphan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.quanlyhocphan.entity.*;
import vn.edu.quanlyhocphan.exception.NghiepVuException;
import vn.edu.quanlyhocphan.exception.ResourceNotFoundException;
import vn.edu.quanlyhocphan.repository.*;
import vn.edu.quanlyhocphan.service.NguyenVongService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class NguyenVongServiceImpl implements NguyenVongService {

    private final KeHoachNguyenVongRepository keHoachRepo;
    private final NguyenVongMonHocRepository nvMonHocRepo;
    private final DangKyNguyenVongRepository dangKyNvRepo;
    private final SinhVienRepository sinhVienRepo;
    private final MonHocRepository monHocRepo;

    // ================================================================
    // ADMIN
    // ================================================================

    @Override
    @Transactional(readOnly = true)
    public List<KeHoachNguyenVong> findAllKeHoach() {
        return keHoachRepo.findAllWithHocKy();
    }

    @Override
    @Transactional(readOnly = true)
    public KeHoachNguyenVong findKeHoachById(Long id) {
        return keHoachRepo.findByIdWithMon(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ke hoach nguyen vong", id));
    }

    @Override
    @Transactional
    public KeHoachNguyenVong saveKeHoach(KeHoachNguyenVong keHoach) {
        return keHoachRepo.save(keHoach);
    }

    @Override
    @Transactional
    public void dongKeHoach(Long id) {
        KeHoachNguyenVong kh = keHoachRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ke hoach nguyen vong", id));
        kh.setTrangThai("DA_DONG");
        keHoachRepo.save(kh);
        log.info("Da dong ke hoach nguyen vong id={}", id);
    }

    @Override
    @Transactional
    public void moKeHoach(Long id) {
        KeHoachNguyenVong kh = keHoachRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ke hoach nguyen vong", id));
        kh.setTrangThai("DANG_MO");
        keHoachRepo.save(kh);
        log.info("Da mo lai ke hoach nguyen vong id={}", id);
    }

    @Override
    @Transactional
    public void themMonVaoKeHoach(Long keHoachId, Long monHocId) {
        if (nvMonHocRepo.existsByKeHoachNguyenVongIdAndMonHocId(keHoachId, monHocId)) {
            throw new NghiepVuException("Mon hoc nay da co trong ke hoach.");
        }
        KeHoachNguyenVong kh = keHoachRepo.findById(keHoachId)
            .orElseThrow(() -> new ResourceNotFoundException("Ke hoach nguyen vong", keHoachId));
        MonHoc mh = monHocRepo.findById(monHocId)
            .orElseThrow(() -> new ResourceNotFoundException("Mon hoc", monHocId));

        NguyenVongMonHoc nvmh = NguyenVongMonHoc.builder()
            .keHoachNguyenVong(kh)
            .monHoc(mh)
            .build();
        nvMonHocRepo.save(nvmh);
    }

    @Override
    @Transactional
    public void xoaMonKhoiKeHoach(Long nguyenVongMonHocId) {
        nvMonHocRepo.deleteById(nguyenVongMonHocId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DangKyNguyenVong> findDangKyByKeHoach(Long keHoachId) {
        return dangKyNvRepo.findByKeHoach(keHoachId);
    }

    // ================================================================
    // SINH VIEN
    // ================================================================

    @Override
    @Transactional(readOnly = true)
    public List<KeHoachNguyenVong> findKeHoachDangMo() {
        return keHoachRepo.findDangMo();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DangKyNguyenVong> findDangKyCuaSinhVien(Long sinhVienId, Long keHoachId) {
        return dangKyNvRepo.findBySinhVienAndKeHoach(sinhVienId, keHoachId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Long> findDaDangKyIds(Long sinhVienId, Long keHoachId) {
        return dangKyNvRepo.findDaDangKyIds(sinhVienId, keHoachId);
    }

    @Override
    @Transactional
    public void dangKy(Long sinhVienId, Long nguyenVongMonHocId) {
        // Kiem tra da dang ky chua
        dangKyNvRepo.findBySinhVienIdAndNguyenVongMonHocId(sinhVienId, nguyenVongMonHocId)
            .ifPresent(dk -> {
                if (!"DA_HUY".equals(dk.getTrangThai())) {
                    throw new NghiepVuException("Ban da dang ky nguyen vong nay roi.");
                }
                // Neu da huy thi mo lai
                dk.setTrangThai("CHO_DUYET");
                dangKyNvRepo.save(dk);
            });

        // Neu chua co ban ghi nao thi tao moi
        if (dangKyNvRepo.findBySinhVienIdAndNguyenVongMonHocId(sinhVienId, nguyenVongMonHocId).isEmpty()) {
            NguyenVongMonHoc nvmh = nvMonHocRepo.findById(nguyenVongMonHocId)
                .orElseThrow(() -> new ResourceNotFoundException("NguyenVongMonHoc", nguyenVongMonHocId));

            // Kiem tra ke hoach con mo khong
            if (!nvmh.getKeHoachNguyenVong().isDangMo()) {
                throw new NghiepVuException("Ke hoach dang ky nguyen vong da dong.");
            }

            SinhVien sv = sinhVienRepo.findById(sinhVienId)
                .orElseThrow(() -> new ResourceNotFoundException("Sinh vien", sinhVienId));

            DangKyNguyenVong dk = DangKyNguyenVong.builder()
                .sinhVien(sv)
                .nguyenVongMonHoc(nvmh)
                .trangThai("CHO_DUYET")
                .build();
            dangKyNvRepo.save(dk);
            log.info("SV [{}] dang ky nguyen vong mon [{}]", sinhVienId, nguyenVongMonHocId);
        }
    }

    @Override
    @Transactional
    public void huyDangKy(Long sinhVienId, Long nguyenVongMonHocId) {
        DangKyNguyenVong dk = dangKyNvRepo
            .findBySinhVienIdAndNguyenVongMonHocId(sinhVienId, nguyenVongMonHocId)
            .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay ban ghi dang ky nguyen vong."));
        dk.setTrangThai("DA_HUY");
        dangKyNvRepo.save(dk);
        log.info("SV [{}] huy dang ky nguyen vong mon [{}]", sinhVienId, nguyenVongMonHocId);
    }

    // ================================================================
    // DUYET / TU CHOI (Admin)
    // ================================================================

    @Override
    @Transactional
    public void duyetDangKy(Long dangKyId) {
        DangKyNguyenVong dk = dangKyNvRepo.findById(dangKyId)
            .orElseThrow(() -> new ResourceNotFoundException("DangKyNguyenVong", dangKyId));
        if ("DA_HUY".equals(dk.getTrangThai())) {
            throw new NghiepVuException("Khong the duyet ban ghi da huy.");
        }
        dk.setTrangThai("DA_DUYET");
        dangKyNvRepo.save(dk);
        log.info("Admin duyet dang ky nguyen vong id={}", dangKyId);
    }

    @Override
    @Transactional
    public void tuChoiDangKy(Long dangKyId) {
        DangKyNguyenVong dk = dangKyNvRepo.findById(dangKyId)
            .orElseThrow(() -> new ResourceNotFoundException("DangKyNguyenVong", dangKyId));
        dk.setTrangThai("DA_HUY");
        dangKyNvRepo.save(dk);
        log.info("Admin tu choi dang ky nguyen vong id={}", dangKyId);
    }

    @Override
    @Transactional
    public int duyetTatCa(Long keHoachId) {
        List<DangKyNguyenVong> choDuyet = dangKyNvRepo.findByKeHoach(keHoachId)
            .stream()
            .filter(dk -> "CHO_DUYET".equals(dk.getTrangThai()))
            .toList();
        choDuyet.forEach(dk -> dk.setTrangThai("DA_DUYET"));
        dangKyNvRepo.saveAll(choDuyet);
        log.info("Admin duyet tat ca {} ban ghi trong ke hoach {}", choDuyet.size(), keHoachId);
        return choDuyet.size();
    }
}
