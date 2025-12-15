package com.example.shoes_store_backend.repository;

import com.example.shoes_store_backend.entity.KhachHangEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KhachHangRepository extends JpaRepository<KhachHangEntity, Integer> {
    KhachHangEntity findByTaiKhoanAndMatKhau(String taiKhoan, String matKhau);
}
