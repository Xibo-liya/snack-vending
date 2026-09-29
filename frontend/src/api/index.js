import request from '@/utils/request'

// 订单状态映射
export const orderStatusMap = {
  pending: { label: '待支付', type: 'warning', color: '#e6a23c' },
  paid: { label: '已支付', type: 'success', color: '#67c23a' },
  completed: { label: '已完成', type: 'success', color: '#67c23a' },
  timeout: { label: '已超时', type: 'info', color: '#909399' },
  failed: { label: '已取消', type: 'danger', color: '#f56c6c' }
}

// ========== 商品接口 ==========
export const getCategories = () => request.get('/categories')

export const getProducts = (category = 'all') =>
  request.get('/products', { params: { category } })

export const getProductDetail = (id) => request.get(`/products/${id}`)

// ========== 轮播接口 ==========
export const getBanners = () => request.get('/banners')

// ========== 设备状态接口 ==========
export const getDeviceStatus = () => request.get('/device/status')

// ========== 订单接口 ==========
export const createOrder = (orderData) =>
  request.post('/orders', orderData)

export const payOrder = (orderNo) =>
  request.post(`/orders/${orderNo}/pay`)

export const getOrderList = () => request.get('/orders')

export const getOrderDetail = (orderNo) =>
  request.get(`/orders/${orderNo}`)

export const cancelOrder = (orderNo) =>
  request.post(`/orders/${orderNo}/cancel`)
