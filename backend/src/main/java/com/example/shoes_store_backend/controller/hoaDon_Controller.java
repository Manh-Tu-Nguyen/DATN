package com.example.shoes_store_backend.controller;

import com.example.shoes_store_backend.service.chiTietHoaDon_Service;
import com.example.shoes_store_backend.service.hoaDon_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quan_ly_hoa_don")
public class hoaDon_Controller {
    @Autowired
    private hoaDon_Service hd_service;

    @Autowired
    private chiTietHoaDon_Service ctdv_service;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(hd_service.getAll());
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String key) {
        return ResponseEntity.ok(hd_service.search(key));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(ctdv_service.getByHoaDonId(id));
    }

}
