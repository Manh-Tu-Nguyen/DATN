package com.example.shoes_store_backend.repository;

import com.example.shoes_store_backend.entity.KhuyenMaiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface KhuyenMaiRepository extends JpaRepository<KhuyenMaiEntity, Integer> {
        boolean existsByMaChuongTrinh(String maChuongTrinh);
    }


