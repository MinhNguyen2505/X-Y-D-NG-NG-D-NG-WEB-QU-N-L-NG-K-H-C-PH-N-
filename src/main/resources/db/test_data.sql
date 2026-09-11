-- =============================================================
-- TEST DATA - Diem thi day du cho SV001 (8 hoc ky) va SV002 (4 hoc ky)
-- Chay trong phpMyAdmin sau khi da co du lieu co ban tu insert_data.sql
-- =============================================================

USE quan_ly_hoc_phan;

-- =============================================================
-- THEM LOP HOC PHAN CAC HK DA QUA (cho SV co lich su hoc)
-- =============================================================

INSERT INTO lop_hoc_phan (ma_lop_hp, mon_hoc_id, hoc_ky_id, giang_vien_id, si_so_toi_da, si_so_hien_tai, trang_thai, version) VALUES
-- HK1 (2022-2023 ky 1) - id hoc_ky se duoc lay tu subquery
('PE1213-K1',  (SELECT id FROM mon_hoc WHERE ma_mon='PE1213'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV005'), 60, 30, 'DONG', 0),
('MI1216-K1',  (SELECT id FROM mon_hoc WHERE ma_mon='MI1216'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV003'), 60, 30, 'DONG', 0),
('MI1209-K1',  (SELECT id FROM mon_hoc WHERE ma_mon='MI1209'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV003'), 60, 30, 'DONG', 0),
('SSH1207-K1', (SELECT id FROM mon_hoc WHERE ma_mon='SSH1207'), (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV007'), 60, 30, 'DONG', 0),
('FL1219-K1',  (SELECT id FROM mon_hoc WHERE ma_mon='FL1219'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV006'), 60, 30, 'DONG', 0),
('MI1208-K1',  (SELECT id FROM mon_hoc WHERE ma_mon='MI1208'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV003'), 60, 30, 'DONG', 0),
('IT1110-K1',  (SELECT id FROM mon_hoc WHERE ma_mon='IT1110'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV001'), 50, 30, 'DONG', 0),
-- HK2 (2022-2023 ky 2)
('FL1220-K2',  (SELECT id FROM mon_hoc WHERE ma_mon='FL1220'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV006'), 60, 30, 'DONG', 0),
('SSH1201-K2', (SELECT id FROM mon_hoc WHERE ma_mon='SSH1201'), (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV007'), 60, 30, 'DONG', 0),
('SSH1206-K2', (SELECT id FROM mon_hoc WHERE ma_mon='SSH1206'), (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV007'), 60, 30, 'DONG', 0),
('MI1212-K2',  (SELECT id FROM mon_hoc WHERE ma_mon='MI1212'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV003'), 60, 30, 'DONG', 0),
('MI1213-K2',  (SELECT id FROM mon_hoc WHERE ma_mon='MI1213'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV003'), 60, 30, 'DONG', 0),
('IT1120-K2',  (SELECT id FROM mon_hoc WHERE ma_mon='IT1120'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV001'), 50, 30, 'DONG', 0),
('IT2070-K2',  (SELECT id FROM mon_hoc WHERE ma_mon='IT2070'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2022-2023' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV004'), 50, 30, 'DONG', 0),
-- HK3 (2023-2024 ky 1)
('SSH1204-K3', (SELECT id FROM mon_hoc WHERE ma_mon='SSH1204'), (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV007'), 60, 30, 'DONG', 0),
('SSH1205-K3', (SELECT id FROM mon_hoc WHERE ma_mon='SSH1205'), (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV007'), 60, 30, 'DONG', 0),
('IT2020-K3',  (SELECT id FROM mon_hoc WHERE ma_mon='IT2020'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV001'), 50, 30, 'DONG', 0),
('IT2030-K3',  (SELECT id FROM mon_hoc WHERE ma_mon='IT2030'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV002'), 50, 30, 'DONG', 0),
('IT2040-K3',  (SELECT id FROM mon_hoc WHERE ma_mon='IT2040'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV004'), 50, 30, 'DONG', 0),
('IT2060-K3',  (SELECT id FROM mon_hoc WHERE ma_mon='IT2060'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV001'), 50, 30, 'DONG', 0),
-- HK4 (2023-2024 ky 2)
('IT2050-K4',  (SELECT id FROM mon_hoc WHERE ma_mon='IT2050'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV004'), 50, 30, 'DONG', 0),
('IT3010-K4',  (SELECT id FROM mon_hoc WHERE ma_mon='IT3010'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV001'), 50, 30, 'DONG', 0),
('IT3020-K4',  (SELECT id FROM mon_hoc WHERE ma_mon='IT3020'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV002'), 50, 30, 'DONG', 0),
('IT3030-K4',  (SELECT id FROM mon_hoc WHERE ma_mon='IT3030'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV001'), 50, 30, 'DONG', 0),
('IT3040-K4',  (SELECT id FROM mon_hoc WHERE ma_mon='IT3040'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV002'), 50, 30, 'DONG', 0),
('QP1101-K4',  (SELECT id FROM mon_hoc WHERE ma_mon='QP1101'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2023-2024' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV007'), 60, 30, 'DONG', 0),
-- HK5 (2024-2025 ky 1)
('IT3050-K5',  (SELECT id FROM mon_hoc WHERE ma_mon='IT3050'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2024-2025' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV005'), 50, 30, 'DONG', 0),
('IT3060-K5',  (SELECT id FROM mon_hoc WHERE ma_mon='IT3060'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2024-2025' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV004'), 50, 30, 'DONG', 0),
('IT4010-K5',  (SELECT id FROM mon_hoc WHERE ma_mon='IT4010'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2024-2025' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV001'), 50, 30, 'DONG', 0),
('IT4030-K5',  (SELECT id FROM mon_hoc WHERE ma_mon='IT4030'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2024-2025' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV003'), 50, 30, 'DONG', 0),
('IT4050-K5',  (SELECT id FROM mon_hoc WHERE ma_mon='IT4050'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2024-2025' AND hoc_ky_thu=1), (SELECT id FROM giang_vien WHERE ma_gv='GV002'), 50, 30, 'DONG', 0),
-- HK6 (2024-2025 ky 2)
('IT4020-K6',  (SELECT id FROM mon_hoc WHERE ma_mon='IT4020'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2024-2025' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV001'), 50, 30, 'DONG', 0),
('IT4060-K6',  (SELECT id FROM mon_hoc WHERE ma_mon='IT4060'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2024-2025' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV002'), 50, 30, 'DONG', 0),
('IT4070-K6',  (SELECT id FROM mon_hoc WHERE ma_mon='IT4070'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2024-2025' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV005'), 50, 30, 'DONG', 0),
('IT4080-K6',  (SELECT id FROM mon_hoc WHERE ma_mon='IT4080'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2024-2025' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV003'), 50, 30, 'DONG', 0),
('IT4040-K6',  (SELECT id FROM mon_hoc WHERE ma_mon='IT4040'),  (SELECT id FROM hoc_ky WHERE nam_hoc='2024-2025' AND hoc_ky_thu=2), (SELECT id FROM giang_vien WHERE ma_gv='GV001'), 50, 30, 'DONG', 0);

-- =============================================================
-- DANG KY + DIEM CHO SV001 — 6 HOC KY (HK1-HK6)
-- Du loai diem: A+, A, B+, B, C+, C, D+, D, F
-- =============================================================

-- HK1: Gioi (phan lon A, B+)
INSERT INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, trang_thai, diem_giua_ky, diem_cuoi_ky, diem_tong_ket) VALUES
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='PE1213-K1'),  'HOAN_THANH', 8.0, 8.5,  8.0),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1216-K1'),  'HOAN_THANH', 6.0, 6.5,  6.2),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1209-K1'),  'HOAN_THANH', 7.5, 6.5,  6.8),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='SSH1207-K1'), 'HOAN_THANH', 7.0, 7.0,  6.9),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='FL1219-K1'),  'HOAN_THANH', 7.0, 7.0,  6.8),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1208-K1'),  'HOAN_THANH', 7.0, 7.0,  6.8),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT1110-K1'),  'HOAN_THANH', 8.0, 7.5,  7.2);

-- HK2: Co 1 mon truot (MI1213 = 4.8), co mon gioi (IT2070 = 7.8)
INSERT INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, trang_thai, diem_giua_ky, diem_cuoi_ky, diem_tong_ket) VALUES
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='FL1220-K2'),  'HOAN_THANH', 3.5, 4.5,  4.4),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='SSH1201-K2'), 'HOAN_THANH', 6.0, 6.5,  6.4),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='SSH1206-K2'), 'HOAN_THANH', 7.0, 6.5,  6.9),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1212-K2'),  'HOAN_THANH', 7.0, 6.5,  6.9),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1213-K2'),  'HOAN_THANH', 5.0, 4.5,  4.8),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT1120-K2'),  'HOAN_THANH', 6.5, 7.0,  6.8),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2070-K2'),  'HOAN_THANH', 7.5, 8.0,  7.8);

-- HK3: Trung binh kha
INSERT INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, trang_thai, diem_giua_ky, diem_cuoi_ky, diem_tong_ket) VALUES
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='SSH1204-K3'), 'HOAN_THANH', 5.5, 5.5,  5.5),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='SSH1205-K3'), 'HOAN_THANH', 7.0, 6.5,  6.9),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2020-K3'),  'HOAN_THANH', 6.5, 5.0,  5.5),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2030-K3'),  'HOAN_THANH', 7.0, 7.5,  7.2),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2040-K3'),  'HOAN_THANH', 8.0, 8.5,  8.3),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2060-K3'),  'HOAN_THANH', 7.5, 8.0,  7.8);

-- HK4: Kha - Gioi
INSERT INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, trang_thai, diem_giua_ky, diem_cuoi_ky, diem_tong_ket) VALUES
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2050-K4'),  'HOAN_THANH', 7.0, 7.5,  7.2),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3010-K4'),  'HOAN_THANH', 8.0, 8.0,  8.0),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3020-K4'),  'HOAN_THANH', 7.5, 7.5,  7.5),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3030-K4'),  'HOAN_THANH', 8.5, 9.0,  8.8),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3040-K4'),  'HOAN_THANH', 7.0, 8.0,  7.5),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='QP1101-K4'),  'HOAN_THANH', 8.0, 8.0,  8.0);

-- HK5: Gioi - Xuat sac
INSERT INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, trang_thai, diem_giua_ky, diem_cuoi_ky, diem_tong_ket) VALUES
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3050-K5'),  'HOAN_THANH', 8.0, 8.5,  8.3),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT3060-K5'),  'HOAN_THANH', 9.0, 9.5,  9.3),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT4010-K5'),  'HOAN_THANH', 9.0, 9.0,  9.0),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT4030-K5'),  'HOAN_THANH', 8.5, 8.5,  8.5),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT4050-K5'),  'HOAN_THANH', 7.0, 8.0,  7.6);

-- HK6: Dang hoc (chua co diem)
INSERT INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, trang_thai, diem_giua_ky, diem_cuoi_ky, diem_tong_ket) VALUES
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT4020-K6'),  'DA_DANG_KY', NULL,  NULL,  NULL),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT4060-K6'),  'DA_DANG_KY', NULL,  NULL,  NULL),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT4070-K6'),  'DA_DANG_KY', NULL,  NULL,  NULL),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT4080-K6'),  'DA_DANG_KY', NULL,  NULL,  NULL),
((SELECT id FROM sinh_vien WHERE mssv='SV001'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT4040-K6'),  'DA_DANG_KY', 6.5,   NULL,  NULL);

-- =============================================================
-- DANG KY + DIEM CHO SV002 — 3 HOC KY
-- =============================================================

-- HK1: SV002 hoc cung lop voi SV001
INSERT INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, trang_thai, diem_giua_ky, diem_cuoi_ky, diem_tong_ket) VALUES
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='PE1213-K1'),  'HOAN_THANH', 7.0, 7.0,  7.0),
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1216-K1'),  'HOAN_THANH', 5.0, 5.5,  5.2),
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1209-K1'),  'HOAN_THANH', 4.0, 5.0,  4.5),
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='SSH1207-K1'), 'HOAN_THANH', 8.0, 8.0,  8.0),
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='FL1219-K1'),  'HOAN_THANH', 6.0, 6.5,  6.2),
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1208-K1'),  'HOAN_THANH', 7.5, 7.0,  7.2),
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT1110-K1'),  'HOAN_THANH', 9.0, 9.5,  9.3);

