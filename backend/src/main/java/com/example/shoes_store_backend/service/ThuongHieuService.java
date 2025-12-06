package com.example.shoes_store_backend.service;

import com.example.shoes_store_backend.dto.ThuongHieuDTO;
import com.example.shoes_store_backend.entity.ThuongHieu;
import com.example.shoes_store_backend.repository.ThuongHieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThuongHieuService {

    @Autowired
    private ThuongHieuRepository repository;

    public List<ThuongHieu> getAll() {
        return repository.findAll();
    }

    public ThuongHieu add(ThuongHieuDTO dto) {
        if (repository.existsByMaThuongHieu(dto.getMaThuongHieu())) {
            throw new RuntimeException("Mã thương hiệu đã tồn tại!");
        }
        ThuongHieu entity = new ThuongHieu();
        entity.setMaThuongHieu(dto.getMaThuongHieu());
        entity.setTenThuongHieu(dto.getTenThuongHieu());
        entity.setTrangThai(dto.getTrangThai());
        return repository.save(entity);
    }

    public ThuongHieu update(Integer id, ThuongHieuDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setMaThuongHieu(dto.getMaThuongHieu());
            existing.setTenThuongHieu(dto.getTenThuongHieu());
            existing.setTrangThai(dto.getTrangThai());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu ID: " + id));
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy thương hiệu để xóa");
        }
        repository.deleteById(id);
    }
}