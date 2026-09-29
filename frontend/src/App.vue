<template>
  <div class="screen-wrapper" :style="wrapperStyle">
    <AppHeader />
    <main class="page-content">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
    <!-- 全局购物车悬浮按钮 -->
    <CartFloating />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import AppHeader from '@/components/AppHeader.vue'
import CartFloating from '@/components/CartFloating.vue'

const scale = ref(1)

const wrapperStyle = computed(() => ({
  transform: `translate(-50%, -50%) scale(${scale.value})`
}))

const resize = () => {
  const w = window.innerWidth
  const h = window.innerHeight
  // 保持 1920x1080 比例等比缩放
  scale.value = Math.min(w / 1920, h / 1080)
}

onMounted(() => {
  resize()
  window.addEventListener('resize', resize)
})

onUnmounted(() => {
  window.removeEventListener('resize', resize)
})
</script>

<style>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.25s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