-- HK2: SV002
INSERT INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, trang_thai, diem_giua_ky, diem_cuoi_ky, diem_tong_ket) VALUES
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='FL1220-K2'),  'HOAN_THANH', 5.0, 5.5,  5.3),
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='SSH1201-K2'), 'HOAN_THANH', 7.0, 7.5,  7.2),
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='SSH1206-K2'), 'HOAN_THANH', 6.5, 7.0,  6.8),
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1212-K2'),  'HOAN_THANH', 8.0, 8.5,  8.3),
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='MI1213-K2'),  'HOAN_THANH', 7.0, 7.5,  7.2),
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT1120-K2'),  'HOAN_THANH', 8.5, 9.0,  8.8),
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2070-K2'),  'HOAN_THANH', 9.0, 9.5,  9.3);

-- HK3: SV002 dang hoc
INSERT INTO dang_ky_hoc_phan (sinh_vien_id, lop_hoc_phan_id, trang_thai, diem_giua_ky, diem_cuoi_ky, diem_tong_ket) VALUES
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='SSH1204-K3'), 'DA_DANG_KY', 7.0,  NULL,  NULL),
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2020-K3'),  'DA_DANG_KY', 8.5,  NULL,  NULL),
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2030-K3'),  'DA_DANG_KY', 7.5,  NULL,  NULL),
((SELECT id FROM sinh_vien WHERE mssv='SV002'), (SELECT id FROM lop_hoc_phan WHERE ma_lop_hp='IT2060-K3'),  'DA_DANG_KY', 6.0,  NULL,  NULL);

