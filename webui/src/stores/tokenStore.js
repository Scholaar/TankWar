import { ref } from 'vue'
import { defineStore } from 'pinia'

const useTokenStore = defineStore('useTokenStore', {
  state:() => ({
    token: ref(''),
    userName: ref(''),
  }),
})

export default useTokenStore