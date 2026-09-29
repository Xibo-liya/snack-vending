<template>
  <div class="products page-container">
    <!-- 分类标签 -->
    <div class="category-bar">
      <div
        v-for="cat in categories"
        :key="cat.id"
        class="category-tab"
        :class="{ active: activeCategory === cat.id }"
        @click="switchCategory(cat.id)"
      >
        <el-icon><component :is="cat.icon" /></el-icon>
        <span>{{ cat.name }}</span>
      </div>
    </div>

    <!-- 商品列表 -->
    <div class="product-list-area">
      <div v-if="loading" class="loading-wrapper">
        <el-icon class="is-loading" :size="48"><Loading /></el-icon>
      </div>

      <div v-else-if="productList.length === 0" class="empty-state">
        <div class="empty-icon">📭</div>
        <div class="empty-text">暂无商品</div>
        <div class="empty-desc">该分类下暂无商品，敬请期待</div>
      </div>

      <div v-else class="product-grid">
        <ProductCard
          v-for="product in productList"
          :key="product.id"
          :product="product"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import ProductCard from '@/components/ProductCard.vue'
import { getCategories, getProducts } from '@/api'

const categories = ref([])
const productList = ref([])
const activeCategory = ref('all')
const loading = ref(true)

const fetchCategories = async () => {
  const res = await getCategories()
  categories.value = res.data
}

const fetchProducts = async (category = 'all') => {
  loading.value = true
  try {
    const res = await getProducts(category)
    productList.value = res.data
  } catch {
    productList.value = []
  } finally {
    loading.value = false
  }
}

const switchCategory = (id) => {
  if (activeCategory.value === id) return
  activeCategory.value = id
  fetchProducts(id)
}

onMounted(async () => {
  await fetchCategories()
  await fetchProducts()
})
</script>

<style scoped>
.products {
  padding: 24px 60px 30px;
  gap: 20px;
  overflow: hidden;
}

.category-bar {
  display: flex;
  gap: 14px;
  flex-shrink: 0;
}

.category-tab {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px 32px;
  background: #fff;
  border-radius: 30px;
  font-size: 20px;
  color: #606266;
  cursor: pointer;
  transition: all 0.25s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  font-weight: 600;
}

.category-tab:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.category-tab.active {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
  color: #fff;
  box-shadow: 0 6px 16px rgba(238, 90, 82, 0.35);
}

.category-tab:active {
  transform: scale(0.95);
}

.product-list-area {
  flex: 1;
  overflow-y: auto;
  padding-right: 8px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 22px;
}

.loading-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}
</style>
