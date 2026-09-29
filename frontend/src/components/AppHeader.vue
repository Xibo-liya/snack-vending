<template>
  <header class="app-header">
    <div class="logo">
      <span class="logo-icon">🍿</span>
      <span>零食自助售货</span>
    </div>

    <nav class="nav-menu">
      <div
        class="nav-btn"
        :class="{ active: $route.path === '/' }"
        @click="$router.push('/')"
      >
        <el-icon><HomeFilled /></el-icon>
        <span>首页</span>
      </div>
      <div
        class="nav-btn"
        :class="{ active: $route.path === '/products' }"
        @click="$router.push('/products')"
      >
        <el-icon><Goods /></el-icon>
        <span>全部商品</span>
      </div>
      <div
        class="nav-btn"
        :class="{ active: $route.path === '/orders' }"
        @click="$router.push('/orders')"
      >
        <el-icon><Document /></el-icon>
        <span>我的订单</span>
      </div>
    </nav>

    <div class="device-info">
      <div class="device-status">
        <span class="status-dot" :class="{ offline: !device.online }"></span>
        <span>{{ device.online ? '正常营业' : '维护中' }}</span>
      </div>
      <div class="device-temp">
        <el-icon><Snowflake /></el-icon>
        <span>{{ device.temp }}</span>
      </div>
    </div>
  </header>
</template>

<script setup>
import { useDeviceStore } from '@/store/device'

const deviceStore = useDeviceStore()
const device = deviceStore.status
</script>
