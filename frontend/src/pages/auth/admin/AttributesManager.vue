<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue';
import axios from 'axios';
import { Icon } from '@iconify/vue';

// --- CẤU HÌNH CÁC LOẠI THUỘC TÍNH ---
const ATTRIBUTE_TYPES = {
  BRAND: {
    id: 'BRAND',
    name: 'Thương Hiệu',
    apiUrl: 'http://localhost:8080/api/thuong-hieu',
    fields: { code: 'maThuongHieu', name: 'tenThuongHieu' }, 
    icon: 'mdi:tag-text-outline'
  },
  ORIGIN: {
    id: 'ORIGIN',
    name: 'Xuất Xứ',
    apiUrl: 'http://localhost:8080/api/xuat-xu',
    fields: { code: 'maXuatXu', name: 'tenXuatXu' },
    icon: 'mdi:map-marker-radius'
  },
  COLOR: {
    id: 'COLOR',
    name: 'Màu Sắc',
    apiUrl: 'http://localhost:8080/api/mau-sac',
    fields: { code: 'maMau', name: 'tenMau' },
    icon: 'mdi:palette'
  },
  SIZE: {
    id: 'SIZE',
    name: 'Kích Cỡ',
    apiUrl: 'http://localhost:8080/api/size',
    fields: { code: 'maSize', name: 'tenSize' },
    icon: 'mdi:ruler'
  }
};

// --- STATE ---
const currentType = ref('BRAND'); 
const items = ref([]);
const loading = ref(false);
const submitting = ref(false);
const showModal = ref(false);
const isEditing = ref(false);

const currentConfig = computed(() => ATTRIBUTE_TYPES[currentType.value]);

const formData = reactive({
  id: null,
  code: '', 
  name: '', 
  trangThai: true,
  extra: '' 
});

// --- API ACTIONS ---

const fetchData = async () => {
  try {
    loading.value = true;
    const response = await axios.get(currentConfig.value.apiUrl);
    items.value = response.data;
  } catch (err) {
    console.error("Lỗi tải dữ liệu:", err);
    items.value = []; 
  } finally {
    loading.value = false;
  }
};

const handleSave = async () => {
  if (!formData.code || !formData.name) {
    alert("Vui lòng nhập đầy đủ mã và tên!");
    return;
  }

  try {
    submitting.value = true;
    
    const payload = {
        [currentConfig.value.fields.code]: formData.code,
        [currentConfig.value.fields.name]: formData.name,
        trangThai: formData.trangThai
    };


    if (isEditing.value) {
      await axios.put(`${currentConfig.value.apiUrl}/${formData.id}`, payload);
      alert("Cập nhật thành công!");
    } else {
      await axios.post(currentConfig.value.apiUrl, payload);
      alert("Thêm mới thành công!");
    }
    
    closeModal();
    fetchData();
  } catch (err) {
    alert("Lỗi: " + (err.response?.data?.message || err.message));
  } finally {
    submitting.value = false;
  }
};

const handleDelete = async (id) => {
  if (!confirm(`Bạn có chắc muốn xóa ${currentConfig.value.name} này?`)) return;
  try {
    await axios.delete(`${currentConfig.value.apiUrl}/${id}`);
    alert("Đã xóa thành công!");
    fetchData();
  } catch (err) {
    alert("Không thể xóa (có thể dữ liệu đang được sử dụng).");
  }
};

// --- HELPER ---
const getDynamicField = (item, fieldType) => {
    const fieldName = currentConfig.value.fields[fieldType];
    return item[fieldName];
};

const getStatusBadge = (status) => {
  return status 
    ? { text: 'Hoạt động', class: 'bg-green-100 text-green-700 border-green-200' }
    : { text: 'Ngưng', class: 'bg-red-100 text-red-700 border-red-200' };
};

// --- MODAL ---
const openAddModal = () => {
  isEditing.value = false;
  formData.id = null;
  formData.code = '';
  formData.name = '';
  formData.trangThai = true;
  formData.extra = '';
  showModal.value = true;
};

const openEditModal = (item) => {
  isEditing.value = true;
  formData.id = item.id;
  formData.code = item[currentConfig.value.fields.code];
  formData.name = item[currentConfig.value.fields.name];
  formData.trangThai = item.trangThai;
  showModal.value = true;
};

const closeModal = () => showModal.value = false;

watch(currentType, () => {
  fetchData();
});

onMounted(() => {
  fetchData();
});
</script>

