-- =============================================================
-- CHI INSERT - Chay sau khi da DELETE sach du lieu cu
-- phpMyAdmin: Import tab -> chon file nay -> Go
-- =============================================================

USE quan_ly_hoc_phan;

-- 1. NGANH
INSERT INTO nganh (ma_nganh, ten_nganh) VALUES
('CNTT', 'Cong nghe Thong tin'),
('KTPM', 'Ky thuat Phan mem'),
('HTTT', 'He thong Thong tin'),
('KHMT', 'Khoa hoc May tinh'),
('MMT',  'Mang may tinh va Truyen thong');

-- 2. KHOI KIEN THUC
INSERT INTO khoi_kien_thuc (ma_khoi, ten_khoi, nganh_id) VALUES
('GDTC',  'Giao duc the chat',                   NULL),
('QPAN',  'Giao duc Quoc phong va An ninh',       NULL),
('KT1.1', 'Khoi kien thuc giao duc dai cuong',    (SELECT id FROM nganh WHERE ma_nganh='CNTT')),
('KT2.1', 'Khoi kien thuc co so nganh',           (SELECT id FROM nganh WHERE ma_nganh='CNTT')),
('KT3.1', 'Khoi kien thuc chuyen nganh',          (SELECT id FROM nganh WHERE ma_nganh='CNTT')),
('KT4.1', 'Khoi tot nghiep',                      (SELECT id FROM nganh WHERE ma_nganh='CNTT')),
('KT1.2', 'Khoi kien thuc giao duc dai cuong',    (SELECT id FROM nganh WHERE ma_nganh='KTPM')),
('KT2.2', 'Khoi kien thuc co so nganh',           (SELECT id FROM nganh WHERE ma_nganh='KTPM')),
('KT3.2', 'Khoi kien thuc chuyen nganh',          (SELECT id FROM nganh WHERE ma_nganh='KTPM')),
('KT4.2', 'Khoi tot nghiep',                      (SELECT id FROM nganh WHERE ma_nganh='KTPM'));

-- 3. GIANG VIEN
INSERT INTO giang_vien (ma_gv, ho_ten, email, mat_khau, khoa_bo_mon) VALUES
('GV001','Nguyen Van An',  'gv001@email.com','$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny','Khoa CNTT'),
('GV002','Tran Thi Bich',  'gv002@email.com','$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny','Khoa CNTT'),
('GV003','Le Van Cuong',   'gv003@email.com','$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny','Khoa Toan - Tin'),
('GV004','Pham Thi Dung',  'gv004@email.com','$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny','Khoa Dien tu'),
('GV005','Hoang Van Em',   'gv005@email.com','$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny','Khoa CNTT'),
('GV006','Vu Thi Phuong',  'gv006@email.com','$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny','Khoa CNTT'),
('GV007','Ngo Van Quang',  'gv007@email.com','$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny','Khoa Ly luan chinh tri');

-- 4. SINH VIEN
INSERT INTO sinh_vien (mssv, ho_ten, email, mat_khau, ngay_sinh, lop_sinh_hoat, khoa_hoc, nganh_id, trang_thai) VALUES
('SV001','Nguyen Thi Anh','sv001@email.com','$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny','2004-03-15','CNTT-K22A','K22',(SELECT id FROM nganh WHERE ma_nganh='CNTT'),'DANG_HOC'),
('SV002','Tran Van Binh', 'sv002@email.com','$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny','2004-07-20','CNTT-K22A','K22',(SELECT id FROM nganh WHERE ma_nganh='CNTT'),'DANG_HOC'),
('SV003','Le Thi Cam',    'sv003@email.com','$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny','2003-11-05','KTPM-K22B','K22',(SELECT id FROM nganh WHERE ma_nganh='KTPM'),'DANG_HOC'),
('SV004','Pham Van Duc',  'sv004@email.com','$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny','2004-01-30','HTTT-K22C','K22',(SELECT id FROM nganh WHERE ma_nganh='HTTT'),'DANG_HOC'),
('SV005','Hoang Thi Eo',  'sv005@email.com','$2a$10$bT28j9LV6ccKA5D3razdEOTbAWbKbE1TJDoxaep4m2uL0rBYktiny','2003-09-12','CNTT-K21A','K21',(SELECT id FROM nganh WHERE ma_nganh='CNTT'),'DANG_HOC');

