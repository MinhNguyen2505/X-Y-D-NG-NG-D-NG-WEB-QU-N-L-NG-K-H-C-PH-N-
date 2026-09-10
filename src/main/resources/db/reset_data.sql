-- =============================================================
-- RESET + INSERT DU LIEU MAU DAY DU
-- Password tat ca: 123456
-- Hash BCrypt: $2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny
-- =============================================================

USE quan_ly_hoc_phan;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE dang_ky_dinh_huong;
TRUNCATE TABLE dinh_huong;
TRUNCATE TABLE dang_ky_nguyen_vong;
TRUNCATE TABLE nguyen_vong_mon_hoc;
TRUNCATE TABLE ke_hoach_nguyen_vong;
TRUNCATE TABLE dang_ky_hoc_phan;
TRUNCATE TABLE lich_thi;
TRUNCATE TABLE lich_hoc;
TRUNCATE TABLE lop_hoc_phan;
TRUNCATE TABLE chuong_trinh_dao_tao;
TRUNCATE TABLE mon_tien_quyet;
TRUNCATE TABLE hoc_ky;
TRUNCATE TABLE mon_hoc;
TRUNCATE TABLE sinh_vien;
TRUNCATE TABLE giang_vien;
TRUNCATE TABLE khoi_kien_thuc;
TRUNCATE TABLE nganh;
SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================
-- 1. NGANH
-- =============================================================
INSERT INTO nganh (ma_nganh, ten_nganh) VALUES
('CNTT', 'Cong nghe Thong tin'),
('KTPM', 'Ky thuat Phan mem'),
('HTTT', 'He thong Thong tin'),
('KHMT', 'Khoa hoc May tinh'),
('MMT',  'Mang may tinh va Truyen thong');

-- =============================================================
-- 2. KHOI KIEN THUC
-- nganh_id NULL = dung chung cho moi nganh
-- =============================================================
INSERT INTO khoi_kien_thuc (ma_khoi, ten_khoi, nganh_id) VALUES
-- Chung
('GDTC', 'Giao duc the chat',                    NULL),
('QPAN', 'Giao duc Quoc phong va An ninh',        NULL),
-- CNTT (nganh_id = 1)
('KT1.1', 'Khoi kien thuc giao duc dai cuong',    1),
('KT2.1', 'Khoi kien thuc co so nganh',           1),
('KT3.1', 'Khoi kien thuc chuyen nganh',          1),
('KT4.1', 'Khoi tot nghiep',                      1),
-- KTPM (nganh_id = 2)
('KT1.2', 'Khoi kien thuc giao duc dai cuong',    2),
('KT2.2', 'Khoi kien thuc co so nganh',           2),
('KT3.2', 'Khoi kien thuc chuyen nganh',          2),
('KT4.2', 'Khoi tot nghiep',                      2);

-- =============================================================
-- 3. GIANG VIEN
-- =============================================================
INSERT INTO giang_vien (ma_gv, ho_ten, email, mat_khau, khoa_bo_mon) VALUES
('GV001', 'Nguyen Van An',   'gv001@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa CNTT'),
('GV002', 'Tran Thi Bich',   'gv002@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa CNTT'),
('GV003', 'Le Van Cuong',    'gv003@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa Toan - Tin'),
('GV004', 'Pham Thi Dung',   'gv004@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa Dien tu'),
('GV005', 'Hoang Van Em',    'gv005@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa CNTT'),
('GV006', 'Vu Thi Phuong',   'gv006@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa CNTT'),
('GV007', 'Ngo Van Quang',   'gv007@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', 'Khoa Ly luan chinh tri');

