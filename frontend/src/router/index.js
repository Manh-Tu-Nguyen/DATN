import { createRouter, createWebHistory } from 'vue-router'

import AdminLayout from '../layouts/AdminLayout.vue'
import DefaultLayout from '../layouts/DefaultLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: DefaultLayout,
      children: [
        {
          path: '',
          name: 'home',
          component: () => import('../components/products/productsHome.vue')
        }
      ]
    },

    // 2. Route cho Admin
    {
      path: '/admin',
      component: AdminLayout,
      children: [
        {
          path: 'orders',
          component: () => import('../pages/auth/admin/OrderManager.vue')
        },
        {
          path: 'products',
          component: () => import('../pages/auth/admin/ProductsManager.vue')
        },
        {
          path: 'staffs',
          component: () => import('../pages/auth/admin/StaffsManager.vue')
        },
        {
          path: 'vouchers', // /admin/vouchers
          component: () => import('../pages/auth/admin/VoucherManager.vue')
        },
        {
          path: 'promotions', // /admin/promotions
          component: () => import('../pages/auth/admin/PromotionManager.vue')
        }

      ]
    }
  ]
})

export default router