-- 5. MON HOC
INSERT INTO mon_hoc (ma_mon, ten_mon, so_tin_chi, so_tiet_ly_thuyet, so_tiet_thuc_hanh, mo_ta) VALUES
('PE1213', 'Giao duc the chat',               3, 0,  90, 'The duc the thao'),
('QP1101', 'Giao duc quoc phong - an ninh',   8, 90, 30, 'QPAN toan khoa'),
('SSH1201','Triet hoc Mac - Lenin',            3, 45, 0,  'Triet hoc Mac Lenin'),
('SSH1204','Lich su Dang Cong san Viet Nam',   2, 30, 0,  'Lich su Dang'),
('SSH1205','Tu tuong Ho Chi Minh',             2, 30, 0,  'Tu tuong HCM'),
('SSH1206','Phap luat dai cuong',              2, 30, 0,  'Phap luat'),
('SSH1207','Ky nang mem',                      3, 45, 0,  'Ky nang giao tiep'),
('FL1219', 'Tieng Anh 1',                      3, 30, 30, 'Tieng Anh co ban 1'),
('FL1220', 'Tieng Anh 2',                      3, 30, 30, 'Tieng Anh co ban 2'),
('MI1208', 'Tin hoc dai cuong',                3, 30, 30, 'Tin hoc van phong'),
('MI1212', 'Dai so',                           2, 30, 0,  'Dai so tuyen tinh'),
('MI1213', 'Toan roi rac',                     2, 30, 0,  'Toan roi rac CNTT'),
('MI1216', 'Xac suat thong ke',                2, 30, 0,  'Xac suat va thong ke'),
('MI1209', 'Giai tich',                        3, 45, 0,  'Giai tich toan'),
('IT1110', 'Lap trinh C',                      3, 30, 30, 'Lap trinh ngon ngu C'),
('IT1120', 'Nhap mon lap trinh',               3, 30, 30, 'Nhap mon Python/Java'),
('IT2020', 'Cau truc du lieu va giai thuat',   3, 30, 30, 'CTDL va GT'),
('IT2030', 'Lap trinh huong doi tuong',        3, 30, 30, 'OOP Java'),
('IT2040', 'He dieu hanh',                     3, 45, 0,  'OS Linux/Windows'),
('IT2050', 'Mang may tinh',                    3, 45, 0,  'TCP/IP networking'),
('IT2060', 'Co so du lieu',                    3, 30, 30, 'SQL & thiet ke CSDL'),
('IT2070', 'Kien truc may tinh',               2, 30, 0,  'Kien truc von Neumann'),
('IT3010', 'Phan tich thiet ke he thong',      3, 45, 0,  'SA/SD & UML'),
('IT3020', 'Cong nghe phan mem',               3, 45, 0,  'SDLC, Agile, Scrum'),
('IT3030', 'Lap trinh Web',                    3, 30, 30, 'HTML/CSS/JS/Spring'),
('IT3040', 'He quan tri CSDL',                 3, 30, 30, 'MySQL, Oracle'),
('IT3050', 'An toan thong tin',                3, 45, 0,  'Bao mat he thong'),
('IT3060', 'Tri tue nhan tao',                 3, 45, 0,  'AI co ban'),
('IT4010', 'Phat trien ung dung Web nang cao', 3, 30, 30, 'ReactJS, Spring Boot'),
('IT4020', 'Dieu phoi dich vu dam may',        3, 30, 30, 'Docker, Kubernetes'),
('IT4030', 'Hoc may',                          3, 45, 0,  'Machine Learning'),
('IT4040', 'Xu ly ngon ngu tu nhien',          3, 30, 30, 'NLP'),
('IT4050', 'Lap trinh di dong',                3, 30, 30, 'Android/iOS'),
('IT4060', 'Kiem thu phan mem',                3, 30, 30, 'Testing & QA'),
('IT4070', 'Thi giac may tinh',                3, 30, 30, 'Computer Vision'),
('IT4080', 'Big Data',                         3, 30, 30, 'Hadoop, Spark'),
('IT4090', 'Do an mon hoc',                    3, 0,  90, 'Do an nhom'),
('IT4100', 'Thuc tap doanh nghiep',            3, 0,  90, 'Thuc tap ngoai truong'),
('IT4110', 'Chuyen de cong nghe moi',          2, 30, 0,  'Chuyen de tu chon'),
('IT4120', 'Phat trien game',                  3, 30, 30, 'Game dev Unity'),
('IT5010', 'Khoa luan tot nghiep',             7, 0,  0,  'KLTN cuoi khoa'),
('IT5020', 'Do an tot nghiep',                 5, 0,  0,  'Do an thay the KLTN');

