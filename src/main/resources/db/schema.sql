-- =============================================================
-- Schema: Quan ly dang ky hoc phan theo tin chi
-- Database: MySQL >= 8.0.16 (de dung CHECK constraint)
-- Thu tu tao bang: cha truoc, con sau (tranh loi FK)
-- =============================================================

CREATE DATABASE IF NOT EXISTS quan_ly_hoc_phan
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE quan_ly_hoc_phan;

-- =============================================================
-- 1. NGANH
-- =============================================================
CREATE TABLE IF NOT EXISTS nganh (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_nganh   VARCHAR(20)  NOT NULL,
    ten_nganh  VARCHAR(255) NOT NULL,
    CONSTRAINT uq_nganh_ma_nganh UNIQUE (ma_nganh)
);

-- =============================================================
-- 2. SINH_VIEN
-- =============================================================
CREATE TABLE IF NOT EXISTS sinh_vien (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    mssv           VARCHAR(20)  NOT NULL,
    ho_ten         VARCHAR(255) NOT NULL,
    email          VARCHAR(255) NOT NULL,
    mat_khau       VARCHAR(255) NOT NULL,
    ngay_sinh      DATE,
    lop_sinh_hoat  VARCHAR(50),
    khoa_hoc       VARCHAR(20),
    nganh_id       BIGINT,
    trang_thai     VARCHAR(30)  NOT NULL DEFAULT 'DANG_HOC',
    CONSTRAINT uq_sv_mssv  UNIQUE (mssv),
    CONSTRAINT uq_sv_email UNIQUE (email),
    CONSTRAINT fk_sv_nganh FOREIGN KEY (nganh_id) REFERENCES nganh (id)
);

-- =============================================================
-- 3. GIANG_VIEN
-- =============================================================
CREATE TABLE IF NOT EXISTS giang_vien (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_gv        VARCHAR(20)  NOT NULL,
    ho_ten       VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    mat_khau     VARCHAR(255) NOT NULL,
    khoa_bo_mon  VARCHAR(255),
    CONSTRAINT uq_gv_ma_gv UNIQUE (ma_gv),
    CONSTRAINT uq_gv_email UNIQUE (email)
);

-- =============================================================
-- 4. MON_HOC
-- =============================================================
CREATE TABLE IF NOT EXISTS mon_hoc (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_mon              VARCHAR(20)  NOT NULL,
    ten_mon             VARCHAR(255) NOT NULL,
    so_tin_chi          INT          NOT NULL,
    so_tiet_ly_thuyet   INT          NOT NULL DEFAULT 0,
    so_tiet_thuc_hanh   INT          NOT NULL DEFAULT 0,
    mo_ta               TEXT,
    CONSTRAINT uq_mh_ma_mon UNIQUE (ma_mon),
    CONSTRAINT chk_mh_tin_chi CHECK (so_tin_chi > 0)
);

-- =============================================================
-- 5. MON_TIEN_QUYET
-- CHECK: mon_hoc_id <> mon_tien_quyet_id (khong tu tien quyet chinh minh)
-- =============================================================
CREATE TABLE IF NOT EXISTS mon_tien_quyet (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    mon_hoc_id          BIGINT NOT NULL,
    mon_tien_quyet_id   BIGINT NOT NULL,
    CONSTRAINT uq_mtq UNIQUE (mon_hoc_id, mon_tien_quyet_id),
    CONSTRAINT chk_mtq_no_self CHECK (mon_hoc_id <> mon_tien_quyet_id),
    CONSTRAINT fk_mtq_mon_hoc    FOREIGN KEY (mon_hoc_id)        REFERENCES mon_hoc (id),
    CONSTRAINT fk_mtq_tien_quyet FOREIGN KEY (mon_tien_quyet_id) REFERENCES mon_hoc (id)
);

