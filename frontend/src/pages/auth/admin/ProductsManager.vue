<script setup>
import { ref, reactive, onMounted } from 'vue';
import axios from 'axios';
import { Icon } from '@iconify/vue';

const API_URL = 'http://localhost:8080/api/san-pham';

const products = ref([]);
const loading = ref(false);
const showModal = ref(false);
const isEditing = ref(false);
const submitting = ref(false);

const formData = reactive({
  id: null,
  maSanPham: '',
  tenSanPham: '',
  idThuongHieu: null, 
  idXuatXu: null,
  trangThai: true
});

const listThuongHieu = ref([
  { id: 1, ten: 'Nike' }, { id: 2, ten: 'Adidas' }, { id: 3, ten: 'Puma' },
  { id: 4, ten: 'Bata' }, { id: 5, ten: 'Apex' }
]);
const listXuatXu = ref([
  { id: 1, ten: 'Việt Nam' }, { id: 2, ten: 'Mỹ' }, { id: 3, ten: 'Trung Quốc' }
]);

const fetchProducts = async () => {
  try {
    loading.value = true;
    const response = await axios.get(API_URL);
    products.value = response.data;
  } catch (err) {
    console.error("Lỗi tải dữ liệu:", err);
  } finally {
    loading.value = false;
  }
};

const handleSave = async () => {
  if (!formData.maSanPham || !formData.tenSanPham) {
    alert("Vui lòng nhập Mã và Tên sản phẩm!");
    return;
  }

  try {
    submitting.value = true;
    const payload = { 
        ...formData,
        idThuongHieu: formData.idThuongHieu || null,
        idXuatXu: formData.idXuatXu || null
    };

    if (isEditing.value) {
      await axios.put(`${API_URL}/${formData.id}`, payload);
      alert('Cập nhật thành công!');
    } else {
      delete payload.id; 
      await axios.post(API_URL, payload);
      alert('Thêm mới thành công!');
    }
    
    await fetchProducts(); 
    closeModal();
  } catch (err) {
    console.error(err);
    alert('Lỗi: ' + (err.response?.data || err.message));
  } finally {
    submitting.value = false;
  }
};

const handleDelete = async (id) => {
  if(!confirm("Bạn có chắc chắn muốn xóa sản phẩm này không?")) return;
  
  try {
    await axios.delete(`${API_URL}/${id}`);
    products.value = products.value.filter(item => item.id !== id);
    alert("Đã xóa thành công!");
  } catch (err) {
    console.error(err);
    alert("Lỗi khi xóa!");
  }
}

// --- MODAL ---
const openAddModal = () => {
  isEditing.value = false;
  Object.assign(formData, { 
    id: null, maSanPham: '', tenSanPham: '', 
    idThuongHieu: null, idXuatXu: null, trangThai: true 
  });
  showModal.value = true;
};

const openEditModal = (item) => {
  isEditing.value = true;
  Object.assign(formData, {
      id: item.id,
      maSanPham: item.maSanPham,
      tenSanPham: item.tenSanPham,
      idThuongHieu: item.idThuongHieu,
      idXuatXu: item.idXuatXu,
      trangThai: item.trangThai
  });
  showModal.value = true;
};

const closeModal = () => showModal.value = false;
const getTHName = (id) => listThuongHieu.value.find(x => x.id === id)?.ten || '---';
const getXXName = (id) => listXuatXu.value.find(x => x.id === id)?.ten || '---';

onMounted(() => {
  fetchProducts();
});
</script>

