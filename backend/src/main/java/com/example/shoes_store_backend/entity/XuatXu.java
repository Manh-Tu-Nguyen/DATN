package com.example.shoes_store_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "XUAT_XU")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class XuatXu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "MA_XUAT_XU", nullable = false, unique = true)
    private String maXuatXu;

    @Column(name = "TEN_XUAT_XU", nullable = false)
    private String tenXuatXu;

    @Column(name = "TRANG_THAI")
    private Boolean trangThai;
}