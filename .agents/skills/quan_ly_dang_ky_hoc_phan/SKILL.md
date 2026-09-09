# Prompt: Xây dựng ứng dụng Quản lý đăng ký học phần theo tín chỉ

## Bối cảnh dự án

Xây dựng ứng dụng web quản lý đăng ký học phần theo tín chỉ, sử dụng:
- **Backend**: Spring Boot, Spring Data JPA
- **Frontend**: Thymeleaf (server-side rendering)
- **Database**: MySql
- **Bảo mật**: Spring Security (phân quyền theo role)

## Vai trò người dùng (3 role)

1. **Sinh viên**: xem chương trình đào tạo, đăng ký/hủy đăng ký học phần, xem thời khóa biểu, xem kết quả học tập
2. **Giảng viên**: xem danh sách lớp mình dạy, nhập điểm
3. **Phòng đào tạo (Admin)**: quản lý môn học, mở lớp học phần theo học kỳ, quản lý sinh viên/giảng viên

## Schema cơ sở dữ liệu (10 bảng)

```
nganh
├── id (PK)
├── ma_nganh (UNIQUE)
└── ten_nganh

sinh_vien
├── id (PK)
├── mssv (UNIQUE)
├── ho_ten
├── email (UNIQUE)
├── mat_khau
├── ngay_sinh
├── lop_sinh_hoat
├── khoa_hoc
├── nganh_id (FK -> nganh.id)
└── trang_thai

giang_vien
├── id (PK)
├── ma_gv (UNIQUE)
├── ho_ten
├── email (UNIQUE)
├── mat_khau
└── khoa_bo_mon

mon_hoc
├── id (PK)
├── ma_mon (UNIQUE)
├── ten_mon
├── so_tin_chi
├── so_tiet_ly_thuyet
├── so_tiet_thuc_hanh
└── mo_ta

mon_tien_quyet
├── id (PK)
├── mon_hoc_id (FK -> mon_hoc.id)
├── mon_tien_quyet_id (FK -> mon_hoc.id)
├── UNIQUE(mon_hoc_id, mon_tien_quyet_id)
└── CHECK(mon_hoc_id <> mon_tien_quyet_id)

chuong_trinh_dao_tao
├── id (PK)
├── nganh_id (FK -> nganh.id)
├── mon_hoc_id (FK -> mon_hoc.id)
├── hoc_ky_thu
├── bat_buoc
└── UNIQUE(nganh_id, mon_hoc_id)

hoc_ky
├── id (PK)
├── ten_hoc_ky
├── nam_hoc
├── hoc_ky_thu
├── ngay_bat_dau_dk
├── ngay_ket_thuc_dk
├── ngay_bat_dau_hoc
├── ngay_ket_thuc_hoc
├── tin_chi_toi_thieu
├── tin_chi_toi_da
└── UNIQUE(nam_hoc, hoc_ky_thu)

lop_hoc_phan
├── id (PK)
├── ma_lop_hp (UNIQUE)
├── mon_hoc_id (FK -> mon_hoc.id)
├── hoc_ky_id (FK -> hoc_ky.id)
├── giang_vien_id (FK -> giang_vien.id)
├── si_so_toi_da
├── si_so_hien_tai
├── trang_thai
└── version (dùng cho @Version - optimistic locking)

lich_hoc
├── id (PK)
├── lop_hoc_phan_id (FK -> lop_hoc_phan.id)
├── thu
├── tiet_bat_dau
├── tiet_ket_thuc
└── phong

dang_ky_hoc_phan
├── id (PK)
├── sinh_vien_id (FK -> sinh_vien.id)
├── lop_hoc_phan_id (FK -> lop_hoc_phan.id)
├── ngay_dang_ky
├── trang_thai (DA_DANG_KY / DA_HUY / HOAN_THANH)
├── diem_giua_ky (CHECK 0-10, nullable)
├── diem_cuoi_ky (CHECK 0-10, nullable)
├── diem_tong_ket (CHECK 0-10, nullable)
└── UNIQUE(sinh_vien_id, lop_hoc_phan_id)
```

**Lưu ý thiết kế quan trọng:**
- `dang_ky_hoc_phan` KHÔNG có `hoc_ky_id` — học kỳ suy ra qua `lop_hoc_phan.hoc_ky_id` để tránh transitive dependency
- `lop_hoc_phan` KHÔNG có cột phòng học — phòng chỉ nằm ở `lich_hoc.phong` vì 1 lớp có thể học nhiều buổi/phòng khác nhau

## Các ràng buộc nghiệp vụ bắt buộc phải xử lý ở tầng Service

