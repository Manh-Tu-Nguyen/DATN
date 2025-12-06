package com.example.shoes_store_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "THUONG_HIEU")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThuongHieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "MA_THUONG_HIEU", nullable = false, unique = true)
    private String maThuongHieu;

    @Column(name = "TEN_THUONG_HIEU", nullable = false)
    private String tenThuongHieu;

    @Column(name = "TRANG_THAI")
    private Boolean trangThai;
}