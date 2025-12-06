<script setup>
import { ref, reactive, onMounted } from 'vue';
import axios from 'axios';
import { Icon } from '@iconify/vue';

// --- CẤU HÌNH API ---
const API_URL = 'http://localhost:8080/api/nhan-vien';
const API_ROLE_URL = 'http://localhost:8080/api/chuc-vu';

// --- STATE ---
const staffList = ref([]);
const roleList = ref([]); 
const loading = ref(true);
const submitting = ref(false);
const error = ref(null);

const showModal = ref(false);
const isEditing = ref(false);

const formData = reactive({
  id: null,
  maNhanVien: '',
  hoTen: '',
  idChucVu: '', 
  gioiTinh: true, 
  ngaySinh: '',
  email: '',
  taiKhoan: '',
  matKhau: '',
  luong: 0,
  diaChi: '',
  sdt: '',
  hinhAnh: '',
  trangThai: true
});

// --- HELPER FUNCTIONS ---
const formatCurrency = (value) => {
  if (!value) return '0 đ';
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

const formatDate = (dateString) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleDateString('vi-VN');
};

const formatGender = (gender) => gender ? 'Nam' : 'Nữ';

const getRoleName = (chucVuObj) => {
    if (!chucVuObj) return '---';
    return chucVuObj.tenChucVu || chucVuObj.ten || 'Nhân viên';
};

const getStatusBadge = (status) => {
  return status 
    ? { text: 'Đang làm việc', class: 'bg-green-100 text-green-700 border-green-200' }
    : { text: 'Đã nghỉ việc', class: 'bg-gray-100 text-gray-500 border-gray-200' };
};

// --- API ACTIONS ---

const fetchStaff = async () => {
  try {
    loading.value = true;
    const response = await axios.get(API_URL);
    staffList.value = response.data;
  } catch (err) {
    console.error("Lỗi tải nhân viên:", err);
    error.value = "Không thể kết nối đến Server.";
  } finally {
    loading.value = false;
  }
};

const fetchRoles = async () => {
    try {
        const res = await axios.get(API_ROLE_URL);
        roleList.value = res.data;
    } catch (err) {
        console.error("Lỗi tải chức vụ, dùng dữ liệu mẫu tạm thời.");
        roleList.value = [
            { id: 1, tenChucVu: 'Quản trị viên' },
            { id: 2, tenChucVu: 'Nhân viên bán hàng' },
            { id: 3, tenChucVu: 'Nhân viên kho' }
        ];
    }
}

const handleSave = async () => {
    if (!formData.maNhanVien || !formData.hoTen || !formData.taiKhoan) {
        alert("Vui lòng điền đầy đủ các trường bắt buộc (*)");
        return;
    }

    try {
        submitting.value = true;
        const payload = { ...formData };

        if (isEditing.value) {
            await axios.put(`${API_URL}/${formData.id}`, payload);
            alert("Cập nhật thành công!");
        } else {
            delete payload.id; 
            await axios.post(API_URL, payload);
            alert("Thêm nhân viên thành công!");
        }

        closeModal();
        fetchStaff(); 
    } catch (err) {
        console.error(err);
        const msg = err.response?.data?.message || err.response?.data || err.message;
        alert("Lỗi: " + (typeof msg === 'object' ? JSON.stringify(msg) : msg));
    } finally {
        submitting.value = false;
    }
};

const handleDelete = async (id) => {
    if (!confirm("Bạn có chắc chắn muốn xóa nhân viên này?")) return;
    try {
        await axios.delete(`${API_URL}/${id}`);
        alert("Đã xóa thành công!");
        fetchStaff();
    } catch (err) {
        alert("Lỗi khi xóa: " + err.message);
    }
};

// --- MODAL ACTIONS ---
const openAddModal = () => {
    isEditing.value = false;
    Object.assign(formData, {
        id: null, maNhanVien: '', hoTen: '', idChucVu: '', gioiTinh: true,
        ngaySinh: '', email: '', taiKhoan: '', matKhau: '', luong: 0,
        diaChi: '', sdt: '', hinhAnh: '', trangThai: true
    });
    showModal.value = true;
};

const openEditModal = (staff) => {
    isEditing.value = true;
    Object.assign(formData, {
        id: staff.id,
        maNhanVien: staff.maNhanVien,
        hoTen: staff.hoTen,
        idChucVu: staff.chucVu?.id || '', 
        gioiTinh: staff.gioiTinh,
        ngaySinh: staff.ngaySinh, 
        email: staff.email,
        taiKhoan: staff.taiKhoan,
        matKhau: staff.matKhau, 
        luong: staff.luong,
        diaChi: staff.diaChi,
        sdt: staff.sdt,
        hinhAnh: staff.hinhAnh,
        trangThai: staff.trangThai
    });
    showModal.value = true;
};

