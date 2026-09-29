<template>
  <div class="payment page-container">
    <!-- 支付中状态 -->
    <div v-if="status === 'paying'" class="paying-wrapper">
      <div class="paying-content">
        <div class="paying-header">
          <h1>扫码支付</h1>
          <p>请使用微信/支付宝扫描下方二维码完成支付</p>
        </div>

        <div class="paying-main">
          <!-- 二维码 -->
          <div class="qr-section">
            <div class="qr-box">
              <canvas ref="qrCanvas" class="qr-canvas"></canvas>
              <div class="qr-overlay" v-if="qrLoading">
                <el-icon class="is-loading" :size="40"><Loading /></el-icon>
              </div>
            </div>
            <div class="qr-amount">
              <span class="amount-label">支付金额</span>
              <span class="amount-value">¥{{ order.totalAmount?.toFixed(2) }}</span>
            </div>
            <div class="countdown" :class="{ warn: remaining <= 60 }">
              <el-icon><Timer /></el-icon>
              <span>支付剩余时间 {{ formatTime(remaining) }}</span>
            </div>
          </div>

          <!-- 订单信息 -->
          <div class="order-section card">
            <div class="card-title">
              <el-icon><Document /></el-icon>
              <span>订单信息</span>
            </div>
            <div class="order-detail">
              <div class="detail-row">
                <span class="detail-label">订单编号</span>
                <span class="detail-value">{{ order.orderNo }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">下单时间</span>
                <span class="detail-value">{{ order.createTime }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">商品数量</span>
                <span class="detail-value">{{ order.totalCount }} 件</span>
              </div>
              <div class="detail-row total">
                <span class="detail-label">应付金额</span>
                <span class="detail-value amount">¥{{ order.totalAmount?.toFixed(2) }}</span>
              </div>
            </div>

            <div class="pay-actions">
              <el-button class="mock-pay-btn" type="success" size="large" @click="mockPay">
                <el-icon><Check /></el-icon>
                模拟支付成功
              </el-button>
              <el-button class="cancel-btn" text @click="cancelOrder">取消支付</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 支付成功 -->
    <div v-else-if="status === 'success'" class="success-wrapper scale-in">
      <div class="success-card">
        <div class="success-icon">
          <div class="success-circle">
            <el-icon :size="90"><CircleCheck /></el-icon>
          </div>
        </div>
        <h1 class="success-title">支付成功</h1>
        <p class="success-desc">请前往取货口取商品</p>
        <div class="pickup-hint">
          <el-icon :size="32"><Box /></el-icon>
          <span>取货口正在出货，请稍候...</span>
        </div>
        <div class="success-amount">
          已支付 <span>¥{{ order.totalAmount?.toFixed(2) }}</span>
        </div>
        <div class="auto-back">{{ backCount }}秒后自动返回首页</div>
      </div>
    </div>

    <!-- 支付失败/超时 -->
    <div v-else-if="status === 'failed' || status === 'timeout'" class="failed-wrapper scale-in">
      <div class="failed-card">
        <div class="failed-icon">
          <el-icon :size="90" color="#f56c6c"><CircleClose /></el-icon>
        </div>
        <h1 class="failed-title">{{ status === 'timeout' ? '支付超时' : '支付失败' }}</h1>
        <p class="failed-desc">
          {{ status === 'timeout' ? '订单已自动取消，请重新下单' : '支付过程出现异常，请重试' }}
        </p>
        <div class="failed-actions">
          <el-button type="danger" size="large" @click="$router.push('/products')">
            重新选购
          </el-button>
          <el-button size="large" @click="$router.push('/orders')">
            查看订单
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import QRCode from 'qrcode'
import { payOrder, getOrderDetail, cancelOrder as cancelOrderApi } from '@/api'
import { useCartStore } from '@/store/cart'

const route = useRoute()
const router = useRouter()
const cart = useCartStore()

const orderNo = route.query.orderNo
const order = ref({})
const status = ref('paying') // paying | success | failed | timeout
const remaining = ref(300) // 5分钟
const qrCanvas = ref(null)
const qrLoading = ref(true)
const backCount = ref(3)

let countdownTimer = null
let backTimer = null

const formatTime = (s) => {
  const m = Math.floor(s / 60)
  const sec = s % 60
  return `${String(m).padStart(2, '0')}:${String(sec).padStart(2, '0')}`
}

const generateQR = async () => {
  qrLoading.value = true
  try {
    const qrText = `snackvending://pay?orderNo=${orderNo}&amount=${order.value.totalAmount}`
    await QRCode.toCanvas(qrCanvas.value, qrText, {
      width: 280,
      margin: 1,
      color: { dark: '#303133', light: '#ffffff' }
    })
  } catch (e) {
    console.error('QR生成失败', e)
  } finally {
    qrLoading.value = false
  }
}

const startCountdown = () => {
  countdownTimer = setInterval(() => {
    remaining.value--
    if (remaining.value <= 0) {
      handleTimeout()
    }
  }, 1000)
}

const handleTimeout = async () => {
  clearInterval(countdownTimer)
  status.value = 'timeout'
  try {
    await cancelOrderApi(orderNo)
  } catch (e) {
    // ignore
  }
}

const mockPay = async () => {
  clearInterval(countdownTimer)
  try {
    await payOrder(orderNo)
    status.value = 'success'
    cart.clearCart()
    startBackCountdown()
  } catch (e) {
    status.value = 'failed'
    ElMessage.error('支付失败')
  }
}

const cancelOrder = async () => {
  clearInterval(countdownTimer)
  try {
    await cancelOrderApi(orderNo)
    status.value = 'failed'
  } catch (e) {
    // ignore
  }
}

const startBackCountdown = () => {
  backCount.value = 3
  backTimer = setInterval(() => {
    backCount.value--
    if (backCount.value <= 0) {
      clearInterval(backTimer)
      router.push('/')
    }
  }, 1000)
}

const fetchOrder = async () => {
  try {
    const res = await getOrderDetail(orderNo)
    order.value = res.data || {}
    await generateQR()
    startCountdown()
  } catch (e) {
    ElMessage.error('订单加载失败')
    router.push('/products')
  }
}

onMounted(fetchOrder)

onUnmounted(() => {
  clearInterval(countdownTimer)
  clearInterval(backTimer)
})
</script>

<style scoped>
.payment {
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #fef0f0 0%, #fff5f5 100%);
}

/* ===== 支付中 ===== */
.paying-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.paying-content {
  width: 1200px;
}

.paying-header {
  text-align: center;
  margin-bottom: 36px;
}

.paying-header h1 {
  font-size: 42px;
  color: #303133;
  margin-bottom: 12px;
}

.paying-header p {
  font-size: 22px;
  color: #909399;
}

.paying-main {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
}

.qr-section {
  background: #fff;
  border-radius: 24px;
  padding: 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
}

.qr-box {
  position: relative;
  width: 320px;
  height: 320px;
  border: 12px solid #f7f8fa;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 28px;
}

.qr-canvas {
  width: 280px;
  height: 280px;
}

.qr-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  border-radius: 8px;
}

.qr-amount {
  text-align: center;
  margin-bottom: 20px;
}

.amount-label {
  display: block;
  font-size: 18px;
  color: #909399;
  margin-bottom: 8px;
}

.amount-value {
  font-size: 48px;
  color: #ee5a52;
  font-weight: 700;
}

.countdown {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  color: #606266;
  padding: 10px 24px;
  background: #f0f9eb;
  border-radius: 30px;
}

.countdown.warn {
  background: #fef0f0;
  color: #f56c6c;
  animation: shake 0.5s ease infinite;
}

/* ===== 订单信息 ===== */
.order-section {
  padding: 32px;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 22px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid #f0f2f5;
}

.order-detail {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 32px;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  font-size: 20px;
}

.detail-label {
  color: #909399;
}

.detail-value {
  font-weight: 600;
  color: #303133;
}

.detail-row.total .detail-value.amount {
  color: #ee5a52;
  font-size: 28px;
}

.pay-actions {
  display: flex;
  flex-direction: column;
  gap: 14px;
  align-items: center;
}

.mock-pay-btn {
  width: 100%;
  height: 60px !important;
  font-size: 22px !important;
  border-radius: 30px !important;
}

.cancel-btn {
  font-size: 18px !important;
  color: #909399 !important;
}

/* ===== 成功 ===== */
.success-wrapper,
.failed-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.success-card,
.failed-card {
  background: #fff;
  border-radius: 28px;
  padding: 60px 80px;
  text-align: center;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.1);
}