-- 6. MON TIEN QUYET
INSERT INTO mon_tien_quyet (mon_hoc_id, mon_tien_quyet_id) VALUES
((SELECT id FROM mon_hoc WHERE ma_mon='IT2020'),(SELECT id FROM mon_hoc WHERE ma_mon='IT1110')),
((SELECT id FROM mon_hoc WHERE ma_mon='IT2030'),(SELECT id FROM mon_hoc WHERE ma_mon='IT1110')),
((SELECT id FROM mon_hoc WHERE ma_mon='IT3040'),(SELECT id FROM mon_hoc WHERE ma_mon='IT2060')),
((SELECT id FROM mon_hoc WHERE ma_mon='IT3030'),(SELECT id FROM mon_hoc WHERE ma_mon='IT2030')),
((SELECT id FROM mon_hoc WHERE ma_mon='IT4010'),(SELECT id FROM mon_hoc WHERE ma_mon='IT3030')),
((SELECT id FROM mon_hoc WHERE ma_mon='IT4030'),(SELECT id FROM mon_hoc WHERE ma_mon='MI1216')),
((SELECT id FROM mon_hoc WHERE ma_mon='IT3010'),(SELECT id FROM mon_hoc WHERE ma_mon='IT2020')),
((SELECT id FROM mon_hoc WHERE ma_mon='IT3020'),(SELECT id FROM mon_hoc WHERE ma_mon='IT3010'));

-- 7. HOC KY
INSERT INTO hoc_ky (ten_hoc_ky, nam_hoc, hoc_ky_thu, ngay_bat_dau_dk, ngay_ket_thuc_dk, ngay_bat_dau_hoc, ngay_ket_thuc_hoc, tin_chi_toi_thieu, tin_chi_toi_da) VALUES
('Hoc ky 1 - 2022-2023','2022-2023',1,'2022-08-01','2022-08-31','2022-09-05','2023-01-15',10,25),
('Hoc ky 2 - 2022-2023','2022-2023',2,'2023-01-10','2023-02-10','2023-02-13','2023-06-30',10,25),
('Hoc ky 1 - 2023-2024','2023-2024',1,'2023-08-01','2023-08-31','2023-09-04','2024-01-15',10,25),
('Hoc ky 2 - 2023-2024','2023-2024',2,'2024-01-10','2024-02-10','2024-02-12','2024-06-30',10,25),
('Hoc ky 1 - 2024-2025','2024-2025',1,'2024-08-01','2024-08-31','2024-09-02','2025-01-15',10,25),
('Hoc ky 2 - 2024-2025','2024-2025',2,'2025-01-10','2025-02-10','2025-02-10','2025-06-30',10,25),
('Hoc ky 1 - 2025-2026','2025-2026',1,'2025-08-01','2025-08-31','2025-09-01','2026-01-15',10,25),
('Hoc ky 1 - 2026-2027','2026-2027',1,'2026-09-01','2026-09-30','2026-10-01','2027-01-15',10,25);

