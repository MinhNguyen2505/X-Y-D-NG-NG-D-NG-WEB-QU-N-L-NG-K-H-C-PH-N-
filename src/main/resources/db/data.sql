-- =============================================================
-- Du lieu mau de test 3 role: Admin, Sinh Vien, Giang Vien
-- Password tat ca: 123456 (BCrypt)
-- =============================================================

USE quan_ly_hoc_phan;

-- =============================================================
-- 1. NGANH
-- =============================================================
INSERT INTO nganh (ma_nganh, ten_nganh) VALUES
('CNTT',   'Cong nghe Thong tin'),
('KTPM',   'Ky thuat Phan mem'),
('HTTT',   'He thong Thong tin'),
('KHMT',   'Khoa hoc May tinh'),
('MMT',    'Mang may tinh va Truyen thong');

-- =============================================================
-- 2. GIANG VIEN (mat_khau = 123456 BCrypt)
-- =============================================================
INSERT INTO giang_vien (ma_gv, ho_ten, email, mat_khau, khoa_bo_mon) VALUES
('GV001', 'Nguyen Van An',    'gv001@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa CNTT'),
('GV002', 'Tran Thi Bich',    'gv002@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa CNTT'),
('GV003', 'Le Van Cuong',     'gv003@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa Toan - Tin'),
('GV004', 'Pham Thi Dung',    'gv004@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa Dien tu - Vien thong'),
('GV005', 'Hoang Van Em',     'gv005@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa CNTT');

-- =============================================================
-- 3. SINH VIEN (mat_khau = 123456 BCrypt)
--    nganh_id: 1=CNTT, 2=KTPM, 3=HTTT
-- =============================================================
INSERT INTO sinh_vien (mssv, ho_ten, email, mat_khau, ngay_sinh, lop_sinh_hoat, khoa_hoc, nganh_id, trang_thai) VALUES
('SV001', 'Nguyen Thi Anh',   'sv001@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', '2004-03-15', 'CNTT-K22A', 'K22', 1, 'DANG_HOC'),
('SV002', 'Tran Van Binh',    'sv002@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', '2004-07-20', 'CNTT-K22A', 'K22', 1, 'DANG_HOC'),
('SV003', 'Le Thi Cam',       'sv003@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', '2003-11-05', 'KTPM-K22B', 'K22', 2, 'DANG_HOC'),
('SV004', 'Pham Van Duc',     'sv004@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', '2004-01-30', 'HTTT-K22C', 'K22', 3, 'DANG_HOC'),
('SV005', 'Hoang Thi Eo',     'sv005@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', '2003-09-12', 'CNTT-K21A', 'K21', 1, 'DANG_HOC');

-- =============================================================
-- 4. MON HOC
-- =============================================================
INSERT INTO mon_hoc (ma_mon, ten_mon, so_tin_chi, so_tiet_ly_thuyet, so_tiet_thuc_hanh, mo_ta) VALUES
('MATH101', 'Toan roi rac',              3, 45, 0,  'Co so toan hoc cho CNTT'),
('PROG101', 'Lap trinh can ban',         3, 30, 30, 'Ngon ngu C/C++'),
('PROG201', 'Lap trinh huong doi tuong', 3, 30, 30, 'Java OOP'),
('DS201',   'Cau truc du lieu va GT',    3, 30, 30, 'CTDL co ban'),
('DB301',   'Co so du lieu',             3, 30, 30, 'SQL va thiet ke CSDL'),
('NET301',  'Mang may tinh',             3, 45, 0,  'TCP/IP, OSI'),
('WEB401',  'Lap trinh Web',             3, 30, 30, 'HTML, CSS, JS, Spring Boot'),
('OS201',   'He dieu hanh',              3, 45, 0,  'Linux, Windows');

-- =============================================================
-- 5. MON TIEN QUYET
--    PROG201 can PROG101 truoc
--    DS201   can PROG101 truoc
--    DB301   can MATH101 truoc
--    WEB401  can PROG201 va DB301 truoc
-- =============================================================
INSERT INTO mon_tien_quyet (mon_hoc_id, mon_tien_quyet_id) VALUES
(3, 2),  -- PROG201 <- PROG101
(4, 2),  -- DS201   <- PROG101
(5, 1),  -- DB301   <- MATH101
(7, 3),  -- WEB401  <- PROG201
(7, 5);  -- WEB401  <- DB301

