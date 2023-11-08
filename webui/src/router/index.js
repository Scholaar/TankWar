import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PlayRoomView from '../views/PlayRoomView.vue'
import WebTankView from '../views/WebTankView.vue'

import useTokenStore from '@/stores/tokenStore'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue')
    },
    {
      path: '/playroom',
      name: 'playroom',
      component: PlayRoomView
    },
    {
      path: '/store',
      name: 'store',
      component: () => import('../views/StoreView.vue')
    },
    {
      path: '/play',
      name: 'play',
      component: WebTankView
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('../views/NotFoundView.vue')
    }
  ]
})

// router.beforeEach((to, from, next) => {
//   const tokenStore = useTokenStore()
//   if (tokenStore.token === "" && to.path !== '/') {
//     ElMessage({
//       message: '身份验证失败',
//       type: 'error',
//     })
//     next('/')
//   } else {
//     next()
//   }
// })

export default router
