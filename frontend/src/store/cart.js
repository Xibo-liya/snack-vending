import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useCartStore = defineStore('cart', () => {
  // 购物车列表：{ id, name, price, image, qty, stock }
  const items = ref([])

  const totalCount = computed(() =>
    items.value.reduce((sum, item) => sum + item.qty, 0)
  )

  const totalPrice = computed(() =>
    items.value.reduce((sum, item) => sum + item.qty * item.price, 0)
  )

  // 加入购物车
  const addItem = (product) => {
    const existing = items.value.find((i) => i.id === product.id)
    if (existing) {
      if (existing.qty >= product.stock) return false
      existing.qty++
    } else {
      items.value.push({
        id: product.id,
        name: product.name,
        price: product.price,
        image: product.image,
        stock: product.stock,
        qty: 1
      })
    }
    return true
  }

  // 减少数量
  const decreaseItem = (id) => {
    const existing = items.value.find((i) => i.id === id)
    if (!existing) return
    if (existing.qty > 1) {
      existing.qty--
    } else {
      removeItem(id)
    }
  }

  // 移除商品
  const removeItem = (id) => {
    items.value = items.value.filter((i) => i.id !== id)
  }

  // 清空购物车
  const clearCart = () => {
    items.value = []
  }

  return {
    items,
    totalCount,
    totalPrice,
    addItem,
    decreaseItem,
    removeItem,
    clearCart
  }
})
