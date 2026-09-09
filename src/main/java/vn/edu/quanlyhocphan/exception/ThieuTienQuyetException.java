package vn.edu.quanlyhocphan.exception;

/** SV chua hoan thanh mon tien quyet truoc khi dang ky mon nay. */
public class ThieuTienQuyetException extends NghiepVuException {
    public ThieuTienQuyetException(String tenMonTienQuyet) {
        super("Ban chua hoan thanh mon tien quyet: [" + tenMonTienQuyet + "]. "
            + "Vui long hoan thanh mon tien quyet truoc khi dang ky.");
    }
}
