package com.example.shoes_store_backend.controller;

import com.example.shoes_store_backend.dto.NhanVienDTO;
import com.example.shoes_store_backend.entity.NhanVien;
import com.example.shoes_store_backend.service.NhanVienService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/nhan-vien")
public class NhanVienController {

    @Autowired
    private NhanVienService nvService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(nvService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        NhanVien nv = nvService.getById(id);
        if (nv != null) {
            return ResponseEntity.ok(nv);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy nhân viên");
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody NhanVienDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(nvService.add(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody NhanVienDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            return ResponseEntity.ok(nvService.update(id, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            nvService.delete(id);
            return ResponseEntity.ok("Xóa thành công nhân viên ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}