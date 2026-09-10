-- =============================================================
-- RESET + INSERT DU LIEU MAU
-- Chay file nay trong phpMyAdmin de cap nhat hash BCrypt dung
-- Password tat ca: 123456
-- Hash BCrypt dung: $2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny
-- =============================================================

USE quan_ly_hoc_phan;

-- Xoa du lieu cu (thu tu nguoc FK)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE dang_ky_hoc_phan;
TRUNCATE TABLE lich_hoc;
TRUNCATE TABLE lop_hoc_phan;
TRUNCATE TABLE chuong_trinh_dao_tao;
TRUNCATE TABLE mon_tien_quyet;
TRUNCATE TABLE hoc_ky;
TRUNCATE TABLE mon_hoc;
TRUNCATE TABLE sinh_vien;
TRUNCATE TABLE giang_vien;
TRUNCATE TABLE nganh;
SET FOREIGN_KEY_CHECKS = 1;

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
-- 2. GIANG VIEN  |  mat_khau = 123456
-- =============================================================
INSERT INTO giang_vien (ma_gv, ho_ten, email, mat_khau, khoa_bo_mon) VALUES
('GV001', 'Nguyen Van An',  'gv001@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa CNTT'),
('GV002', 'Tran Thi Bich',  'gv002@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa CNTT'),
('GV003', 'Le Van Cuong',   'gv003@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa Toan - Tin'),
('GV004', 'Pham Thi Dung',  'gv004@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa Dien tu'),
('GV005', 'Hoang Van Em',   'gv005@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa CNTT');

-- =============================================================
-- 3. SINH VIEN  |  mat_khau = 123456
-- =============================================================
INSERT INTO sinh_vien (mssv, ho_ten, email, mat_khau, ngay_sinh, lop_sinh_hoat, khoa_hoc, nganh_id, trang_thai) VALUES
('SV001', 'Nguyen Thi Anh',  'sv001@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', '2004-03-15', 'CNTT-K22A', 'K22', 1, 'DANG_HOC'),
('SV002', 'Tran Van Binh',   'sv002@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', '2004-07-20', 'CNTT-K22A', 'K22', 1, 'DANG_HOC'),
('SV003', 'Le Thi Cam',      'sv003@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', '2003-11-05', 'KTPM-K22B', 'K22', 2, 'DANG_HOC'),
('SV004', 'Pham Van Duc',    'sv004@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', '2004-01-30', 'HTTT-K22C', 'K22', 3, 'DANG_HOC'),
('SV005', 'Hoang Thi Eo',    'sv005@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', '2003-09-12', 'CNTT-K21A', 'K21', 1, 'DANG_HOC');

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
-- =============================================================
INSERT INTO mon_tien_quyet (mon_hoc_id, mon_tien_quyet_id) VALUES
(3, 2),  -- PROG201 <- PROG101
(4, 2),  -- DS201   <- PROG101
(5, 1),  -- DB301   <- MATH101
(7, 3),  -- WEB401  <- PROG201
(7, 5);  -- WEB401  <- DB301

-- =============================================================
-- 6. CHUONG TRINH DAO TAO (nganh CNTT)
-- =============================================================
INSERT INTO chuong_trinh_dao_tao (nganh_id, mon_hoc_id, hoc_ky_thu, bat_buoc) VALUES
(1, 1, 1, TRUE),
(1, 2, 1, TRUE),
(1, 8, 2, TRUE),
(1, 3, 2, TRUE),
(1, 4, 3, TRUE),
(1, 5, 3, TRUE),
(1, 6, 4, FALSE),
(1, 7, 4, TRUE);

-- =============================================================
-- 7. HOC KY
-- =============================================================
INSERT INTO hoc_ky (ten_hoc_ky, nam_hoc, hoc_ky_thu, ngay_bat_dau_dk, ngay_ket_thuc_dk, ngay_bat_dau_hoc, ngay_ket_thuc_hoc, tin_chi_toi_thieu, tin_chi_toi_da) VALUES
('Hoc ky 1 nam 2025-2026', '2025-2026', 1, '2025-08-01', '2025-08-31', '2025-09-01', '2026-01-15', 10, 25),
('Hoc ky 2 nam 2025-2026', '2025-2026', 2, '2026-01-15', '2026-02-15', '2026-02-16', '2026-06-30', 10, 25),
('Hoc ky 1 nam 2026-2027', '2026-2027', 1, '2026-09-01', '2026-09-30', '2026-10-01', '2027-01-15', 10, 25);

