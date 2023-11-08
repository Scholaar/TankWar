import { ref } from 'vue'
import { defineStore } from 'pinia'

const useTokenStore = defineStore('useTokenStore', {
  state:() => ({
    token: ref(''),
  }),
})

export default useTokenStore