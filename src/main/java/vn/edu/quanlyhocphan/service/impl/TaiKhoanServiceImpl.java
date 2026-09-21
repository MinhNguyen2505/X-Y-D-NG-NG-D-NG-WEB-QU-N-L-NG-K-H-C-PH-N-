package vn.edu.quanlyhocphan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.quanlyhocphan.entity.Admin;
import vn.edu.quanlyhocphan.entity.GiangVien;
import vn.edu.quanlyhocphan.entity.QuanLy;
import vn.edu.quanlyhocphan.entity.SinhVien;
import vn.edu.quanlyhocphan.exception.NghiepVuException;
import vn.edu.quanlyhocphan.exception.ResourceNotFoundException;
import vn.edu.quanlyhocphan.repository.AdminRepository;
import vn.edu.quanlyhocphan.repository.GiangVienRepository;
import vn.edu.quanlyhocphan.repository.QuanLyRepository;
import vn.edu.quanlyhocphan.repository.SinhVienRepository;
import vn.edu.quanlyhocphan.service.TaiKhoanService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaiKhoanServiceImpl implements TaiKhoanService {

    private final AdminRepository adminRepository;
    private final QuanLyRepository quanLyRepository;
    private final SinhVienRepository sinhVienRepository;
    private final GiangVienRepository giangVienRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void doiMatKhau(String email, String matKhauCu, String matKhauMoi, String xacNhanMatKhauMoi) {
        if (matKhauCu == null || matKhauCu.isBlank()) {
            throw new NghiepVuException("Vui lòng nhập mật khẩu hiện tại.");
        }
        if (matKhauMoi == null || matKhauMoi.length() < 6) {
            throw new NghiepVuException("Mật khẩu mới phải có ít nhất 6 ký tự.");
        }
        if (!matKhauMoi.equals(xacNhanMatKhauMoi)) {
            throw new NghiepVuException("Xác nhận mật khẩu mới không trùng khớp.");
        }
        if (matKhauCu.equals(matKhauMoi)) {
            throw new NghiepVuException("Mật khẩu mới không được trùng với mật khẩu hiện tại.");
        }

        // 1. Kiem tra Admin
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (!passwordEncoder.matches(matKhauCu, admin.getMatKhau())) {
                throw new NghiepVuException("Mật khẩu hiện tại không chính xác.");
            }
            admin.setMatKhau(passwordEncoder.encode(matKhauMoi));
            adminRepository.save(admin);
            log.info("Admin [{}] da doi mat khau thanh cong", email);
            return;
        }

        // 2. Kiem tra Quan Ly
        Optional<QuanLy> qlOpt = quanLyRepository.findByEmail(email);
        if (qlOpt.isPresent()) {
            QuanLy ql = qlOpt.get();
            if (!passwordEncoder.matches(matKhauCu, ql.getMatKhau())) {
                throw new NghiepVuException("Mật khẩu hiện tại không chính xác.");
            }
            ql.setMatKhau(passwordEncoder.encode(matKhauMoi));
            quanLyRepository.save(ql);
            log.info("QuanLy [{}] da doi mat khau thanh cong", email);
            return;
        }

        // 3. Kiem tra Sinh Vien
        Optional<SinhVien> svOpt = sinhVienRepository.findByEmail(email);
        if (svOpt.isPresent()) {
            SinhVien sv = svOpt.get();
            if (!passwordEncoder.matches(matKhauCu, sv.getMatKhau())) {
                throw new NghiepVuException("Mật khẩu hiện tại không chính xác.");
            }
            sv.setMatKhau(passwordEncoder.encode(matKhauMoi));
            sinhVienRepository.save(sv);
            log.info("SinhVien [{}] da doi mat khau thanh cong", email);
            return;
        }

        // 4. Kiem tra Giang Vien
        Optional<GiangVien> gvOpt = giangVienRepository.findByEmail(email);
        if (gvOpt.isPresent()) {
            GiangVien gv = gvOpt.get();
            if (!passwordEncoder.matches(matKhauCu, gv.getMatKhau())) {
                throw new NghiepVuException("Mật khẩu hiện tại không chính xác.");
            }
            gv.setMatKhau(passwordEncoder.encode(matKhauMoi));
            giangVienRepository.save(gv);
            log.info("GiangVien [{}] da doi mat khau thanh cong", email);
            return;
        }

        throw new NghiepVuException("Không tìm thấy tài khoản người dùng: " + email);
    }

    @Override
    @Transactional
    public void resetMatKhau(String loaiUser, Long id, String matKhauMacDinh) {
        String encodedPass = passwordEncoder.encode(matKhauMacDinh);
        switch (loaiUser.toLowerCase()) {
            case "sinh-vien" -> {
                SinhVien sv = sinhVienRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Sinh viên", id));
                sv.setMatKhau(encodedPass);
                sinhVienRepository.save(sv);
                log.info("Reset mat khau cho SinhVien ID [{}]", id);
            }
            case "giang-vien" -> {
                GiangVien gv = giangVienRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Giảng viên", id));
                gv.setMatKhau(encodedPass);
                giangVienRepository.save(gv);
                log.info("Reset mat khau cho GiangVien ID [{}]", id);
            }
            case "quan-ly" -> {
                QuanLy ql = quanLyRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Quản lý", id));
                ql.setMatKhau(encodedPass);
                quanLyRepository.save(ql);
                log.info("Reset mat khau cho QuanLy ID [{}]", id);
            }
            default -> throw new NghiepVuException("Loại người dùng không hợp lệ: " + loaiUser);
        }
    }
}
