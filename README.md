# Hệ Thống Quản Lý Đăng Ký Học Phần Theo Tín Chỉ 🎓

Hệ thống ứng dụng Web hỗ trợ quản lý công tác đào tạo theo hệ thống tín chỉ, được thiết kế chuyên biệt để giải quyết bài toán đăng ký học phần với số lượng truy cập lớn và nhiều ràng buộc nghiệp vụ phức tạp.

## 🚀 Công Nghệ Sử Dụng

- **Backend**: Java 17, Spring Boot 3.x, Spring Data JPA, Spring Security
- **Frontend**: HTML5, CSS3 (Vanilla - không dùng thư viện ngoài để tối ưu), Thymeleaf Template Engine
- **Database**: MySQL 8.x
- **Build Tool**: Maven

## ⭐ Các Tính Năng Nổi Bật

Hệ thống được phân quyền chặt chẽ với 4 Role chính:

1. **Sinh Viên (Student)**
   - Đăng ký học phần (Kiểm tra realtime 7 ràng buộc nghiệp vụ học vụ).
   - Đăng ký nguyện vọng mở lớp, đăng ký thi lại cải thiện điểm.
   - Xem thời khóa biểu, chương trình đào tạo, tín chỉ tích lũy và kết quả học tập.

2. **Cán bộ Quản lý (Phòng Đào Tạo)**
   - Mở/Đóng các Lớp học phần theo học kỳ.
   - Duyệt đơn đăng ký nguyện vọng, đơn thi lại của sinh viên.
   - Quản lý định hướng học tập.
   - Báo cáo thống kê tình hình đăng ký tín chỉ (Bản đồ phân bổ, biểu đồ tròn,...).

3. **Giảng Viên (Teacher)**
   - Xem danh sách lớp học phần được phân công giảng dạy.
   - Nhập điểm (Giữa kỳ, cuối kỳ) trực tiếp.
   - Điểm danh sinh viên.

4. **Quản trị viên (Admin)**
   - Quản lý toàn bộ dữ liệu danh mục: Môn học, Học kỳ, Ngành học, Chương trình đào tạo.
   - Quản lý và cấp phát tài khoản cho Sinh viên, Giảng viên và Quản lý.

## ⚙️ Logic Nghiệp Vụ Cốt Lõi

Chức năng **Đăng ký tín chỉ** của sinh viên là trái tim của hệ thống, được xử lý chặt chẽ qua 7 bước kiểm tra ràng buộc:
1. Kiểm tra **Thời gian đăng ký** (trong thời hạn học kỳ mở đăng ký).
2. Kiểm tra **Trạng thái lớp** (chỉ cho phép đăng ký lớp đang MỞ).
3. Kiểm tra **Trùng lớp / Trùng môn** (không được đăng ký 2 lớp của cùng 1 môn trong 1 kỳ).
4. Kiểm tra **Môn tiên quyết** (phải hoàn thành môn tiên quyết trước).
5. Kiểm tra **Số lượng tín chỉ** (không vượt quá số tín chỉ tối đa của học kỳ).
6. Kiểm tra **Xung đột lịch học** (thuật toán kiểm tra giao thoa tiết học và ngày học).
7. Kiểm tra **Sĩ số** bằng cơ chế **Optimistic Locking** (`@Version`) ở Database để xử lý bài toán Race Condition (Nhiều SV tranh nhau slot cuối cùng trong cùng 1 mili-giây).

## 🛠️ Hướng Dẫn Cài Đặt và Chạy Project

### 1. Cài đặt Cơ Sở Dữ Liệu
Hệ thống cung cấp sẵn script để nạp cơ sở dữ liệu và dữ liệu mẫu đầy đủ.
- Tạo một database trong MySQL tên là `quan_ly_hoc_phan`.
- Mở thư mục `src/main/resources/db/`.
- Chạy file `schema.sql` để tạo toàn bộ cấu trúc bảng và khóa ngoại.
- Chạy file `data_mau.sql` để nạp dữ liệu mẫu (có sẵn định hướng, lớp, lịch học, lịch thi, sinh viên,...).
- *(Lưu ý: Nếu sau này bạn muốn reset dữ liệu về nguyên bản, chỉ cần chạy file `reset.sql`)*.

### 2. Cấu hình Ứng dụng
Mở file `src/main/resources/application.properties` và sửa thông tin đăng nhập Database của bạn:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/quan_ly_hoc_phan?useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=mat_khau_cua_ban
```

### 3. Chạy Ứng dụng
Sử dụng Maven để chạy:
```bash
mvn spring-boot:run
```
Truy cập vào ứng dụng qua trình duyệt: `http://localhost:8080`

## 🔑 Tài Khoản Truy Cập Mẫu

Tất cả các tài khoản demo trong `data_mau.sql` đều dùng chung mật khẩu là: **`123456`**

- **Admin**: `admin`
- **Quản lý**: `QL001` (Nguyễn Văn Quản)
- **Giảng viên**: `GV001` (Nguyễn Văn An)
- **Sinh viên**: `SV001`, `SV002`, `SV003`,...