-- 8. CHUONG TRINH DAO TAO CNTT - HK1
INSERT INTO chuong_trinh_dao_tao (nganh_id,mon_hoc_id,hoc_ky_thu,bat_buoc,khoi_id,so_tiet_phi,hoc_ky_du_kien_label,lt_bt,so_tiet_th,btl_damh,tt_da_kltn) VALUES
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='PE1213'), 1,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='GDTC'  AND nganh_id IS NULL),3, '2022_2023_1(1)',0, 90,0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='MI1216'), 1,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT1.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),2, '2022_2023_1(1)',30,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='MI1209'), 1,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT1.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2022_2023_1(1)',45,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='SSH1207'),1,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT1.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2022_2023_1(1)',45,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='FL1219'), 1,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT1.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2022_2023_1(1)',30,30,0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='MI1208'), 1,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT1.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2022_2023_1(1)',30,30,0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT1110'), 1,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT2.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2022_2023_1(1)',30,30,0,0);

-- CTDT - HK2
INSERT INTO chuong_trinh_dao_tao (nganh_id,mon_hoc_id,hoc_ky_thu,bat_buoc,khoi_id,so_tiet_phi,hoc_ky_du_kien_label,lt_bt,so_tiet_th,btl_damh,tt_da_kltn) VALUES
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='FL1220'), 2,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT1.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2022_2023_2(2)',30,30,0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='SSH1201'),2,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT1.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2022_2023_2(2)',45,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='SSH1206'),2,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT1.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),2, '2022_2023_2(2)',30,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='MI1212'), 2,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT1.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),2, '2022_2023_2(2)',30,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='MI1213'), 2,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT1.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),2, '2022_2023_2(2)',30,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT1120'), 2,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT2.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2022_2023_2(2)',30,30,0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT2070'), 2,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT2.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),2, '2022_2023_2(2)',30,0, 0,0);

-- CTDT - HK3
INSERT INTO chuong_trinh_dao_tao (nganh_id,mon_hoc_id,hoc_ky_thu,bat_buoc,khoi_id,so_tiet_phi,hoc_ky_du_kien_label,lt_bt,so_tiet_th,btl_damh,tt_da_kltn) VALUES
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='SSH1204'),3,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT1.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),2, '2023_2024_1(1)',30,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='SSH1205'),3,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT1.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),2, '2023_2024_1(1)',30,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT2020'), 3,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT2.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2023_2024_1(1)',30,30,0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT2030'), 3,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT2.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2023_2024_1(1)',30,30,0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT2040'), 3,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT2.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2023_2024_1(1)',45,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT2060'), 3,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT2.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2023_2024_1(1)',30,30,0,0);

-- CTDT - HK4
INSERT INTO chuong_trinh_dao_tao (nganh_id,mon_hoc_id,hoc_ky_thu,bat_buoc,khoi_id,so_tiet_phi,hoc_ky_du_kien_label,lt_bt,so_tiet_th,btl_damh,tt_da_kltn) VALUES
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT2050'), 4,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT2.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2023_2024_2(2)',45,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT3010'), 4,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT2.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2023_2024_2(2)',45,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT3020'), 4,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT2.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2023_2024_2(2)',45,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT3030'), 4,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT2.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2023_2024_2(2)',30,30,0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT3040'), 4,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT2.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2023_2024_2(2)',30,30,0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='QP1101'), 4,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='QPAN'  AND nganh_id IS NULL),8, '2023_2024_2(2)',90,30,0,0);

-- CTDT - HK5
INSERT INTO chuong_trinh_dao_tao (nganh_id,mon_hoc_id,hoc_ky_thu,bat_buoc,khoi_id,so_tiet_phi,hoc_ky_du_kien_label,lt_bt,so_tiet_th,btl_damh,tt_da_kltn) VALUES
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT3050'), 5,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT3.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2024_2025_1(1)',45,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT3060'), 5,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT3.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2024_2025_1(1)',45,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT4010'), 5,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT3.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2024_2025_1(1)',30,30,0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT4030'), 5,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT3.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2024_2025_1(1)',45,0, 0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT4050'), 5,0,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT3.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2024_2025_1(1)',30,30,0,0);

