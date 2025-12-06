package com.example.shoes_store_backend.repository;

import com.example.shoes_store_backend.entity.XuatXu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XuatXuRepository extends JpaRepository<XuatXu, Integer> {
    boolean existsByMaXuatXu(String maXuatXu);
}
