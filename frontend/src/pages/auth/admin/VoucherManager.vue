<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import axios from 'axios';
import { Icon } from '@iconify/vue';

// --- CONFIG ---
const API_URL = 'http://localhost:8080/voucher';

// --- STATE ---
const vouchers = ref([]);
const loading = ref(true);
const error = ref(null);
const submitting = ref(false);

const showModal = ref(false);
const isEditing = ref(false);

const formData = reactive({
    id: null,
    maVoucher: '',
    tenVoucher: '',
    giaTri: 0,
    kieuGiam: 'PERCENT', 
    ngayBatDau: '',
    ngayKetThuc: '',
    trangThai: true
});

const errors = reactive({
    maVoucher: '',
    tenVoucher: '',
    giaTri: '',
    ngayBatDau: '',
    ngayKetThuc: ''
});

const validateForm = () => {
    let isValid = true;
    
    Object.keys(errors).forEach(key => errors[key] = '');

    if (!formData.maVoucher.trim()) {
        errors.maVoucher = 'Mã voucher không được để trống';
        isValid = false;
    }

    if (!formData.tenVoucher.trim()) {
        errors.tenVoucher = 'Tên voucher không được để trống';
        isValid = false;
    }

    if (formData.giaTri <= 0) {
        errors.giaTri = 'Giá trị giảm phải lớn hơn 0';
        isValid = false;
    }
    if (formData.kieuGiam === 'PERCENT' && formData.giaTri > 100) {
        errors.giaTri = 'Giảm giá phần trăm không được quá 100%';
        isValid = false;
    }

    if (!formData.ngayBatDau) {
        errors.ngayBatDau = 'Chọn ngày bắt đầu';
        isValid = false;
    }
    if (!formData.ngayKetThuc) {
        errors.ngayKetThuc = 'Chọn ngày kết thúc';
        isValid = false;
    }

    if (formData.ngayBatDau && formData.ngayKetThuc) {
        const start = new Date(formData.ngayBatDau);
        const end = new Date(formData.ngayKetThuc);
        if (end < start) {
            errors.ngayKetThuc = 'Ngày kết thúc phải sau ngày bắt đầu';
            isValid = false;
        }
    }

    return isValid;
};

// --- API ACTIONS ---
const fetchVouchers = async () => {
    try {
        loading.value = true;
        const response = await axios.get(`${API_URL}/get-all`);
        vouchers.value = response.data;
    } catch (err) {
        console.error(err);
        error.value = "Không thể kết nối đến Server.";
    } finally {
        loading.value = false;
    }
};

const handleSave = async () => {
    if (!validateForm()) return; 

    try {
        submitting.value = true;
        const payload = { ...formData }; 

        if (isEditing.value) {
            await axios.put(`${API_URL}/update/${formData.id}`, payload);
            alert("Cập nhật thành công!");
        } else {
            delete payload.id;
            await axios.post(`${API_URL}/add`, payload);
            alert("Thêm mới thành công!");
        }
        
        closeModal();
        fetchVouchers();
    } catch (err) {
        alert("Lỗi: " + (err.response?.data || err.message));
    } finally {
        submitting.value = false;
    }
};

const handleDelete = async (id) => {
    if (!confirm("Bạn chắc chắn muốn xóa voucher này?")) return;
    try {
        await axios.delete(`${API_URL}/delete/${id}`);
        alert("Đã xóa thành công!");
        fetchVouchers();
    } catch (err) {
        alert("Lỗi khi xóa: " + err.message);
    }
};

// --- HELPER UI ---
const formatDate = (dateString) => {
    if (!dateString) return '';
    return new Date(dateString).toLocaleDateString('vi-VN');
};

const formatDiscount = (val, type) => {
    if (type === 'PERCENT' || type === '%') return `Giảm ${val}%`;
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val);
};

const getStatusBadge = (status, endDate) => {
    const today = new Date();
    today.setHours(0,0,0,0); 
    const end = new Date(endDate);
    
    if (end < today) return { text: 'Đã hết hạn', class: 'bg-orange-100 text-orange-700 border-orange-200' };
    if (status) return { text: 'Đang hoạt động', class: 'bg-green-100 text-green-700 border-green-200' };
    return { text: 'Ngưng hoạt động', class: 'bg-gray-100 text-gray-500 border-gray-200' };
};

// --- MODAL ACTIONS ---
const openAddModal = () => {
    isEditing.value = false;
    Object.assign(formData, {
        id: null, maVoucher: '', tenVoucher: '', giaTri: 0, 
        kieuGiam: 'PERCENT', ngayBatDau: '', ngayKetThuc: '', trangThai: true
    });
    Object.keys(errors).forEach(key => errors[key] = '');
    showModal.value = true;
};

