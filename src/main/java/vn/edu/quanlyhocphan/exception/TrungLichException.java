package vn.edu.quanlyhocphan.exception;

/** Lich hoc bi xung dot (trung thu + tiet) voi lop da dang ky. */
public class TrungLichException extends NghiepVuException {
    public TrungLichException(String maLopHpXungDot) {
        super("Lich hoc bi trung voi lop [" + maLopHpXungDot + "] da dang ky truoc do.");
    }
    public TrungLichException(String thu, String tiet) {
        super("Lich hoc bi trung: Thu " + thu + ", tiet " + tiet + ".");
    }
}
