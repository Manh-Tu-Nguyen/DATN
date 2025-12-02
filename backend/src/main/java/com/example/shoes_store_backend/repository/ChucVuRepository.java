package com.example.shoes_store_backend.repository;

import com.example.shoes_store_backend.entity.ChucVu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChucVuRepository extends JpaRepository<ChucVu, Integer> {  boolean existsByMaChucVu(String maChucVu);
}