-- =============================================================
-- 8. LOP HOC PHAN (hoc ky 3 - dang mo dang ky)
-- =============================================================
INSERT INTO lop_hoc_phan (ma_lop_hp, mon_hoc_id, hoc_ky_id, giang_vien_id, si_so_toi_da, si_so_hien_tai, trang_thai, version) VALUES
('MATH101-01', 1, 3, 3, 50, 0, 'MO', 0),
('MATH101-02', 1, 3, 3, 40, 0, 'MO', 0),
('PROG101-01', 2, 3, 1, 45, 0, 'MO', 0),
('PROG101-02', 2, 3, 2, 45, 0, 'MO', 0),
('OS201-01',   8, 3, 5, 50, 0, 'MO', 0),
('DB301-01',   5, 3, 1, 40, 0, 'MO', 0),
('PROG201-01', 3, 3, 2, 45, 0, 'MO', 0),
('WEB401-01',  7, 3, 4, 35, 0, 'MO', 0);

-- =============================================================
-- 9. LICH HOC
-- =============================================================
INSERT INTO lich_hoc (lop_hoc_phan_id, thu, tiet_bat_dau, tiet_ket_thuc, phong) VALUES
(1, 2, 1, 3, 'A101'),
(2, 4, 1, 3, 'A102'),
(3, 2, 6, 8, 'B201'),
(3, 4, 6, 8, 'Lab1'),
(4, 3, 1, 3, 'B202'),
(4, 5, 1, 3, 'Lab2'),
(5, 3, 6, 8, 'A201'),
(6, 5, 6, 8, 'A301'),
(6, 6, 6, 8, 'Lab3'),
(7, 6, 1, 3, 'B301'),
(7, 4, 1, 3, 'Lab4'),
(8, 5, 1, 3, 'C401');

-- =============================================================
-- KE HOACH NGUYEN VONG mau
-- =============================================================
INSERT INTO ke_hoach_nguyen_vong (ten_ke_hoach, mo_ta, hoc_ky_id, ngay_bat_dau, ngay_ket_thuc, trang_thai) VALUES
('Đăng ký nguyện vọng HK1 2026-2027',
 'Sinh viên đăng ký nguyện vọng các môn học dự kiến cho học kỳ 1 năm học 2026-2027',
 3, '2026-09-01', '2026-09-30', 'DANG_MO');

-- Them cac mon hoc vao ke hoach (ke_hoach id=1, cac mon id 1-8)
INSERT INTO nguyen_vong_mon_hoc (ke_hoach_nguyen_vong_id, mon_hoc_id) VALUES
(1, 1), -- MATH101
(1, 2), -- PROG101
(1, 3), -- PROG201
(1, 4), -- DS201
(1, 5), -- DB301
(1, 6), -- NET301
(1, 7), -- WEB401
(1, 8); -- OS201

-- =============================================================
-- DINH HUONG mau (nganh CNTT id=1, KTPM id=2)
-- =============================================================
INSERT INTO dinh_huong (ten_dinh_huong, mo_ta, nganh_id, ngay_bat_dau, ngay_ket_thuc, che_do_dang_ky, trang_thai) VALUES
('Công nghệ phần mềm',   'Định hướng phát triển phần mềm, lập trình ứng dụng', 1, '2026-09-01', '2026-09-30', 'BAT_BUOC', 'DANG_MO'),
('Hệ thống thông tin',   'Định hướng phân tích thiết kế hệ thống thông tin',    1, '2026-09-01', '2026-09-30', 'BAT_BUOC', 'DANG_MO'),
('Kỹ thuật phần mềm',    'Định hướng kiểm thử và đảm bảo chất lượng phần mềm', 2, '2026-09-01', '2026-09-30', 'TU_CHON',  'DANG_MO'),
('Phân tích dữ liệu',    'Định hướng xử lý và phân tích dữ liệu lớn',          2, '2026-09-01', '2026-09-30', 'TU_CHON',  'DANG_MO');