-- =============================================================
-- 6. CHUONG_TRINH_DAO_TAO
-- =============================================================
CREATE TABLE IF NOT EXISTS chuong_trinh_dao_tao (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    nganh_id     BIGINT  NOT NULL,
    mon_hoc_id   BIGINT  NOT NULL,
    hoc_ky_thu   INT     NOT NULL,
    bat_buoc     BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_ctdt UNIQUE (nganh_id, mon_hoc_id),
    CONSTRAINT fk_ctdt_nganh   FOREIGN KEY (nganh_id)   REFERENCES nganh   (id),
    CONSTRAINT fk_ctdt_mon_hoc FOREIGN KEY (mon_hoc_id) REFERENCES mon_hoc (id)
);

-- =============================================================
-- 7. HOC_KY
-- =============================================================
CREATE TABLE IF NOT EXISTS hoc_ky (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten_hoc_ky          VARCHAR(100) NOT NULL,
    nam_hoc             VARCHAR(20)  NOT NULL,
    hoc_ky_thu          INT          NOT NULL,
    ngay_bat_dau_dk     DATE         NOT NULL,
    ngay_ket_thuc_dk    DATE         NOT NULL,
    ngay_bat_dau_hoc    DATE         NOT NULL,
    ngay_ket_thuc_hoc   DATE         NOT NULL,
    tin_chi_toi_thieu   INT          NOT NULL DEFAULT 0,
    tin_chi_toi_da      INT          NOT NULL DEFAULT 25,
    CONSTRAINT uq_hk UNIQUE (nam_hoc, hoc_ky_thu),
    CONSTRAINT chk_hk_dk  CHECK (ngay_ket_thuc_dk  >= ngay_bat_dau_dk),
    CONSTRAINT chk_hk_hoc CHECK (ngay_ket_thuc_hoc >= ngay_bat_dau_hoc),
    CONSTRAINT chk_hk_tc  CHECK (tin_chi_toi_da >= tin_chi_toi_thieu)
);

-- =============================================================
-- 8. LOP_HOC_PHAN
-- KHONG co cot phong - phong chi nam o lich_hoc.phong
-- version: dung cho Optimistic Locking (@Version trong JPA)
-- =============================================================
CREATE TABLE IF NOT EXISTS lop_hoc_phan (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_lop_hp        VARCHAR(50)  NOT NULL,
    mon_hoc_id       BIGINT       NOT NULL,
    hoc_ky_id        BIGINT       NOT NULL,
    giang_vien_id    BIGINT,
    si_so_toi_da     INT          NOT NULL DEFAULT 50,
    si_so_hien_tai   INT          NOT NULL DEFAULT 0,
    trang_thai       VARCHAR(30)  NOT NULL DEFAULT 'MO',
    version          BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT uq_lhp_ma_lop_hp UNIQUE (ma_lop_hp),
    CONSTRAINT chk_lhp_si_so CHECK (si_so_hien_tai >= 0 AND si_so_hien_tai <= si_so_toi_da),
    CONSTRAINT fk_lhp_mon_hoc    FOREIGN KEY (mon_hoc_id)    REFERENCES mon_hoc   (id),
    CONSTRAINT fk_lhp_hoc_ky     FOREIGN KEY (hoc_ky_id)     REFERENCES hoc_ky    (id),
    CONSTRAINT fk_lhp_giang_vien FOREIGN KEY (giang_vien_id) REFERENCES giang_vien(id)
);

-- =============================================================
-- 9. LICH_HOC
-- phong nam o day (khong phai lop_hoc_phan) vi 1 lop co the
-- hoc nhieu buoi o nhieu phong khac nhau
-- thu: 2-8 (Thu Hai=2 ... Chu Nhat=8)
-- =============================================================
CREATE TABLE IF NOT EXISTS lich_hoc (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    lop_hoc_phan_id  BIGINT      NOT NULL,
    thu              INT         NOT NULL,
    tiet_bat_dau     INT         NOT NULL,
    tiet_ket_thuc    INT         NOT NULL,
    phong            VARCHAR(50),
    CONSTRAINT chk_lh_thu  CHECK (thu BETWEEN 2 AND 8),
    CONSTRAINT chk_lh_tiet CHECK (tiet_bat_dau >= 1 AND tiet_ket_thuc >= tiet_bat_dau),
    CONSTRAINT fk_lh_lop_hoc_phan FOREIGN KEY (lop_hoc_phan_id) REFERENCES lop_hoc_phan (id)
);

