<template>
  <div
    class="product-card"
    :class="{ soldout: product.stock === 0 }"
  >
    <div class="product-img">
      <span class="img-emoji">{{ product.image }}</span>
      <div v-if="product.stock === 0" class="soldout-mask">
        <span>已售罄</span>
      </div>
    </div>
    <div class="product-info">
      <div class="product-name">{{ product.name }}</div>
      <div class="product-desc">{{ product.description }}</div>
      <div class="product-bottom">
        <div class="product-price">
          <span class="currency">¥</span>
          <span class="amount">{{ product.price.toFixed(2) }}</span>
        </div>
        <div class="product-stock">库存：{{ product.stock }}</div>
      </div>
      <div class="add-area">
        <button
          v-if="product.stock > 0"
          class="add-btn"
          @click="handleAdd"
        >
          <el-icon><Plus /></el-icon>
          加入购物车
        </button>
        <button v-else class="add-btn disabled" disabled>
          <el-icon><Warning /></el-icon>
          已售罄
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { useCartStore } from '@/store/cart'

const props = defineProps({
  product: {
    type: Object,
    required: true
  }
})

const cart = useCartStore()

const handleAdd = () => {
  if (props.product.stock === 0) return
  const success = cart.addItem(props.product)
  if (success) {
    ElMessage.success(`已加入：${props.product.name}`)
  } else {
    ElMessage.warning('库存不足，无法继续添加')
  }
}
</script>

<style scoped>
.product-card {
  background: #fff;
  border-radius: 18px;
  overflow: hidden;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  cursor: pointer;
  display: flex;
  flex-direction: column;
}

.product-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 10px 28px rgba(0, 0, 0, 0.12);
}

.product-card.soldout {
  opacity: 0.6;
  cursor: not-allowed;
}

.product-card.soldout:hover {
  transform: none;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
}

.product-img {
  height: 200px;
  background: linear-gradient(135deg, #fff5f5 0%, #ffe8e8 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.img-emoji {
  font-size: 100px;
}

.soldout-mask {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
}

.soldout-mask span {
  color: #fff;
  font-size: 32px;
  font-weight: 700;
  border: 3px solid #fff;
  padding: 8px 20px;
  border-radius: 10px;
  transform: rotate(-15deg);
}

.product-info {
  padding: 18px 20px 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 22px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-desc {
  font-size: 15px;
  color: #909399;
  margin-bottom: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-bottom {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  margin-bottom: 14px;
}

.product-price {
  display: flex;
  align-items: baseline;
  color: #ee5a52;
}

.currency {
  font-size: 18px;
  font-weight: 600;
}

.amount {
  font-size: 30px;
  font-weight: 700;
}

.product-stock {
  font-size: 15px;
  color: #909399;
}

.add-area {
  margin-top: auto;
}

.add-btn {
  width: 100%;
  height: 52px;
  border: none;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
  color: #fff;
  border-radius: 26px;
  font-size: 20px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.2s;
}

.add-btn:active {
  transform: scale(0.95);
}

.add-btn.disabled {
  background: #c0c4cc;
  cursor: not-allowed;
}
</style>
