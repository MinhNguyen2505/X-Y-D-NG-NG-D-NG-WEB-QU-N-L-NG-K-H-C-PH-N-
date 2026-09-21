-- =============================================================
-- data_mau.sql — Dữ liệu mẫu đầy đủ để demo/test
-- Chạy SAU schema.sql (hoặc dùng reset.sql để reset + insert lại)
-- Mật khẩu tất cả tài khoản: 123456
-- =============================================================

USE quan_ly_hoc_phan;

-- 1. BỔ SUNG ĐỊNH HƯỚNG CHO TẤT CẢ CÁC NGÀNH CÒN THIẾU
INSERT IGNORE INTO dinh_huong (id, ten_dinh_huong, mo_ta, nganh_id, ngay_bat_dau, ngay_ket_thuc, che_do_dang_ky, trang_thai) VALUES
(11, 'He thong Thong tin Doanh nghiep (ERP)', 'Chuyen sau ve he thong ERP va phan tich quy trinh kinh doanh', 43, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(12, 'Quan tri Co so Du lieu va Dien toan Dam may', 'Chuyen sau Cloud Computing va Database Administration', 43, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(13, 'Tri tue Nhan tao va Thi giac May tinh', 'Nghien cuu AI, Deep Learning va Computer Vision', 44, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(14, 'Khoa hoc Du lieu va Big Data', 'Khai pha du lieu lon va phan tich thong ke', 44, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(15, 'An toan va Bao mat Mang May tinh', 'Chuyen gia Cyber Security va Quan tri mang', 45, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(16, 'Mang Truyen thong va Dien toan Bien', 'Edge Computing va mang the he moi', 45, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(17, 'Vi mach va He thong Nhung IoT', 'Thiet ke chip vi mach va phan cung nhung', 46, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(18, 'Thong tin Dien tu Vien thong 5G/6G', 'Mang vo tuyen toc do cao', 46, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(19, 'O to Dien va Xe Tu hanh Autonomous', 'Phat trien xe thong minh va pin xe dien', 47, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(20, 'Kiem dinh va Dich vu Ky thuat O to', 'Chan doan loi va van hanh he thong garage', 47, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(21, 'Ket cau Cong trinh Dan dung va Cong nghiep', 'Tinh toan ket cau be tong cot thep va thep', 48, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(22, 'Quan ly Du an va Kinh te Xay dung', 'Du toan va dieu hanh tien do cong trinh', 48, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(23, 'Marketing So va Thuong mai Dien tu', 'E-Commerce va tiep thi da kenh', 49, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(24, 'Quan tri Khoi nghiep va Doi moi Sang tao', 'Phat trien mo hinh kinh doanh moi', 49, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(25, 'Ke toan - Kiem toan Quoc te (ACCA)', 'Chung chi ke toan kiem toan chuyen nghiep', 50, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(26, 'Phan tich Tai chinh va Dau tu Chung khoan', 'Dau tu va quan tri rui ro tai chinh', 50, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(27, 'Bien phien dich Tieng Anh Thuong mai', 'Dich thuat cao cap va giao tiep quoc te', 51, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(28, 'Tieng Anh Cong nghe va Truyen thong', 'Tieng Anh chuyen nganh Ky thuat - CNTT', 51, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(29, 'Quan tri Chuoi cung ung Toan cau', 'Logistics quoc te va xuat nhap khau', 52, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO'),
(30, 'Quan ly Kho van va Giao nhan Thong minh', 'Kho thong minh Smart Warehousing', 52, '2026-01-01', '2027-12-31', 'TU_CHON', 'DANG_MO');

-- 2. ĐĂNG KÝ ĐỊNH HƯỚNG CHO SINH VIÊN
INSERT IGNORE INTO dang_ky_dinh_huong (sinh_vien_id, dinh_huong_id, ngay_dang_ky, trang_thai) VALUES
(36, 7, '2026-09-01 08:30:00', 'DA_DANG_KY'),
(37, 8, '2026-09-01 09:15:00', 'DA_DANG_KY'),
(38, 9, '2026-09-02 10:00:00', 'DA_DANG_KY'),
(39, 11, '2026-09-02 11:20:00', 'DA_DANG_KY'),
(40, 7, '2026-09-02 14:00:00', 'DA_DANG_KY'),
(41, 17, '2026-09-03 08:45:00', 'DA_DANG_KY'),
(42, 18, '2026-09-03 09:30:00', 'DA_DANG_KY'),
(43, 19, '2026-09-03 10:15:00', 'DA_DANG_KY'),
(44, 20, '2026-09-04 14:10:00', 'DA_DANG_KY'),
(45, 21, '2026-09-04 15:30:00', 'DA_DANG_KY'),
(46, 22, '2026-09-05 08:20:00', 'DA_DANG_KY'),
(47, 23, '2026-09-05 09:40:00', 'DA_DANG_KY'),
(48, 24, '2026-09-05 10:50:00', 'DA_DANG_KY'),
(49, 25, '2026-09-06 14:00:00', 'DA_DANG_KY'),
(50, 26, '2026-09-06 15:10:00', 'DA_DANG_KY'),
(51, 27, '2026-09-07 08:30:00', 'DA_DANG_KY'),
(52, 28, '2026-09-07 09:15:00', 'DA_DANG_KY'),
(53, 25, '2026-09-07 10:00:00', 'DA_DANG_KY'),
(54, 29, '2026-09-08 08:00:00', 'DA_DANG_KY');

-- 3. TẠO HỌC PHẦN BỊ ĐIỂM F (KHÔNG ĐẠT, < 4.0) CHO HỌC KỲ TRƯỚC (HỌC KỲ 26)
-- SV001 trượt môn MI1216 (Xác suất thống kê)
INSERT INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, ngay_dang_ky, trang_thai, diem_giua_ky, diem_cuoi_ky, diem_tong_ket)
SELECT 36, id, '2025-08-15 08:00:00', 'HOAN_THANH', 3.0, 3.0, 3.0
FROM lop_hoc_phan WHERE mon_hoc_id = 105 AND hoc_ky_id = 26 LIMIT 1
ON DUPLICATE KEY UPDATE diem_tong_ket=3.0, diem_cuoi_ky=3.0;

-- SV002 trượt môn IT2020 (Cấu trúc dữ liệu)
INSERT INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, ngay_dang_ky, trang_thai, diem_giua_ky, diem_cuoi_ky, diem_tong_ket)
SELECT 37, id, '2025-08-15 08:00:00', 'HOAN_THANH', 3.5, 2.5, 2.9
FROM lop_hoc_phan WHERE mon_hoc_id = 109 AND hoc_ky_id = 26 LIMIT 1
ON DUPLICATE KEY UPDATE diem_tong_ket=2.9, diem_cuoi_ky=2.5;

-- SV003 trượt môn IT3050 (An toàn thông tin)
INSERT INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, ngay_dang_ky, trang_thai, diem_giua_ky, diem_cuoi_ky, diem_tong_ket)
SELECT 38, id, '2025-08-15 08:00:00', 'HOAN_THANH', 4.0, 2.0, 2.8
FROM lop_hoc_phan WHERE mon_hoc_id = 119 AND hoc_ky_id = 26 LIMIT 1
ON DUPLICATE KEY UPDATE diem_tong_ket=2.8, diem_cuoi_ky=2.0;

-- SV004 trượt môn MI1216
INSERT INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, ngay_dang_ky, trang_thai, diem_giua_ky, diem_cuoi_ky, diem_tong_ket)
SELECT 39, id, '2025-08-15 08:00:00', 'HOAN_THANH', 3.0, 2.0, 2.4
FROM lop_hoc_phan WHERE mon_hoc_id = 105 AND hoc_ky_id = 26 LIMIT 1
ON DUPLICATE KEY UPDATE diem_tong_ket=2.4, diem_cuoi_ky=2.0;

-- 4. BỔ SUNG ĐĂNG KÝ THI LẠI VỚI ĐỦ 3 TRẠNG THÁI: CHO_DUYET, DA_DUYET, DA_HUY
-- Xóa cũ để tạo mới sạch đẹp
DELETE FROM dang_ky_thi_lai WHERE sinh_vien_id IN (36, 37, 38, 39);

INSERT INTO dang_ky_thi_lai (sinh_vien_id, dang_ky_hp_id, ngay_dang_ky, trang_thai, ghi_chu)
SELECT dk.sinh_vien_id, dk.id, NOW(), 'CHO_DUYET', 'Xin thi lai cai thien diem F de tot nghiep dung han'
FROM dang_ky_hoc_phan dk
WHERE dk.diem_tong_ket IS NOT NULL AND dk.diem_tong_ket < 4.0 AND dk.sinh_vien_id IN (36, 37)
LIMIT 2;

INSERT INTO dang_ky_thi_lai (sinh_vien_id, dang_ky_hp_id, ngay_dang_ky, trang_thai, ghi_chu)
SELECT dk.sinh_vien_id, dk.id, '2026-09-10 14:00:00', 'DA_DUYET', 'Du dieu kien du thi lai dot 1'
FROM dang_ky_hoc_phan dk
WHERE dk.diem_tong_ket IS NOT NULL AND dk.diem_tong_ket < 4.0 AND dk.sinh_vien_id = 38
LIMIT 1;

INSERT INTO dang_ky_thi_lai (sinh_vien_id, dang_ky_hp_id, ngay_dang_ky, trang_thai, ghi_chu)
SELECT dk.sinh_vien_id, dk.id, '2026-09-12 10:00:00', 'DA_HUY', 'Huy don do sinh vien dang ky hoc lai'
FROM dang_ky_hoc_phan dk
WHERE dk.diem_tong_ket IS NOT NULL AND dk.diem_tong_ket < 4.0 AND dk.sinh_vien_id = 39
LIMIT 1;

-- 5. BỔ SUNG ĐĂNG KÝ NGUYỆN VỌNG VỚI ĐỦ CÁC TRẠNG THÁI: CHO_DUYET, DA_DUYET, DA_HUY
INSERT IGNORE INTO dang_ky_nguyen_vong (sinh_vien_id, nguyen_vong_mon_hoc_id, ngay_dang_ky, trang_thai) VALUES
(36, 21, '2026-09-05 08:00:00', 'DA_DUYET'),
(36, 20, '2026-09-05 08:05:00', 'DA_DUYET'),
(37, 21, '2026-09-05 09:10:00', 'DA_DUYET'),
(38, 20, '2026-09-05 10:15:00', 'DA_DUYET'),
(39, 18, '2026-09-06 14:20:00', 'DA_DUYET'),
(40, 19, '2026-09-06 15:30:00', 'DA_DUYET'),
(37, 22, '2026-09-07 08:40:00', 'DA_HUY'),
(38, 23, '2026-09-07 09:50:00', 'DA_HUY'),
(40, 22, '2026-09-07 11:00:00', 'DA_HUY');

-- 6. ĐĂNG KÝ HỌC PHẦN CHO HỌC KỲ HIỆN TẠI (HỌC KỲ 27)
-- Đảm bảo phân bổ tín chỉ rải đều cả 5 nhóm (1-5, 6-10, 11-15, 16-20, >20)
-- Nhóm 1-5 TC
INSERT IGNORE INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, ngay_dang_ky, trang_thai) VALUES
(40, 25, '2026-09-01 08:00:00', 'DA_DANG_KY');

-- Nhóm 6-10 TC
INSERT IGNORE INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, ngay_dang_ky, trang_thai) VALUES
(36, 28, '2026-09-01 08:10:00', 'DA_DANG_KY'),
(37, 27, '2026-09-01 08:20:00', 'DA_DANG_KY'),
(38, 25, '2026-09-01 08:30:00', 'DA_DANG_KY'),
(38, 27, '2026-09-01 08:35:00', 'DA_DANG_KY'),
(38, 28, '2026-09-01 08:40:00', 'DA_DANG_KY');

-- Nhóm 11-15 TC
INSERT IGNORE INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, ngay_dang_ky, trang_thai) VALUES
(39, 25, '2026-09-01 09:00:00', 'DA_DANG_KY'),
(39, 27, '2026-09-01 09:05:00', 'DA_DANG_KY'),
(39, 28, '2026-09-01 09:10:00', 'DA_DANG_KY'),
(39, 31, '2026-09-01 09:15:00', 'DA_DANG_KY'),

(41, 101, '2026-09-01 09:20:00', 'DA_DANG_KY'),
(41, 102, '2026-09-01 09:25:00', 'DA_DANG_KY'),
(41, 93, '2026-09-01 09:30:00', 'DA_DANG_KY'),
(41, 94, '2026-09-01 09:35:00', 'DA_DANG_KY'),

(42, 101, '2026-09-01 09:40:00', 'DA_DANG_KY'),
(42, 102, '2026-09-01 09:45:00', 'DA_DANG_KY'),
(42, 93, '2026-09-01 09:50:00', 'DA_DANG_KY'),
(42, 31, '2026-09-01 09:55:00', 'DA_DANG_KY');

-- Nhóm 16-20 TC
INSERT IGNORE INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, ngay_dang_ky, trang_thai) VALUES
(43, 103, '2026-09-01 10:00:00', 'DA_DANG_KY'),
(43, 104, '2026-09-01 10:05:00', 'DA_DANG_KY'),
(43, 95, '2026-09-01 10:10:00', 'DA_DANG_KY'),
(43, 96, '2026-09-01 10:15:00', 'DA_DANG_KY'),
(43, 31, '2026-09-01 10:20:00', 'DA_DANG_KY'),
(43, 28, '2026-09-01 10:25:00', 'DA_DANG_KY'),

(44, 103, '2026-09-01 10:30:00', 'DA_DANG_KY'),
(44, 104, '2026-09-01 10:35:00', 'DA_DANG_KY'),
(44, 95, '2026-09-01 10:40:00', 'DA_DANG_KY'),
(44, 96, '2026-09-01 10:45:00', 'DA_DANG_KY'),
(44, 31, '2026-09-01 10:50:00', 'DA_DANG_KY'),
(44, 32, '2026-09-01 10:55:00', 'DA_DANG_KY'),

(45, 105, '2026-09-01 11:00:00', 'DA_DANG_KY'),
(45, 106, '2026-09-01 11:05:00', 'DA_DANG_KY'),
(45, 97, '2026-09-01 11:10:00', 'DA_DANG_KY'),
(45, 98, '2026-09-01 11:15:00', 'DA_DANG_KY'),
(45, 31, '2026-09-01 11:20:00', 'DA_DANG_KY'),
(45, 25, '2026-09-01 11:25:00', 'DA_DANG_KY');

-- Nhóm >20 TC
INSERT IGNORE INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, ngay_dang_ky, trang_thai) VALUES
(47, 107, '2026-09-01 13:00:00', 'DA_DANG_KY'),
(47, 108, '2026-09-01 13:05:00', 'DA_DANG_KY'),
(47, 99, '2026-09-01 13:10:00', 'DA_DANG_KY'),
(47, 100, '2026-09-01 13:15:00', 'DA_DANG_KY'),
(47, 31, '2026-09-01 13:20:00', 'DA_DANG_KY'),
(47, 25, '2026-09-01 13:25:00', 'DA_DANG_KY'),
(47, 28, '2026-09-01 13:30:00', 'DA_DANG_KY'),

(49, 109, '2026-09-01 13:35:00', 'DA_DANG_KY'),
(49, 110, '2026-09-01 13:40:00', 'DA_DANG_KY'),
(49, 99, '2026-09-01 13:45:00', 'DA_DANG_KY'),
(49, 100, '2026-09-01 13:50:00', 'DA_DANG_KY'),
(49, 31, '2026-09-01 13:55:00', 'DA_DANG_KY'),
(49, 25, '2026-09-01 14:00:00', 'DA_DANG_KY'),
(49, 32, '2026-09-01 14:05:00', 'DA_DANG_KY');

-- Cập nhật sĩ số thực tế theo số bản ghi DA_DANG_KY/HOAN_THANH
UPDATE lop_hoc_phan lhp
SET lhp.si_so_hien_tai = (
    SELECT COUNT(*) FROM dang_ky_hoc_phan dk
    WHERE dk.lop_hoc_phan_id = lhp.id AND dk.trang_thai IN ('DA_DANG_KY', 'HOAN_THANH')
);


-- 7. LỊCH HỌC
INSERT IGNORE INTO lich_hoc (lop_hoc_phan_id, thu, tiet_bat_dau, tiet_ket_thuc, phong) VALUES
(25, 2, 1, 3, 'A1-301'),
(26, 3, 4, 6, 'A1-302'),
(27, 4, 7, 9, 'B2-201'),
(28, 5, 1, 3, 'B2-202'),
(29, 6, 4, 6, 'C3-101'),
(30, 7, 7, 9, 'C3-102'),
(31, 2, 7, 10, 'A2-101'),
(32, 3, 1, 3, 'A2-102'),
(93, 2, 1, 3, 'D1-201'),
(94, 3, 4, 6, 'D1-202'),
(95, 4, 7, 9, 'E2-101'),
(96, 5, 1, 3, 'E2-102'),
(97, 6, 4, 6, 'F3-201'),
(98, 7, 7, 9, 'F3-202'),
(99, 2, 4, 6, 'G1-101'),
(100, 3, 7, 9, 'G1-102'),
(101, 4, 1, 3, 'D1-301'),
(102, 5, 4, 6, 'D1-302'),
(103, 6, 7, 9, 'E2-201'),
(104, 7, 1, 3, 'E2-202'),
(105, 2, 7, 9, 'F3-301'),
(106, 3, 1, 3, 'F3-302'),
(107, 4, 4, 6, 'G1-201'),
(108, 5, 7, 9, 'G1-202'),
(109, 6, 1, 3, 'H2-101'),
(110, 7, 4, 6, 'H2-102'),
(111, 2, 1, 3, 'H2-201'),
(112, 3, 4, 6, 'H2-202');

-- 8. LỊCH THI
INSERT IGNORE INTO lich_thi (lop_hoc_phan_id, loai_thi, ngay_thi, gio_bat_dau, gio_ket_thuc, phong_thi, hinh_thuc, ghi_chu) VALUES
(25, 'CUOI_KY', '2027-01-05', '07:30:00', '09:30:00', 'Hoi truong A', 'MAY_TINH', 'Thi thuc hanh may'),
(26, 'CUOI_KY', '2027-01-05', '13:30:00', '15:30:00', 'Hoi truong A', 'MAY_TINH', 'Thi thuc hanh may'),
(27, 'CUOI_KY', '2027-01-06', '07:30:00', '09:00:00', 'B2-201', 'TU_LUAN', 'Khong su dung tai lieu'),
(28, 'CUOI_KY', '2027-01-07', '09:30:00', '11:00:00', 'B2-202', 'TRAC_NGHIEM', 'Thi trac nghiem tren giay'),
(29, 'CUOI_KY', '2027-01-08', '07:30:00', '10:00:00', 'Lab CNTT 1', 'VAN_DAP', 'Bao cao do an mon hoc'),
(30, 'CUOI_KY', '2027-01-09', '13:30:00', '15:00:00', 'C3-102', 'TU_LUAN', 'Su dung 1 to giay A4 viet tay'),
(31, 'CUOI_KY', '2027-01-10', '07:30:00', '09:30:00', 'Hoi truong B', 'TU_LUAN', 'Dem theo may tinh bo tui'),
(32, 'CUOI_KY', '2027-01-11', '09:30:00', '11:00:00', 'A2-102', 'TRAC_NGHIEM', 'Trac nghiem 40 cau'),
(101, 'CUOI_KY', '2027-01-12', '07:30:00', '09:00:00', 'D1-301', 'TU_LUAN', 'Khong su dung tai lieu'),
(103, 'CUOI_KY', '2027-01-13', '13:30:00', '15:00:00', 'E2-201', 'TU_LUAN', 'Khong su dung tai lieu'),
(105, 'CUOI_KY', '2027-01-14', '07:30:00', '09:30:00', 'F3-301', 'DO_AN', 'Nop thuyet minh do an BIM'),
(107, 'CUOI_KY', '2027-01-15', '09:30:00', '11:00:00', 'G1-201', 'TIEU_LUAN', 'Nop bai tap lon nhom');

-- 9. ĐIỂM DANH
INSERT IGNORE INTO diem_danh (dang_ky_id, ngay_hoc, tiet_bat_dau, tiet_ket_thuc, trang_thai, ghi_chu)
SELECT dk.id, '2026-09-08', 1, 3, 'CO_MAT', 'Diem danh dau gio'
FROM dang_ky_hoc_phan dk
WHERE dk.lop_hoc_phan_id = 25
LIMIT 20;

INSERT IGNORE INTO diem_danh (dang_ky_id, ngay_hoc, tiet_bat_dau, tiet_ket_thuc, trang_thai, ghi_chu)
SELECT dk.id, '2026-09-15', 1, 3, 'CO_MAT', 'Co mat day du'
FROM dang_ky_hoc_phan dk
WHERE dk.lop_hoc_phan_id = 25
LIMIT 15;

INSERT IGNORE INTO diem_danh (dang_ky_id, ngay_hoc, tiet_bat_dau, tiet_ket_thuc, trang_thai, ghi_chu)
SELECT dk.id, '2026-09-15', 1, 3, 'VANG_CO_PHEP', 'Xin phep nghi om'
FROM dang_ky_hoc_phan dk
WHERE dk.lop_hoc_phan_id = 25
ORDER BY dk.id DESC
LIMIT 2;

INSERT IGNORE INTO diem_danh (dang_ky_id, ngay_hoc, tiet_bat_dau, tiet_ket_thuc, trang_thai, ghi_chu)
SELECT dk.id, '2026-09-15', 1, 3, 'VANG_KHONG_PHEP', 'Vang mat khong ly do'
FROM dang_ky_hoc_phan dk
WHERE dk.lop_hoc_phan_id = 25
ORDER BY dk.id DESC
LIMIT 1;
