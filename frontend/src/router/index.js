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

    {
      path: '/admin',
      component: AdminLayout,
      redirect: '/admin/dashboard', 
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
          path: 'attributes',
          component: () => import('../pages/auth/admin/AttributesManager.vue')
        },
        {
          path: 'staffs',
          component: () => import('../pages/auth/admin/StaffsManager.vue')
        },
        {
          path: 'vouchers', 
          component: () => import('../pages/auth/admin/VoucherManager.vue')
        },
        {
          path: 'promotions', 
          component: () => import('../pages/auth/admin/PromotionManager.vue')
        }
      ]
    },
    
    { 
       path: '/:pathMatch(.*)*', 
       redirect: '/admin/products' 
    }
  ]
})

export default router