<template>
  <div class="order-confirm page-container">
    <div class="confirm-header">
      <el-button text @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        返回继续选购
      </el-button>
      <h1 class="confirm-title">订单确认</h1>
      <div style="width: 160px;"></div>
    </div>

    <div class="confirm-body">
      <!-- 商品清单 -->
      <div class="order-items card">
        <div class="card-title">
          <el-icon><ShoppingCart /></el-icon>
          <span>商品清单</span>
          <span class="item-count">共 {{ cart.totalCount }} 件</span>
        </div>
        <div class="items-list">
          <div
            v-for="item in cart.items"
            :key="item.id"
            class="order-item"
          >
            <div class="item-img">{{ item.image }}</div>
            <div class="item-info">
              <div class="item-name">{{ item.name }}</div>
              <div class="item-price">¥{{ item.price.toFixed(2) }}</div>
            </div>
            <div class="item-qty">x{{ item.qty }}</div>
            <div class="item-subtotal">¥{{ (item.price * item.qty).toFixed(2) }}</div>
          </div>
        </div>
      </div>

      <!-- 订单信息 -->
      <div class="order-info card">
        <div class="card-title">
          <el-icon><Document /></el-icon>
          <span>订单信息</span>
        </div>
        <div class="info-rows">
          <div class="info-row">
            <span class="info-label">订单编号</span>
            <span class="info-value">{{ orderNo || '生成中...' }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">下单时间</span>
            <span class="info-value">{{ orderTime || '--' }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">取货方式</span>
            <span class="info-value">自助取货口取货</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部结算栏 -->
    <div class="confirm-footer">
      <div class="footer-left">
        <div class="price-row">
          <span>商品总额：</span>
          <span class="price">¥{{ cart.totalPrice.toFixed(2) }}</span>
        </div>
        <div class="price-row">
          <span>优惠：</span>
          <span class="price discount">-¥0.00</span>
        </div>
      </div>
      <div class="footer-right">
        <div class="pay-total">
          <span class="pay-label">应付金额：</span>
          <span class="pay-amount">¥{{ cart.totalPrice.toFixed(2) }}</span>
        </div>
        <el-button
          type="danger"
          size="large"
          class="submit-btn"
          :loading="submitting"
          @click="submitOrder"
        >
          提交订单
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useCartStore } from '@/store/cart'
import { createOrder } from '@/api'

const router = useRouter()
const cart = useCartStore()
const orderNo = ref('')
const orderTime = ref('')
const submitting = ref(false)

const goBack = () => {
  router.push('/products')
}

const submitOrder = async () => {
  if (cart.totalCount === 0) {
    ElMessage.warning('购物车为空，请先选购商品')
    return
  }
  submitting.value = true
  try {
    const res = await createOrder({
      items: JSON.parse(JSON.stringify(cart.items)),
      totalAmount: cart.totalPrice,
      totalCount: cart.totalCount
    })
    orderNo.value = res.data.orderNo
    orderTime.value = res.data.createTime
    ElMessage.success('订单创建成功，请支付')
    setTimeout(() => {
      router.push({ path: '/order/payment', query: { orderNo: res.data.orderNo } })
    }, 500)
  } catch (e) {
    ElMessage.error('订单创建失败，请重试')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  if (cart.totalCount === 0) {
    ElMessage.warning('购物车为空，请先选购商品')
    router.push('/products')
  }
})
</script>

<style scoped>
.order-confirm {
  padding: 24px 60px 0;
}

.confirm-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.confirm-title {
  font-size: 32px;
  font-weight: 700;
  color: #303133;
}

.confirm-body {
  flex: 1;
  display: grid;
  grid-template-columns: 1.5fr 1fr;
  gap: 24px;
  overflow: hidden;
}

.card {
  padding: 24px 28px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 22px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 2px solid #f0f2f5;
}

.item-count {
  margin-left: auto;
  font-size: 16px;
  color: #909399;
  font-weight: 400;
}

.items-list {
  flex: 1;
  overflow-y: auto;
  padding-right: 8px;
}

.order-item {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #f7f8fa;
  border-radius: 12px;
  margin-bottom: 12px;
}

.item-img {
  width: 64px;
  height: 64px;
  border-radius: 10px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  margin-right: 16px;
  flex-shrink: 0;
}

.item-info {
  flex: 1;
}

.item-name {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 6px;
}

.item-price {
  font-size: 16px;
  color: #909399;
}

.item-qty {
  font-size: 20px;
  color: #606266;
  margin: 0 30px;
}

.item-subtotal {
  font-size: 22px;
  color: #ee5a52;
  font-weight: 700;
  width: 120px;
  text-align: right;
}

.info-rows {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 20px;
}

.info-label {
  color: #909399;
}

.info-value {
  font-weight: 600;
  color: #303133;
}

.confirm-footer {
  height: 110px;
  flex-shrink: 0;
  background: #fff;
  border-radius: 20px 20px 0 0;
  margin: 0 -60px;
  padding: 0 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 -4px 16px rgba(0, 0, 0, 0.06);
}

.footer-left {
  display: flex;
  gap: 40px;
}

.price-row {
  font-size: 18px;
  color: #606266;
}

.price {
  font-size: 22px;
  font-weight: 600;
  color: #303133;
}

.price.discount {
  color: #67c23a;
}

.footer-right {
  display: flex;
  align-items: center;
  gap: 30px;
}

.pay-total {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.pay-label {
  font-size: 22px;
  color: #606266;
}

.pay-amount {
  font-size: 44px;
  color: #ee5a52;
  font-weight: 700;
}

.submit-btn {
  height: 64px !important;
  padding: 0 60px !important;
  font-size: 24px !important;
  border-radius: 40px !important;
}
</style>