-- =============================================================
-- 10. DANG_KY_HOC_PHAN
-- KHONG co hoc_ky_id - hoc ky suy ra qua lop_hoc_phan.hoc_ky_id
-- trang_thai: DA_DANG_KY / DA_HUY / HOAN_THANH
-- =============================================================
CREATE TABLE IF NOT EXISTS dang_ky_hoc_phan (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    sinh_vien_id      BIGINT      NOT NULL,
    lop_hoc_phan_id   BIGINT      NOT NULL,
    ngay_dang_ky      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    trang_thai        VARCHAR(30) NOT NULL DEFAULT 'DA_DANG_KY',
    diem_giua_ky      DECIMAL(4,2),
    diem_cuoi_ky      DECIMAL(4,2),
    diem_tong_ket     DECIMAL(4,2),
    CONSTRAINT uq_dkhp UNIQUE (sinh_vien_id, lop_hoc_phan_id),
    CONSTRAINT chk_dkhp_diem_gk CHECK (diem_giua_ky  IS NULL OR (diem_giua_ky  BETWEEN 0 AND 10)),
    CONSTRAINT chk_dkhp_diem_ck CHECK (diem_cuoi_ky  IS NULL OR (diem_cuoi_ky  BETWEEN 0 AND 10)),
    CONSTRAINT chk_dkhp_diem_tk CHECK (diem_tong_ket IS NULL OR (diem_tong_ket BETWEEN 0 AND 10)),
    CONSTRAINT fk_dkhp_sinh_vien     FOREIGN KEY (sinh_vien_id)    REFERENCES sinh_vien    (id),
    CONSTRAINT fk_dkhp_lop_hoc_phan  FOREIGN KEY (lop_hoc_phan_id) REFERENCES lop_hoc_phan (id)
);

-- =============================================================
-- INDEX bo sung (tang toc cac query nghiep vu thuong dung)
-- =============================================================

-- Tim lich hoc theo lop (check trung lich)
CREATE INDEX idx_lh_thu_tiet ON lich_hoc (lop_hoc_phan_id, thu, tiet_bat_dau, tiet_ket_thuc);

-- Tim dang ky cua SV (xem thoi khoa bieu, kiem tra trung lop)
CREATE INDEX idx_dkhp_sv ON dang_ky_hoc_phan (sinh_vien_id, trang_thai);

-- Tim dang ky theo lop (dem si so, nhap diem GV)
CREATE INDEX idx_dkhp_lhp ON dang_ky_hoc_phan (lop_hoc_phan_id, trang_thai);

-- Tim lop hoc phan theo hoc ky
CREATE INDEX idx_lhp_hk ON lop_hoc_phan (hoc_ky_id, trang_thai);

-- =============================================================
-- 11. KE_HOACH_NGUYEN_VONG
-- Admin tao ke hoach, gom cac mon hoc de SV dang ky nguyen vong
-- trang_thai: DANG_MO / DA_DONG
-- =============================================================
CREATE TABLE IF NOT EXISTS ke_hoach_nguyen_vong (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten_ke_hoach    VARCHAR(255) NOT NULL,
    mo_ta           TEXT,
    hoc_ky_id       BIGINT NOT NULL,
    ngay_bat_dau    DATE NOT NULL,
    ngay_ket_thuc   DATE NOT NULL,
    trang_thai      VARCHAR(20) NOT NULL DEFAULT 'DANG_MO',
    CONSTRAINT fk_khnv_hoc_ky FOREIGN KEY (hoc_ky_id) REFERENCES hoc_ky(id),
    CONSTRAINT chk_khnv_trang_thai CHECK (trang_thai IN ('DANG_MO','DA_DONG'))
);

-- =============================================================
-- 12. NGUYEN_VONG_MON_HOC
-- Cac mon hoc duoc admin them vao 1 ke hoach nguyen vong
-- =============================================================
CREATE TABLE IF NOT EXISTS nguyen_vong_mon_hoc (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    ke_hoach_nguyen_vong_id BIGINT NOT NULL,
    mon_hoc_id              BIGINT NOT NULL,
    CONSTRAINT uq_nvmh UNIQUE (ke_hoach_nguyen_vong_id, mon_hoc_id),
    CONSTRAINT fk_nvmh_ke_hoach FOREIGN KEY (ke_hoach_nguyen_vong_id) REFERENCES ke_hoach_nguyen_vong(id),
    CONSTRAINT fk_nvmh_mon_hoc  FOREIGN KEY (mon_hoc_id) REFERENCES mon_hoc(id)
);

