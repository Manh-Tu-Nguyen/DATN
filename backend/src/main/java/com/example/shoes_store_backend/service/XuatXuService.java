package com.example.shoes_store_backend.service;

import com.example.shoes_store_backend.dto.XuatXuDTO;
import com.example.shoes_store_backend.entity.XuatXu;
import com.example.shoes_store_backend.repository.XuatXuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class XuatXuService {

    @Autowired
    private XuatXuRepository repository;

    public List<XuatXu> getAll() {
        return repository.findAll();
    }

    public XuatXu add(XuatXuDTO dto) {
        if (repository.existsByMaXuatXu(dto.getMaXuatXu())) {
            throw new RuntimeException("Mã xuất xứ đã tồn tại!");
        }
        XuatXu entity = new XuatXu();
        entity.setMaXuatXu(dto.getMaXuatXu());
        entity.setTenXuatXu(dto.getTenXuatXu());
        entity.setTrangThai(dto.getTrangThai());
        return repository.save(entity);
    }

    public XuatXu update(Integer id, XuatXuDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setMaXuatXu(dto.getMaXuatXu());
            existing.setTenXuatXu(dto.getTenXuatXu());
            existing.setTrangThai(dto.getTrangThai());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy xuất xứ ID: " + id));
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy xuất xứ để xóa");
        }
        repository.deleteById(id);
    }
}