1. **Không cho đăng ký trùng lịch học** — so sánh `lich_hoc` (thu, tiet_bat_dau, tiet_ket_thuc) giữa các lớp học phần SV đã đăng ký trong cùng học kỳ
2. **Kiểm tra sĩ số còn chỗ** trước khi đăng ký — dùng `@Transactional` + `@Version` (optimistic locking) trên `lop_hoc_phan` để tránh race condition khi nhiều SV đăng ký cùng lúc
3. **Kiểm tra môn tiên quyết** — SV phải đã hoàn thành (có điểm đạt) môn tiên quyết trước khi đăng ký môn kế tiếp, tra qua bảng `mon_tien_quyet`
4. **Giới hạn tổng số tín chỉ** đăng ký trong 1 học kỳ theo `hoc_ky.tin_chi_toi_thieu` / `tin_chi_toi_da`
5. **Không đăng ký trùng lớp** đã đăng ký (bảng đã có UNIQUE constraint, nhưng vẫn nên check ở Service để trả thông báo lỗi rõ ràng thay vì bắt exception từ DB)
6. **Đăng ký chỉ được thực hiện trong khung thời gian mở** — kiểm tra thời điểm hiện tại nằm trong `hoc_ky.ngay_bat_dau_dk` và `ngay_ket_thuc_dk`
7. Khi đăng ký thành công: `si_so_hien_tai + 1`; khi hủy: `si_so_hien_tai - 1` — bắt buộc trong cùng transaction với ghi/xóa `dang_ky_hoc_phan`

## Thứ tự triển khai mong muốn

1. Setup project Spring Boot (Web, Data JPA, Thymeleaf, MySQL Driver, Validation, Security, Lombok)
2. Tạo database MySQL, chạy schema SQL ở trên (kèm CHECK constraint nếu MySQL >= 8.0.16)
3. Viết Entity JPA cho 10 bảng, đúng quan hệ `@ManyToOne`/`@OneToMany`, `@Version`, `@UniqueConstraint`
4. Viết Repository (`JpaRepository`) kèm các query method cần cho nghiệp vụ (check trùng đăng ký, lấy lịch học theo SV/học kỳ...)
5. Viết Service — toàn bộ 7 ràng buộc nghiệp vụ ở trên, có `@Transactional`
6. Viết Controller (AuthController, SinhVienController, AdminController, GiangVienController)
7. Viết giao diện Thymeleaf: layout dùng chung (fragment) trước, rồi từng trang theo thứ tự — đăng nhập → admin CRUD → SV đăng ký học phần → thời khóa biểu → GV nhập điểm
8. Thêm Spring Security, phân quyền theo 3 role, mã hóa mật khẩu bằng BCrypt
9. Hoàn thiện UX: thông báo lỗi rõ ràng cho từng trường hợp fail khi đăng ký (hết chỗ, trùng lịch, thiếu tiên quyết, vượt tín chỉ)
10. Test edge case: 2 SV tranh 1 chỗ cuối, đăng ký thiếu tiên quyết, trùng lịch, vượt tín chỉ tối đa

## Yêu cầu khi bắt đầu code

- Bắt đầu từ bước 1-3 trước (setup + entity), chưa cần viết UI ngay
- Với mỗi Entity, giải thích ngắn gọn các annotation quan trọng đã dùng (đặc biệt `@Version`, `@UniqueConstraint`, hướng quan hệ `@ManyToOne`)
- Code Service phải tách rõ từng ràng buộc nghiệp vụ thành method riêng, dễ test và dễ đọc, không dồn tất cả logic vào 1 method `dangKy()` khổng lồ
- Dùng exception tùy chỉnh (VD: `HetChoException`, `TrungLichException`, `ThieuTienQuyetException`) thay vì throw `RuntimeException` chung chung, để Controller dễ bắt và hiển thị thông báo lỗi tương ứng

## Quy tắc làm việc xuyên suốt dự án

- **Sau mỗi bước/mỗi lần trả lời xong, phải đọc lại toàn bộ prompt này** (đặc biệt phần schema và ràng buộc nghiệp vụ) trước khi làm bước tiếp theo — không dựa vào trí nhớ từ các câu trả lời trước, vì dự án nhiều bước, dễ quên chi tiết (VD: quên `dang_ky_hoc_phan` không có `hoc_ky_id`, quên `lop_hoc_phan` không có cột phòng).
- Nếu phát hiện code đã viết ở bước trước không khớp với schema/ràng buộc trong prompt này, phải dừng lại báo cho tôi biết trước khi tiếp tục, không tự ý sửa âm thầm.
- Trước khi bắt đầu mỗi bước mới, tóm tắt ngắn gọn 1-2 câu về việc bước này sẽ làm gì và liên quan thế nào đến các bước đã làm trước đó.
- **Khi làm frontend, mọi giao diện phải đồng bộ với nhau** — dùng chung 1 layout/fragment (header, sidebar, footer, màu sắc, font, khoảng cách, kiểu button, kiểu bảng, kiểu form, kiểu thông báo lỗi/thành công) cho tất cả các trang của cả 3 role. Không được để trang sau có style khác trang trước (VD: trang admin dùng bảng kiểu khác trang sinh viên, nút bấm màu khác nhau giữa các trang). Trước khi viết 1 trang mới, phải xem lại các trang đã làm trước đó để giữ đúng bố cục, màu sắc, cách đặt tên class CSS đã dùng.