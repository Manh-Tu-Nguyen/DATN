<script setup>
import { ref, reactive, onMounted } from 'vue';
import axios from 'axios';
import { Icon } from '@iconify/vue';

const API_URL = 'http://localhost:8080/api/khuyen-mai';

const promotions = ref([]);
const loading = ref(true);
const error = ref(null);
const submitting = ref(false);

const showModal = ref(false);
const isEditing = ref(false);

const formData = reactive({
    id: null,
    maChuongTrinh: '',
    tenChuongTrinh: '',
    phanTramGiam: 0, 
    ngayBatDau: '',
    ngayKetThuc: '',
    trangThai: true,
    moTa: ''
});

const errors = reactive({
    maChuongTrinh: '',
    tenChuongTrinh: '',
    phanTramGiam: '',
    ngayBatDau: '',
    ngayKetThuc: ''
});

const formatDate = (dateString) => {
    if (!dateString) return '';
    return new Date(dateString).toLocaleDateString('vi-VN');
};

const formatPercent = (decimalValue) => {
    if (decimalValue == null) return '0%';
    return `${Math.round(decimalValue * 100)}%`;
};

const getStatusBadge = (status, endDate) => {
    const today = new Date();
    today.setHours(0,0,0,0);
    const end = new Date(endDate);
    
    if (endDate && end < today) {
        return { text: 'Đã kết thúc', class: 'bg-orange-100 text-orange-700 border-orange-200' };
    }
    if (status) {
        return { text: 'Đang diễn ra', class: 'bg-green-100 text-green-700 border-green-200' };
    }
    return { text: 'Tạm ngưng', class: 'bg-gray-100 text-gray-500 border-gray-200' };
};

// --- VALIDATION---
const validateForm = () => {
    let isValid = true;
    Object.keys(errors).forEach(key => errors[key] = '');

    if (!formData.maChuongTrinh.trim()) {
        errors.maChuongTrinh = 'Mã chương trình không được để trống';
        isValid = false;
    }

    if (!formData.tenChuongTrinh.trim()) {
        errors.tenChuongTrinh = 'Tên chương trình không được để trống';
        isValid = false;
    }

    if (formData.phanTramGiam <= 0 || formData.phanTramGiam > 100) {
        errors.phanTramGiam = 'Phần trăm giảm phải từ 1% đến 100%';
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
        if (new Date(formData.ngayKetThuc) < new Date(formData.ngayBatDau)) {
            errors.ngayKetThuc = 'Ngày kết thúc phải sau ngày bắt đầu';
            isValid = false;
        }
    }

    return isValid;
};

// --- API ACTIONS ---
const fetchPromotions = async () => {
    try {
        loading.value = true;
        const response = await axios.get(`${API_URL}/get-all`);
        promotions.value = response.data;
    } catch (err) {
        console.error(err);
        error.value = "Lỗi kết nối Server!";
    } finally {
        loading.value = false;
    }
};

const handleSave = async () => {
    if (!validateForm()) return;

    try {
        submitting.value = true;
        
        const payload = { ...formData };
        
        payload.phanTramGiam = payload.phanTramGiam / 100;

        if (isEditing.value) {
            await axios.put(`${API_URL}/update/${formData.id}`, payload);
            alert("Cập nhật thành công!");
        } else {
            delete payload.id;
            await axios.post(`${API_URL}/add`, payload);
            alert("Thêm mới thành công!");
        }
        
        closeModal();
        fetchPromotions();
    } catch (err) {
        const msg = err.response?.data || err.message;
        alert("Lỗi: " + msg);
    } finally {
        submitting.value = false;
    }
};

const handleDelete = async (id) => {
    if (!confirm("Bạn có chắc muốn xóa chương trình này?")) return;
    try {
        await axios.delete(`${API_URL}/delete/${id}`);
        alert("Xóa thành công!");
        fetchPromotions();
    } catch (err) {
        alert("Lỗi khi xóa: " + err.message);
    }
};

// --- MODAL ACTIONS ---
const openAddModal = () => {
    isEditing.value = false;
    Object.keys(errors).forEach(k => errors[k] = '');
    Object.assign(formData, {
        id: null, maChuongTrinh: '', tenChuongTrinh: '', 
        phanTramGiam: 0, ngayBatDau: '', ngayKetThuc: '', 
        trangThai: true, moTa: ''
    });
    showModal.value = true;
};