-- =============================================================
-- 4. SINH VIEN
-- =============================================================
INSERT INTO sinh_vien (mssv, ho_ten, email, mat_khau, ngay_sinh, lop_sinh_hoat, khoa_hoc, nganh_id, trang_thai) VALUES
('SV001', 'Nguyen Thi Anh',  'sv001@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', '2004-03-15', 'CNTT-K22A', 'K22', 1, 'DANG_HOC'),
('SV002', 'Tran Van Binh',   'sv002@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', '2004-07-20', 'CNTT-K22A', 'K22', 1, 'DANG_HOC'),
('SV003', 'Le Thi Cam',      'sv003@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', '2003-11-05', 'KTPM-K22B', 'K22', 2, 'DANG_HOC'),
('SV004', 'Pham Van Duc',    'sv004@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', '2004-01-30', 'HTTT-K22C', 'K22', 3, 'DANG_HOC'),
('SV005', 'Hoang Thi Eo',    'sv005@email.com', '$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny', '2003-09-12', 'CNTT-K21A', 'K21', 1, 'DANG_HOC');

-- =============================================================
-- 5. MON HOC (day du cho 8 HK - nganh CNTT)
-- =============================================================
INSERT INTO mon_hoc (ma_mon, ten_mon, so_tin_chi, so_tiet_ly_thuyet, so_tiet_thuc_hanh, mo_ta) VALUES
-- GDTC / QPAN
('PE1213',  'Giao duc the chat',                    3, 0,  90, 'The duc the thao'),
('QP1101',  'Giao duc quoc phong - an ninh',         8, 90, 30, 'QPAN toan khoa'),
-- HK1 - KT1.1 Dai cuong
('SSH1201', 'Triet hoc Mac - Lenin',                 3, 45, 0,  'Triet hoc Mac Lenin'),
('SSH1204', 'Lich su Dang Cong san Viet Nam',        2, 30, 0,  'Lich su Dang'),
('SSH1205', 'Tu tuong Ho Chi Minh',                  2, 30, 0,  'Tu tuong HCM'),
('SSH1206', 'Phap luat dai cuong',                   2, 30, 0,  'Phap luat'),
('SSH1207', 'Ky nang mem',                           3, 45, 0,  'Ky nang giao tiep'),
('FL1219',  'Tieng Anh 1',                           3, 30, 30, 'Tieng Anh co ban 1'),
('FL1220',  'Tieng Anh 2',                           3, 30, 30, 'Tieng Anh co ban 2'),
('MI1208',  'Tin hoc dai cuong',                     3, 30, 30, 'Tin hoc van phong'),
('MI1212',  'Dai so',                                2, 30, 0,  'Dai so tuyen tinh'),
('MI1213',  'Toan roi rac',                          2, 30, 0,  'Toan roi rac CNTT'),
('MI1216',  'Xac suat thong ke',                     2, 30, 0,  'Xac suat va thong ke'),
('MI1209',  'Giai tich',                             3, 45, 0,  'Giai tich toan'),
-- HK2 - KT2.1 Co so nganh
('IT1110',  'Lap trinh C',                           3, 30, 30, 'Lap trinh ngon ngu C'),
('IT1120',  'Nhap mon lap trinh',                    3, 30, 30, 'Nhap mon Python/Java'),
('IT2020',  'Cau truc du lieu va giai thuat',        3, 30, 30, 'CTDL va GT'),
('IT2030',  'Lap trinh huong doi tuong',             3, 30, 30, 'OOP Java'),
('IT2040',  'He dieu hanh',                          3, 45, 0,  'OS Linux/Windows'),
('IT2050',  'Mang may tinh',                         3, 45, 0,  'TCP/IP networking'),
('IT2060',  'Co so du lieu',                         3, 30, 30, 'SQL & thiet ke CSDL'),
('IT2070',  'Kien truc may tinh',                    2, 30, 0,  'Kien truc von Neumann'),
-- HK3-4 - KT2.1 tiep
('IT3010',  'Phan tich thiet ke he thong',           3, 45, 0,  'SA/SD & UML'),
('IT3020',  'Cong nghe phan mem',                    3, 45, 0,  'SDLC, Agile, Scrum'),
('IT3030',  'Lap trinh Web',                         3, 30, 30, 'HTML/CSS/JS/Spring'),
('IT3040',  'He quan tri CSDL',                      3, 30, 30, 'MySQL, Oracle'),
('IT3050',  'An toan thong tin',                     3, 45, 0,  'Bao mat he thong'),
('IT3060',  'Tri tue nhan tao',                      3, 45, 0,  'AI co ban'),
-- HK5-6 - KT3.1 Chuyen nganh
('IT4010',  'Phat trien ung dung Web nang cao',      3, 30, 30, 'ReactJS, Spring Boot'),
('IT4020',  'Dieu phoi dich vu dam may',             3, 30, 30, 'Docker, Kubernetes'),
('IT4030',  'Hoc may',                               3, 45, 0,  'Machine Learning'),
('IT4040',  'Xu ly ngon ngu tu nhien',               3, 30, 30, 'NLP'),
('IT4050',  'Lap trinh di dong',                     3, 30, 30, 'Android/iOS'),
('IT4060',  'Kiem thu phan mem',                     3, 30, 30, 'Testing & QA'),
('IT4070',  'Thi giac may tinh',                     3, 30, 30, 'Computer Vision'),
('IT4080',  'Big Data',                              3, 30, 30, 'Hadoop, Spark'),
-- HK7 - KT3.1 + tu chon
('IT4090',  'Do an mon hoc',                         3, 0,  90, 'Do an nhom'),
('IT4100',  'Thuc tap doanh nghiep',                 3, 0,  90, 'Thuc tap ngoai truong'),
('IT4110',  'Chuyen de cong nghe moi',               2, 30, 0,  'Chuyen de tu chon'),
('IT4120',  'Phat trien game',                       3, 30, 30, 'Game dev Unity'),
-- HK8 - KT4.1 Tot nghiep
('IT5010',  'Khoa luan tot nghiep',                  7, 0,  0,  'KLTN cuoi khoa'),
('IT5020',  'Do an tot nghiep',                      5, 0,  0,  'Do an thay the KLTN');

