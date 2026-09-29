@echo off
chcp 65001 >nul
setlocal
cd /d %~dp0\..

echo ========================================
echo   构建零食自助售货系统 - 免安装绿色版
echo ========================================
echo.

rem 要求本机已安装 JDK17+、Maven、Node（仅构建时需要，使用者无需安装）
where java >nul 2>nul || (echo [错误] 未找到 java，请先安装 JDK 17+ && pause && exit /b 1)
where mvn  >nul 2>nul || (echo [错误] 未找到 mvn，请先安装 Maven 3.9+ && pause && exit /b 1)
where node >nul 2>nul || (echo [错误] 未找到 node，请先安装 Node.js 20+ && pause && exit /b 1)

echo [1/5] 构建前端...
pushd frontend
call npm install || (echo [错误] 前端依赖安装失败 && popd && exit /b 1)
call npm run build || (echo [错误] 前端构建失败 && popd && exit /b 1)
popd

echo [2/5] 复制前端产物到后端 static...
if not exist "server\src\main\resources\static" mkdir "server\src\main\resources\static"
xcopy /e /y /q "frontend\dist\*" "server\src\main\resources\static\" >nul

echo [3/5] 打包后端 JAR...
pushd server
call mvn clean package -DskipTests -B -q || (echo [错误] JAR 打包失败 && popd && exit /b 1)
popd

echo [4/5] 生成内置精简 JRE...
if exist "dist-portable\runtime" rmdir /s /q "dist-portable\runtime"
if not exist "dist-portable" mkdir "dist-portable"
jlink --add-modules java.base,java.compiler,java.datatransfer,java.desktop,java.instrument,java.logging,java.management,java.naming,java.net.http,java.prefs,java.scripting,java.security.jgss,java.security.sasl,java.sql,java.sql.rowset,java.transaction.xa,java.xml,jdk.crypto.cryptoki,jdk.crypto.ec,jdk.jfr,jdk.unsupported,jdk.zipfs --output "dist-portable\runtime" --strip-debug --no-header-files --no-man-pages --compress=2

echo [5/5] 组装绿色版目录...
copy /y "server\target\snack-vending-server.jar" "dist-portable\snack-vending-server.jar" >nul
if not exist "dist-portable\start.bat" copy /y "scripts\start-template.bat" "dist-portable\start.bat" >nul
if exist "dist-portable\data" rmdir /s /q "dist-portable\data"

echo.
echo ========================================
echo   构建完成！产物在 dist-portable 目录
echo   双击 start.bat 即可运行，可直接压缩分发
echo ========================================
pause
