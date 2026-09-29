<template>
  <div class="orders page-container">
    <div class="orders-header">
      <h1 class="orders-title">
        <el-icon><Document /></el-icon>
        我的订单
      </h1>
      <div class="orders-actions">
        <el-button @click="fetchOrders" :loading="loading">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
        <el-button type="danger" @click="$router.push('/products')">
          <el-icon><ShoppingCart /></el-icon>
          继续选购
        </el-button>
      </div>
    </div>

    <div class="orders-body">
      <div v-if="loading" class="loading-wrapper">
        <el-icon class="is-loading" :size="48"><Loading /></el-icon>
      </div>

      <div v-else-if="orderList.length === 0" class="empty-state">
        <div class="empty-icon">📋</div>
        <div class="empty-text">暂无订单</div>
        <div class="empty-desc">还没有购买记录，快去选购吧~</div>
        <el-button type="danger" size="large" class="go-shop" @click="$router.push('/products')">
          去选购
        </el-button>
      </div>

      <div v-else class="order-list">
        <div
          v-for="order in orderList"
          :key="order.orderNo"
          class="order-card card"
        >
          <div class="order-card-header">
            <div class="order-meta">
              <span class="order-no">订单号：{{ order.orderNo }}</span>
              <span class="order-time">{{ order.createTime }}</span>
            </div>
            <el-tag
              :type="getTagType(order.status)"
              size="large"
              class="order-status"
            >
              {{ getStatusLabel(order.status) }}
            </el-tag>
          </div>

          <div class="order-card-body">
            <div class="order-items-preview">
              <div
                v-for="item in order.items"
                :key="item.id"
                class="mini-item"
              >
                <span class="mini-img">{{ item.image }}</span>
                <span class="mini-name">{{ item.name }}</span>
                <span class="mini-qty">x{{ item.qty }}</span>
              </div>
            </div>
            <div class="order-summary">
              <div class="summary-row">
                <span>共 {{ order.totalCount }} 件商品</span>
              </div>
              <div class="summary-amount">
                <span>合计：</span>
                <span class="amount">¥{{ order.totalAmount?.toFixed(2) }}</span>
              </div>
            </div>
          </div>

          <div class="order-card-footer">
            <div v-if="order.payTime" class="pay-time">
              支付时间：{{ order.payTime }}
            </div>
            <div class="footer-actions">
              <el-button
                v-if="order.status === 'pending'"
                type="danger"
                size="small"
                @click="goPay(order.orderNo)"
              >
                立即支付
              </el-button>
              <el-button
                v-if="order.status === 'paid' || order.status === 'completed'"
                type="success"
                size="small"
                @click="showPickupTip"
              >
                <el-icon><Box /></el-icon>
                取货指引
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderList, orderStatusMap } from '@/api'

const router = useRouter()
const orderList = ref([])
const loading = ref(true)

const fetchOrders = async () => {
  loading.value = true
  try {
    const res = await getOrderList()
    orderList.value = res.data
  } catch {
    orderList.value = []
    ElMessage.error('订单加载失败')
  } finally {
    loading.value = false
  }
}

const getStatusLabel = (status) => orderStatusMap[status]?.label || '未知'
const getTagType = (status) => orderStatusMap[status]?.type || 'info'

const goPay = (orderNo) => {
  router.push({ path: '/order/payment', query: { orderNo } })
}

const showPickupTip = () => {
  ElMessageBox.alert(
    '请前往售货机下方取货口，按下取货按钮，等待商品出货后取出。',
    '取货指引',
    { confirmButtonText: '我知道了', type: 'info' }
  )
}

onMounted(fetchOrders)
</script>

<style scoped>
.orders {
  padding: 24px 60px 30px;
  gap: 20px;
  overflow: hidden;
}

.orders-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-shrink: 0;
}

.orders-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 32px;
  font-weight: 700;
  color: #303133;
}

.orders-actions {
  display: flex;
  gap: 12px;
}

.orders-body {
  flex: 1;
  overflow-y: auto;
  padding-right: 8px;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-card {
  padding: 24px 28px;
}

.order-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 18px;
  border-bottom: 2px solid #f0f2f5;
  margin-bottom: 18px;
}

.order-meta {
  display: flex;
  gap: 28px;
  font-size: 18px;
  color: #606266;
}

.order-no {
  font-weight: 600;
  color: #303133;
}

.order-status {
  font-size: 18px !important;
  padding: 8px 20px !important;
  height: auto !important;
  border-radius: 20px !important;
}

.order-card-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 18px;
}

.order-items-preview {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  flex: 1;
}

.mini-item {
  display: flex;
  align-items: center;
  gap: 6px;
  background: #f7f8fa;
  padding: 8px 14px;
  border-radius: 20px;
  font-size: 16px;
}

.mini-img {
  font-size: 24px;
}

.mini-name {
  max-width: 140px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.mini-qty {
  color: #ee5a52;
  font-weight: 600;
}

.order-summary {
  text-align: right;
  flex-shrink: 0;
  margin-left: 20px;
}

.summary-row {
  font-size: 16px;
  color: #909399;
  margin-bottom: 6px;
}

.summary-amount {
  font-size: 18px;
  color: #606266;
}

.summary-amount .amount {
  font-size: 28px;
  color: #ee5a52;
  font-weight: 700;
}

.order-card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px dashed #ebeef5;
}

.pay-time {
  font-size: 16px;
  color: #909399;
}

.footer-actions {
  display: flex;
  gap: 10px;
}

.go-shop {
  margin-top: 24px;
  height: 56px !important;
  padding: 0 48px !important;
  font-size: 22px !important;
  border-radius: 30px !important;
}
</style>