-- =============================================================
-- 6. MON TIEN QUYET
-- =============================================================
INSERT INTO mon_tien_quyet (mon_hoc_id, mon_tien_quyet_id) VALUES
-- CTDL can lap trinh C
((SELECT id FROM mon_hoc WHERE ma_mon='IT2020'), (SELECT id FROM mon_hoc WHERE ma_mon='IT1110')),
-- OOP can lap trinh C
((SELECT id FROM mon_hoc WHERE ma_mon='IT2030'), (SELECT id FROM mon_hoc WHERE ma_mon='IT1110')),
-- He QTCSDL can Co so DL
((SELECT id FROM mon_hoc WHERE ma_mon='IT3040'), (SELECT id FROM mon_hoc WHERE ma_mon='IT2060')),
-- Lap trinh Web can OOP
((SELECT id FROM mon_hoc WHERE ma_mon='IT3030'), (SELECT id FROM mon_hoc WHERE ma_mon='IT2030')),
-- Web nang cao can Lap trinh Web
((SELECT id FROM mon_hoc WHERE ma_mon='IT4010'), (SELECT id FROM mon_hoc WHERE ma_mon='IT3030')),
-- ML can Xac suat thong ke
((SELECT id FROM mon_hoc WHERE ma_mon='IT4030'), (SELECT id FROM mon_hoc WHERE ma_mon='MI1216')),
-- PTTK can CTDL
((SELECT id FROM mon_hoc WHERE ma_mon='IT3010'), (SELECT id FROM mon_hoc WHERE ma_mon='IT2020')),
-- CNPM can PTTK
((SELECT id FROM mon_hoc WHERE ma_mon='IT3020'), (SELECT id FROM mon_hoc WHERE ma_mon='IT3010'));

