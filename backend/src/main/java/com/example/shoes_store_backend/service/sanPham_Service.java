package com.example.shoes_store_backend.service;

import com.example.shoes_store_backend.dto.sanPham_DTO;
import com.example.shoes_store_backend.entity.sanPham_Entity;
import com.example.shoes_store_backend.repository.sanPham_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class sanPham_Service {
    @Autowired
    private sanPham_Repository sp_repository;

    public List<sanPham_DTO> getAllSanPham(){
        return sp_repository.getAllSanPham();
    }

    public sanPham_Entity getById(Integer id) {
        return sp_repository.findById(id).orElse(null);
    }

    public sanPham_Entity add(sanPham_Entity sanPham){
        sanPham.setId(null);
        return sp_repository.save(sanPham);
    }

    public sanPham_Entity update(Integer id, sanPham_Entity sanPhamUpdate) {
        return sp_repository.findById(id).map(existing -> {
            existing.setMaSanPham(sanPhamUpdate.getMaSanPham());
            existing.setTenSanPham(sanPhamUpdate.getTenSanPham());
            existing.setIdThuongHieu(sanPhamUpdate.getIdThuongHieu());
            existing.setIdXuatXu(sanPhamUpdate.getIdXuatXu());
            existing.setTrangThai(sanPhamUpdate.getTrangThai());
            // Không cập nhật ngày tạo
            return sp_repository.save(existing);
        }).orElse(null);
    }

    public boolean delete(Integer id) {
        if (sp_repository.existsById(id)) {
            sp_repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<sanPham_DTO> search(String keyword){
        return sp_repository.search(keyword);
    }
}