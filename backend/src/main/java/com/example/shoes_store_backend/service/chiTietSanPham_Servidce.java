package com.example.shoes_store_backend.service;

import com.example.shoes_store_backend.dto.chiTietSanPham_DTO;
import com.example.shoes_store_backend.entity.chiTietSanPham_Entity;
import com.example.shoes_store_backend.repository.chiTietSanPham_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class chiTietSanPham_Servidce {
    @Autowired
    private chiTietSanPham_Repository chiTietSanPham_repository;

    public List<chiTietSanPham_DTO> getAllChiTietSanPham() {
        return chiTietSanPham_repository.getAllChiTietSanPham();
    }

    public chiTietSanPham_Entity add(chiTietSanPham_Entity chiTietSanPham_entity) {
        return chiTietSanPham_repository.save(chiTietSanPham_entity);
    }

    public boolean delete(Integer id) {
        Optional<chiTietSanPham_Entity> entity = chiTietSanPham_repository.findById(id);
        if (entity.isPresent()) {
            chiTietSanPham_repository.delete(entity.get());
            return true;
        }
        return false;
    }
    public chiTietSanPham_Entity update(Integer id, chiTietSanPham_Entity chiTietSanPham_entity) {
        Optional<chiTietSanPham_Entity> opt = chiTietSanPham_repository.findById(id);
        if (opt.isEmpty()) {
            return null;
        }

        chiTietSanPham_Entity existing = opt.get();
        existing.setIdSanPham(chiTietSanPham_entity.getIdSanPham());
        existing.setIdSize(chiTietSanPham_entity.getIdSize());
        existing.setIdMauSac(chiTietSanPham_entity.getIdMauSac());
        existing.setIdChiTietKhuyenMai(chiTietSanPham_entity.getIdChiTietKhuyenMai());
        existing.setGiaBan(chiTietSanPham_entity.getGiaBan());
        existing.setSoLuongTon(chiTietSanPham_entity.getSoLuongTon());
        existing.setTrangThai(chiTietSanPham_entity.getTrangThai());
        existing.setNgayTao(chiTietSanPham_entity.getNgayTao());

        return chiTietSanPham_repository.save(existing);
    }

}

