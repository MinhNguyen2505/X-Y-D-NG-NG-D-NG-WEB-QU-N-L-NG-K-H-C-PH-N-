package vn.edu.quanlyhocphan.exception;

/** SV da dang ky lop hoc phan nay roi (rang buoc UNIQUE). */
public class TrungLopException extends NghiepVuException {
    public TrungLopException(String maLopHp) {
        super("Ban da dang ky lop hoc phan [" + maLopHp + "] roi. Khong the dang ky lai.");
    }
}
