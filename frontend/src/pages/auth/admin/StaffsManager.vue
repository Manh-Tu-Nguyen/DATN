<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { Icon } from '@iconify/vue';

const staffList = ref([]);
const loading = ref(true);
const error = ref(null);


const formatCurrency = (value) => {
    if (!value) return '0 đ';
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

const formatDate = (dateString) => {
    if (!dateString) return '';
    return new Date(dateString).toLocaleDateString('vi-VN');
};

const formatGender = (gender) => {
    if (gender === null) return 'N/A';
    return gender ? 'Nam' : 'Nữ'; 
};

const getRoleName = (chucVuObj) => {
    if (!chucVuObj) return 'Chưa phân quyền';
    return chucVuObj.ten || chucVuObj.tenChucVu || 'Nhân viên'; 
};

// Badge trạng thái
const getStatusBadge = (status) => {
    if (status === true) {
        return { text: 'Đang làm việc', class: 'bg-green-100 text-green-700 border border-green-200' };
    } else {
        return { text: 'Đã nghỉ việc', class: 'bg-gray-200 text-gray-600 border border-gray-300' };
    }
};

const fetchStaff = async () => {
    try {
        loading.value = true;
        const response = await axios.get('http://localhost:8080/api/nhan_vien/get_all');
        staffList.value = response.data;
        loading.value = false;
    } catch (err) {
        console.error("Lỗi tải nhân viên:", err);
        error.value = "Không thể kết nối đến Server.";
        loading.value = false;
    }
};

onMounted(() => {
    fetchStaff();
});
</script>

<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800 flex items-center gap-2">
        <Icon icon="mdi:card-account-details" class="text-pink-600"/> Quản lý Nhân Viên
      </h1>
      <button class="bg-pink-600 text-white px-4 py-2 rounded hover:bg-pink-700 font-medium flex items-center gap-2 shadow transition transform hover:-translate-y-0.5">
        <Icon icon="mdi:account-plus" /> Thêm nhân viên
      </button>
    </div>

    <div v-if="loading" class="text-center py-10 text-gray-500">
        Đang tải danh sách nhân sự...
    </div>

    <div v-else-if="error" class="text-center py-10 text-red-500 bg-red-50 rounded">
        {{ error }}
    </div>

    <div v-else class="bg-white rounded-lg shadow overflow-hidden border border-gray-200">
      <table class="min-w-full leading-normal">
        <thead>
          <tr class="bg-gray-100 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
            <th class="px-5 py-3 border-b-2 border-gray-200">Mã / Tài Khoản</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Họ Tên & Chức Vụ</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Liên Hệ</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Lương & Ngày vào</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Thông Tin Cá Nhân</th>
            <th class="px-5 py-3 border-b-2 border-gray-200">Trạng Thái</th>
            <th class="px-5 py-3 border-b-2 border-gray-200 text-center">Thao Tác</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="staff in staffList" :key="staff.id" class="hover:bg-pink-50 transition duration-150">
            
            <td class="px-5 py-4 border-b border-gray-200 text-sm">
              <div class="font-bold text-gray-700">{{ staff.maNhanVien }}</div>
              <div class="text-xs text-blue-500 mt-1 flex items-center gap-1">
                 <Icon icon="mdi:account-key" width="12"/> {{ staff.taiKhoan }}
              </div>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm">
                <div class="flex items-center">
                    <div class="flex-shrink-0 w-10 h-10">
                        <img class="w-full h-full rounded-full border border-gray-300 object-cover"
                             :src="staff.hinhAnh ? staff.hinhAnh : `https://ui-avatars.com/api/?name=${staff.hoTen}&background=random`"
                             alt="" />
                    </div>
                    <div class="ml-3">
                        <p class="text-gray-900 whitespace-no-wrap font-bold text-base">
                            {{ staff.hoTen }}
                        </p>
                        <span class="bg-blue-100 text-blue-800 text-xs px-2 py-0.5 rounded-full mt-1 inline-block">
                            {{ getRoleName(staff.chucVu) }}
                        </span>
                    </div>
                </div>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm text-gray-600">
                <div class="flex items-center gap-2 mb-1">
                    <Icon icon="mdi:phone" class="text-gray-400"/> {{ staff.sdt }}
                </div>
                <div class="flex items-center gap-2 mb-1 truncate max-w-[150px]" :title="staff.email">
                    <Icon icon="mdi:email" class="text-gray-400"/> {{ staff.email }}
                </div>
                <div class="flex items-center gap-2 text-xs text-gray-500 truncate max-w-[150px]" :title="staff.diaChi">
                    <Icon icon="mdi:map-marker" class="text-gray-400"/> {{ staff.diaChi }}
                </div>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm">
                <div class="font-bold text-green-600">
                    {{ formatCurrency(staff.luong) }}
                </div>
                <div class="text-xs text-gray-500 mt-1">
                    Ngày vào: {{ formatDate(staff.ngayTao) }}
                </div>
            </td>

             <td class="px-5 py-4 border-b border-gray-200 text-sm text-gray-600">
                <div>Giới tính: <strong>{{ formatGender(staff.gioiTinh) }}</strong></div>
                <div class="text-xs mt-1">Sinh nhật: {{ formatDate(staff.ngaySinh) }}</div>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm">
              <span :class="getStatusBadge(staff.trangThai).class" class="px-3 py-1 rounded-full text-xs font-semibold flex items-center w-fit gap-1">
                 <Icon v-if="staff.trangThai" icon="mdi:check-circle-outline" />
                 <Icon v-else icon="mdi:close-circle-outline" />
                 {{ getStatusBadge(staff.trangThai).text }}
              </span>
            </td>

            <td class="px-5 py-4 border-b border-gray-200 text-sm text-center">
              <button class="text-blue-600 hover:text-blue-800 mx-2 p-1 hover:bg-blue-100 rounded" title="Sửa">
                <Icon icon="mdi:account-edit" width="20"/>
              </button>
              <button class="text-red-500 hover:text-red-700 mx-2 p-1 hover:bg-red-100 rounded" title="Xóa/Nghỉ việc">
                 <Icon icon="mdi:account-off" width="20"/>
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <div v-if="!loading && staffList.length === 0" class="flex flex-col items-center justify-center py-10 text-gray-400">
        <Icon icon="mdi:account-search-outline" width="64" class="mb-3 opacity-50"/>
        <p>Chưa có nhân viên nào trong danh sách.</p>
      </div>
    </div>
  </div>
</template>