-- =============================================================
-- CAP NHAT HOC KY THUC TE (hoc_ky_thuc_te_label) cho CTDT
-- Giup hien thi "da hoc khi nao" trong trang CTDT
-- =============================================================
UPDATE chuong_trinh_dao_tao SET hoc_ky_thuc_te_label = '2022_2023_1(1)'
WHERE mon_hoc_id IN (SELECT id FROM mon_hoc WHERE ma_mon IN ('PE1213','MI1216','MI1209','SSH1207','FL1219','MI1208','IT1110'))
  AND nganh_id = (SELECT id FROM nganh WHERE ma_nganh = 'CNTT');

UPDATE chuong_trinh_dao_tao SET hoc_ky_thuc_te_label = '2022_2023_2(2)'
WHERE mon_hoc_id IN (SELECT id FROM mon_hoc WHERE ma_mon IN ('FL1220','SSH1201','SSH1206','MI1212','MI1213','IT1120','IT2070'))
  AND nganh_id = (SELECT id FROM nganh WHERE ma_nganh = 'CNTT');

UPDATE chuong_trinh_dao_tao SET hoc_ky_thuc_te_label = '2023_2024_1(1)'
WHERE mon_hoc_id IN (SELECT id FROM mon_hoc WHERE ma_mon IN ('SSH1204','SSH1205','IT2020','IT2030','IT2040','IT2060'))
  AND nganh_id = (SELECT id FROM nganh WHERE ma_nganh = 'CNTT');

UPDATE chuong_trinh_dao_tao SET hoc_ky_thuc_te_label = '2023_2024_2(2)'
WHERE mon_hoc_id IN (SELECT id FROM mon_hoc WHERE ma_mon IN ('IT2050','IT3010','IT3020','IT3030','IT3040','QP1101'))
  AND nganh_id = (SELECT id FROM nganh WHERE ma_nganh = 'CNTT');

UPDATE chuong_trinh_dao_tao SET hoc_ky_thuc_te_label = '2024_2025_1(1)'
WHERE mon_hoc_id IN (SELECT id FROM mon_hoc WHERE ma_mon IN ('IT3050','IT3060','IT4010','IT4030','IT4050'))
  AND nganh_id = (SELECT id FROM nganh WHERE ma_nganh = 'CNTT');
