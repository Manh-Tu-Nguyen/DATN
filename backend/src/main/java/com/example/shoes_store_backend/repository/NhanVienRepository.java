package com.example.shoes_store_backend.repository;

import com.example.shoes_store_backend.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NhanVienRepository extends JpaRepository<NhanVien,Integer>  {
    // Kiểm tra mã nhân viên đã tồn tại chưa
    boolean existsByMaNhanVien(String maNhanVien);

    // Kiểm tra tài khoản đã tồn tại chưa
    boolean existsByTaiKhoan(String taiKhoan);
}
