package com.example.shoes_store_backend.service;

import com.example.shoes_store_backend.entity.NhanVien;
import com.example.shoes_store_backend.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class NhanVienService {

    @Autowired
    private NhanVienRepository repository;

    // Lấy tất cả nhân viên
    public List<NhanVien> getAll() {
        return repository.findAll();
    }

    // Thêm nhân viên mới
    public NhanVien add(NhanVien nv) {

        // Kiểm tra mã nhân viên tồn tại chưa
        if (repository.existsByMaNhanVien(nv.getMaNhanVien())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã nhân viên đã tồn tại");
        }

        // Kiểm tra tài khoản tồn tại chưa
        if (repository.existsByTaiKhoan(nv.getTaiKhoan())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản đã tồn tại");
        }

        return repository.save(nv);
    }

    // Sửa nhân viên theo id
    public NhanVien update(Integer id, NhanVien nv) {

        Optional<NhanVien> optional = repository.findById(id);
        if (!optional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên với id = " + id);
        }

        NhanVien existing = optional.get();
        existing.setMaNhanVien(nv.getMaNhanVien());
        existing.setHoTen(nv.getHoTen());
        existing.setTaiKhoan(nv.getTaiKhoan());
        existing.setMatKhau(nv.getMatKhau());
        existing.setTrangThai(nv.getTrangThai());

        return repository.save(existing);
    }

    // Xóa nhân viên theo id
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên với id = " + id);
        }
        repository.deleteById(id);
    }
}
