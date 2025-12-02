package com.example.shoes_store_backend.dto;

import java.math.BigDecimal;
import java.util.Date;

public interface hoaDon_DTO {
    Integer getId();
    String getMaHoaDon();
    String getTenKhachHang();
    String getDiaChi();
    String getSoDienThoai();
    Date getNgayTao();
    BigDecimal getTongTien();
    Boolean getTrangThai();
}
