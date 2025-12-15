package com.example.shoes_store_backend.service;

import com.example.shoes_store_backend.dto.chiTietHoaDon;
import com.example.shoes_store_backend.repository.chiTietHoaDon_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class chiTietHoaDon_Service {
    @Autowired
    private chiTietHoaDon_Repo cthd_repo;

    public List<chiTietHoaDon> getByHoaDonId(Integer idHoaDon) {
        return cthd_repo.getChiTietByHoaDon(idHoaDon);
    }
}