-- CTDT - HK6
INSERT INTO chuong_trinh_dao_tao (nganh_id,mon_hoc_id,hoc_ky_thu,bat_buoc,khoi_id,so_tiet_phi,hoc_ky_du_kien_label,lt_bt,so_tiet_th,btl_damh,tt_da_kltn) VALUES
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT4020'), 6,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT3.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2024_2025_2(2)',30,30,0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT4040'), 6,0,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT3.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2024_2025_2(2)',30,30,0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT4060'), 6,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT3.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2024_2025_2(2)',30,30,0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT4070'), 6,0,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT3.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2024_2025_2(2)',30,30,0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT4080'), 6,0,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT3.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2024_2025_2(2)',30,30,0,0);

-- CTDT - HK7
INSERT INTO chuong_trinh_dao_tao (nganh_id,mon_hoc_id,hoc_ky_thu,bat_buoc,khoi_id,so_tiet_phi,hoc_ky_du_kien_label,lt_bt,so_tiet_th,btl_damh,tt_da_kltn) VALUES
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT4090'), 7,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT3.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2025_2026_1(1)',0,0,90,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT4100'), 7,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT3.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2025_2026_1(1)',0,0,90,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT4110'), 7,0,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT3.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),2, '2025_2026_1(1)',30,0,0,0),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT4120'), 7,0,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT3.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),3, '2025_2026_1(1)',30,30,0,0);

-- CTDT - HK8
INSERT INTO chuong_trinh_dao_tao (nganh_id,mon_hoc_id,hoc_ky_thu,bat_buoc,khoi_id,so_tiet_phi,hoc_ky_du_kien_label,lt_bt,so_tiet_th,btl_damh,tt_da_kltn) VALUES
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT5010'), 8,1,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT4.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),7, '2025_2026_2(2)',0,0,0,210),
((SELECT id FROM nganh WHERE ma_nganh='CNTT'),(SELECT id FROM mon_hoc WHERE ma_mon='IT5020'), 8,0,(SELECT id FROM khoi_kien_thuc WHERE ma_khoi='KT4.1' AND nganh_id=(SELECT id FROM nganh WHERE ma_nganh='CNTT')),5, '2025_2026_2(2)',0,0,0,150);

