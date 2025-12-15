package com.example.shoes_store_backend.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "MAU_SAC")
public class MauSac {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "MA_MAU", nullable = false, unique = true)
    private String maMau;

    @Column(name = "TEN_MAU", nullable = false)
    private String tenMau;

    @Column(name = "TRANG_THAI", nullable = false)
    private Boolean trangThai;

    @Column(name = "NGAY_TAO", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ngayTao;

    @PrePersist
    public void prePersist() {
        if (ngayTao == null) {
            ngayTao = new Date();
        }
    }

    // Getter và Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getMaMau() { return maMau; }
    public void setMaMau(String maMau) { this.maMau = maMau; }

    public String getTenMau() { return tenMau; }
    public void setTenMau(String tenMau) { this.tenMau = tenMau; }

    public Boolean getTrangThai() { return trangThai; }
    public void setTrangThai(Boolean trangThai) { this.trangThai = trangThai; }

    public Date getNgayTao() { return ngayTao; }
    public void setNgayTao(Date ngayTao) { this.ngayTao = ngayTao; }
}
