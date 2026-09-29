@echo off
chcp 65001 >nul
title 零食自助售货系统
cd /d %~dp0

echo ========================================
echo    零食自助售货系统启动中...
echo.
echo    首次启动约需 10 秒，请耐心等待
echo    浏览器将自动打开，运行期间请勿关闭本窗口
echo ========================================
echo.

rem 10秒后自动用默认浏览器打开系统页面
start "" /b cmd /c "timeout /t 10 /nobreak >nul && start http://localhost:8080"

rem 使用内置JRE启动（无需在电脑上安装Java）
runtime\bin\java.exe -jar snack-vending-server.jar

echo.
echo 系统已停止运行。
pause
