package com.example.shoes_store_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "NHAN_VIEN")
@Getter
@Setter
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CHUC_VU", nullable = false)
    private ChucVu chucVu;

    @Column(name = "MA_NHAN_VIEN", nullable = false, unique = true)
    private String maNhanVien;

    @Column(name = "HO_TEN", nullable = false)
    private String hoTen;

    @Column(name = "GIOI_TINH", nullable = false)
    private Boolean gioiTinh;

    @Column(name = "NGAY_SINH", nullable = false)
    private LocalDate ngaySinh;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "TAI_KHOAN", nullable = false, unique = true)
    private String taiKhoan;

    @Column(name = "MAT_KHAU", nullable = false)
    private String matKhau;

    @Column(name = "LUONG", nullable = false, precision = 18, scale = 2)
    private BigDecimal luong;

    @Column(name = "DIA_CHI", nullable = false)
    private String diaChi;

    @Column(name = "SDT", nullable = false)
    private String sdt;

    @Column(name = "HINH_ANH")
    private String hinhAnh;

    @Column(name = "TRANG_THAI", nullable = false)
    private Boolean trangThai;

    @Column(name = "NGAY_TAO", nullable = false)
    private LocalDate ngayTao;
}
