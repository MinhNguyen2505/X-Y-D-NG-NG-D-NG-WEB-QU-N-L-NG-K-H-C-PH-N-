package vn.edu.quanlyhocphan.exception;

/**
 * Khong tim thay entity (SinhVien, LopHocPhan, HocKy...) theo id hoac ma tuong ung.
 */
public class EntityNotFoundException extends NghiepVuException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
