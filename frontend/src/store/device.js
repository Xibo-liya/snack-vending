import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useDeviceStore = defineStore('device', () => {
  const status = ref({
    online: true,
    status: 'normal',
    temp: '4°C',
    message: '设备运行正常'
  })

  const setStatus = (s) => {
    status.value = { ...status.value, ...s }
  }

  return { status, setStatus }
})