-- 9. LOP HOC PHAN
INSERT INTO lop_hoc_phan (ma_lop_hp,mon_hoc_id,hoc_ky_id,giang_vien_id,si_so_toi_da,si_so_hien_tai,trang_thai,version) VALUES
('IT3030-01',(SELECT id FROM mon_hoc WHERE ma_mon='IT3030'),(SELECT id FROM hoc_ky WHERE nam_hoc='2026-2027' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV001'),50,0,'MO',0),
('IT3030-02',(SELECT id FROM mon_hoc WHERE ma_mon='IT3030'),(SELECT id FROM hoc_ky WHERE nam_hoc='2026-2027' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV002'),45,0,'MO',0),
('IT4030-01',(SELECT id FROM mon_hoc WHERE ma_mon='IT4030'),(SELECT id FROM hoc_ky WHERE nam_hoc='2026-2027' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV003'),50,0,'MO',0),
('IT3050-01',(SELECT id FROM mon_hoc WHERE ma_mon='IT3050'),(SELECT id FROM hoc_ky WHERE nam_hoc='2026-2027' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV005'),50,0,'MO',0),
('IT4010-01',(SELECT id FROM mon_hoc WHERE ma_mon='IT4010'),(SELECT id FROM hoc_ky WHERE nam_hoc='2026-2027' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV001'),40,0,'MO',0),
('IT3060-01',(SELECT id FROM mon_hoc WHERE ma_mon='IT3060'),(SELECT id FROM hoc_ky WHERE nam_hoc='2026-2027' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV004'),50,0,'MO',0),
('MI1216-01',(SELECT id FROM mon_hoc WHERE ma_mon='MI1216'),(SELECT id FROM hoc_ky WHERE nam_hoc='2026-2027' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV003'),60,0,'MO',0),
('IT2020-01',(SELECT id FROM mon_hoc WHERE ma_mon='IT2020'),(SELECT id FROM hoc_ky WHERE nam_hoc='2026-2027' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV002'),50,0,'MO',0);

-- 10. LICH HOC
INSERT INTO lich_hoc (lop_hoc_phan_id,thu,tiet_bat_dau,tiet_ket_thuc,phong) VALUES
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3030-01'),2,1,3,'A101'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3030-01'),4,6,8,'Lab1'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3030-02'),3,1,3,'A102'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3030-02'),5,6,8,'Lab2'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT4030-01'),2,6,8,'B201'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3050-01'),3,6,8,'A201'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT4010-01'),4,1,3,'B301'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT4010-01'),6,6,8,'Lab3'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3060-01'),5,1,3,'C101'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1216-01'),2,1,3,'A301'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2020-01'),3,1,3,'B201'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2020-01'),5,1,3,'Lab4');

-- 11. LICH THI
INSERT INTO lich_thi (lop_hoc_phan_id,loai_thi,ngay_thi,gio_bat_dau,gio_ket_thuc,phong_thi,hinh_thuc,ghi_chu) VALUES
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3030-01'),'GIUA_KY','2026-11-10','07:30:00','09:00:00','A101','TU_LUAN',   'Thi giua ky Lap trinh Web'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3030-01'),'CUOI_KY','2027-01-05','07:30:00','09:30:00','A201','TRAC_NGHIEM','Thi cuoi ky Lap trinh Web'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3030-02'),'CUOI_KY','2027-01-06','07:30:00','09:30:00','A202','TRAC_NGHIEM','Thi cuoi ky LP Web lop 2'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT4030-01'),'CUOI_KY','2027-01-08','09:30:00','11:00:00','B101','TU_LUAN',   'Thi Machine Learning'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3050-01'),'CUOI_KY','2027-01-10','07:30:00','09:30:00','A301','TU_LUAN',   'Thi An toan TT'),
((SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1216-01'),'CUOI_KY','2027-01-03','07:30:00','09:00:00','A101','TRAC_NGHIEM','Thi Xac suat thong ke');

-- 12. KE HOACH NGUYEN VONG
INSERT INTO ke_hoach_nguyen_vong (ten_ke_hoach,mo_ta,hoc_ky_id,ngay_bat_dau,ngay_ket_thuc,trang_thai) VALUES
('Dang ky nguyen vong HK1 2026-2027','Dang ky mon hoc du kien HK1 2026-2027',
 (SELECT id FROM hoc_ky WHERE nam_hoc='2026-2027' AND hoc_ky_thu=1),
 '2026-09-01','2026-09-30','DANG_MO');

INSERT INTO nguyen_vong_mon_hoc (ke_hoach_nguyen_vong_id,mon_hoc_id) VALUES
((SELECT id FROM ke_hoach_nguyen_vong WHERE ten_ke_hoach='Dang ky nguyen vong HK1 2026-2027'),(SELECT id FROM mon_hoc WHERE ma_mon='IT3030')),
((SELECT id FROM ke_hoach_nguyen_vong WHERE ten_ke_hoach='Dang ky nguyen vong HK1 2026-2027'),(SELECT id FROM mon_hoc WHERE ma_mon='IT4030')),
((SELECT id FROM ke_hoach_nguyen_vong WHERE ten_ke_hoach='Dang ky nguyen vong HK1 2026-2027'),(SELECT id FROM mon_hoc WHERE ma_mon='IT3050')),
((SELECT id FROM ke_hoach_nguyen_vong WHERE ten_ke_hoach='Dang ky nguyen vong HK1 2026-2027'),(SELECT id FROM mon_hoc WHERE ma_mon='IT4010')),
((SELECT id FROM ke_hoach_nguyen_vong WHERE ten_ke_hoach='Dang ky nguyen vong HK1 2026-2027'),(SELECT id FROM mon_hoc WHERE ma_mon='IT3060')),
((SELECT id FROM ke_hoach_nguyen_vong WHERE ten_ke_hoach='Dang ky nguyen vong HK1 2026-2027'),(SELECT id FROM mon_hoc WHERE ma_mon='MI1216')),
((SELECT id FROM ke_hoach_nguyen_vong WHERE ten_ke_hoach='Dang ky nguyen vong HK1 2026-2027'),(SELECT id FROM mon_hoc WHERE ma_mon='IT2020'));

-- 13. DINH HUONG
INSERT INTO dinh_huong (ten_dinh_huong,mo_ta,nganh_id,ngay_bat_dau,ngay_ket_thuc,che_do_dang_ky,trang_thai) VALUES
('Cong nghe phan mem', 'Phat trien phan mem, lap trinh ung dung',(SELECT id FROM nganh WHERE ma_nganh='CNTT'),'2026-09-01','2026-09-30','BAT_BUOC','DANG_MO'),
('He thong thong tin', 'Phan tich thiet ke he thong',            (SELECT id FROM nganh WHERE ma_nganh='CNTT'),'2026-09-01','2026-09-30','BAT_BUOC','DANG_MO'),
('Ky thuat phan mem',  'Kiem thu va dam bao chat luong PM',      (SELECT id FROM nganh WHERE ma_nganh='KTPM'),'2026-09-01','2026-09-30','TU_CHON', 'DANG_MO'),
('Phan tich du lieu',  'Xu ly va phan tich du lieu lon',         (SELECT id FROM nganh WHERE ma_nganh='KTPM'),'2026-09-01','2026-09-30','TU_CHON', 'DANG_MO');

-- 14. DANG KY HOC PHAN
INSERT INTO dang_ky_hoc_phan (sinh_vien_id,lop_hoc_phan_id,trang_thai,diem_giua_ky,diem_cuoi_ky,diem_tong_ket) VALUES
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3030-01'),'DA_DANG_KY',NULL,NULL,NULL),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT4030-01'),'DA_DANG_KY',NULL,NULL,NULL),
((SELECT id FROM sinh_vien WHERE mssv='SV002'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3030-02'),'DA_DANG_KY',NULL,NULL,NULL),
((SELECT id FROM sinh_vien WHERE mssv='SV002'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3050-01'),'DA_DANG_KY',NULL,NULL,NULL);

-- =============================================================
-- 15. DIEM MAU CHO SV001 (de test trang Tin chi tich luy)
-- SV001 da hoan thanh cac mon HK1, HK2, HK3 voi diem thuc te
-- =============================================================

-- Them lop hoc phan cac HK cu (da ket thuc) de co dang ky + diem
INSERT INTO lop_hoc_phan (ma_lop_hp,mon_hoc_id,hoc_ky_id,giang_vien_id,si_so_toi_da,si_so_hien_tai,trang_thai,version) VALUES
-- HK1 (2022-2023 ky 1)
('PE1213-01', (SELECT id FROM mon_hoc WHERE ma_mon='PE1213'), (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV005'),60,30,'DONG',0),
('MI1216-HK1',(SELECT id FROM mon_hoc WHERE ma_mon='MI1216'), (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV003'),60,30,'DONG',0),
('MI1209-01', (SELECT id FROM mon_hoc WHERE ma_mon='MI1209'), (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV003'),60,30,'DONG',0),
('SSH1207-01',(SELECT id FROM mon_hoc WHERE ma_mon='SSH1207'),(SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV007'),60,30,'DONG',0),
('FL1219-01', (SELECT id FROM mon_hoc WHERE ma_mon='FL1219'), (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV006'),60,30,'DONG',0),
('MI1208-01', (SELECT id FROM mon_hoc WHERE ma_mon='MI1208'), (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV003'),60,30,'DONG',0),
('IT1110-01', (SELECT id FROM mon_hoc WHERE ma_mon='IT1110'), (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV001'),50,30,'DONG',0),
-- HK2 (2022-2023 ky 2)
('FL1220-01', (SELECT id FROM mon_hoc WHERE ma_mon='FL1220'), (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=2),(SELECT id FROM giang_vien WHERE ma_gv='GV006'),60,30,'DONG',0),
('SSH1201-01',(SELECT id FROM mon_hoc WHERE ma_mon='SSH1201'),(SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=2),(SELECT id FROM giang_vien WHERE ma_gv='GV007'),60,30,'DONG',0),
('SSH1206-01',(SELECT id FROM mon_hoc WHERE ma_mon='SSH1206'),(SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=2),(SELECT id FROM giang_vien WHERE ma_gv='GV007'),60,30,'DONG',0),
('MI1212-01', (SELECT id FROM mon_hoc WHERE ma_mon='MI1212'), (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=2),(SELECT id FROM giang_vien WHERE ma_gv='GV003'),60,30,'DONG',0),
('MI1213-01', (SELECT id FROM mon_hoc WHERE ma_mon='MI1213'), (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=2),(SELECT id FROM giang_vien WHERE ma_gv='GV003'),60,30,'DONG',0),
('IT1120-01', (SELECT id FROM mon_hoc WHERE ma_mon='IT1120'), (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=2),(SELECT id FROM giang_vien WHERE ma_gv='GV001'),50,30,'DONG',0),
('IT2070-01', (SELECT id FROM mon_hoc WHERE ma_mon='IT2070'), (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=2),(SELECT id FROM giang_vien WHERE ma_gv='GV004'),50,30,'DONG',0),
-- HK3 (2023-2024 ky 1)
('SSH1204-01',(SELECT id FROM mon_hoc WHERE ma_mon='SSH1204'),(SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV007'),60,30,'DONG',0),
('SSH1205-01',(SELECT id FROM mon_hoc WHERE ma_mon='SSH1205'),(SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV007'),60,30,'DONG',0),
('IT2020-HK3',(SELECT id FROM mon_hoc WHERE ma_mon='IT2020'), (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV001'),50,30,'DONG',0),
('IT2030-01', (SELECT id FROM mon_hoc WHERE ma_mon='IT2030'), (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV002'),50,30,'DONG',0),
('IT2040-01', (SELECT id FROM mon_hoc WHERE ma_mon='IT2040'), (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV004'),50,30,'DONG',0),
('IT2060-01', (SELECT id FROM mon_hoc WHERE ma_mon='IT2060'), (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=1),(SELECT id FROM giang_vien WHERE ma_gv='GV001'),50,30,'DONG',0);

-- Dang ky hoc phan + diem cho SV001 (HK1)
INSERT INTO dang_ky_hoc_phan (sinh_vien_id,lop_hoc_phan_id,trang_thai,diem_giua_ky,diem_cuoi_ky,diem_tong_ket) VALUES
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='PE1213-01'), 'HOAN_THANH', 8.0, 8.0, 8.0),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1216-HK1'),'HOAN_THANH', 6.0, 6.5, 6.2),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1209-01'), 'HOAN_THANH', 7.0, 6.5, 6.8),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='SSH1207-01'),'HOAN_THANH', 7.0, 6.5, 6.9),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='FL1219-01'), 'HOAN_THANH', 6.5, 7.0, 6.8),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1208-01'), 'HOAN_THANH', 6.5, 7.0, 6.8),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT1110-01'), 'HOAN_THANH', 7.0, 7.5, 7.2),
-- HK2
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='FL1220-01'), 'HOAN_THANH', 3.5, 4.5, 4.4),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='SSH1201-01'),'HOAN_THANH', 6.0, 6.5, 6.4),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='SSH1206-01'),'HOAN_THANH', 7.0, 6.5, 6.9),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1212-01'), 'HOAN_THANH', 7.0, 6.5, 6.9),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1213-01'), 'HOAN_THANH', 5.0, 4.5, 4.8),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT1120-01'), 'HOAN_THANH', 6.5, 7.0, 6.8),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2070-01'), 'HOAN_THANH', 7.5, 8.0, 7.8),
-- HK3
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='SSH1204-01'),'HOAN_THANH', 5.5, 5.5, 5.5),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='SSH1205-01'),'HOAN_THANH', 7.0, 6.5, 6.9),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2020-HK3'),'HOAN_THANH', 6.5, 5.0, 5.5),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2030-01'), 'HOAN_THANH', 7.0, 7.5, 7.2),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2040-01'), 'HOAN_THANH', 8.0, 8.5, 8.3),
((SELECT id FROM sinh_vien WHERE mssv='SV001'),(SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2060-01'), 'HOAN_THANH', 7.5, 8.0, 7.8);