-- =============================================================
-- 13. DANG_KY_NGUYEN_VONG
-- SV dang ky nguyen vong theo tung mon trong ke hoach
-- trang_thai: CHO_DUYET / DA_DUYET / DA_HUY
-- =============================================================
CREATE TABLE IF NOT EXISTS dang_ky_nguyen_vong (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    sinh_vien_id            BIGINT NOT NULL,
    nguyen_vong_mon_hoc_id  BIGINT NOT NULL,
    ngay_dang_ky            DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    trang_thai              VARCHAR(20) NOT NULL DEFAULT 'CHO_DUYET',
    CONSTRAINT uq_dknv UNIQUE (sinh_vien_id, nguyen_vong_mon_hoc_id),
    CONSTRAINT chk_dknv_trang_thai CHECK (trang_thai IN ('CHO_DUYET','DA_DUYET','DA_HUY')),
    CONSTRAINT fk_dknv_sinh_vien        FOREIGN KEY (sinh_vien_id)           REFERENCES sinh_vien(id),
    CONSTRAINT fk_dknv_nguyen_vong_mh   FOREIGN KEY (nguyen_vong_mon_hoc_id) REFERENCES nguyen_vong_mon_hoc(id)
);

-- =============================================================
-- 14. DINH_HUONG
-- Admin tao dinh huong hoc tap cho tung nganh
-- trang_thai: DANG_MO / DA_DONG
-- =============================================================
CREATE TABLE IF NOT EXISTS dinh_huong (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten_dinh_huong  VARCHAR(255) NOT NULL,
    mo_ta           TEXT,
    nganh_id        BIGINT NOT NULL,
    ngay_bat_dau    DATE,
    ngay_ket_thuc   DATE,
    che_do_dang_ky  VARCHAR(50) DEFAULT 'BAT_BUOC',
    trang_thai      VARCHAR(20) NOT NULL DEFAULT 'DANG_MO',
    CONSTRAINT fk_dh_nganh FOREIGN KEY (nganh_id) REFERENCES nganh(id)
);

-- =============================================================
-- 15. DANG_KY_DINH_HUONG
-- SV dang ky 1 dinh huong (moi SV chi duoc chon 1 dinh huong
-- trong cung 1 nganh tại 1 thoi diem)
-- =============================================================
CREATE TABLE IF NOT EXISTS dang_ky_dinh_huong (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    sinh_vien_id    BIGINT NOT NULL,
    dinh_huong_id   BIGINT NOT NULL,
    ngay_dang_ky    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    trang_thai      VARCHAR(20) NOT NULL DEFAULT 'DA_DANG_KY',
    CONSTRAINT uq_dkdh UNIQUE (sinh_vien_id, dinh_huong_id),
    CONSTRAINT fk_dkdh_sv FOREIGN KEY (sinh_vien_id) REFERENCES sinh_vien(id),
    CONSTRAINT fk_dkdh_dh FOREIGN KEY (dinh_huong_id) REFERENCES dinh_huong(id)
);

-- =============================================================
-- 16. KHOI_KIEN_THUC
-- Nhom cac mon hoc theo khoi (VD: KT1.1, KT3.1, GDTC, QPAN...)
-- =============================================================
CREATE TABLE IF NOT EXISTS khoi_kien_thuc (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_khoi     VARCHAR(30)  NOT NULL,
    ten_khoi    VARCHAR(255) NOT NULL,
    nganh_id    BIGINT,
    CONSTRAINT uq_khoi_ma UNIQUE (ma_khoi, nganh_id),
    CONSTRAINT fk_khoi_nganh FOREIGN KEY (nganh_id) REFERENCES nganh(id)
);

