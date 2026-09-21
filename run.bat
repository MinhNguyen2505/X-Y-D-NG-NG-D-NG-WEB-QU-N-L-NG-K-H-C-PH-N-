@echo off
title Server Quan Ly Hoc Phan
color 0A

echo ========================================================
echo      KHOI DONG HE THONG QUAN LY DANG KY HOC PHAN
echo ========================================================
echo.
echo [*] Dang bien dich va khoi dong Spring Boot...
echo [*] Vui long doi 15-30 giay.
echo [*] Khi nao thay dong "Started QuanLyHocPhanApplication",
echo     hay mo trinh duyet va truy cap: http://localhost:8080
echo.
echo [*] An Ctrl + C de tat server khi khong dung nua.
echo ========================================================
echo.

call mvn spring-boot:run

pause
