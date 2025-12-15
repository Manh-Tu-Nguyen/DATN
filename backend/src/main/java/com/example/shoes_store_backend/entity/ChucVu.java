package com.example.shoes_store_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "CHUC_VU")
@Getter
@Setter
public class ChucVu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "MA_CHUC_VU", nullable = false, unique = true)
    private String maChucVu;

    @Column(name = "TEN_CHUC_VU", nullable = false)
    private String tenChucVu;

    @Column(name = "TRANG_THAI", nullable = false)
    private Boolean trangThai;


    @OneToMany(mappedBy = "chucVu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NhanVien> nhanViens;
}
