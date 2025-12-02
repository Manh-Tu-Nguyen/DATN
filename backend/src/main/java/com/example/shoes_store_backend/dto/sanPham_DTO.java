package com.example.shoes_store_backend.dto;

import java.util.Date;

public interface sanPham_DTO {
    Integer  getid();
    Integer getIdXuatXu();
    Integer getIdThuongHieu();
    String getmaSanPham();
    String gettenSanPham();
    Date getngayTao();
    Boolean getTrangThai();

}