-- =============================================================
-- 7. HOC KY (8 hoc ky tuong ung 4 nam hoc)
-- =============================================================
INSERT INTO hoc_ky (ten_hoc_ky, nam_hoc, hoc_ky_thu, ngay_bat_dau_dk, ngay_ket_thuc_dk, ngay_bat_dau_hoc, ngay_ket_thuc_hoc, tin_chi_toi_thieu, tin_chi_toi_da) VALUES
('Hoc ky 1 - 2022-2023', '2022-2023', 1, '2022-08-01','2022-08-31','2022-09-05','2023-01-15', 10, 25),
('Hoc ky 2 - 2022-2023', '2022-2023', 2, '2023-01-10','2023-02-10','2023-02-13','2023-06-30', 10, 25),
('Hoc ky 1 - 2023-2024', '2023-2024', 1, '2023-08-01','2023-08-31','2023-09-04','2024-01-15', 10, 25),
('Hoc ky 2 - 2023-2024', '2023-2024', 2, '2024-01-10','2024-02-10','2024-02-12','2024-06-30', 10, 25),
('Hoc ky 1 - 2024-2025', '2024-2025', 1, '2024-08-01','2024-08-31','2024-09-02','2025-01-15', 10, 25),
('Hoc ky 2 - 2024-2025', '2024-2025', 2, '2025-01-10','2025-02-10','2025-02-10','2025-06-30', 10, 25),
('Hoc ky 1 - 2025-2026', '2025-2026', 1, '2025-08-01','2025-08-31','2025-09-01','2026-01-15', 10, 25),
('Hoc ky 1 - 2026-2027', '2026-2027', 1, '2026-09-01','2026-09-30','2026-10-01','2027-01-15', 10, 25);

-- =============================================================
-- 8. CHUONG TRINH DAO TAO - NGANH CNTT (id=1) - DAY DU 8 HK
-- cot: nganh_id, mon_hoc_id, hoc_ky_thu, bat_buoc,
--      khoi_id, so_tiet_phi, hoc_ky_du_kien_label, lt_bt, so_tiet_th, btl_damh, tt_da_kltn
-- khoi_kien_thuc ids: GDTC=1, QPAN=2, KT1.1=3, KT2.1=4, KT3.1=5, KT4.1=6
-- =============================================================

-- ---- HK 1 (nam 1 ky 1) ----
INSERT INTO chuong_trinh_dao_tao (nganh_id, mon_hoc_id, hoc_ky_thu, bat_buoc, khoi_id, so_tiet_phi, hoc_ky_du_kien_label, lt_bt, so_tiet_th, btl_damh, tt_da_kltn) VALUES
(1, (SELECT id FROM mon_hoc WHERE ma_mon='PE1213'),  1, TRUE, 1, 3,  '2022_2023_1(1)', 0,  90, 0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='MI1216'),  1, TRUE, 3, 2,  '2022_2023_1(1)', 30, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='MI1209'),  1, TRUE, 3, 3,  '2022_2023_1(1)', 45, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='SSH1207'), 1, TRUE, 3, 3,  '2022_2023_1(1)', 45, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='FL1219'),  1, TRUE, 3, 3,  '2022_2023_1(1)', 30, 30, 0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='MI1208'),  1, TRUE, 3, 3,  '2022_2023_1(1)', 30, 30, 0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT1110'),  1, TRUE, 4, 3,  '2022_2023_1(1)', 30, 30, 0, 0);

-- ---- HK 2 (nam 1 ky 2) ----
INSERT INTO chuong_trinh_dao_tao (nganh_id, mon_hoc_id, hoc_ky_thu, bat_buoc, khoi_id, so_tiet_phi, hoc_ky_du_kien_label, lt_bt, so_tiet_th, btl_damh, tt_da_kltn) VALUES
(1, (SELECT id FROM mon_hoc WHERE ma_mon='FL1220'),  2, TRUE, 3, 3,  '2022_2023_2(2)', 30, 30, 0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='SSH1201'), 2, TRUE, 3, 3,  '2022_2023_2(2)', 45, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='SSH1206'), 2, TRUE, 3, 2,  '2022_2023_2(2)', 30, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='MI1212'),  2, TRUE, 3, 2,  '2022_2023_2(2)', 30, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='MI1213'),  2, TRUE, 3, 2,  '2022_2023_2(2)', 30, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT1120'),  2, TRUE, 4, 3,  '2022_2023_2(2)', 30, 30, 0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT2070'),  2, TRUE, 4, 2,  '2022_2023_2(2)', 30, 0,  0, 0);

