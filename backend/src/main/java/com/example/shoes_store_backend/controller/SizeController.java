package com.example.shoes_store_backend.controller;

import jakarta.validation.Valid;
import com.example.shoes_store_backend.dto.SizeDTO;
import com.example.shoes_store_backend.entity.Size;
import com.example.shoes_store_backend.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/size")
public class SizeController {

    @Autowired
    private SizeService sizeService;

    @GetMapping("/get_all")
    public ResponseEntity<List<Size>> getAll() {
        return ResponseEntity.ok(sizeService.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody SizeDTO dto) {
        try {
            Size entity = new Size();
            entity.setMaSize(dto.getMaSize());
            entity.setTenSize(dto.getTenSize());
            entity.setTrangThai(dto.getTrangThai());
            sizeService.add(entity);
            return ResponseEntity.ok("Thêm size thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody SizeDTO dto) {
        try {
            Size entity = new Size();
            entity.setMaSize(dto.getMaSize());
            entity.setTenSize(dto.getTenSize());
            entity.setTrangThai(dto.getTrangThai());
            sizeService.update(id, entity);
            return ResponseEntity.ok("Cập nhật size thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            sizeService.delete(id);
            return ResponseEntity.ok("Xóa size thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Lỗi: " + e.getMessage());
        }
    }
}
