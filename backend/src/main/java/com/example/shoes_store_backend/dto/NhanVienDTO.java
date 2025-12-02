package com.example.shoes_store_backend.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class NhanVienDTO {

    private Integer id;

    @NotNull(message = "Chức vụ không được để trống")
    private Integer idChucVu;

    @NotBlank(message = "Mã nhân viên không được để trống")
    private String maNhanVien;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên không được vượt quá 100 ký tự")
    private String hoTen;

    @NotNull(message = "Giới tính không được để trống")
    private Boolean gioiTinh; // true = Nam, false = Nữ

    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private LocalDate ngaySinh;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Tài khoản không được để trống")
    private String taiKhoan;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String matKhau;

    @NotNull(message = "Lương không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Lương phải lớn hơn 0")
    private BigDecimal luong;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String diaChi;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10,15}$", message = "Số điện thoại phải có từ 10 đến 15 chữ số")
    private String sdt;

    private String hinhAnh;

    @NotNull(message = "Trạng thái không được để trống")
    private Boolean trangThai; // true = đang làm, false = nghỉ việc

    private LocalDate ngayTao;
}