-- =============================================================
-- 17. CHUONG_TRINH_DAO_TAO cap nhat: them khoi_id
-- (ALTER TABLE de khong pha du lieu cu)
-- =============================================================
ALTER TABLE chuong_trinh_dao_tao
    ADD COLUMN IF NOT EXISTS khoi_id BIGINT NULL,
    ADD COLUMN IF NOT EXISTS diem_dat DECIMAL(4,2) NULL DEFAULT 5.00,
    ADD CONSTRAINT fk_ctdt_khoi FOREIGN KEY (khoi_id) REFERENCES khoi_kien_thuc(id);

-- =============================================================
-- 18. LICH_THI
-- Lich thi cuoi ky / giua ky cho tung lop hoc phan
-- loai_thi: GIUA_KY / CUOI_KY
-- hinh_thuc: TU_LUAN / TRAC_NGHIEM / THUC_HANH / VAN_DAP
-- =============================================================
CREATE TABLE IF NOT EXISTS lich_thi (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    lop_hoc_phan_id  BIGINT       NOT NULL,
    loai_thi         VARCHAR(20)  NOT NULL DEFAULT 'CUOI_KY',
    ngay_thi         DATE         NOT NULL,
    gio_bat_dau      TIME         NOT NULL,
    gio_ket_thuc     TIME         NOT NULL,
    phong_thi        VARCHAR(50),
    hinh_thuc        VARCHAR(30)  NOT NULL DEFAULT 'TU_LUAN',
    ghi_chu          TEXT,
    CONSTRAINT chk_lt_gio CHECK (gio_ket_thuc > gio_bat_dau),
    CONSTRAINT chk_lt_loai CHECK (loai_thi IN ('GIUA_KY','CUOI_KY')),
    CONSTRAINT fk_lt_lop_hoc_phan FOREIGN KEY (lop_hoc_phan_id) REFERENCES lop_hoc_phan(id)
);

CREATE INDEX IF NOT EXISTS idx_lt_lhp ON lich_thi (lop_hoc_phan_id);

-- =============================================================
-- 19. ALTER chuong_trinh_dao_tao: them cac cot cho trang CTDT
-- =============================================================
ALTER TABLE chuong_trinh_dao_tao
    ADD COLUMN IF NOT EXISTS so_tiet_phi          INT          NULL,
    ADD COLUMN IF NOT EXISTS hoc_ky_du_kien_label VARCHAR(50)  NULL,
    ADD COLUMN IF NOT EXISTS hoc_ky_thuc_te_label VARCHAR(50)  NULL,
    ADD COLUMN IF NOT EXISTS lt_bt                INT          NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS so_tiet_th           INT          NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS btl_damh             INT          NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS tt_da_kltn           INT          NOT NULL DEFAULT 0;

-- =============================================================
-- 20. ADMIN (thay hardcode trong CustomUserDetailsService)
-- =============================================================
CREATE TABLE IF NOT EXISTS admin (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    mat_khau   VARCHAR(255) NOT NULL,
    ho_ten     VARCHAR(255) NOT NULL,
    CONSTRAINT uq_admin_email UNIQUE (email)
);

-- =============================================================
-- 21. DANG_KY_THI_LAI
-- SV dang ky thi lai mon chua dat (diem_tong_ket < 5)
-- trang_thai: CHO_DUYET / DA_DUYET / DA_HUY
-- =============================================================
CREATE TABLE IF NOT EXISTS dang_ky_thi_lai (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    sinh_vien_id     BIGINT      NOT NULL,
    dang_ky_hp_id    BIGINT      NOT NULL,
    ngay_dang_ky     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    trang_thai       VARCHAR(20) NOT NULL DEFAULT 'CHO_DUYET',
    ghi_chu          TEXT,
    CONSTRAINT uq_dktl UNIQUE (sinh_vien_id, dang_ky_hp_id),
    CONSTRAINT chk_dktl_tt CHECK (trang_thai IN ('CHO_DUYET','DA_DUYET','DA_HUY')),
    CONSTRAINT fk_dktl_sv   FOREIGN KEY (sinh_vien_id)  REFERENCES sinh_vien(id),
    CONSTRAINT fk_dktl_dkhp FOREIGN KEY (dang_ky_hp_id) REFERENCES dang_ky_hoc_phan(id)
);
