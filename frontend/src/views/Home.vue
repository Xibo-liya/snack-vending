<template>
  <div class="home page-container">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <div class="welcome-left">
        <h1 class="welcome-title">
          <span class="wave">👋</span> 欢迎光临
        </h1>
        <p class="welcome-sub">自助选购 · 扫码支付 · 即取即走</p>
        <div class="welcome-steps">
          <div class="step">
            <div class="step-num">1</div>
            <div class="step-text">选择商品</div>
          </div>
          <div class="step-arrow">→</div>
          <div class="step">
            <div class="step-num">2</div>
            <div class="step-text">加入购物车</div>
          </div>
          <div class="step-arrow">→</div>
          <div class="step">
            <div class="step-num">3</div>
            <div class="step-text">扫码支付</div>
          </div>
          <div class="step-arrow">→</div>
          <div class="step">
            <div class="step-num">4</div>
            <div class="step-text">取货口取货</div>
          </div>
        </div>
        <el-button
          type="danger"
          size="large"
          class="start-btn"
          @click="$router.push('/products')"
        >
          <el-icon><ShoppingCart /></el-icon>
          开始选购
        </el-button>
      </div>
      <div class="welcome-right">
        <div class="mascot">🍿</div>
      </div>
    </div>

    <!-- 轮播公告 -->
    <div class="banner-section">
      <div class="section-title">
        <el-icon><Bell /></el-icon>
        <span>活动公告</span>
      </div>
      <el-carousel
        height="240px"
        :interval="4000"
        arrow="never"
        indicator-position="outside"
      >
        <el-carousel-item v-for="banner in banners" :key="banner.id">
          <div class="banner-item" :style="{ background: banner.bg }">
            <div class="banner-icon">{{ banner.icon }}</div>
            <div class="banner-text">
              <h2>{{ banner.title }}</h2>
              <p>{{ banner.desc }}</p>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 设备状态 + 快捷入口 -->
    <div class="info-section">
      <div class="device-card">
        <div class="info-card-title">
          <el-icon><Cpu /></el-icon>
          <span>设备状态</span>
        </div>
        <div class="device-status-detail">
          <div class="status-row">
            <span class="status-label">运行状态</span>
            <span class="status-value online">
              <span class="status-dot"></span>
              {{ device.status === 'normal' ? '正常营业' : '维护中' }}
            </span>
          </div>
          <div class="status-row">
            <span class="status-label">柜内温度</span>
            <span class="status-value">{{ device.temp }}</span>
          </div>
          <div class="status-row">
            <span class="status-label">设备编号</span>
            <span class="status-value">SV-2026-001</span>
          </div>
        </div>
      </div>

      <div class="quick-card">
        <div class="info-card-title">
          <el-icon><Lightning /></el-icon>
          <span>快捷入口</span>
        </div>
        <div class="quick-grid">
          <div class="quick-item" @click="$router.push('/products')">
            <div class="quick-icon">🛒</div>
            <div class="quick-text">选购商品</div>
          </div>
          <div class="quick-item" @click="$router.push('/orders')">
            <div class="quick-icon">📋</div>
            <div class="quick-text">我的订单</div>
          </div>
          <div class="quick-item" @click="showTip">
            <div class="quick-icon">❓</div>
            <div class="quick-text">使用帮助</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessageBox } from 'element-plus'
import { getBanners, getDeviceStatus } from '@/api'
import { useDeviceStore } from '@/store/device'

const banners = ref([])
const deviceStore = useDeviceStore()
const device = deviceStore.status

const fetchData = async () => {
  try {
    const [bannerRes, deviceRes] = await Promise.all([
      getBanners(),
      getDeviceStatus()
    ])
    banners.value = bannerRes.data
    deviceStore.setStatus(deviceRes.data)
  } catch (e) {
    console.error(e)
  }
}

const showTip = () => {
  ElMessageBox.alert(
    '1. 点击商品上的「加入购物车」按钮\n2. 选好后点击右下角购物车按钮\n3. 确认订单后扫码支付\n4. 支付成功后前往取货口取货',
    '使用帮助',
    {
      confirmButtonText: '我知道了',
      type: 'info'
    }
  )
}

onMounted(fetchData)
</script>

<style scoped>
.home {
  padding: 30px 60px;
  gap: 24px;
  overflow-y: auto;
}

.welcome-banner {
  background: linear-gradient(135deg, #ff6b6b 0%, #ff8e53 100%);
  border-radius: 24px;
  padding: 40px 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 8px 24px rgba(255, 107, 107, 0.3);
  color: #fff;
  position: relative;
  overflow: hidden;
}

.welcome-left {
  flex: 1;
}

.welcome-title {
  font-size: 52px;
  font-weight: 700;
  margin-bottom: 12px;
}

.wave {
  display: inline-block;
  animation: wave 2s infinite;
  transform-origin: 70% 70%;
}

@keyframes wave {
  0%, 100% { transform: rotate(0deg); }
  25% { transform: rotate(20deg); }
  75% { transform: rotate(-10deg); }
}

.welcome-sub {
  font-size: 24px;
  opacity: 0.9;
  margin-bottom: 32px;
}

.welcome-steps {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 36px;
  flex-wrap: wrap;
}

.step {
  display: flex;
  align-items: center;
  gap: 10px;
  background: rgba(255, 255, 255, 0.2);
  padding: 10px 20px;
  border-radius: 30px;
}

.step-num {
  width: 32px;
  height: 32px;
  line-height: 32px;
  text-align: center;
  background: #fff;
  color: #ee5a52;
  border-radius: 50%;
  font-weight: 700;
  font-size: 18px;
}

.step-text {
  font-size: 18px;
}

.step-arrow {
  font-size: 24px;
  opacity: 0.7;
}

.start-btn {
  height: 64px !important;
  padding: 0 56px !important;
  font-size: 26px !important;
  border-radius: 40px !important;
  background: #fff !important;
  color: #ee5a52 !important;
  border: none !important;
  font-weight: 700 !important;
}

.start-btn:active {
  transform: scale(0.95);
}

.welcome-right {
  flex-shrink: 0;
}

.mascot {
  font-size: 220px;
  animation: bounce 3s ease-in-out infinite;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-20px); }
}

.banner-section {
  background: #fff;
  border-radius: 20px;
  padding: 24px 30px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
}

.section-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 24px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 16px;
}

.banner-item {
  height: 100%;
  border-radius: 16px;
  display: flex;
  align-items: center;
  padding: 0 50px;
  gap: 30px;
  color: #fff;
}

.banner-icon {
  font-size: 100px;
}

.banner-text h2 {
  font-size: 36px;
  margin-bottom: 10px;
}

.banner-text p {
  font-size: 22px;
  opacity: 0.9;
}

.info-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

.device-card,
.quick-card {
  background: #fff;
  border-radius: 20px;
  padding: 28px 32px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
}

.info-card-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 22px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 20px;
}

.device-status-detail {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.status-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 20px;
}

.status-label {
  color: #909399;
}

.status-value {
  font-weight: 600;
  color: #303133;
}

.status-value.online {
  color: #67c23a;
  display: flex;
  align-items: center;
  gap: 8px;
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 28px 0;
  background: #f7f8fa;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.2s;
}

.quick-item:hover {
  background: #fff0f0;
  transform: translateY(-3px);
}

.quick-item:active {
  transform: scale(0.96);
}

.quick-icon {
  font-size: 56px;
  margin-bottom: 10px;
}

.quick-text {
  font-size: 18px;
  color: #606266;
  font-weight: 600;
}
</style>
