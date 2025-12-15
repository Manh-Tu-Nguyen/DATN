<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { Icon } from '@iconify/vue';

// 1. STATE
const vouchers = ref([]);
const loading = ref(true);
const error = ref(null);

// 2. UTILS
// Format ngày
const formatDate = (dateString) => {
    if (!dateString) return '';
    return new Date(dateString).toLocaleDateString('vi-VN');
};

// Format giá trị giảm (Xử lý theo KIEU_GIAM)
const formatDiscount = (val, type) => {
    // Giả sử type lưu trong DB là "%" hoặc "VND"/"MONEY"
    if (type === '%' || type === 'PERCENT') {
        return `Giảm ${val}%`;
    }
    // Mặc định là tiền
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val);
};

// Badge trạng thái
const getStatusBadge = (status, endDate) => {
    // Kiểm tra thêm: Nếu ngày kết thúc đã qua -> Hết hạn
    const today = new Date();
    const end = new Date(endDate);
    
    if (end < today) {
        return { text: 'Đã hết hạn', class: 'bg-orange-100 text-orange-700 border border-orange-200' };
    }

    if (status === true) {
        return { text: 'Đang hoạt động', class: 'bg-green-100 text-green-700 border border-green-200' };
    } else {
        return { text: 'Ngưng hoạt động', class: 'bg-gray-200 text-gray-600 border border-gray-300' };
    }
};

// 3. API CALL
const fetchVouchers = async () => {
    try {
        loading.value = true;
        // Đảm bảo bạn đã tạo Controller cho Voucher
        const response = await axios.get('http://localhost:8080/voucher/get-all');
        vouchers.value = response.data;
        loading.value = false;
    } catch (err) {
        console.error("Lỗi tải voucher:", err);
        error.value = "Không thể kết nối đến Server.";
        loading.value = false;
    }
};

onMounted(() => {
    fetchVouchers();
});
</script>

<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800 flex items-center gap-2">
        <Icon icon="mdi:ticket-percent" class="text-pink-600"/> Quản lý Voucher
      </h1>
      <button class="bg-pink-600 text-white px-4 py-2 rounded hover:bg-pink-700 font-medium flex items-center gap-2 shadow transition transform hover:-translate-y-0.5">
        <Icon icon="mdi:plus" /> Tạo Voucher mới
      </button>
    </div>

    <div v-if="loading" class="text-center py-10 text-gray-500">
        Đang tải danh sách khuyến mãi...
    </div>

    <div v-else-if="error" class="text-center py-10 text-red-500 bg-red-50 rounded">
        {{ error }}
    </div>

    <div v-else class="bg-white rounded-lg shadow overflow-hidden border border-gray-200">
      <table class="min-w-full leading-normal">
        <thead>
          <tr class="bg-gray-100 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
            <th class="px-5 py-3 border-b-2 border-gray-200">Mã / Tên Voucher</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Giá Trị Giảm</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Thời Gian Áp Dụng</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Trạng Thái</th>
            <th class="px-5 py-3 border-b-2 border-gray-200 text-center">Thao Tác</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="v in vouchers" :key="v.id" class="hover:bg-pink-50 transition duration-150">
            
            <td class="px-5 py-4 border-b border-gray-200 text-sm">
              <div class="flex items-center gap-3">
                  <div class="bg-pink-100 p-2 rounded text-pink-600">
                      <Icon icon="mdi:ticket-confirmation-outline" width="24"/>
                  </div>
                  <div>
                      <div class="font-bold text-gray-800 text-base uppercase tracking-wide">{{ v.maVoucher }}</div>
                      <div class="text-xs text-gray-500">{{ v.tenVoucher }}</div>
                  </div>
              </div>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm">
                <span class="font-bold text-red-500 text-lg">
                    {{ formatDiscount(v.giaTri, v.kieuGiam) }}
                </span>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm text-gray-600">
                <div class="flex items-center gap-1">
                    <Icon icon="mdi:calendar-start" class="text-green-600"/> {{ formatDate(v.ngayBatDau) }}
                </div>
                <div class="flex items-center gap-1 mt-1">
                    <Icon icon="mdi:calendar-end" class="text-red-600"/> {{ formatDate(v.ngayKetThuc) }}
                </div>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm">
              <span :class="getStatusBadge(v.trangThai, v.ngayKetThuc).class" class="px-3 py-1 rounded-full text-xs font-semibold flex items-center w-fit gap-1">
                 <Icon v-if="v.trangThai" icon="mdi:check-circle-outline" />
                 <Icon v-else icon="mdi:close-circle-outline" />
                 {{ getStatusBadge(v.trangThai, v.ngayKetThuc).text }}
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

      <div v-if="!loading && vouchers.length === 0" class="flex flex-col items-center justify-center py-10 text-gray-400">
        <Icon icon="mdi:ticket-outline" width="64" class="mb-3 opacity-50"/>
        <p>Chưa có voucher nào.</p>
      </div>
    </div>
  </div>
</template>