<script setup>
import { ref } from 'vue';
import { Icon } from '@iconify/vue';

// State để kiểm soát việc mở menu Marketing
const isMarketingOpen = ref(false);

// Hàm toggle (đóng/mở)
const toggleMarketing = () => {
  isMarketingOpen.value = !isMarketingOpen.value;
};
</script>

<template>
  <div class="flex h-screen bg-gray-100">
    <aside class="w-64 bg-gray-800 text-white flex flex-col transition-all duration-300">
      <div class="p-4 text-2xl font-bold border-b border-gray-700 flex items-center gap-2">
        <Icon icon="ri:admin-fill" class="text-pink-500"/> ADMIN
      </div>
      
      <nav class="flex-1 p-4 space-y-1 overflow-y-auto">
        
        <router-link to="/admin/dashboard" class="flex items-center gap-3 py-2.5 px-4 rounded transition-colors hover:bg-gray-700" active-class="bg-pink-600 hover:bg-pink-700">
           <Icon icon="mdi:view-dashboard" width="20"/> Dashboard
        </router-link>

        <router-link to="/admin/products" class="flex items-center gap-3 py-2.5 px-4 rounded transition-colors hover:bg-gray-700" active-class="bg-pink-600 hover:bg-pink-700">
           <Icon icon="icon-park-solid:sport" width="20"/> Sản phẩm
        </router-link>

        <router-link to="/admin/orders" class="flex items-center gap-3 py-2.5 px-4 rounded transition-colors hover:bg-gray-700" active-class="bg-pink-600 hover:bg-pink-700">
           <Icon icon="icon-park-solid:bill" width="20"/> Hóa đơn
        </router-link>

        <router-link to="/admin/staffs" class="flex items-center gap-3 py-2.5 px-4 rounded transition-colors hover:bg-gray-700" active-class="bg-pink-600 hover:bg-pink-700">
           <Icon icon="mdi:card-account-details" width="20"/> Nhân viên
        </router-link>

        <div>
            <button 
                @click="toggleMarketing"
                class="w-full flex items-center justify-between py-2.5 px-4 rounded transition-colors hover:bg-gray-700 text-gray-300 hover:text-white"
                :class="{ 'bg-gray-700': isMarketingOpen }"
            >
                <div class="flex items-center gap-3">
                    <Icon icon="mdi:ticket-percent" width="20"/> Marketing
                </div>
                <Icon icon="mdi:chevron-down" width="20" class="transition-transform duration-200" :class="{ 'rotate-180': isMarketingOpen }"/>
            </button>

            <div v-show="isMarketingOpen" class="mt-1 space-y-1 bg-gray-900 rounded-lg overflow-hidden transition-all">
                <router-link to="/admin/vouchers" class="flex items-center gap-3 py-2 pl-12 pr-4 text-sm text-gray-400 hover:text-white hover:bg-gray-700 block" active-class="text-pink-400 font-bold">
                    <Icon icon="mdi:ticket-outline" width="16"/> Voucher
                </router-link>
                
                <router-link to="/admin/promotions" class="flex items-center gap-3 py-2 pl-12 pr-4 text-sm text-gray-400 hover:text-white hover:bg-gray-700 block" active-class="text-pink-400 font-bold">
                    <Icon icon="mdi:sale" width="16"/> Đợt giảm giá
                </router-link>
            </div>
        </div>

      </nav>
      
      <div class="p-4 border-t border-gray-700">
         <button class="flex items-center gap-2 text-gray-400 hover:text-white w-full">
            <Icon icon="mdi:logout" /> Đăng xuất
         </button>
      </div>
    </aside>

    <main class="flex-1 h-full overflow-y-auto">
       <header class="bg-white shadow-sm py-3 px-6 flex justify-end items-center sticky top-0 z-10">
          <div class="flex items-center gap-2">
             <span class="text-sm font-semibold text-gray-600">Xin chào, Admin</span>
             <div class="w-8 h-8 rounded-full bg-pink-500 text-white flex items-center justify-center font-bold">A</div>
          </div>
       </header>

       <div class="p-6">
          <router-view />
       </div>
    </main>
  </div>
</template>