const openEditModal = (item) => {
    isEditing.value = true;
    Object.keys(errors).forEach(k => errors[k] = '');
    Object.assign(formData, {
        id: item.id,
        maChuongTrinh: item.maChuongTrinh,
        tenChuongTrinh: item.tenChuongTrinh,
        phanTramGiam: item.phanTramGiam * 100, 
        ngayBatDau: item.ngayBatDau,
        ngayKetThuc: item.ngayKetThuc,
        trangThai: item.trangThai,
        moTa: item.moTa
    });
    showModal.value = true;
};

const closeModal = () => showModal.value = false;

onMounted(() => {
    fetchPromotions();
});
</script>

<template>
  <div class="p-6 bg-gray-50 min-h-screen font-sans">
    
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm border border-gray-100">
      <h1 class="text-2xl font-bold text-gray-800 flex items-center gap-2">
        <div class="p-2 bg-purple-100 rounded-lg">
             <Icon icon="mdi:sale" class="text-purple-600 w-6 h-6"/> 
        </div>
        Quản lý Đợt Giảm Giá
      </h1>
      <button @click="openAddModal" class="bg-purple-600 text-white px-4 py-2.5 rounded-lg hover:bg-purple-700 font-medium flex items-center gap-2 shadow-md transition-all">
        <Icon icon="mdi:plus" width="20" /> Tạo Chương Trình
      </button>
    </div>

    <div v-if="loading" class="text-center py-10">
         <Icon icon="eos-icons:loading" width="35" class="text-purple-500 animate-spin mx-auto"/>
         <p class="text-gray-500 mt-2">Đang tải dữ liệu...</p>
    </div>
    
    <div v-else-if="error" class="text-center py-10 text-red-500 bg-red-50 rounded border border-red-100">
        {{ error }}
    </div>

    <div v-else class="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
      <table class="min-w-full leading-normal">
        <thead>
          <tr class="bg-gray-50 text-left text-xs font-bold text-gray-500 uppercase tracking-wider border-b border-gray-200">
            <th class="px-5 py-4">Mã / Tên Chương Trình</th>
            <th class="px-5 py-4">Mức Giảm</th>
            <th class="px-5 py-4">Thời Gian</th>
            <th class="px-5 py-4">Trạng Thái</th>
            <th class="px-5 py-4 text-center">Thao Tác</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100">
          <tr v-if="promotions.length === 0">
              <td colspan="5" class="px-6 py-10 text-center text-gray-400 italic">Chưa có chương trình khuyến mãi nào.</td>
          </tr>
          <tr v-for="item in promotions" :key="item.id" class="hover:bg-purple-50/30 transition duration-150">
            
            <td class="px-5 py-4">
              <div class="flex items-center gap-3">
                  <div class="bg-purple-100 p-2 rounded-lg text-purple-600">
                      <Icon icon="mdi:tag-percent" width="24"/>
                  </div>
                  <div>
                      <div class="font-bold text-gray-800 text-sm uppercase tracking-wide">{{ item.maChuongTrinh }}</div>
                      <div class="text-xs text-gray-500 max-w-[200px] truncate" :title="item.tenChuongTrinh">{{ item.tenChuongTrinh }}</div>
                  </div>
              </div>
            </td>

            <td class="px-5 py-4">
                <span class="font-bold text-red-500 text-lg">
                    {{ formatPercent(item.phanTramGiam) }}
                </span>
            </td>

            <td class="px-5 py-4 text-sm text-gray-600 space-y-1">
                <div class="flex items-center gap-1.5">
                    <Icon icon="mdi:calendar-start" class="text-green-600" width="16"/> {{ formatDate(item.ngayBatDau) }}
                </div>
                <div class="flex items-center gap-1.5">
                    <Icon icon="mdi:calendar-end" class="text-red-600" width="16"/> {{ formatDate(item.ngayKetThuc) }}
                </div>
            </td>

            <td class="px-5 py-4">
              <span :class="getStatusBadge(item.trangThai, item.ngayKetThuc).class" class="px-2.5 py-1 rounded-full text-xs font-semibold flex items-center w-fit gap-1 border">
                 <Icon :icon="item.trangThai ? 'mdi:check-circle' : 'mdi:close-circle'" width="14"/>
                 {{ getStatusBadge(item.trangThai, item.ngayKetThuc).text }}
              </span>
            </td>

            <td class="px-5 py-4 text-center">
              <div class="flex justify-center gap-2">
                  <button @click="openEditModal(item)" class="text-blue-600 bg-blue-50 p-2 rounded-lg hover:bg-blue-100 transition" title="Sửa">
                    <Icon icon="mdi:pencil" width="18"/>
                  </button>
                  <button @click="handleDelete(item.id)" class="text-red-500 bg-red-50 p-2 rounded-lg hover:bg-red-100 transition" title="Xóa">
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
                            <Icon :icon="isEditing ? 'mdi:file-edit' : 'mdi:plus-box'" class="text-purple-600"/>
                            {{ isEditing ? 'Cập nhật Chương Trình' : 'Thêm Chương Trình Mới' }}
                        </h3>
                        <button @click="closeModal" class="text-gray-400 hover:text-gray-600 p-1 rounded-full hover:bg-gray-100 transition">
                            <Icon icon="mdi:close" width="24" />
                        </button>
                    </div>

                    <div class="p-6">
                        <form @submit.prevent="handleSave" class="space-y-4">
                            
                            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Mã Chương Trình <span class="text-red-500">*</span></label>
                                    <input v-model="formData.maChuongTrinh" :disabled="isEditing" type="text" 
                                           class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-purple-500 disabled:bg-gray-100 uppercase"
                                           :class="{'border-red-500 focus:ring-red-200': errors.maChuongTrinh}"
                                           placeholder="KM2024">
                                    <p v-if="errors.maChuongTrinh" class="text-red-500 text-xs mt-1">{{ errors.maChuongTrinh }}</p>
                                </div>
                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Tên Chương Trình <span class="text-red-500">*</span></label>
                                    <input v-model="formData.tenChuongTrinh" type="text" 
                                           class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-purple-500"
                                           :class="{'border-red-500 focus:ring-red-200': errors.tenChuongTrinh}"
                                           placeholder="Siêu sale tháng 5">
                                    <p v-if="errors.tenChuongTrinh" class="text-red-500 text-xs mt-1">{{ errors.tenChuongTrinh }}</p>
                                </div>
                            </div>

                            <div>
                                <label class="block text-sm font-medium text-gray-700 mb-1">
                                    Mức giảm giá (%) <span class="text-red-500">*</span>
                                </label>
                                <div class="relative">
                                    <input v-model="formData.phanTramGiam" type="number" step="1" min="1" max="100"
                                           class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-purple-500 pr-8"
                                           :class="{'border-red-500 focus:ring-red-200': errors.phanTramGiam}">
                                    <span class="absolute right-3 top-2 text-gray-500">%</span>
                                </div>
                                <p v-if="errors.phanTramGiam" class="text-red-500 text-xs mt-1">{{ errors.phanTramGiam }}</p>
                                <p class="text-xs text-gray-400 mt-1">Nhập giá trị từ 1 đến 100.</p>
                            </div>

                            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Ngày bắt đầu <span class="text-red-500">*</span></label>
                                    <input v-model="formData.ngayBatDau" type="date" 
                                           class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-purple-500"
                                           :class="{'border-red-500 focus:ring-red-200': errors.ngayBatDau}">
                                    <p v-if="errors.ngayBatDau" class="text-red-500 text-xs mt-1">{{ errors.ngayBatDau }}</p>
                                </div>
                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Ngày kết thúc <span class="text-red-500">*</span></label>
                                    <input v-model="formData.ngayKetThuc" type="date" 
                                           class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-purple-500"
                                           :class="{'border-red-500 focus:ring-red-200': errors.ngayKetThuc}">
                                    <p v-if="errors.ngayKetThuc" class="text-red-500 text-xs mt-1">{{ errors.ngayKetThuc }}</p>
                                </div>
                            </div>

                            <div>
                                <label class="block text-sm font-medium text-gray-700 mb-1">Mô tả chi tiết</label>
                                <textarea v-model="formData.moTa" rows="3" 
                                          class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-purple-500"
                                          placeholder="Điều kiện áp dụng, ghi chú..."></textarea>
                            </div>

                            <div class="flex items-center gap-3 mt-2 p-3 bg-gray-50 rounded-lg border">
                                <input type="checkbox" v-model="formData.trangThai" id="activeStatus" class="w-5 h-5 text-purple-600 rounded focus:ring-purple-500 cursor-pointer">
                                <label for="activeStatus" class="text-sm font-medium text-gray-700 cursor-pointer select-none">Kích hoạt chương trình ngay</label>
                            </div>

                        </form>
                    </div>

                    <div class="px-6 py-4 bg-gray-50 flex flex-row-reverse gap-3 border-t border-gray-100">
                         <button @click="handleSave" :disabled="submitting"
                            class="bg-purple-600 text-white px-5 py-2.5 rounded-lg hover:bg-purple-700 font-medium shadow-md flex items-center gap-2 disabled:opacity-70 transition-all">
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