<template>
  <div class="p-6 bg-gray-50 min-h-screen font-sans">
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm border border-gray-100">
      <h1 class="text-2xl font-bold text-gray-800 flex items-center gap-3">
        <div class="p-2 bg-pink-100 rounded-lg">
          <Icon icon="icon-park-solid:sport" class="text-pink-600 w-6 h-6" /> 
        </div>
        Quản lý Sản Phẩm
      </h1>
      <button @click="openAddModal" class="bg-pink-600 text-white px-5 py-2.5 rounded-lg shadow-md hover:bg-pink-700 transition-all duration-200 flex items-center gap-2 font-medium">
        <Icon icon="mdi:plus-circle-outline" width="20" /> Thêm mới
      </button>
    </div>

    <div class="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
      <div v-if="loading" class="p-10 text-center text-gray-500 flex flex-col items-center">
        <Icon icon="eos-icons:loading" width="30" class="mb-2 text-pink-500 animate-spin"/>
        Đang tải dữ liệu...
      </div>
      
      <table v-else class="min-w-full leading-normal">
        <thead class="bg-gray-50 text-xs font-bold text-gray-500 uppercase tracking-wider border-b border-gray-200">
          <tr>
            <th class="px-6 py-4 text-left">Mã SP</th>
            <th class="px-6 py-4 text-left">Tên Sản Phẩm</th>
            <th class="px-6 py-4 text-left">Thông tin chi tiết</th>
            <th class="px-6 py-4 text-center">Trạng Thái</th>
            <th class="px-6 py-4 text-center w-36">Thao Tác</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100">
          <tr v-if="products.length === 0">
            <td colspan="5" class="px-6 py-8 text-center text-gray-400 italic">
              Chưa có dữ liệu nào. Hãy thêm mới!
            </td>
          </tr>
          <tr v-for="item in products" :key="item.id" class="hover:bg-gray-50 transition-colors duration-150">
            <td class="px-6 py-4 text-sm font-bold text-blue-600 font-mono bg-blue-50/50 w-32">
              {{ item.maSanPham }}
            </td>
            <td class="px-6 py-4 text-sm font-semibold text-gray-800">
              {{ item.tenSanPham }}
            </td>
            <td class="px-6 py-4 text-sm text-gray-600 space-y-1.5">
              <div class="flex items-center gap-2">
                <Icon icon="mdi:tag-outline" class="text-gray-400 w-4 h-4"/> 
                <span class="font-medium">TH:</span> {{ getTHName(item.idThuongHieu) }}
              </div>
              <div class="flex items-center gap-2">
                <Icon icon="mdi:map-marker-outline" class="text-gray-400 w-4 h-4"/> 
                <span class="font-medium">XX:</span> {{ getXXName(item.idXuatXu) }}
              </div>
            </td>
            <td class="px-6 py-4 text-center">
              <span v-if="item.trangThai" class="inline-flex items-center gap-1 bg-green-100 text-green-700 text-xs px-2.5 py-1 rounded-full font-semibold border border-green-200 shadow-sm">
                <span class="w-1.5 h-1.5 rounded-full bg-green-500"></span> Hoạt động
              </span>
              <span v-else class="inline-flex items-center gap-1 bg-red-100 text-red-700 text-xs px-2.5 py-1 rounded-full font-semibold border border-red-200 shadow-sm">
                <span class="w-1.5 h-1.5 rounded-full bg-red-500"></span> Ngưng
              </span>
            </td>
            <td class="px-6 py-4 text-center">
              <div class="flex justify-center gap-2">
                <button @click="openEditModal(item)" class="group text-blue-600 hover:bg-blue-100 p-2 rounded-lg transition-colors" title="Chỉnh sửa">
                  <Icon icon="mdi:pencil" width="20" class="group-hover:scale-110 transition-transform"/>
                </button>
                <button @click="handleDelete(item.id)" class="group text-red-500 hover:bg-red-100 p-2 rounded-lg transition-colors" title="Xóa">
                  <Icon icon="mdi:trash-can-outline" width="20" class="group-hover:scale-110 transition-transform"/>
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showModal" class="relative z-[999]" aria-labelledby="modal-title" role="dialog" aria-modal="true">
      <div class="fixed inset-0 bg-gray-900/60 backdrop-blur-sm transition-opacity" @click="closeModal"></div>
      <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
        <div class="flex min-h-full items-center justify-center p-4 text-center sm:p-0">
          <div class="relative transform overflow-hidden rounded-2xl bg-white text-left shadow-2xl transition-all sm:my-8 sm:w-full sm:max-w-lg border border-gray-100">
            <div class="bg-white px-6 py-4 border-b border-gray-100 flex justify-between items-center">
              <h3 class="text-xl font-bold text-gray-900 flex items-center gap-2">
                 <div class="p-1.5 bg-pink-50 rounded-md">
                   <Icon :icon="isEditing ? 'mdi:file-edit' : 'mdi:plus-box'" class="text-pink-600 w-6 h-6"/>
                 </div>
                 {{ isEditing ? 'Cập nhật sản phẩm' : 'Thêm sản phẩm mới' }}
              </h3>
              <button @click="closeModal" class="text-gray-400 hover:text-gray-600 hover:bg-gray-100 p-1 rounded-full transition-colors">
                <Icon icon="mdi:close" width="24" />
              </button>
            </div>
            <div class="bg-white px-6 py-6">
              <form @submit.prevent="handleSave" class="space-y-5">
                <div>
                  <label class="block text-sm font-semibold text-gray-700 mb-1.5">Mã Sản Phẩm <span class="text-red-500">*</span></label>
                  <div class="relative">
                    <input v-model="formData.maSanPham" required type="text" class="w-full pl-10 pr-4 py-2.5 bg-gray-50 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pink-500/20 focus:border-pink-500 focus:bg-white outline-none transition-all" placeholder="Ví dụ: SP001">
                    <Icon icon="mdi:barcode" class="absolute left-3 top-3 text-gray-400 pointer-events-none"/>
                  </div>
                </div>
                <div>
                  <label class="block text-sm font-semibold text-gray-700 mb-1.5">Tên Sản Phẩm <span class="text-red-500">*</span></label>
                  <div class="relative">
                    <input v-model="formData.tenSanPham" required type="text" class="w-full pl-10 pr-4 py-2.5 bg-gray-50 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pink-500/20 focus:border-pink-500 focus:bg-white outline-none transition-all" placeholder="Nhập tên sản phẩm...">
                    <Icon icon="mdi:shoe-sneaker" class="absolute left-3 top-3 text-gray-400 pointer-events-none"/>
                  </div>
                </div>
                <div class="grid grid-cols-2 gap-5">
                  <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-1.5">Thương hiệu</label>
                    <div class="relative">
                      <select v-model="formData.idThuongHieu" class="appearance-none w-full pl-3 pr-10 py-2.5 bg-gray-50 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pink-500/20 focus:border-pink-500 focus:bg-white outline-none transition-all cursor-pointer">
                        <option :value="null" disabled>-- Chọn --</option>
                        <option v-for="t in listThuongHieu" :key="t.id" :value="t.id">{{ t.ten }}</option>
                      </select>
                      <Icon icon="mdi:chevron-down" class="absolute right-3 top-3 text-gray-500 pointer-events-none"/>
                    </div>
                  </div>
                  <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-1.5">Xuất xứ</label>
                    <div class="relative">
                      <select v-model="formData.idXuatXu" class="appearance-none w-full pl-3 pr-10 py-2.5 bg-gray-50 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pink-500/20 focus:border-pink-500 focus:bg-white outline-none transition-all cursor-pointer">
                        <option :value="null" disabled>-- Chọn --</option>
                        <option v-for="x in listXuatXu" :key="x.id" :value="x.id">{{ x.ten }}</option>
                      </select>
                      <Icon icon="mdi:chevron-down" class="absolute right-3 top-3 text-gray-500 pointer-events-none"/>
                    </div>
                  </div>
                </div>
                <div class="flex items-center p-3 bg-gray-50 rounded-lg border border-gray-200 hover:border-pink-300 transition-colors cursor-pointer" @click="formData.trangThai = !formData.trangThai">
                  <div class="relative flex items-center">
                    <input id="active" v-model="formData.trangThai" type="checkbox" class="peer h-5 w-5 text-pink-600 rounded border-gray-300 focus:ring-pink-500 cursor-pointer">
                  </div>
                  <label for="active" class="ml-3 text-sm font-medium text-gray-700 cursor-pointer select-none">Đang kinh doanh</label>
                </div>
              </form>
            </div>
            <div class="bg-gray-50 px-6 py-4 flex flex-row-reverse gap-3 border-t border-gray-100">
              <button @click="handleSave" :disabled="submitting" class="inline-flex justify-center rounded-lg bg-pink-600 px-5 py-2.5 text-sm font-semibold text-white shadow-md hover:bg-pink-700 focus:ring-2 focus:ring-offset-2 focus:ring-pink-500 transition-all disabled:opacity-70 disabled:cursor-not-allowed items-center gap-2 min-w-[100px]">
                <Icon v-if="submitting" icon="eos-icons:loading" class="animate-spin"/>
                {{ submitting ? 'Đang lưu...' : 'Lưu lại' }}
              </button>
              <button @click="closeModal" class="inline-flex justify-center rounded-lg bg-white px-5 py-2.5 text-sm font-semibold text-gray-700 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 hover:text-gray-900 transition-all">
                Hủy bỏ
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>