<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { Icon } from '@iconify/vue';

const orders = ref([]);
const loading = ref(true);
const error = ref(null);


const formatCurrency = (value) => {
    if (!value && value !== 0) return '0 đ';
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

const formatDate = (dateString) => {
    if (!dateString) return '';
    return new Date(dateString).toLocaleString('vi-VN', {
        year: 'numeric', month: '2-digit', day: '2-digit',
        hour: '2-digit', minute: '2-digit'
    });
};

const getStatusBadge = (status) => {
    if (status === true) {
        return { 
            text: 'Đã thanh toán', 
            class: 'bg-green-100 text-green-700 border border-green-200' 
        };
    } else {
        return { 
            text: 'Chưa thanh toán', 
            class: 'bg-yellow-100 text-yellow-700 border border-yellow-200' 
        };
    }
};

const fetchOrders = async () => {
    try {
        loading.value = true;
        const response = await axios.get('http://localhost:8080/quan_ly_hoa_don');
        orders.value = response.data;
        loading.value = false;
    } catch (err) {
        console.error("Lỗi tải hóa đơn:", err);
        error.value = "Không thể tải dữ liệu. Kiểm tra API Backend.";
        loading.value = false;
    }
};

onMounted(() => {
    fetchOrders();
});
</script>

<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800 flex items-center gap-2">
        <Icon icon="icon-park-solid:bill" class="text-pink-600"/> Quản lý Hóa Đơn
      </h1>
      <router-link to="/admin/pos">
        <button class="bg-pink-600 text-white px-4 py-2 rounded hover:bg-pink-700 font-medium flex items-center gap-2 shadow transition transform hover:-translate-y-0.5">
            <Icon icon="mdi:point-of-sale" width="20"/> Bán hàng tại quầy
        </button>
      </router-link>
    </div>

    <div v-if="loading" class="text-center py-10 text-gray-500">
        Đang tải danh sách hóa đơn...
    </div>

    <div v-else-if="error" class="text-center py-10 text-red-500 bg-red-50 rounded">
        {{ error }}
    </div>

    <div v-else class="bg-white rounded-lg shadow overflow-hidden border border-gray-200">
      <table class="min-w-full leading-normal">
        <thead>
          <tr class="bg-gray-100 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
            <th class="px-5 py-3 border-b-2 border-gray-200">Mã HĐ</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Khách Hàng</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Ngày Tạo</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Tổng Tiền</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Trạng Thái</th>
            <th class="px-5 py-3 border-b-2 border-gray-200 text-center">Thao Tác</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" :key="order.id" class="hover:bg-pink-50 transition duration-150">
            
            <td class="px-5 py-4 border-b border-gray-200 text-sm">
              <div class="font-bold text-gray-700">{{ order.maHoaDon || '---' }}</div>
              <div class="text-xs text-gray-400">ID: {{ order.id }}</div>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm">
              <div class="font-bold text-gray-800">{{ order.tenKhachHang || 'Khách lẻ' }}</div>
              <div class="text-xs text-gray-500 flex items-center gap-1 mt-0.5">
                 <Icon icon="mdi:phone" width="12"/> 
                 {{ order.soDienThoai || 'Không có SĐT' }}
              </div>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm text-gray-600">
              {{ formatDate(order.ngayTao) }}
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm">
              <div class="font-bold text-pink-600 text-base">
                  {{ formatCurrency(order.tongTien) }}
              </div>
              <div v-if="order.tongTienGiam > 0" class="text-xs text-green-600 mt-1">
                  - Giảm: {{ formatCurrency(order.tongTienGiam) }}
              </div>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm">
              <span :class="getStatusBadge(order.trangThai).class" class="px-3 py-1 rounded-full text-xs font-semibold flex items-center w-fit gap-1">
                <Icon v-if="order.trangThai" icon="mdi:check-circle" width="14"/>
                <Icon v-else icon="mdi:clock-outline" width="14"/>
                {{ getStatusBadge(order.trangThai).text }}
              </span>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm text-center">
              <button class="text-blue-600 hover:text-blue-800 mx-2 p-1 hover:bg-blue-100 rounded tooltip" title="Xem chi tiết">
                <Icon icon="mdi:eye" width="20"/>
              </button>
              <button class="text-gray-500 hover:text-gray-700 mx-2 p-1 hover:bg-gray-100 rounded" title="In hóa đơn">
                <Icon icon="mdi:printer" width="20"/>
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <div v-if="!loading && orders.length === 0" class="flex flex-col items-center justify-center py-12 text-gray-400">
        <Icon icon="icon-park-outline:transaction-order" width="64" class="mb-3 opacity-50"/>
        <p>Chưa có hóa đơn nào được tạo.</p>
      </div>
    </div>
  </div>
</template>