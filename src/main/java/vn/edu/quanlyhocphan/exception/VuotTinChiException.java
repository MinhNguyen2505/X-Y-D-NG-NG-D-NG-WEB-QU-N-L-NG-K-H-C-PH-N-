package vn.edu.quanlyhocphan.exception;

/** Tong tin chi se vuot qua gioi han cho phep trong hoc ky. */
public class VuotTinChiException extends NghiepVuException {
    public VuotTinChiException(int tinChiHienTai, int tinChiThem, int tinChiToiDa) {
        super("Dang ky that bai: Tong tin chi sau khi dang ky se la "
            + (tinChiHienTai + tinChiThem)
            + " (toi da: " + tinChiToiDa + "). "
            + "Hien tai ban da dang ky " + tinChiHienTai + " tin chi.");
    }
}
