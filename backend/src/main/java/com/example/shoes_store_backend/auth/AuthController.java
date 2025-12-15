package com.example.shoes_store_backend.auth;

//
//import com.example.highsport.entity.KhachHangEntity;
//import com.example.highsport.entity.NhanVienEntity;
//import com.example.highsport.repository.KhachHangRepository;
//import com.example.highsport.repository.NhanVienRepository;

import com.example.shoes_store_backend.entity.KhachHangEntity;
import com.example.shoes_store_backend.entity.NhanVien;
import com.example.shoes_store_backend.repository.KhachHangRepository;
import com.example.shoes_store_backend.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private NhanVienRepository nhanVienRepo;

    @Autowired
    private KhachHangRepository khachHangRepo;

    @PostMapping("/login")
    public Object login(@RequestBody LoginRequest request) {

        // 1. Check nhân viên
        NhanVien nv = nhanVienRepo.findByTaiKhoanAndMatKhau(
                request.getUsername(),
                request.getPassword()
        );

        if (nv != null) {
            return new LoginResponse(
                    nv.getId(),
                    nv.getHoTen(),
                    "STAFF"
            );
        }

        // 2. Check khách hàng
        KhachHangEntity kh = khachHangRepo.findByTaiKhoanAndMatKhau(
                request.getUsername(),
                request.getPassword()
        );

        if (kh != null) {
            return new LoginResponse(
                    kh.getId(),
                    kh.getTenKhachHang(),
                    "CUSTOMER"
            );
        }

        return "Sai tài khoản hoặc mật khẩu";
    }
}
