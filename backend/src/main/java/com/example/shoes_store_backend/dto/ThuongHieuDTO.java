package com.example.shoes_store_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThuongHieuDTO {

    @NotBlank(message = "Mã thương hiệu không được để trống")
    private String maThuongHieu;

    @NotBlank(message = "Tên thương hiệu không được để trống")
    private String tenThuongHieu;

    @NotNull(message = "Trạng thái không được để trống")
    private Boolean trangThai;
}