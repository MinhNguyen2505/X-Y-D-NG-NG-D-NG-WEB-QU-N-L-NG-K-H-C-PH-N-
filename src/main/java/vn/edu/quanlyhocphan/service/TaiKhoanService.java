package vn.edu.quanlyhocphan.service;

public interface TaiKhoanService {

    /**
     * Thay doi mat khau cho tai khoan dang dang nhap (ho tro tat ca cac role: Admin, QuanLy, SinhVien, GiangVien).
     *
     * @param email             Email cua user dang dang nhap
     * @param matKhauCu         Mat khau hien tai
     * @param matKhauMoi        Mat khau moi (toi thieu 6 ky tu)
     * @param xacNhanMatKhauMoi Xac nhan mat khau moi
     */
    void doiMatKhau(String email, String matKhauCu, String matKhauMoi, String xacNhanMatKhauMoi);

    /**
     * Dat lai mat khau ve mac dinh cho user (chuc nang danh cho Admin khi user quen mat khau).
     *
     * @param loaiUser         "sinh-vien", "giang-vien", hoac "quan-ly"
     * @param id               ID cua user
     * @param matKhauMacDinh   Mat khau mac dinh moi (vi du: 123456)
     */
    void resetMatKhau(String loaiUser, Long id, String matKhauMacDinh);
}