const openEditModal = (item) => {
    isEditing.value = true;
    Object.keys(errors).forEach(key => errors[key] = '');
    Object.assign(formData, {
        id: item.id,
        maVoucher: item.maVoucher,
        tenVoucher: item.tenVoucher,
        giaTri: item.giaTri,
        kieuGiam: item.kieuGiam,
        ngayBatDau: item.ngayBatDau,
        ngayKetThuc: item.ngayKetThuc,
        trangThai: item.trangThai
    });
    showModal.value = true;
};

const closeModal = () => showModal.value = false;

onMounted(() => {
    fetchVouchers();
});
</script>

<template>
  <div class="p-6 bg-gray-50 min-h-screen font-sans">
    
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm border border-gray-100">
      <h1 class="text-2xl font-bold text-gray-800 flex items-center gap-2">
        <div class="p-2 bg-pink-100 rounded-lg">
             <Icon icon="mdi:ticket-percent" class="text-pink-600 w-6 h-6"/> 
        </div>
        Quản lý Voucher
      </h1>
      <button @click="openAddModal" class="bg-pink-600 text-white px-4 py-2.5 rounded-lg hover:bg-pink-700 font-medium flex items-center gap-2 shadow-md transition-all">
        <Icon icon="mdi:plus" width="20" /> Tạo Voucher
      </button>
    </div>

    <div v-if="loading" class="text-center py-10">
         <Icon icon="eos-icons:loading" width="35" class="text-pink-500 animate-spin mx-auto"/>
         <p class="text-gray-500 mt-2">Đang tải dữ liệu...</p>
    </div>
    
    <div v-else-if="error" class="text-center py-10 text-red-500 bg-red-50 rounded border border-red-100">
        {{ error }}
    </div>

    <div v-else class="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
      <table class="min-w-full leading-normal">
        <thead>
          <tr class="bg-gray-50 text-left text-xs font-bold text-gray-500 uppercase tracking-wider border-b border-gray-200">
            <th class="px-5 py-4">Mã / Tên Voucher</th>
            <th class="px-5 py-4">Giá Trị Giảm</th>
            <th class="px-5 py-4">Thời Gian</th>
            <th class="px-5 py-4">Trạng Thái</th>
            <th class="px-5 py-4 text-center">Thao Tác</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100">
          <tr v-if="vouchers.length === 0">
              <td colspan="5" class="px-6 py-10 text-center text-gray-400 italic">Chưa có voucher nào.</td>
          </tr>
          <tr v-for="v in vouchers" :key="v.id" class="hover:bg-pink-50/30 transition duration-150">
            
            <td class="px-5 py-4">
              <div class="flex items-center gap-3">
                  <div class="bg-pink-100 p-2 rounded-lg text-pink-600">
                      <Icon icon="mdi:ticket-confirmation-outline" width="24"/>
                  </div>
                  <div>
                      <div class="font-bold text-gray-800 text-sm uppercase tracking-wide">{{ v.maVoucher }}</div>
                      <div class="text-xs text-gray-500 max-w-[200px] truncate">{{ v.tenVoucher }}</div>
                  </div>
              </div>
            </td>

            <td class="px-5 py-4">
                <span class="font-bold text-red-500 text-base">
                    {{ formatDiscount(v.giaTri, v.kieuGiam) }}
                </span>
            </td>

            <td class="px-5 py-4 text-sm text-gray-600 space-y-1">
                <div class="flex items-center gap-1.5">
                    <Icon icon="mdi:calendar-start" class="text-green-600" width="16"/> {{ formatDate(v.ngayBatDau) }}
                </div>
                <div class="flex items-center gap-1.5">
                    <Icon icon="mdi:calendar-end" class="text-red-600" width="16"/> {{ formatDate(v.ngayKetThuc) }}
                </div>
            </td>

            <td class="px-5 py-4">
              <span :class="getStatusBadge(v.trangThai, v.ngayKetThuc).class" class="px-2.5 py-1 rounded-full text-xs font-semibold flex items-center w-fit gap-1 border">
                 <Icon :icon="v.trangThai ? 'mdi:check-circle' : 'mdi:close-circle'" width="14"/>
                 {{ getStatusBadge(v.trangThai, v.ngayKetThuc).text }}
              </span>
            </td>

            <td class="px-5 py-4 text-center">
              <div class="flex justify-center gap-2">
                  <button @click="openEditModal(v)" class="text-blue-600 bg-blue-50 p-2 rounded-lg hover:bg-blue-100 transition" title="Sửa">
                    <Icon icon="mdi:pencil" width="18"/>
                  </button>
                  <button @click="handleDelete(v.id)" class="text-red-500 bg-red-50 p-2 rounded-lg hover:bg-red-100 transition" title="Xóa">
                     <Icon icon="mdi:trash-can-outline" width="18"/>
                  </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showModal" class="relative z-[999]">
        <div class="fixed inset-0 bg-gray-900/60 backdrop-blur-sm transition-opacity" @click="closeModal"></div>
        <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
            <div class="flex min-h-full items-center justify-center p-4">
                <div class="relative bg-white rounded-2xl shadow-xl w-full max-w-2xl overflow-hidden border border-gray-100">
                    
                    <div class="px-6 py-4 border-b border-gray-100 flex justify-between items-center bg-gray-50/50">
                        <h3 class="text-xl font-bold text-gray-800 flex items-center gap-2">
                            <Icon :icon="isEditing ? 'mdi:file-edit' : 'mdi:plus-box'" class="text-pink-600"/>
                            {{ isEditing ? 'Cập nhật Voucher' : 'Thêm Voucher Mới' }}
                        </h3>
                        <button @click="closeModal" class="text-gray-400 hover:text-gray-600 p-1 rounded-full hover:bg-gray-100 transition">
                            <Icon icon="mdi:close" width="24" />
                        </button>
                    </div>

                    <div class="p-6">
                        <form @submit.prevent="handleSave" class="space-y-4">
                            
                            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Mã Voucher <span class="text-red-500">*</span></label>
                                    <input v-model="formData.maVoucher" :disabled="isEditing" type="text" 
                                           class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500 disabled:bg-gray-100 uppercase"
                                           :class="{'border-red-500 focus:ring-red-200': errors.maVoucher}"
                                           placeholder="VC2024">
                                    <p v-if="errors.maVoucher" class="text-red-500 text-xs mt-1">{{ errors.maVoucher }}</p>
                                </div>
                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Tên Voucher <span class="text-red-500">*</span></label>
                                    <input v-model="formData.tenVoucher" type="text" 
                                           class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500"
                                           :class="{'border-red-500 focus:ring-red-200': errors.tenVoucher}"
                                           placeholder="Khuyến mãi mùa hè">
                                    <p v-if="errors.tenVoucher" class="text-red-500 text-xs mt-1">{{ errors.tenVoucher }}</p>
                                </div>
                            </div>

                            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Loại giảm giá</label>
                                    <select v-model="formData.kieuGiam" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500 bg-white">
                                        <option value="PERCENT">Theo phần trăm (%)</option>
                                        <option value="MONEY">Theo số tiền (VND)</option>
                                    </select>
                                </div>
                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">
                                        Giá trị giảm ({{ formData.kieuGiam === 'PERCENT' ? '%' : 'VNĐ' }}) <span class="text-red-500">*</span>
                                    </label>
                                    <input v-model="formData.giaTri" type="number" 
                                           class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500"
                                           :class="{'border-red-500 focus:ring-red-200': errors.giaTri}">
                                    <p v-if="errors.giaTri" class="text-red-500 text-xs mt-1">{{ errors.giaTri }}</p>
                                </div>
                            </div>

                            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Ngày bắt đầu <span class="text-red-500">*</span></label>
                                    <input v-model="formData.ngayBatDau" type="date" 
                                           class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500"
                                           :class="{'border-red-500 focus:ring-red-200': errors.ngayBatDau}">
                                    <p v-if="errors.ngayBatDau" class="text-red-500 text-xs mt-1">{{ errors.ngayBatDau }}</p>
                                </div>
                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Ngày kết thúc <span class="text-red-500">*</span></label>
                                    <input v-model="formData.ngayKetThuc" type="date" 
                                           class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500"
                                           :class="{'border-red-500 focus:ring-red-200': errors.ngayKetThuc}">
                                    <p v-if="errors.ngayKetThuc" class="text-red-500 text-xs mt-1">{{ errors.ngayKetThuc }}</p>
                                </div>
                            </div>

                            <div class="flex items-center gap-3 mt-2 p-3 bg-gray-50 rounded-lg border">
                                <input type="checkbox" v-model="formData.trangThai" id="activeStatus" class="w-5 h-5 text-pink-600 rounded focus:ring-pink-500 cursor-pointer">
                                <label for="activeStatus" class="text-sm font-medium text-gray-700 cursor-pointer select-none">Kích hoạt voucher này ngay</label>
                            </div>

                        </form>
                    </div>

                    <div class="px-6 py-4 bg-gray-50 flex flex-row-reverse gap-3 border-t border-gray-100">
                         <button @click="handleSave" :disabled="submitting"
                            class="bg-pink-600 text-white px-5 py-2.5 rounded-lg hover:bg-pink-700 font-medium shadow-md flex items-center gap-2 disabled:opacity-70 transition-all">
                            <Icon v-if="submitting" icon="eos-icons:loading" class="animate-spin"/>
                            {{ submitting ? 'Đang lưu...' : 'Lưu lại' }}
                        </button>
                        <button @click="closeModal" class="bg-white text-gray-700 px-5 py-2.5 rounded-lg border border-gray-300 hover:bg-gray-50 font-medium transition-all">
                            Hủy bỏ
                        </button>
                    </div>

                </div>
            </div>
        </div>
    </div>

  </div>
</template>