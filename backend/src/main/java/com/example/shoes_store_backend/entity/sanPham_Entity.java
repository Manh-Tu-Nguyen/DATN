package com.example.shoes_store_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "SAN_PHAM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class sanPham_Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "MA_SAN_PHAM")
    private String maSanPham;

    @Column(name = "TEN_SAN_PHAM")
    private String tenSanPham;

    @Column(name = "NGAY_TAO", insertable = false, updatable = false) // Để SQL tự gán GETDATE()
    @Temporal(TemporalType.DATE)
    private Date ngayTao;

    @Column(name = "TRANG_THAI")
    private Boolean trangThai;

    @Column(name = "ID_THUONG_HIEU")
    private Integer idThuongHieu;

    @Column(name = "ID_XUAT_XU")
    private Integer idXuatXu;

    @Column(name = "ID_CHI_TIET_KHUYEN_MAI")
    private Integer idChiTietKhuyenMai;
}