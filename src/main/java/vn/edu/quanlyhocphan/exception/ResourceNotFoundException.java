package vn.edu.quanlyhocphan.exception;

/** Khong tim thay tai nguyen trong DB (dung chung cho moi entity). */
public class ResourceNotFoundException extends NghiepVuException {
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " voi id [" + id + "] khong ton tai.");
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
