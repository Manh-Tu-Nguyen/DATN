package com.example.shoes_store_backend.service;

import com.example.shoes_store_backend.entity.ChucVu;
import com.example.shoes_store_backend.repository.ChucVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ChucVuService {

    @Autowired
    private ChucVuRepository repository;

    public List<ChucVu> getAll() {
        return repository.findAll();
    }

    public ChucVu getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public ChucVu add(ChucVu cv) {
        if (repository.existsByMaChucVu(cv.getMaChucVu())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã chức vụ đã tồn tại");
        }
        cv.setId(null);
        return repository.save(cv);
    }

    public ChucVu update(Integer id, ChucVu cv) {
        return repository.findById(id).map(existing -> {
            existing.setTenChucVu(cv.getTenChucVu());
            existing.setTrangThai(cv.getTrangThai());
            return repository.save(existing);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy chức vụ"));
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy chức vụ");
        }

        repository.deleteById(id);
    }
}