# ===== 零食自助售货系统 - 全栈项目 =====

基于 Vue3 + Spring Boot 3 + MySQL/H2 的自助售货系统全栈实现，适配 1080P 横屏触控售货机场景。

## 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue3 + Vite + Element Plus + Pinia + Vue Router + Axios + QRCode |
| 后端 | Spring Boot 3.2 + Spring Data JPA + Lombok + SpringDoc OpenAPI |
| 数据库 | H2（开发，零配置）/ MySQL 8（生产） |
| 部署 | Docker + Docker Compose + Nginx |
| 构建 | Maven 3.9 + JDK 17 + Node 20 |

## 项目结构

```
snack-vending/
├── frontend/              # Vue3 前端
│   ├── src/
│   │   ├── api/           # 接口封装
│   │   ├── components/    # 组件（Header/购物车/商品卡片）
│   │   ├── views/         # 页面（首页/商品/订单确认/支付/订单中心）
│   │   ├── store/         # Pinia 状态管理
│   │   ├── router/        # 路由
│   │   └── styles/        # 全局样式
│   ├── Dockerfile
│   └── nginx.conf
├── server/                # Spring Boot 后端
│   ├── src/main/java/com/snackvending/
│   │   ├── controller/    # REST 控制器（7类接口）
│   │   ├── service/       # 业务逻辑 + 数据初始化
│   │   ├── entity/        # JPA 实体
│   │   ├── repository/    # 数据访问层
│   │   ├── dto/           # 响应封装
│   │   └── config/        # CORS/异常处理
│   ├── src/main/resources/
│   │   ├── application.yml       # 开发配置（H2）
│   │   └── application-prod.yml  # 生产配置（MySQL）
│   └── Dockerfile
├── docker-compose.yml     # 一键部署（MySQL + 后端 + 前端Nginx）
├── package.json           # 根目录脚本编排
└── .env.example           # 环境变量示例
```

## 快速开始

### 方式零：免安装绿色版（发给别人直接用，推荐）

已构建好单文件一体化程序包：**零食自助售货系统-免安装版.zip**（约 80MB）。

```
解压后的目录：
├── start.bat                    双击启动
├── 使用说明.txt
├── snack-vending-server.jar    系统本体（前端+后端+数据库一体）
└── runtime/                     内置 Java 运行环境
```

- 对方电脑**无需安装 Java、Node、数据库**，解压后双击 `start.bat`，约10秒浏览器自动打开 `http://localhost:8080`
- 数据保存在程序自动生成的 `data/` 目录，删除即重置
- 要求：Windows 10/11 64位
- 重新构建绿色版（代码更新后）：本机装好 JDK17/Maven/Node 后双击 `scripts/build-portable.bat`

### 方式一：本地开发（前后端分离）

**前置条件**：JDK 17+、Maven 3.9+、Node 20+

```bash
# 1. 启动后端（H2 数据库，自动初始化种子数据）
cd server
mvn spring-boot:run
# 后端运行在 http://localhost:8080/api
# API 文档: http://localhost:8080/api/swagger-ui.html
# H2 控制台: http://localhost:8080/api/h2-console

# 2. 启动前端（新终端）
cd frontend
npm install
npm run dev
# 前端运行在 http://localhost:5173，自动代理 /api 到后端
```

**一键启动前后端**（需安装 concurrently）：
```bash
npm install
npm run dev
```

### 方式二：Docker 一键部署（生产）

**前置条件**：Docker + Docker Compose

```bash
# 复制环境变量配置
cp .env.example .env

# 构建并启动（MySQL + 后端 + 前端Nginx）
docker-compose up -d --build

# 访问
# 前端: http://localhost
# 后端API: http://localhost:8080/api
# API文档: http://localhost:8080/swagger-ui.html

# 停止
docker-compose down
```

## API 接口清单

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/categories | 商品分类列表 |
| GET | /api/products?category=all | 商品列表（按分类筛选） |
| GET | /api/products/{id} | 商品详情 |
| GET | /api/banners | 轮播公告 |
| GET | /api/device/status | 设备状态 |
| POST | /api/orders | 创建订单 |
| POST | /api/orders/{orderNo}/pay | 支付订单 |
| POST | /api/orders/{orderNo}/cancel | 取消/超时订单 |
| GET | /api/orders | 订单列表 |
| GET | /api/orders/{orderNo} | 订单详情 |

统一响应格式：`{ "code": 200, "message": "success", "data": ... }`

## 数据库说明

- **开发环境**：使用 H2 文件数据库（`./data/snack_vending.mv.db`），首次启动自动建表并导入22个商品、6个分类、3条轮播、1台设备的种子数据
- **生产环境**：使用 MySQL 8，通过 `SPRING_PROFILES_ACTIVE=prod` 启用，连接配置见 `.env`

## 功能模块

1. **首页**：欢迎横幅、操作指引、轮播公告、设备状态、快捷入口
2. **商品**：6大分类切换、商品网格、库存售罄置灰、一键加购
3. **购物车**：右下角悬浮、数量增减、单品删除、一键清空
4. **结算支付**：订单确认、QR码支付、5分钟超时、成功动画+自动回首页
5. **订单中心**：历史订单、4种状态（待支付/已支付/已完成/已超时）、继续支付

## 扩展预留

- 售货机硬件出货接口对接
- 会员、优惠券、秒杀活动
- 后台数据可视化大屏
