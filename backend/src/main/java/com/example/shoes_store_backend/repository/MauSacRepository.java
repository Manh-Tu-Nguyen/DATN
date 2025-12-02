package com.example.shoes_store_backend.repository;
import com.example.shoes_store_backend.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac, Integer> {
    boolean existsByMaMau(String maMau);
}