<template>
  <div class="p-6 bg-gray-50 min-h-screen font-sans">
    
    <div class="flex flex-col md:flex-row justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm border border-gray-100 gap-4">
      
      <div class="flex items-center gap-3 w-full md:w-auto">
        <div class="p-2 bg-pink-100 rounded-lg">
             <Icon :icon="currentConfig.icon" class="text-pink-600 w-6 h-6" /> 
        </div>
        <div>
            <label class="block text-xs font-bold text-gray-500 uppercase">Đang quản lý</label>
            <div class="relative min-w-[200px]">
                <select v-model="currentType" class="appearance-none w-full bg-transparent font-bold text-gray-800 text-xl focus:outline-none cursor-pointer pr-6">
                    <option v-for="(cfg, key) in ATTRIBUTE_TYPES" :key="key" :value="key">
                        {{ cfg.name }}
                    </option>
                </select>
                <Icon icon="mdi:chevron-down" class="absolute right-0 top-1/2 -translate-y-1/2 text-gray-400 pointer-events-none"/>
            </div>
        </div>
      </div>

      <button @click="openAddModal" class="bg-pink-600 text-white px-5 py-2.5 rounded-lg hover:bg-pink-700 font-medium flex items-center gap-2 shadow-md transition-all w-full md:w-auto justify-center">
        <Icon icon="mdi:plus" width="20" /> Thêm {{ currentConfig.name }}
      </button>
    </div>

    <div class="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
        <div v-if="loading" class="p-10 text-center">
             <Icon icon="eos-icons:loading" width="35" class="text-pink-500 animate-spin mx-auto"/>
             <p class="text-gray-500 mt-2">Đang tải dữ liệu...</p>
        </div>
        
        <table v-else class="min-w-full leading-normal">
            <thead>
                <tr class="bg-gray-50 text-left text-xs font-bold text-gray-500 uppercase tracking-wider border-b border-gray-200">
                    <th class="px-6 py-4">Mã {{ currentConfig.name }}</th>
                    <th class="px-6 py-4">Tên {{ currentConfig.name }}</th>
                    <th class="px-6 py-4 text-center">Trạng Thái</th>
                    <th class="px-6 py-4 text-center">Thao Tác</th>
                </tr>
            </thead>
            <tbody class="divide-y divide-gray-100">
                <tr v-if="items.length === 0">
                    <td colspan="4" class="px-6 py-8 text-center text-gray-400 italic">Chưa có dữ liệu.</td>
                </tr>
                <tr v-for="item in items" :key="item.id" class="hover:bg-pink-50/30 transition">
                    
                    <td class="px-6 py-4 font-mono font-bold text-blue-600">
                        {{ getDynamicField(item, 'code') }}
                    </td>

                    <td class="px-6 py-4 font-medium text-gray-800">
                        <div class="flex items-center gap-2">
                             {{ getDynamicField(item, 'name') }}
                             <span v-if="currentType === 'COLOR'" 
                                   class="w-4 h-4 rounded-full border shadow-sm"
                                   :style="{ backgroundColor: getDynamicField(item, 'code') === 'M01' ? 'black' : 'gray' }">
                                   </span>
                        </div>
                    </td>

                    <td class="px-6 py-4 text-center">
                         <span :class="getStatusBadge(item.trangThai).class" class="px-2.5 py-1 rounded-full text-xs font-semibold inline-flex items-center gap-1 border">
                            <Icon :icon="item.trangThai ? 'mdi:check' : 'mdi:close'" width="14"/>
                            {{ getStatusBadge(item.trangThai).text }}
                         </span>
                    </td>

                    <td class="px-6 py-4 text-center">
                        <div class="flex justify-center gap-2">
                            <button @click="openEditModal(item)" class="text-blue-600 bg-blue-50 p-2 rounded-lg hover:bg-blue-100 transition">
                                <Icon icon="mdi:pencil" width="18"/>
                            </button>
                            <button @click="handleDelete(item.id)" class="text-red-500 bg-red-50 p-2 rounded-lg hover:bg-red-100 transition">
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
                <div class="relative bg-white rounded-2xl shadow-xl w-full max-w-md overflow-hidden border border-gray-100">
                    
                    <div class="px-6 py-4 border-b border-gray-100 flex justify-between items-center bg-gray-50/50">
                        <h3 class="text-xl font-bold text-gray-800 flex items-center gap-2">
                            <Icon :icon="currentConfig.icon" class="text-pink-600"/>
                            {{ isEditing ? 'Cập nhật' : 'Thêm mới' }} {{ currentConfig.name }}
                        </h3>
                        <button @click="closeModal" class="text-gray-400 hover:text-gray-600 p-1 rounded-full hover:bg-gray-100">
                            <Icon icon="mdi:close" width="24" />
                        </button>
                    </div>

                    <div class="p-6 space-y-4">
                        <form @submit.prevent="handleSave">
                            <div class="mb-4">
                                <label class="block text-sm font-medium text-gray-700 mb-1">Mã {{ currentConfig.name }} <span class="text-red-500">*</span></label>
                                <input v-model="formData.code" type="text" 
                                       class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500 uppercase font-mono"
                                       placeholder="Ví dụ: TH01">
                            </div>

                            <div class="mb-4">
                                <label class="block text-sm font-medium text-gray-700 mb-1">Tên {{ currentConfig.name }} <span class="text-red-500">*</span></label>
                                <div class="relative">
                                     <input v-model="formData.name" type="text" 
                                       class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500"
                                       placeholder="Nhập tên thuộc tính...">
                                     
                                     <input v-if="currentType === 'COLOR'" type="color" 
                                            class="absolute right-2 top-2 h-6 w-6 border-none p-0 bg-transparent cursor-pointer"
                                            title="Chọn màu minh họa">
                                </div>
                            </div>

                            <div class="flex items-center gap-3 p-3 bg-gray-50 rounded-lg border">
                                <input type="checkbox" v-model="formData.trangThai" id="status" class="w-5 h-5 text-pink-600 rounded focus:ring-pink-500 cursor-pointer">
                                <label for="status" class="text-sm font-medium text-gray-700 cursor-pointer select-none">Đang hoạt động</label>
                            </div>

                            <div class="mt-6 flex flex-row-reverse gap-3">
                                <button type="submit" :disabled="submitting" class="bg-pink-600 text-white px-5 py-2.5 rounded-lg hover:bg-pink-700 font-medium shadow-md flex items-center gap-2 w-full justify-center disabled:opacity-70">
                                    <Icon v-if="submitting" icon="eos-icons:loading" class="animate-spin"/>
                                    {{ submitting ? 'Đang lưu...' : 'Lưu lại' }}
                                </button>
                                <button type="button" @click="closeModal" class="bg-white text-gray-700 px-5 py-2.5 rounded-lg border border-gray-300 hover:bg-gray-50 font-medium w-full">
                                    Hủy bỏ
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
  </div>
</template>