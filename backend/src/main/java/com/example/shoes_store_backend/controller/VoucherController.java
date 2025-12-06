package com.example.shoes_store_backend.controller;

import com.example.shoes_store_backend.dto.VoucherDTO;
import com.example.shoes_store_backend.entity.VoucherEntity;
import com.example.shoes_store_backend.service.VoucherService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Autowired
    private VoucherService service;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody VoucherDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            VoucherEntity entity = new VoucherEntity();
            BeanUtils.copyProperties(dto, entity);
            service.add(entity);
            return ResponseEntity.ok("Thêm voucher thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody VoucherDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            VoucherEntity entity = new VoucherEntity();
            BeanUtils.copyProperties(dto, entity);
            service.update(id, entity);
            return ResponseEntity.ok("Cập nhật voucher thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (service.delete(id)) {
            return ResponseEntity.ok("Xóa voucher thành công!");
        }
        return ResponseEntity.status(404).body("Không tìm thấy voucher cần xóa");
    }
}