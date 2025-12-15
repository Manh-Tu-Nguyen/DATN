<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { Icon } from '@iconify/vue';

const products = ref([]);    
const loading = ref(true);   
const error = ref(null);     

const formatCurrency = (value) => {
    if (!value) return '0 đ';
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

const getStatusBadge = (status) => {
    if (status) {
        return { text: 'Đang kinh doanh', class: 'bg-green-100 text-green-800 border border-green-200' };
    } else {
        return { text: 'Ngừng kinh doanh', class: 'bg-red-100 text-red-800 border border-red-200' };
    }
};

const fetchProducts = async () => {
    try {
        loading.value = true;
        const response = await axios.get('http://localhost:8080/quan_ly_chi_tiet_san_pham');
        products.value = response.data;
        loading.value = false;
    } catch (err) {
        console.error("Lỗi tải sản phẩm:", err);
        error.value = "Không thể kết nối đến Server.";
        loading.value = false;
    }
};

onMounted(() => {
    fetchProducts();
});
</script>

<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800 flex items-center gap-2">
        <Icon icon="icon-park-solid:sport" class="text-pink-600"/> Quản lý Sản Phẩm
      </h1>
      <button class="bg-pink-600 text-white px-4 py-2 rounded hover:bg-pink-700 font-medium flex items-center gap-2 shadow-lg transition transform hover:-translate-y-0.5">
        <Icon icon="mdi:plus" /> Thêm sản phẩm
      </button>
    </div>

    <div v-if="loading" class="text-center py-10 text-gray-500">
        Đang tải danh sách sản phẩm...
    </div>

    <div v-else-if="error" class="text-center py-10 text-red-500 bg-red-50 rounded">
        {{ error }}
    </div>

    <div v-else class="bg-white rounded-lg shadow overflow-hidden border border-gray-200">
      <table class="min-w-full leading-normal">
        <thead>
          <tr class="bg-gray-100 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
            <th class="px-5 py-3 border-b-2 border-gray-200">ID</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Tên / Thể Loại</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Thuộc tính</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Giá Bán</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Tồn Kho</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Trạng Thái</th>
            <th class="px-5 py-3 border-b-2 border-gray-200 text-center">Thao Tác</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in products" :key="item.id" class="hover:bg-pink-50 transition duration-150">
            <td class="px-5 py-4 border-b border-gray-200 text-sm font-bold text-gray-600">
              #{{ item.id }}
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm">
              <div class="font-bold text-gray-800 text-base">{{ item.theLoai || 'Chưa đặt tên' }}</div>
              <div class="text-xs text-gray-500 mt-1">Mã SP gốc: {{ item.idSanPham }}</div>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm text-gray-600">
               <div class="flex items-center gap-2">
                   <span class="bg-gray-200 px-2 py-0.5 rounded text-xs">Size: {{ item.idSize }}</span>
                   <span class="bg-gray-200 px-2 py-0.5 rounded text-xs">Màu: {{ item.idMauSac }}</span>
               </div>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm font-bold text-pink-600">
              {{ formatCurrency(item.giaBan) }}
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm">
                <span :class="item.soLuongTon > 0 ? 'text-gray-800' : 'text-red-500 font-bold'">
                    {{ item.soLuongTon }}
                </span>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm">
              <span :class="getStatusBadge(item.trangThai).class" class="px-3 py-1 rounded-full text-xs font-semibold">
                {{ getStatusBadge(item.trangThai).text }}
              </span>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm text-center">
              <button class="text-blue-600 hover:text-blue-800 mx-2 p-1 hover:bg-blue-100 rounded" title="Sửa">
                <Icon icon="mdi:pencil" width="20"/>
              </button>
              <button class="text-red-500 hover:text-red-700 mx-2 p-1 hover:bg-red-100 rounded" title="Xóa">
                 <Icon icon="mdi:trash-can-outline" width="20"/>
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <div v-if="!loading && products.length === 0" class="flex flex-col items-center justify-center py-10 text-gray-400">
        <Icon icon="mdi:package-variant-closed" width="48" class="mb-2 opacity-50"/>
        <p>Chưa có sản phẩm nào trong kho.</p>
      </div>
    </div>
  </div>
</template>