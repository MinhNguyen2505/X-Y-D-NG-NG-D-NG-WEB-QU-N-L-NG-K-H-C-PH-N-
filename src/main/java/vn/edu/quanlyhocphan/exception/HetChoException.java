package vn.edu.quanlyhocphan.exception;

/** Lop hoc phan da het cho (si_so_hien_tai >= si_so_toi_da). */
public class HetChoException extends NghiepVuException {
    public HetChoException(String maLopHp) {
        super("Lop hoc phan [" + maLopHp + "] da het cho.");
    }
}
