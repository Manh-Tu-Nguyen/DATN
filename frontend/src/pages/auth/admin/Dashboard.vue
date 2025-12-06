<script setup>
import { ref, reactive, onMounted } from 'vue';
import axios from 'axios';
import { Icon } from '@iconify/vue';

const API_URL = 'http://localhost:8080/quan_ly_thong_ke';

const payments = ref([]);
const loading = ref(false);
const error = ref(null);

const searchParams = reactive({
  startDate: '',
  endDate: ''
});

const summary = reactive({
  totalCount: 0,
  totalAmount: 0
});


const formatCurrency = (value) => {
  if (!value) return '0 ₫';
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

const formatDate = (dateString) => {
  if (!dateString) return '---';
  return new Date(dateString).toLocaleDateString('vi-VN');
};

const getPaymentMethod = (isTransfer) => {
  return isTransfer 
    ? { text: 'Chuyển khoản', icon: 'mdi:credit-card-outline', class: 'text-blue-600 bg-blue-50' }
    : { text: 'Tiền mặt', icon: 'mdi:cash', class: 'text-green-600 bg-green-50' };
};


const fetchAll = async () => {
  try {
    loading.value = true;
    error.value = null;
    
    const response = await axios.get(API_URL);
    payments.value = response.data;

    calculateClientSideSummary(response.data);

  } catch (err) {
    console.error(err);
    error.value = "Lỗi kết nối server!";
  } finally {
    loading.value = false;
  }
};

const handleSearch = async () => {
  if (!searchParams.startDate || !searchParams.endDate) {
    alert("Vui lòng chọn đầy đủ Từ ngày và Đến ngày!");
    return;
  }

  try {
    loading.value = true;
    error.value = null;

    const response = await axios.put(`${API_URL}/search`, null, {
      params: {
        startDate: searchParams.startDate,
        endDate: searchParams.endDate
      }
    });

    const result = response.data;
    
    payments.value = result.data; 
    summary.totalCount = result.tongHoaDon || 0;
    summary.totalAmount = result.tongTienThanhToan || 0;

  } catch (err) {
    console.error(err);
    error.value = "Lỗi khi tìm kiếm!";
  } finally {
    loading.value = false;
  }
};

const calculateClientSideSummary = (dataList) => {
  summary.totalCount = dataList.length;
  summary.totalAmount = dataList.reduce((sum, item) => sum + (item.soTienThanhToan || 0), 0);
};

const resetFilter = () => {
    searchParams.startDate = '';
    searchParams.endDate = '';
    fetchAll();
};

onMounted(() => {
  fetchAll();
});
</script>

<template>
  <div class="p-6 bg-gray-50 min-h-screen font-sans">
    
    <div class="mb-6">
      <h1 class="text-2xl font-bold text-gray-800 flex items-center gap-2">
        <Icon icon="mdi:chart-box-outline" class="text-pink-600" />
        Thống Kê Thanh Toán
      </h1>
      <p class="text-gray-500 text-sm mt-1">Quản lý dòng tiền và lịch sử giao dịch</p>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-6">
        
        <div class="lg:col-span-2 bg-white p-4 rounded-xl shadow-sm border border-gray-200">
            <h3 class="font-semibold text-gray-700 mb-3 flex items-center gap-2">
                <Icon icon="mdi:filter-variant" /> Bộ lọc thời gian
            </h3>
            <div class="flex flex-wrap items-end gap-4">
                <div class="flex-1 min-w-[150px]">
                    <label class="block text-xs font-medium text-gray-500 mb-1">Từ ngày</label>
                    <input v-model="searchParams.startDate" type="date" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500 outline-none text-sm">
                </div>
                <div class="flex-1 min-w-[150px]">
                    <label class="block text-xs font-medium text-gray-500 mb-1">Đến ngày</label>
                    <input v-model="searchParams.endDate" type="date" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500 outline-none text-sm">
                </div>
                <div class="flex gap-2">
                    <button @click="handleSearch" class="bg-pink-600 hover:bg-pink-700 text-white px-4 py-2 rounded-lg text-sm font-medium flex items-center gap-2 transition-colors">
                        <Icon icon="mdi:magnify" /> Tìm kiếm
                    </button>
                    <button @click="resetFilter" class="bg-gray-100 hover:bg-gray-200 text-gray-600 px-4 py-2 rounded-lg text-sm font-medium transition-colors" title="Làm mới">
                        <Icon icon="mdi:refresh" />
                    </button>
                </div>
            </div>
        </div>

        <div class="lg:col-span-1 grid grid-cols-2 gap-4">
            <div class="bg-blue-500 text-white p-4 rounded-xl shadow-md flex flex-col justify-between">
                <div class="text-blue-100 text-xs font-medium uppercase">Tổng Số Giao Dịch</div>
                <div class="text-3xl font-bold mt-2">{{ summary.totalCount }}</div>
                <div class="flex justify-end">
                    <Icon icon="mdi:receipt-text-outline" width="40" class="opacity-30"/>
                </div>
            </div>
            
            <div class="bg-green-500 text-white p-4 rounded-xl shadow-md flex flex-col justify-between">
                <div class="text-green-100 text-xs font-medium uppercase">Tổng Doanh Thu</div>
                <div class="text-xl font-bold mt-2 truncate" :title="formatCurrency(summary.totalAmount)">
                    {{ formatCurrency(summary.totalAmount) }}
                </div>
                <div class="flex justify-end">
                     <Icon icon="mdi:cash-multiple" width="40" class="opacity-30"/>
                </div>
            </div>
        </div>
    </div>

    <div class="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
        
        <div v-if="loading" class="p-10 text-center">
             <Icon icon="eos-icons:loading" width="35" class="text-pink-500 animate-spin mx-auto"/>
             <p class="text-gray-500 mt-2">Đang xử lý dữ liệu...</p>
        </div>

        <div v-else-if="error" class="p-10 text-center text-red-500">
             {{ error }}
        </div>

        <div v-else>
             <table class="min-w-full leading-normal">
                <thead>
                  <tr class="bg-gray-50 text-left text-xs font-bold text-gray-500 uppercase tracking-wider border-b border-gray-200">
                    <th class="px-5 py-4">Mã Thanh Toán</th>
                    <th class="px-5 py-4">Mã Hóa Đơn</th>
                    <th class="px-5 py-4 text-right">Số Tiền</th>
                    <th class="px-5 py-4 text-center">Hình Thức</th>
                    <th class="px-5 py-4">Ngày Thanh Toán</th>
                    <th class="px-5 py-4 text-center">Trạng Thái</th>
                  </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                  <tr v-if="payments.length === 0">
                      <td colspan="6" class="px-6 py-10 text-center text-gray-400 italic">Không tìm thấy dữ liệu phù hợp.</td>
                  </tr>
                  <tr v-for="item in payments" :key="item.id" class="hover:bg-gray-50 transition duration-150">
                    
                    <td class="px-5 py-4 text-sm font-medium text-gray-700">
                        {{ item.maThanhToan }}
                        <div v-if="item.ghiChu" class="text-[10px] text-gray-400 font-normal italic mt-1">
                            Note: {{ item.ghiChu }}
                        </div>
                    </td>

                    <td class="px-5 py-4 text-sm text-blue-600 font-semibold">
                        {{ item.maHoaDon }}
                    </td>

                    <td class="px-5 py-4 text-sm font-bold text-gray-800 text-right">
                        {{ formatCurrency(item.soTienThanhToan) }}
                    </td>

                    <td class="px-5 py-4 text-center">
                        <span :class="getPaymentMethod(item.hinhThanhToan).class" class="px-2.5 py-1 rounded-full text-xs font-semibold inline-flex items-center gap-1">
                            <Icon :icon="getPaymentMethod(item.hinhThanhToan).icon" />
                            {{ getPaymentMethod(item.hinhThanhToan).text }}
                        </span>
                    </td>

                    <td class="px-5 py-4 text-sm text-gray-600">
                        <div class="flex items-center gap-1">
                            <Icon icon="mdi:calendar-clock" class="text-gray-400"/>
                            {{ formatDate(item.ngay) }}
                        </div>
                    </td>

                    <td class="px-5 py-4 text-center">
                        <span v-if="item.trangThai" class="text-green-600 bg-green-100 px-2 py-1 rounded text-xs font-bold">Thành công</span>
                        <span v-else class="text-red-600 bg-red-100 px-2 py-1 rounded text-xs font-bold">Thất bại/Hủy</span>
                    </td>

                  </tr>
                </tbody>
              </table>
        </div>
        
        <div class="px-5 py-3 border-t border-gray-200 bg-gray-50 flex justify-between items-center">
            <span class="text-xs text-gray-500">Hiển thị {{ payments.length }} kết quả</span>
        </div>
    </div>
  </div>
</template>