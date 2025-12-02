package com.example.shoes_store_backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<com.example.shoes_store_backend.entity.VoucherEntity, Integer> {
    boolean existsByMaVoucher(String maVoucher);
}
