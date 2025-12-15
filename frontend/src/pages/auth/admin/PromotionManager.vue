<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { Icon } from '@iconify/vue';

// 1. STATE
const promotions = ref([]);
const loading = ref(true);
const error = ref(null);

// 2. UTILS
// Format ngày
const formatDate = (dateString) => {
    if (!dateString) return '';
    return new Date(dateString).toLocaleDateString('vi-VN');
};

// Logic hiển thị trạng thái thông minh
const getStatusBadge = (status, startDate, endDate) => {
    const today = new Date();
    today.setHours(0, 0, 0, 0); // Reset giờ để so sánh ngày
    const start = new Date(startDate);
    const end = new Date(endDate);

    // 1. Nếu bị tắt thủ công (trangThai = false)
    if (!status) {
        return { text: 'Đã hủy', class: 'bg-gray-200 text-gray-500 border border-gray-300' };
    }

    // 2. Kiểm tra thời gian
    if (end < today) {
        return { text: 'Đã kết thúc', class: 'bg-red-100 text-red-700 border border-red-200' };
    }
    if (start > today) {
        return { text: 'Sắp diễn ra', class: 'bg-blue-100 text-blue-700 border border-blue-200' };
    }

    // 3. Còn lại là đang chạy
    return { text: 'Đang diễn ra', class: 'bg-green-100 text-green-700 border border-green-200' };
};

// 3. API CALL
const fetchPromotions = async () => {
    try {
        loading.value = true;
        // Gọi API Backend
        const response = await axios.get('http://localhost:8080/api/khuyen-mai/get-all');
        promotions.value = response.data;
        loading.value = false;
    } catch (err) {
        console.error("Lỗi tải khuyến mãi:", err);
        error.value = "Không thể kết nối đến Server.";
        loading.value = false;
    }
};

onMounted(() => {
    fetchPromotions();
});
</script>

<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800 flex items-center gap-2">
        <Icon icon="mdi:sale" class="text-pink-600"/> Chương trình Khuyến Mãi
      </h1>
      <button class="bg-pink-600 text-white px-4 py-2 rounded hover:bg-pink-700 font-medium flex items-center gap-2 shadow transition transform hover:-translate-y-0.5">
        <Icon icon="mdi:plus" /> Tạo đợt giảm giá
      </button>
    </div>

    <div v-if="loading" class="text-center py-10 text-gray-500">
        Đang tải dữ liệu...
    </div>

    <div v-else-if="error" class="text-center py-10 text-red-500 bg-red-50 rounded">
        {{ error }}
    </div>

    <div v-else class="bg-white rounded-lg shadow overflow-hidden border border-gray-200">
      <table class="min-w-full leading-normal">
        <thead>
          <tr class="bg-gray-100 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
            <th class="px-5 py-3 border-b-2 border-gray-200">Mã / Tên CT</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Mức Giảm</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Thời Gian</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Mô Tả</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Trạng Thái</th>
            <th class="px-5 py-3 border-b-2 border-gray-200 text-center">Thao Tác</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in promotions" :key="p.id" class="hover:bg-pink-50 transition duration-150">
            
            <td class="px-5 py-4 border-b border-gray-200 text-sm">
              <div class="flex items-center gap-3">
                  <div class="bg-purple-100 p-2 rounded text-purple-600">
                      <Icon icon="mdi:tag-text-outline" width="24"/>
                  </div>
                  <div>
                      <div class="font-bold text-gray-800 text-base uppercase tracking-wide">{{ p.maChuongTrinh }}</div>
                      <div class="text-xs text-gray-500 font-medium">{{ p.tenChuongTrinh }}</div>
                  </div>
              </div>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm">
                <div class="flex items-center gap-1 font-bold text-red-500 text-lg">
                    <Icon icon="mdi:arrow-down-bold" /> {{ p.phanTramGiam }}%
                </div>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm text-gray-600">
                <div class="flex items-center gap-1 text-xs mb-1">
                    <span class="w-12 font-semibold">Bắt đầu:</span> 
                    <span class="text-gray-800">{{ formatDate(p.ngayBatDau) }}</span>
                </div>
                <div class="flex items-center gap-1 text-xs">
                    <span class="w-12 font-semibold">Kết thúc:</span> 
                    <span class="text-red-600 font-medium">{{ formatDate(p.ngayKetThuc) }}</span>
                </div>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm text-gray-500 italic max-w-xs truncate" :title="p.moTa">
                {{ p.moTa || 'Không có mô tả' }}
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm">
              <span :class="getStatusBadge(p.trangThai, p.ngayBatDau, p.ngayKetThuc).class" class="px-3 py-1 rounded-full text-xs font-semibold flex items-center w-fit gap-1">
                 <Icon icon="mdi:circle-medium" />
                 {{ getStatusBadge(p.trangThai, p.ngayBatDau, p.ngayKetThuc).text }}
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

      <div v-if="!loading && promotions.length === 0" class="flex flex-col items-center justify-center py-10 text-gray-400">
        <Icon icon="mdi:tag-off-outline" width="64" class="mb-3 opacity-50"/>
        <p>Chưa có chương trình khuyến mãi nào.</p>
      </div>
    </div>
  </div>
</template>