-- ---- HK 3 (nam 2 ky 1) ----
INSERT INTO chuong_trinh_dao_tao (nganh_id, mon_hoc_id, hoc_ky_thu, bat_buoc, khoi_id, so_tiet_phi, hoc_ky_du_kien_label, lt_bt, so_tiet_th, btl_damh, tt_da_kltn) VALUES
(1, (SELECT id FROM mon_hoc WHERE ma_mon='SSH1204'), 3, TRUE, 3, 2,  '2023_2024_1(1)', 30, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='SSH1205'), 3, TRUE, 3, 2,  '2023_2024_1(1)', 30, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT2020'),  3, TRUE, 4, 3,  '2023_2024_1(1)', 30, 30, 0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT2030'),  3, TRUE, 4, 3,  '2023_2024_1(1)', 30, 30, 0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT2040'),  3, TRUE, 4, 3,  '2023_2024_1(1)', 45, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT2060'),  3, TRUE, 4, 3,  '2023_2024_1(1)', 30, 30, 0, 0);

-- ---- HK 4 (nam 2 ky 2) ----
INSERT INTO chuong_trinh_dao_tao (nganh_id, mon_hoc_id, hoc_ky_thu, bat_buoc, khoi_id, so_tiet_phi, hoc_ky_du_kien_label, lt_bt, so_tiet_th, btl_damh, tt_da_kltn) VALUES
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT2050'),  4, TRUE, 4, 3,  '2023_2024_2(2)', 45, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT3010'),  4, TRUE, 4, 3,  '2023_2024_2(2)', 45, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT3020'),  4, TRUE, 4, 3,  '2023_2024_2(2)', 45, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT3030'),  4, TRUE, 4, 3,  '2023_2024_2(2)', 30, 30, 0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT3040'),  4, TRUE, 4, 3,  '2023_2024_2(2)', 30, 30, 0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='QP1101'),  4, TRUE, 2, 8,  '2023_2024_2(2)', 90, 30, 0, 0);

-- ---- HK 5 (nam 3 ky 1) ----
INSERT INTO chuong_trinh_dao_tao (nganh_id, mon_hoc_id, hoc_ky_thu, bat_buoc, khoi_id, so_tiet_phi, hoc_ky_du_kien_label, lt_bt, so_tiet_th, btl_damh, tt_da_kltn) VALUES
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT3050'),  5, TRUE,  5, 3,  '2024_2025_1(1)', 45, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT3060'),  5, TRUE,  5, 3,  '2024_2025_1(1)', 45, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT4010'),  5, TRUE,  5, 3,  '2024_2025_1(1)', 30, 30, 0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT4030'),  5, TRUE,  5, 3,  '2024_2025_1(1)', 45, 0,  0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT4050'),  5, FALSE, 5, 3,  '2024_2025_1(1)', 30, 30, 0, 0);

-- ---- HK 6 (nam 3 ky 2) ----
INSERT INTO chuong_trinh_dao_tao (nganh_id, mon_hoc_id, hoc_ky_thu, bat_buoc, khoi_id, so_tiet_phi, hoc_ky_du_kien_label, lt_bt, so_tiet_th, btl_damh, tt_da_kltn) VALUES
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT4020'),  6, TRUE,  5, 3,  '2024_2025_2(2)', 30, 30, 0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT4040'),  6, FALSE, 5, 3,  '2024_2025_2(2)', 30, 30, 0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT4060'),  6, TRUE,  5, 3,  '2024_2025_2(2)', 30, 30, 0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT4070'),  6, FALSE, 5, 3,  '2024_2025_2(2)', 30, 30, 0, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT4080'),  6, FALSE, 5, 3,  '2024_2025_2(2)', 30, 30, 0, 0);

