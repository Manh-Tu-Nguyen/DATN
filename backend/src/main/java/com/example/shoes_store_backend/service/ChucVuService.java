package com.example.shoes_store_backend.service;

import com.example.shoes_store_backend.entity.ChucVu;
import com.example.shoes_store_backend.repository.ChucVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChucVuService {

    @Autowired
    private ChucVuRepository repository;

    // Lấy danh sách tất cả chức vụ
    public List<ChucVu> getAll() {
        return repository.findAll();
    }

    // Thêm mới chức vụ
    public ChucVu add(ChucVu chucVu) {
        if (repository.existsByMaChucVu(chucVu.getMaChucVu())) {
            throw new RuntimeException("Mã chức vụ đã tồn tại");
        }
        return repository.save(chucVu);
    }

    // Cập nhật chức vụ (nếu cần)
    public ChucVu update(Integer id, ChucVu chucVu) {
        ChucVu existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chức vụ có ID: " + id));

        existing.setMaChucVu(chucVu.getMaChucVu());
        existing.setTenChucVu(chucVu.getTenChucVu());
        existing.setTrangThai(chucVu.getTrangThai());

        return repository.save(existing);
    }

    // Xóa chức vụ
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Không tồn tại chức vụ có ID: " + id);
        }
        repository.deleteById(id);
    }
}
