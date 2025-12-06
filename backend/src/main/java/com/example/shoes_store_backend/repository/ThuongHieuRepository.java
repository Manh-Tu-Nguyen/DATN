package com.example.shoes_store_backend.repository;

import com.example.shoes_store_backend.entity.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Integer> {
    boolean existsByMaThuongHieu(String maThuongHieu);
}