-- ---- HK 7 (nam 4 ky 1) ----
INSERT INTO chuong_trinh_dao_tao (nganh_id, mon_hoc_id, hoc_ky_thu, bat_buoc, khoi_id, so_tiet_phi, hoc_ky_du_kien_label, lt_bt, so_tiet_th, btl_damh, tt_da_kltn) VALUES
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT4090'),  7, TRUE,  5, 3,  '2025_2026_1(1)', 0, 0, 90, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT4100'),  7, TRUE,  5, 3,  '2025_2026_1(1)', 0, 0, 90, 0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT4110'),  7, FALSE, 5, 2,  '2025_2026_1(1)', 30, 0, 0,  0),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT4120'),  7, FALSE, 5, 3,  '2025_2026_1(1)', 30, 30, 0, 0);

-- ---- HK 8 (nam 4 ky 2) - Tot nghiep ----
INSERT INTO chuong_trinh_dao_tao (nganh_id, mon_hoc_id, hoc_ky_thu, bat_buoc, khoi_id, so_tiet_phi, hoc_ky_du_kien_label, lt_bt, so_tiet_th, btl_damh, tt_da_kltn) VALUES
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT5010'),  8, TRUE,  6, 7,  '2025_2026_2(2)', 0, 0, 0, 210),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT5020'),  8, FALSE, 6, 5,  '2025_2026_2(2)', 0, 0, 0, 150);

-- =============================================================
-- 9. LOP HOC PHAN (hoc ky 8 - dang mo dang ky HK1 2026-2027)
-- =============================================================
INSERT INTO lop_hoc_phan (ma_lop_hp, mon_hoc_id, hoc_ky_id, giang_vien_id, si_so_toi_da, si_so_hien_tai, trang_thai, version) VALUES
('IT3030-01', (SELECT id FROM mon_hoc WHERE ma_mon='IT3030'), 8, 1, 50, 0, 'MO', 0),
('IT3030-02', (SELECT id FROM mon_hoc WHERE ma_mon='IT3030'), 8, 2, 45, 0, 'MO', 0),
('IT4030-01', (SELECT id FROM mon_hoc WHERE ma_mon='IT4030'), 8, 3, 50, 0, 'MO', 0),
('IT3050-01', (SELECT id FROM mon_hoc WHERE ma_mon='IT3050'), 8, 5, 50, 0, 'MO', 0),
('IT4010-01', (SELECT id FROM mon_hoc WHERE ma_mon='IT4010'), 8, 1, 40, 0, 'MO', 0),
('IT3060-01', (SELECT id FROM mon_hoc WHERE ma_mon='IT3060'), 8, 4, 50, 0, 'MO', 0),
('MI1216-01', (SELECT id FROM mon_hoc WHERE ma_mon='MI1216'), 8, 3, 60, 0, 'MO', 0),
('IT2020-01', (SELECT id FROM mon_hoc WHERE ma_mon='IT2020'), 8, 2, 50, 0, 'MO', 0);

-- =============================================================
-- 10. LICH HOC
-- =============================================================
INSERT INTO lich_hoc (lop_hoc_phan_id, thu, tiet_bat_dau, tiet_ket_thuc, phong) VALUES
(1, 2, 1,  3,  'A101'), (1, 4, 6,  8,  'Lab1'),
(2, 3, 1,  3,  'A102'), (2, 5, 6,  8,  'Lab2'),
(3, 2, 6,  8,  'B201'),
(4, 3, 6,  8,  'A201'),
(5, 4, 1,  3,  'B301'), (5, 6, 6,  8,  'Lab3'),
(6, 5, 1,  3,  'C101'),
(7, 2, 1,  3,  'A301'),
(8, 3, 1,  3,  'B201'), (8, 5, 1,  3,  'Lab4');

