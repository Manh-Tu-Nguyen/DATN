package com.example.shoes_store_backend.controller;

import jakarta.validation.Valid;
import com.example.shoes_store_backend.dto.MauSacDTO;
import com.example.shoes_store_backend.entity.MauSac;
import com.example.shoes_store_backend.service.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mau_sac")
public class MauSacController {

    @Autowired
    private MauSacService mauSacService;

    @GetMapping("/get_all")
    public ResponseEntity<List<MauSac>> getAll() {
        return ResponseEntity.ok(mauSacService.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody MauSacDTO dto) {
        try {
            MauSac entity = new MauSac();
            entity.setMaMau(dto.getMaMau());
            entity.setTenMau(dto.getTenMau());
            entity.setTrangThai(dto.getTrangThai());
            mauSacService.add(entity);
            return ResponseEntity.ok("Thêm màu sắc thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody MauSacDTO dto) {
        try {
            MauSac entity = new MauSac();
            entity.setMaMau(dto.getMaMau());
            entity.setTenMau(dto.getTenMau());
            entity.setTrangThai(dto.getTrangThai());
            mauSacService.update(id, entity);
            return ResponseEntity.ok("Cập nhật màu sắc thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            mauSacService.delete(id);
            return ResponseEntity.ok("Xóa màu sắc thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Lỗi: " + e.getMessage());
        }
    }
}
