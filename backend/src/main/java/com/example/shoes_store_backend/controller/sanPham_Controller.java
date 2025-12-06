package com.example.shoes_store_backend.controller;

import com.example.shoes_store_backend.dto.sanPham_DTO; // Import thêm DTO
import com.example.shoes_store_backend.entity.sanPham_Entity;
import com.example.shoes_store_backend.service.sanPham_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/san-pham")
public class sanPham_Controller {

    @Autowired
    private sanPham_Service sp_service;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(sp_service.getAllSanPham());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        sanPham_Entity sp = sp_service.getById(id);
        if (sp != null) {
            return ResponseEntity.ok(sp);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm");
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody sanPham_Entity sanPham) {
        try {
            sanPham_Entity newSP = sp_service.add(sanPham);
            return ResponseEntity.status(HttpStatus.CREATED).body(newSP);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi thêm mới: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody sanPham_Entity sanPham) {
        try {
            sanPham_Entity updatedSP = sp_service.update(id, sanPham);
            if (updatedSP != null) {
                return ResponseEntity.ok(updatedSP);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi cập nhật: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            if (sp_service.delete(id)) {
                return ResponseEntity.ok("Xóa thành công");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy ID để xóa");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi xóa: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(defaultValue = "") String keyword) {
        return ResponseEntity.ok(sp_service.search(keyword));
    }
}