<template>
  <!-- 购物车悬浮按钮 -->
  <div
    v-if="cart.totalCount > 0"
    class="cart-floating scale-in"
    @click="openCart"
  >
    <div class="cart-icon">
      <el-icon :size="40"><ShoppingCart /></el-icon>
      <span class="cart-badge">{{ cart.totalCount }}</span>
    </div>
    <div class="cart-info">
      <div class="cart-total">¥{{ cart.totalPrice.toFixed(2) }}</div>
      <div class="cart-tip">去结算</div>
    </div>
  </div>

  <!-- 购物车详情弹窗 -->
  <el-dialog
    v-model="dialogVisible"
    title="购物车"
    width="900px"
    :close-on-click-modal="false"
    class="cart-dialog"
  >
    <div v-if="cart.items.length === 0" class="empty-state">
      <div class="empty-icon">🛒</div>
      <div class="empty-text">购物车空空如也</div>
      <div class="empty-desc">快去挑选喜欢的零食吧~</div>
    </div>

    <div v-else class="cart-list">
      <div
        v-for="item in cart.items"
        :key="item.id"
        class="cart-item"
      >
        <div class="cart-item-img">{{ item.image }}</div>
        <div class="cart-item-info">
          <div class="cart-item-name">{{ item.name }}</div>
          <div class="cart-item-price">¥{{ item.price.toFixed(2) }}</div>
        </div>
        <div class="cart-item-actions">
          <div class="qty-control">
            <button class="qty-btn" @click="cart.decreaseItem(item.id)">
              <el-icon><Minus /></el-icon>
            </button>
            <span class="qty-num">{{ item.qty }}</span>
            <button
              class="qty-btn"
              :class="{ disabled: item.qty >= item.stock }"
              @click="handleIncrease(item)"
            >
              <el-icon><Plus /></el-icon>
            </button>
          </div>
          <button class="remove-btn" @click="cart.removeItem(item.id)">
            <el-icon><Delete /></el-icon>
          </button>
        </div>
      </div>
    </div>

    <template #footer>
      <div class="cart-footer">
        <div class="cart-footer-left">
          <span class="clear-btn" @click="handleClear">
            <el-icon><Delete /></el-icon> 清空购物车
          </span>
        </div>
        <div class="cart-footer-right">
          <span class="total-label">合计：</span>
          <span class="total-amount">¥{{ cart.totalPrice.toFixed(2) }}</span>
          <el-button
            type="danger"
            size="large"
            class="checkout-btn"
            @click="goCheckout"
          >
            去结算 ({{ cart.totalCount }})
          </el-button>
        </div>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useCartStore } from '@/store/cart'

const cart = useCartStore()
const router = useRouter()
const dialogVisible = ref(false)

const openCart = () => {
  dialogVisible.value = true
}

const handleIncrease = (item) => {
  if (item.qty >= item.stock) {
    ElMessage.warning('库存不足')
    return
  }
  cart.addItem({ id: item.id, name: item.name, price: item.price, image: item.image, stock: item.stock })
}

const handleClear = () => {
  ElMessageBox.confirm('确定要清空购物车吗？', '提示', {
    confirmButtonText: '确定清空',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      cart.clearCart()
      ElMessage.success('购物车已清空')
    })
    .catch(() => {})
}

const goCheckout = () => {
  if (cart.totalCount === 0) {
    ElMessage.warning('请先选购商品')
    return
  }
  dialogVisible.value = false
  router.push('/order/confirm')
}
</script>

<style scoped>
.cart-floating {
  position: fixed;
  right: 60px;
  bottom: 60px;
  width: 220px;
  height: 100px;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
  border-radius: 60px;
  display: flex;
  align-items: center;
  padding: 0 12px 0 12px;
  cursor: pointer;
  box-shadow: 0 10px 30px rgba(238, 90, 82, 0.4);
  z-index: 999;
  transition: all 0.3s ease;
}

.cart-floating:hover {
  transform: translateY(-4px) scale(1.03);
  box-shadow: 0 14px 36px rgba(238, 90, 82, 0.5);
}

.cart-icon {
  position: relative;
  width: 76px;
  height: 76px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.25);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-right: 8px;
  flex-shrink: 0;
}

.cart-badge {
  position: absolute;
  top: -4px;
  right: -4px;
  min-width: 28px;
  height: 28px;
  line-height: 28px;
  padding: 0 8px;
  background: #fff;
  color: #ee5a52;
  border-radius: 14px;
  font-size: 16px;
  font-weight: 700;
  text-align: center;
}

.cart-info {
  flex: 1;
  color: #fff;
}

.cart-total {
  font-size: 26px;
  font-weight: 700;
}

.cart-tip {
  font-size: 16px;
  opacity: 0.9;
}

.cart-list {
  max-height: 560px;
  overflow-y: auto;
  padding-right: 8px;
}

.cart-item {
  display: flex;
  align-items: center;
  padding: 20px;
  background: #f7f8fa;
  border-radius: 14px;
  margin-bottom: 14px;
}

.cart-item-img {
  width: 80px;
  height: 80px;
  border-radius: 12px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48px;
  margin-right: 20px;
  flex-shrink: 0;
}

.cart-item-info {
  flex: 1;
}

.cart-item-name {
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 8px;
}

.cart-item-price {
  font-size: 20px;
  color: #ee5a52;
  font-weight: 600;
}

.cart-item-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.qty-control {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 24px;
  padding: 4px;
}

.qty-btn {
  width: 44px;
  height: 44px;
  border: none;
  background: #f0f2f5;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 20px;
  transition: all 0.2s;
}

.qty-btn:active {
  transform: scale(0.9);
}

.qty-btn.disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.qty-num {
  min-width: 48px;
  text-align: center;
  font-size: 22px;
  font-weight: 600;
}

.remove-btn {
  width: 44px;
  height: 44px;
  border: none;
  background: #fef0f0;
  color: #f56c6c;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 18px;
}

.remove-btn:active {
  transform: scale(0.9);
}

.cart-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 12px;
}

.cart-footer-left {
  font-size: 18px;
  color: #909399;
  cursor: pointer;
}

.clear-btn {
  display: flex;
  align-items: center;
  gap: 6px;
}

.clear-btn:hover {
  color: #f56c6c;
}

.cart-footer-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.total-label {
  font-size: 22px;
  color: #606266;
}

.total-amount {
  font-size: 32px;
  color: #ee5a52;
  font-weight: 700;
}

.checkout-btn {
  height: 56px !important;
  padding: 0 40px !important;
  font-size: 22px !important;
  border-radius: 30px !important;
}
</style>