-- =============================================================
-- 11. LICH THI (mau cho HK hien tai)
-- =============================================================
INSERT INTO lich_thi (lop_hoc_phan_id, loai_thi, ngay_thi, gio_bat_dau, gio_ket_thuc, phong_thi, hinh_thuc, ghi_chu) VALUES
(1, 'GIUA_KY', '2026-11-10', '07:30:00', '09:00:00', 'A101', 'TU_LUAN',    'Thi giua ky Lap trinh Web'),
(1, 'CUOI_KY', '2027-01-05', '07:30:00', '09:30:00', 'A201', 'TRAC_NGHIEM','Thi cuoi ky Lap trinh Web'),
(2, 'CUOI_KY', '2027-01-06', '07:30:00', '09:30:00', 'A202', 'TRAC_NGHIEM','Thi cuoi ky LP Web lop 2'),
(3, 'CUOI_KY', '2027-01-08', '09:30:00', '11:00:00', 'B101', 'TU_LUAN',    'Thi Machine Learning'),
(4, 'CUOI_KY', '2027-01-10', '07:30:00', '09:30:00', 'A301', 'TU_LUAN',    'Thi An toan TT'),
(7, 'CUOI_KY', '2027-01-03', '07:30:00', '09:00:00', 'A101', 'TRAC_NGHIEM','Thi Xac suat thong ke');

-- =============================================================
-- 12. KE HOACH NGUYEN VONG
-- =============================================================
INSERT INTO ke_hoach_nguyen_vong (ten_ke_hoach, mo_ta, hoc_ky_id, ngay_bat_dau, ngay_ket_thuc, trang_thai) VALUES
('Dang ky nguyen vong HK1 2026-2027', 'Dang ky mon hoc du kien HK1 2026-2027', 8, '2026-09-01', '2026-09-30', 'DANG_MO');

INSERT INTO nguyen_vong_mon_hoc (ke_hoach_nguyen_vong_id, mon_hoc_id) VALUES
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT3030')),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT4030')),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT3050')),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT4010')),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT3060')),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='MI1216')),
(1, (SELECT id FROM mon_hoc WHERE ma_mon='IT2020'));

-- =============================================================
-- 13. DINH HUONG
-- =============================================================
INSERT INTO dinh_huong (ten_dinh_huong, mo_ta, nganh_id, ngay_bat_dau, ngay_ket_thuc, che_do_dang_ky, trang_thai) VALUES
('Cong nghe phan mem',  'Phat trien phan mem, lap trinh ung dung', 1, '2026-09-01', '2026-09-30', 'BAT_BUOC', 'DANG_MO'),
('He thong thong tin',  'Phan tich thiet ke he thong',             1, '2026-09-01', '2026-09-30', 'BAT_BUOC', 'DANG_MO'),
('Ky thuat phan mem',   'Kiem thu va dam bao chat luong PM',       2, '2026-09-01', '2026-09-30', 'TU_CHON',  'DANG_MO'),
('Phan tich du lieu',   'Xu ly va phan tich du lieu lon',          2, '2026-09-01', '2026-09-30', 'TU_CHON',  'DANG_MO');

-- =============================================================
-- 14. DANG KY HOC PHAN mau (SV001 da hoan thanh 1 so mon)
-- =============================================================
INSERT INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, trang_thai, diem_giua_ky, diem_cuoi_ky, diem_tong_ket) VALUES
(1, 1, 'DA_DANG_KY', NULL, NULL, NULL),
(1, 3, 'DA_DANG_KY', NULL, NULL, NULL),
(2, 2, 'DA_DANG_KY', NULL, NULL, NULL),
(2, 4, 'DA_DANG_KY', NULL, NULL, NULL);
