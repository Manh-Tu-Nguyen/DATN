package com.example.shoes_store_backend.service;

import com.example.shoes_store_backend.dto.hoaDon_DTO;
import com.example.shoes_store_backend.entity.hoaDon_Entity;
import com.example.shoes_store_backend.repository.hoaDon_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class hoaDon_Service {
    @Autowired
    private hoaDon_Repository repo;

    // lấy danh sách hóa đơn
    public List<hoaDon_DTO> getAll() {
        return repo.getAllHoaDon();
    }

    // tìm kiếm theo mã
    public List<hoaDon_DTO> search(String keyword) {
        return repo.searchHoaDon(keyword);
    }

    // lấy 1 hóa đơn theo id
    public hoaDon_Entity getById(Integer id) {
        return repo.findById(id).orElse(null);
    }

}
