package com.example.shoes_store_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class XuatXuDTO {

    @NotBlank(message = "Mã xuất xứ không được để trống")
    private String maXuatXu;

    @NotBlank(message = "Tên xuất xứ không được để trống")
    private String tenXuatXu;

    @NotNull(message = "Trạng thái không được để trống")
    private Boolean trangThai;
}