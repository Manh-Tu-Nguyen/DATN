package com.example.shoes_store_backend.service;

import com.example.shoes_store_backend.entity.Size;
import com.example.shoes_store_backend.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    public List<Size> getAll() {
        return sizeRepository.findAll();
    }

    public Size add(Size size) {
        if (size.getNgayTao() == null) {
            size.setNgayTao(new Date());
        }
        return sizeRepository.save(size);
    }

    public Size update(Integer id, Size dto) {
        Size entity = sizeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy size với ID: " + id));
        entity.setMaSize(dto.getMaSize());
        entity.setTenSize(dto.getTenSize());
        entity.setTrangThai(dto.getTrangThai());
        // giữ nguyên ngayTao
        return sizeRepository.save(entity);
    }

    public void delete(Integer id) {
        Size entity = sizeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy size với ID: " + id));
        sizeRepository.delete(entity);
    }
}
