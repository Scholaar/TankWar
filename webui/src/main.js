import './assets/main.css'
import useTokenStore from '@/stores/tokenStore'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)

const tokenStore = useTokenStore()

app.mount('#app')
