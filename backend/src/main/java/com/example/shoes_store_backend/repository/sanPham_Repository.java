package com.example.shoes_store_backend.repository;

import com.example.shoes_store_backend.dto.sanPham_DTO;
import com.example.shoes_store_backend.entity.sanPham_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface sanPham_Repository extends JpaRepository<sanPham_Entity, Integer> {

    // Lấy tất cả sản phẩm
    @Query(value = """
        SELECT ID ,ID_XUAT_XU , ID_THUONG_HIEU, MA_SAN_PHAM ,TEN_SAN_PHAM, NGAY_TAO , TRANG_THAI
        FROM SAN_PHAM
        """, nativeQuery = true)
    List<sanPham_DTO> getAllSanPham();

    @Query(value = """
    SELECT ID ,ID_XUAT_XU , ID_THUONG_HIEU, MA_SAN_PHAM ,TEN_SAN_PHAM, NGAY_TAO , TRANG_THAI
    FROM SAN_PHAM
    WHERE TEN_SAN_PHAM LIKE %?1% OR MA_SAN_PHAM LIKE %?1%
""", nativeQuery = true)
    List<sanPham_DTO> search(String keyword);



}

