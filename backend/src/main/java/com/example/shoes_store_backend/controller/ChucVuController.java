package com.example.shoes_store_backend.controller;

import jakarta.validation.Valid;
import com.example.shoes_store_backend.dto.ChucVuDTO;
import com.example.shoes_store_backend.entity.ChucVu;
import com.example.shoes_store_backend.service.ChucVuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chuc_vu")
public class ChucVuController {

    @Autowired
    private ChucVuService service;

    @GetMapping("/get_all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody ChucVuDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            String msg = result.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(msg);
        }
        try {
            ChucVu entity = new ChucVu();
            BeanUtils.copyProperties(dto, entity);
            service.add(entity);
            return ResponseEntity.ok("Thêm chức vụ thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody ChucVuDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            String msg = result.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(msg);
        }
        try {
            ChucVu entity = new ChucVu();
            BeanUtils.copyProperties(dto, entity);
            service.update(id, entity);
            return ResponseEntity.ok("Cập nhật chức vụ thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.ok("Xóa chức vụ thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Lỗi: " + e.getMessage());
        }
    }
}