.success-icon {
  margin-bottom: 28px;
}

.success-circle {
  width: 140px;
  height: 140px;
  border-radius: 50%;
  background: #f0f9eb;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
  color: #67c23a;
  animation: bounce 0.6s ease;
}

.success-title {
  font-size: 40px;
  color: #67c23a;
  margin-bottom: 16px;
}

.success-desc {
  font-size: 26px;
  color: #303133;
  margin-bottom: 24px;
}

.pickup-hint {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  font-size: 20px;
  color: #606266;
  padding: 16px 28px;
  background: #ecf5ff;
  border-radius: 16px;
  margin-bottom: 28px;
}

.success-amount {
  font-size: 22px;
  color: #909399;
  margin-bottom: 20px;
}

.success-amount span {
  font-size: 32px;
  color: #ee5a52;
  font-weight: 700;
}

.auto-back {
  font-size: 16px;
  color: #c0c4cc;
}

/* ===== 失败 ===== */
.failed-icon {
  margin-bottom: 28px;
}

.failed-title {
  font-size: 40px;
  color: #f56c6c;
  margin-bottom: 16px;
}

.failed-desc {
  font-size: 22px;
  color: #606266;
  margin-bottom: 36px;
}

.failed-actions {
  display: flex;
  gap: 20px;
  justify-content: center;
}

.failed-actions .el-button {
  height: 56px !important;
  padding: 0 40px !important;
  font-size: 20px !important;
  border-radius: 30px !important;
}
</style>