const closeModal = () => showModal.value = false;

// --- LIFECYCLE ---
onMounted(() => {
  fetchStaff();
  fetchRoles();
});
</script>

<template>
  <div class="p-6 bg-gray-50 min-h-screen">
    <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm border border-gray-100">
      <h1 class="text-2xl font-bold text-gray-800 flex items-center gap-2">
        <div class="p-2 bg-pink-100 rounded-lg">
             <Icon icon="mdi:card-account-details" class="text-pink-600 w-6 h-6" /> 
        </div>
        Quản lý Nhân Viên
      </h1>
      <button @click="openAddModal"
        class="bg-pink-600 text-white px-4 py-2.5 rounded-lg hover:bg-pink-700 font-medium flex items-center gap-2 shadow-md transition-all">
        <Icon icon="mdi:account-plus" width="20"/> Thêm nhân viên
      </button>
    </div>

    <div v-if="loading" class="text-center py-10">
        <Icon icon="eos-icons:loading" width="35" class="text-pink-500 animate-spin mx-auto"/>
        <p class="text-gray-500 mt-2">Đang tải danh sách...</p>
    </div>
    <div v-else-if="error" class="text-center py-10 text-red-500 bg-red-50 rounded border border-red-100">
      {{ error }}
    </div>

    <div v-else class="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
      <table class="min-w-full leading-normal">
        <thead>
          <tr class="bg-gray-50 text-left text-xs font-bold text-gray-500 uppercase tracking-wider border-b border-gray-200">
            <th class="px-5 py-4">Mã / Tài Khoản</th>
            <th class="px-5 py-4">Họ Tên & Chức Vụ</th>
            <th class="px-5 py-4">Liên Hệ</th>
            <th class="px-5 py-4">Lương & Ngày vào</th>
            <th class="px-5 py-4">Thông Tin Cá Nhân</th>
            <th class="px-5 py-4">Trạng Thái</th>
            <th class="px-5 py-4 text-center">Thao Tác</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100">
          <tr v-if="staffList.length === 0">
              <td colspan="7" class="px-6 py-10 text-center text-gray-400 italic">Chưa có dữ liệu</td>
          </tr>
          <tr v-for="staff in staffList" :key="staff.id" class="hover:bg-pink-50/30 transition duration-150">
            <td class="px-5 py-4">
              <div class="font-bold text-gray-800">{{ staff.maNhanVien }}</div>
              <div class="text-xs text-blue-600 mt-1 flex items-center gap-1 bg-blue-50 w-fit px-1.5 py-0.5 rounded">
                <Icon icon="mdi:account-key" width="12" /> {{ staff.taiKhoan }}
              </div>
            </td>

            <td class="px-5 py-4">
              <div class="flex items-center">
                <div class="flex-shrink-0 w-10 h-10">
                  <img class="w-full h-full rounded-full border border-gray-200 object-cover"
                    :src="staff.hinhAnh || `https://ui-avatars.com/api/?name=${staff.hoTen}&background=random`"
                    alt="" />
                </div>
                <div class="ml-3">
                  <p class="text-gray-900 font-bold text-sm">{{ staff.hoTen }}</p>
                  <span class="bg-purple-100 text-purple-700 text-[10px] px-2 py-0.5 rounded-full mt-1 inline-block font-semibold uppercase tracking-wide">
                    {{ getRoleName(staff.chucVu) }}
                  </span>
                </div>
              </div>
            </td>

            <td class="px-5 py-4 text-sm text-gray-600 space-y-1">
              <div class="flex items-center gap-2"><Icon icon="mdi:phone" class="text-gray-400" /> {{ staff.sdt }}</div>
              <div class="flex items-center gap-2 max-w-[150px] truncate" :title="staff.email"><Icon icon="mdi:email" class="text-gray-400" /> {{ staff.email }}</div>
            </td>

            <td class="px-5 py-4">
              <div class="font-bold text-green-600">{{ formatCurrency(staff.luong) }}</div>
              <div class="text-xs text-gray-400 mt-1">Ngày vào: {{ formatDate(staff.ngayTao) }}</div>
            </td>

            <td class="px-5 py-4 text-sm text-gray-600">
               <div class="flex items-center gap-1 mb-1">
                   <Icon :icon="staff.gioiTinh ? 'mdi:gender-male' : 'mdi:gender-female'" :class="staff.gioiTinh ? 'text-blue-500' : 'text-pink-500'"/>
                   {{ formatGender(staff.gioiTinh) }}
               </div>
               <div class="text-xs flex items-center gap-1"><Icon icon="mdi:cake-variant" class="text-gray-400"/> {{ formatDate(staff.ngaySinh) }}</div>
            </td>

            <td class="px-5 py-4">
              <span :class="getStatusBadge(staff.trangThai).class"
                class="px-2.5 py-1 rounded-full text-xs font-semibold flex items-center w-fit gap-1 border">
                <Icon :icon="staff.trangThai ? 'mdi:check-circle' : 'mdi:close-circle'" width="14"/>
                {{ getStatusBadge(staff.trangThai).text }}
              </span>
            </td>

            <td class="px-5 py-4 text-center">
              <div class="flex justify-center gap-2">
                <button @click="openEditModal(staff)" class="text-blue-600 bg-blue-50 p-2 rounded-lg hover:bg-blue-100 transition" title="Sửa">
                  <Icon icon="mdi:pencil" width="18" />
                </button>
                <button @click="handleDelete(staff.id)" class="text-red-500 bg-red-50 p-2 rounded-lg hover:bg-red-100 transition" title="Xóa">
                  <Icon icon="mdi:trash-can-outline" width="18" />
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
                <div class="relative bg-white rounded-2xl shadow-xl w-full max-w-4xl overflow-hidden border border-gray-100">
                    
                    <div class="px-6 py-4 border-b border-gray-100 flex justify-between items-center bg-gray-50/50">
                        <h3 class="text-xl font-bold text-gray-800 flex items-center gap-2">
                            <Icon :icon="isEditing ? 'mdi:account-edit' : 'mdi:account-plus'" class="text-pink-600"/>
                            {{ isEditing ? 'Cập nhật thông tin nhân viên' : 'Thêm nhân viên mới' }}
                        </h3>
                        <button @click="closeModal" class="text-gray-400 hover:text-gray-600 p-1 rounded-full hover:bg-gray-100 transition">
                            <Icon icon="mdi:close" width="24" />
                        </button>
                    </div>

                    <div class="p-6">
                        <form @submit.prevent="handleSave" class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            
                            <div class="space-y-4">
                                <h4 class="font-semibold text-gray-700 border-b pb-2 mb-3">Thông tin Tài khoản</h4>
                                
                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Mã NV <span class="text-red-500">*</span></label>
                                    <input v-model="formData.maNhanVien" :disabled="isEditing" type="text" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500 disabled:bg-gray-100" placeholder="NV001">
                                </div>
                                
                                <div class="grid grid-cols-2 gap-4">
                                    <div>
                                        <label class="block text-sm font-medium text-gray-700 mb-1">Tài khoản <span class="text-red-500">*</span></label>
                                        <input v-model="formData.taiKhoan" type="text" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500">
                                    </div>
                                    <div>
                                        <label class="block text-sm font-medium text-gray-700 mb-1">Mật khẩu <span class="text-red-500">*</span></label>
                                        <input v-model="formData.matKhau" type="password" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500">
                                    </div>
                                </div>

                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Chức vụ <span class="text-red-500">*</span></label>
                                    <select v-model="formData.idChucVu" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500 bg-white">
                                        <option value="" disabled>-- Chọn chức vụ --</option>
                                        <option v-for="role in roleList" :key="role.id" :value="role.id">{{ role.tenChucVu }}</option>
                                    </select>
                                </div>

                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Lương cơ bản</label>
                                    <input v-model="formData.luong" type="number" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500">
                                </div>
                                
                                <div class="flex items-center gap-3 mt-4 p-3 bg-gray-50 rounded-lg border">
                                    <input type="checkbox" v-model="formData.trangThai" id="status" class="w-5 h-5 text-pink-600 rounded focus:ring-pink-500">
                                    <label for="status" class="text-sm font-medium text-gray-700 cursor-pointer select-none">Đang làm việc</label>
                                </div>
                            </div>

                            <div class="space-y-4">
                                <h4 class="font-semibold text-gray-700 border-b pb-2 mb-3">Thông tin Cá nhân</h4>

                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Họ và Tên <span class="text-red-500">*</span></label>
                                    <input v-model="formData.hoTen" type="text" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500" placeholder="Nguyễn Văn A">
                                </div>

                                <div class="grid grid-cols-2 gap-4">
                                    <div>
                                        <label class="block text-sm font-medium text-gray-700 mb-1">Ngày sinh</label>
                                        <input v-model="formData.ngaySinh" type="date" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500">
                                    </div>
                                    <div>
                                        <label class="block text-sm font-medium text-gray-700 mb-1">Giới tính</label>
                                        <select v-model="formData.gioiTinh" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500 bg-white">
                                            <option :value="true">Nam</option>
                                            <option :value="false">Nữ</option>
                                        </select>
                                    </div>
                                </div>

                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
                                    <input v-model="formData.email" type="email" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500">
                                </div>
                                
                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Số điện thoại</label>
                                    <input v-model="formData.sdt" type="text" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500">
                                </div>

                                <div>
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Địa chỉ</label>
                                    <textarea v-model="formData.diaChi" rows="2" class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-pink-500"></textarea>
                                </div>
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