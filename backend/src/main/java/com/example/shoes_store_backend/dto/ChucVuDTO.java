package com.example.shoes_store_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChucVuDTO {

    @NotBlank(message = "Mã chức vụ không được để trống")
    private String maChucVu;

    @NotBlank(message = "Tên chức vụ không được để trống")
    private String tenChucVu;

    @NotNull(message = "Trạng thái không được để trống")
    private Boolean trangThai;

    // Getter và Setter
    public String getMaChucVu() {
        return maChucVu;
    }

    public void setMaChucVu(String maChucVu) {
        this.maChucVu = maChucVu;
    }

    public String getTenChucVu() {
        return tenChucVu;
    }

    public void setTenChucVu(String tenChucVu) {
        this.tenChucVu = tenChucVu;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }
}
