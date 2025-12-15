package com.example.shoes_store_backend.controller;

import com.example.shoes_store_backend.entity.NhanVien;
import com.example.shoes_store_backend.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nhan_vien")
public class NhanVienController {

    @Autowired
    private NhanVienService nhanVienService;

    // Lấy danh sách tất cả nhân viên
    @GetMapping("/get_all")
    public ResponseEntity<List<NhanVien>> getAll() {
        List<NhanVien> list = nhanVienService.getAll();
        return ResponseEntity.ok(list);
    }

    // Thêm nhân viên mới
    @PostMapping("/add")
    public ResponseEntity<NhanVien> add(@RequestBody NhanVien nv) {
        NhanVien created = nhanVienService.add(nv);
        return ResponseEntity.ok(created);
    }

    // Sửa nhân viên theo id
    @PutMapping("/update/{id}")
    public ResponseEntity<NhanVien> update(@PathVariable Integer id, @RequestBody NhanVien nv) {
        NhanVien updated = nhanVienService.update(id, nv);
        return ResponseEntity.ok(updated);
    }

    // Xóa nhân viên theo id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        nhanVienService.delete(id);
        return ResponseEntity.ok("Xóa nhân viên thành công với id = " + id);
    }
}
