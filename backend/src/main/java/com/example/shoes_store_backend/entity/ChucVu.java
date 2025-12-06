package com.example.shoes_store_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CHUC_VU")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}