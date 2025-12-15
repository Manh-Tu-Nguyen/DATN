package com.example.shoes_store_backend.repository;
import com.example.shoes_store_backend.dto.chiTietSanPham_DTO;
import com.example.shoes_store_backend.entity.chiTietSanPham_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface chiTietSanPham_Repository extends JpaRepository<chiTietSanPham_Entity, Integer> {

    @Query(value = """
            SELECT * FROM CHI_TIET_SAN_PHAM
        """, nativeQuery = true)
    List<chiTietSanPham_DTO> getAllChiTietSanPham();


}
