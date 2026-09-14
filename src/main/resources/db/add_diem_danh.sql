USE quan_ly_hoc_phan;

CREATE TABLE IF NOT EXISTS diem_danh (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    dang_ky_id       BIGINT      NOT NULL,
    ngay_hoc         DATE        NOT NULL,
    tiet_bat_dau     INT         NOT NULL,
    tiet_ket_thuc    INT         NOT NULL,
    trang_thai       VARCHAR(30) NOT NULL DEFAULT 'CO_MAT',
    ghi_chu          TEXT,
    CONSTRAINT uq_dd UNIQUE (dang_ky_id, ngay_hoc, tiet_bat_dau),
    CONSTRAINT chk_dd_tt CHECK (trang_thai IN ('CO_MAT','VANG_CO_PHEP','VANG_KHONG_PHEP')),
    CONSTRAINT fk_dd_dkhp FOREIGN KEY (dang_ky_id) REFERENCES dang_ky_hoc_phan(id)
);

CREATE INDEX IF NOT EXISTS idx_dd_dkhp ON diem_danh (dang_ky_id);
CREATE INDEX IF NOT EXISTS idx_dd_ngay ON diem_danh (ngay_hoc);
