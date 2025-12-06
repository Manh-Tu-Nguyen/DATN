package com.example.shoes_store_backend.repository;

import com.example.shoes_store_backend.dto.sanPham_DTO;
import com.example.shoes_store_backend.entity.sanPham_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface sanPham_Repository extends JpaRepository<sanPham_Entity, Integer> {

    @Query(value = """
        SELECT 
            sp.ID AS id, 
            sp.MA_SAN_PHAM AS maSanPham, 
            sp.TEN_SAN_PHAM AS tenSanPham, 
            sp.NGAY_TAO AS ngayTao, 
            sp.TRANG_THAI AS trangThai,
            sp.ID_THUONG_HIEU AS idThuongHieu,
            sp.ID_XUAT_XU AS idXuatXu,
            th.TEN_THUONG_HIEU AS tenThuongHieu,
            xx.TEN_XUAT_XU AS tenXuatXu
        FROM SAN_PHAM sp
        LEFT JOIN THUONG_HIEU th ON sp.ID_THUONG_HIEU = th.ID
        LEFT JOIN XUAT_XU xx ON sp.ID_XUAT_XU = xx.ID
        ORDER BY sp.ID DESC
        """, nativeQuery = true)
    List<sanPham_DTO> getAllSanPham();

    @Query(value = """
        SELECT 
            sp.ID AS id, 
            sp.MA_SAN_PHAM AS maSanPham, 
            sp.TEN_SAN_PHAM AS tenSanPham, 
            sp.NGAY_TAO AS ngayTao, 
            sp.TRANG_THAI AS trangThai,
            sp.ID_THUONG_HIEU AS idThuongHieu,
            sp.ID_XUAT_XU AS idXuatXu,
            th.TEN_THUONG_HIEU AS tenThuongHieu,
            xx.TEN_XUAT_XU AS tenXuatXu
        FROM SAN_PHAM sp
        LEFT JOIN THUONG_HIEU th ON sp.ID_THUONG_HIEU = th.ID
        LEFT JOIN XUAT_XU xx ON sp.ID_XUAT_XU = xx.ID
        WHERE sp.TEN_SAN_PHAM LIKE CONCAT(N'%', ?1, '%') 
           OR sp.MA_SAN_PHAM LIKE CONCAT('%', ?1, '%')
        ORDER BY sp.ID DESC
    """, nativeQuery = true)
    List<sanPham_DTO> search(String keyword);
}