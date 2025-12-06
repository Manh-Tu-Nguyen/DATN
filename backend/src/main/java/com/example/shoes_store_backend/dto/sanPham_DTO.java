package com.example.shoes_store_backend.dto;

import java.util.Date;

public interface sanPham_DTO {
    Integer getId();
    String getMaSanPham();
    String getTenSanPham();
    Date getNgayTao();
    Boolean getTrangThai();
    Integer getIdThuongHieu();
    Integer getIdXuatXu();
    String getTenThuongHieu();
    String getTenXuatXu();
}