package vn.edu.quanlyhocphan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point cua ung dung Quan Ly Dang Ky Hoc Phan.
 *
 * @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
 */
@SpringBootApplication
public class QuanLyHocPhanApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuanLyHocPhanApplication.class, args);
    }
}
