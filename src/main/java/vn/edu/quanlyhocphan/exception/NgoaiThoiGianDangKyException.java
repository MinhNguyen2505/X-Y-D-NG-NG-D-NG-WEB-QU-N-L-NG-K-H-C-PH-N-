package vn.edu.quanlyhocphan.exception;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Dang ky/huy ngoai khung thoi gian cho phep cua hoc ky. */
public class NgoaiThoiGianDangKyException extends NghiepVuException {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public NgoaiThoiGianDangKyException(LocalDate batDau, LocalDate ketThuc) {
        super("Hien tai khong trong thoi gian dang ky hoc phan. "
            + "Thoi gian dang ky: " + batDau.format(FMT)
            + " den " + ketThuc.format(FMT) + ".");
    }

    public NgoaiThoiGianDangKyException(String message) {
        super(message);
    }
}