-- =============================================================
-- 6. CHUONG TRINH DAO TAO (nganh CNTT - id=1)
-- =============================================================
INSERT INTO chuong_trinh_dao_tao (nganh_id, mon_hoc_id, hoc_ky_thu, bat_buoc) VALUES
(1, 1, 1, TRUE),   -- MATH101 - HK1
(1, 2, 1, TRUE),   -- PROG101 - HK1
(1, 8, 2, TRUE),   -- OS201   - HK2
(1, 3, 2, TRUE),   -- PROG201 - HK2
(1, 4, 3, TRUE),   -- DS201   - HK3
(1, 5, 3, TRUE),   -- DB301   - HK3
(1, 6, 4, FALSE),  -- NET301  - HK4 (tu chon)
(1, 7, 4, TRUE);   -- WEB401  - HK4

-- =============================================================
-- 7. HOC KY
--    Hoc ky hien tai: mo dang ky de test
-- =============================================================
INSERT INTO hoc_ky (ten_hoc_ky, nam_hoc, hoc_ky_thu, ngay_bat_dau_dk, ngay_ket_thuc_dk, ngay_bat_dau_hoc, ngay_ket_thuc_hoc, tin_chi_toi_thieu, tin_chi_toi_da) VALUES
('Hoc ky 1 nam 2025-2026', '2025-2026', 1, '2025-08-01', '2025-08-31', '2025-09-01', '2026-01-15', 10, 25),
('Hoc ky 2 nam 2025-2026', '2025-2026', 2, '2026-01-15', '2026-02-15', '2026-02-16', '2026-06-30', 10, 25),
-- Hoc ky dang mo dang ky (ngay hien tai 09/09/2026 nam trong khoang)
('Hoc ky 1 nam 2026-2027', '2026-2027', 1, '2026-09-01', '2026-09-30', '2026-10-01', '2027-01-15', 10, 25);

-- =============================================================
-- 8. LOP HOC PHAN (hoc ky 3 - id=3, dang mo dang ky)
--    giang_vien_id: GV001=1, GV002=2, GV003=3, GV004=4, GV005=5
-- =============================================================
INSERT INTO lop_hoc_phan (ma_lop_hp, mon_hoc_id, hoc_ky_id, giang_vien_id, si_so_toi_da, si_so_hien_tai, trang_thai, version) VALUES
('MATH101-01', 1, 3, 3, 50, 0, 'MO', 0),
('MATH101-02', 1, 3, 3, 40, 0, 'MO', 0),
('PROG101-01', 2, 3, 1, 45, 0, 'MO', 0),
('PROG101-02', 2, 3, 2, 45, 0, 'MO', 0),
('OS201-01',   8, 3, 5, 50, 0, 'MO', 0),
('DB301-01',   5, 3, 1, 40, 0, 'MO', 0),  -- co mon tien quyet MATH101
('PROG201-01', 3, 3, 2, 45, 0, 'MO', 0),  -- co mon tien quyet PROG101
('WEB401-01',  7, 3, 4, 35, 0, 'MO', 0);  -- co mon tien quyet PROG201+DB301

-- =============================================================
-- 9. LICH HOC
--    thu: 2=Thu Hai, 3=Thu Ba, 4=Thu Tu, 5=Thu Nam, 6=Thu Sau
--    tiet: 1-5 sang, 6-10 chieu
-- =============================================================
INSERT INTO lich_hoc (lop_hoc_phan_id, thu, tiet_bat_dau, tiet_ket_thuc, phong) VALUES
-- MATH101-01: Thu 2, tiet 1-3
(1, 2, 1, 3, 'A101'),
-- MATH101-02: Thu 4, tiet 1-3
(2, 4, 1, 3, 'A102'),
-- PROG101-01: Thu 2, tiet 6-8 + Thu 4, tiet 6-8 (ly thuyet + thuc hanh)
(3, 2, 6, 8, 'B201'),
(3, 4, 6, 8, 'Lab1'),
-- PROG101-02: Thu 3, tiet 1-3 + Thu 5, tiet 1-3
(4, 3, 1, 3, 'B202'),
(4, 5, 1, 3, 'Lab2'),
-- OS201-01: Thu 3, tiet 6-8
(5, 3, 6, 8, 'A201'),
-- DB301-01: Thu 5, tiet 6-8 + Thu 6, tiet 6-8
(6, 5, 6, 8, 'A301'),
(6, 6, 6, 8, 'Lab3'),
-- PROG201-01: Thu 2, tiet 1-3 -- TRUNG LICH voi MATH101-01 de test
-- Thay bang Thu 6, tiet 1-3
(7, 6, 1, 3, 'B301'),
(7, 4, 1, 3, 'Lab4'),
-- WEB401-01: Thu 5, tiet 1-3
(8, 5, 1, 3, 'C401');
