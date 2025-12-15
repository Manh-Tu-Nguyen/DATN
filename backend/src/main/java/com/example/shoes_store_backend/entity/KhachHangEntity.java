package com.example.shoes_store_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "KHACH_HANG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KhachHangEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "MA_KHACH_HANG", unique = true)
    private String maKhachHang;

    @Column(name = "TEN_KHACH_HANG")
    private String tenKhachHang;

    @Column(name = "GIOI_TINH")
    private Boolean gioiTinh;

    @Column(name = "NGAY_SINH")
    private LocalDate ngaySinh;

    @Column(name = "HINH_ANH")
    private String hinhAnh;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "TAI_KHOAN", nullable = false, unique = true)
    private String taiKhoan;

    @Column(name = "MAT_KHAU", nullable = false)
    private String matKhau;

    @Column(name = "SDT")
    private String sdt;

    @Column(name = "TRANG_THAI")
    private Boolean trangThai;

    @Column(name = "NGAY_TAO")
    private LocalDate ngayTao;
}
