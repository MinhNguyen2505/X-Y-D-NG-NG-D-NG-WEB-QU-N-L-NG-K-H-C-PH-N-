-- =============================================================
-- stored_procedures.sql
-- Database: MySQL >= 8.0  |  Chay sau schema.sql
-- =============================================================
USE quan_ly_hoc_phan;

DROP PROCEDURE IF EXISTS sp_tim_hoc_phan_gia_tri_cao_nhat;
DROP PROCEDURE IF EXISTS sp_hoc_phan_dang_ky_nhieu_nhat;

DELIMITER //

-- =============================================================
-- SP1: Tim hoc phan co gia tri cao nhat (tinh theo so_tin_chi)
-- IN  p_top_n      INT  : lay top N mon, mac dinh = 5
-- OUT p_so_ket_qua INT  : so dong tra ve
-- =============================================================
CREATE PROCEDURE sp_tim_hoc_phan_gia_tri_cao_nhat(
    IN  p_top_n       INT,
    OUT p_so_ket_qua  INT
)
BEGIN
    DECLARE v_limit INT;
    SET v_limit = IF(p_top_n IS NULL OR p_top_n <= 0, 5, p_top_n);

    -- Bang tam de ho tro LIMIT ben trong stored procedure
    DROP TEMPORARY TABLE IF EXISTS tmp_sp1;
    CREATE TEMPORARY TABLE tmp_sp1 AS
    SELECT
        mh.id                    AS mon_hoc_id,
        mh.ma_mon,
        mh.ten_mon,
        mh.so_tin_chi,
        mh.so_tiet_ly_thuyet,
        mh.so_tiet_thuc_hanh,
        COUNT(DISTINCT lhp.id)   AS so_lop_dang_day,
        COUNT(DISTINCT dkhp.id)  AS tong_sv_dang_ky,
        (mh.so_tin_chi * COUNT(CASE WHEN dkhp.trang_thai = 'HOAN_THANH' THEN 1 END))
                                 AS gia_tri_tong_hop
    FROM mon_hoc mh
    LEFT JOIN lop_hoc_phan     lhp  ON lhp.mon_hoc_id       = mh.id
    LEFT JOIN dang_ky_hoc_phan dkhp ON dkhp.lop_hoc_phan_id = lhp.id
                                   AND dkhp.trang_thai      <> 'DA_HUY'
    GROUP BY
        mh.id, mh.ma_mon, mh.ten_mon,
        mh.so_tin_chi, mh.so_tiet_ly_thuyet, mh.so_tiet_thuc_hanh
    ORDER BY
        mh.so_tin_chi       DESC,
        gia_tri_tong_hop    DESC,
        tong_sv_dang_ky     DESC
    LIMIT v_limit;

    SELECT COUNT(*) INTO p_so_ket_qua FROM tmp_sp1;

    SELECT
        mon_hoc_id,
        ma_mon,
        ten_mon,
        so_tin_chi,
        so_tiet_ly_thuyet,
        so_tiet_thuc_hanh,
        so_lop_dang_day,
        tong_sv_dang_ky,
        gia_tri_tong_hop
    FROM tmp_sp1
    ORDER BY so_tin_chi DESC, gia_tri_tong_hop DESC;

    DROP TEMPORARY TABLE IF EXISTS tmp_sp1;
END//


-- =============================================================
-- SP2: Tim hoc phan duoc dang ky nhieu nhat trong khoang thoi gian
-- IN p_tu_ngay    DATE   : ngay bat dau (bao gom)
-- IN p_den_ngay   DATE   : ngay ket thuc (bao gom), NULL = hom nay
-- IN p_top_n      INT    : lay top N, mac dinh = 10
-- IN p_hoc_ky_id  BIGINT : loc theo hoc ky cu the (NULL = tat ca)
-- =============================================================
CREATE PROCEDURE sp_hoc_phan_dang_ky_nhieu_nhat(
    IN p_tu_ngay    DATE,
    IN p_den_ngay   DATE,
    IN p_top_n      INT,
    IN p_hoc_ky_id  BIGINT
)
BEGIN
    DECLARE v_limit INT;

    -- Xu ly gia tri mac dinh
    IF p_tu_ngay IS NULL THEN
        SET p_tu_ngay = DATE_FORMAT(NOW(), '%Y-01-01');
    END IF;
    IF p_den_ngay IS NULL THEN
        SET p_den_ngay = CURDATE();
    END IF;

    -- Validate khoang thoi gian
    IF p_tu_ngay > p_den_ngay THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Loi: tu_ngay khong duoc lon hon den_ngay';
    END IF;

    SET v_limit = IF(p_top_n IS NULL OR p_top_n <= 0, 10, p_top_n);

    SELECT
        mh.id                                       AS mon_hoc_id,
        mh.ma_mon,
        mh.ten_mon,
        mh.so_tin_chi,
        hk.ten_hoc_ky,
        hk.nam_hoc,
        lhp.ma_lop_hp,
        COUNT(dkhp.id)                              AS so_luot_dang_ky,
        COUNT(CASE WHEN dkhp.trang_thai = 'HOAN_THANH' THEN 1 END)
                                                    AS so_hoan_thanh,
        COUNT(CASE WHEN dkhp.trang_thai = 'DA_DANG_KY' THEN 1 END)
                                                    AS so_dang_hoc,
        ROUND(
            COUNT(CASE WHEN dkhp.trang_thai = 'HOAN_THANH' THEN 1 END) * 100.0
            / NULLIF(COUNT(dkhp.id), 0),
        1)                                          AS ty_le_hoan_thanh_pct,
        MIN(dkhp.ngay_dang_ky)                      AS ngay_dk_dau_tien,
        MAX(dkhp.ngay_dang_ky)                      AS ngay_dk_cuoi_cung
    FROM dang_ky_hoc_phan dkhp
    JOIN lop_hoc_phan     lhp  ON lhp.id  = dkhp.lop_hoc_phan_id
    JOIN mon_hoc          mh   ON mh.id   = lhp.mon_hoc_id
    JOIN hoc_ky           hk   ON hk.id   = lhp.hoc_ky_id
    WHERE
        dkhp.trang_thai IN ('DA_DANG_KY', 'HOAN_THANH')
        AND DATE(dkhp.ngay_dang_ky) BETWEEN p_tu_ngay AND p_den_ngay
        AND (p_hoc_ky_id IS NULL OR lhp.hoc_ky_id = p_hoc_ky_id)
    GROUP BY
        mh.id, mh.ma_mon, mh.ten_mon, mh.so_tin_chi,
        hk.ten_hoc_ky, hk.nam_hoc, lhp.ma_lop_hp
    ORDER BY so_luot_dang_ky DESC, so_hoan_thanh DESC
    LIMIT v_limit;

END//

DELIMITER ;

-- =============================================================
-- Vi du su dung:
--
-- [SP1] Top 5 hoc phan tin chi cao nhat:
--   CALL sp_tim_hoc_phan_gia_tri_cao_nhat(5, @n);
--   SELECT @n AS tong_ket_qua;
--
-- [SP2] Top 10 dang ky nhieu nhat nam 2026, tat ca hoc ky:
--   CALL sp_hoc_phan_dang_ky_nhieu_nhat('2026-01-01', '2026-12-31', 10, NULL);
--
-- [SP2] Top 5 trong hoc ky co id = 2, thang 9/2026:
--   CALL sp_hoc_phan_dang_ky_nhieu_nhat('2026-09-01', '2026-09-30', 5, 2);
-- =============================================================
