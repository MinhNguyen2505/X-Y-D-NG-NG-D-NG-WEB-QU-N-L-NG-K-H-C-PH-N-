package vn.edu.quanlyhocphan.exception;

/**
 * Base class cho tat ca exception nghiep vu cua he thong.
 * Cac exception cu the (HetChoException, TrungLichException, ...)
 * nen extend class nay de de phan biet voi RuntimeException thong thuong.
 */
public class NghiepVuException extends RuntimeException {

    public NghiepVuException(String message) {
        super(message);
    }

    public NghiepVuException(String message, Throwable cause) {
        super(message, cause);
    }
}
