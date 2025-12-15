package com.example.shoes_store_backend.service;

import com.example.shoes_store_backend.entity.MauSac;
import com.example.shoes_store_backend.repository.MauSacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MauSacService {

    @Autowired
    private MauSacRepository mauSacRepository;

    public List<MauSac> getAll() {
        return mauSacRepository.findAll();
    }

    public MauSac add(MauSac mauSac) {
        if (mauSac.getNgayTao() == null) {
            mauSac.setNgayTao(new Date());
        }
        return mauSacRepository.save(mauSac);
    }

    public MauSac update(Integer id, MauSac dto) {
        MauSac entity = mauSacRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy màu sắc với ID: " + id));
        entity.setMaMau(dto.getMaMau());
        entity.setTenMau(dto.getTenMau());
        entity.setTrangThai(dto.getTrangThai());
        // giữ nguyên ngayTao, không set null
        return mauSacRepository.save(entity);
    }

    public void delete(Integer id) {
        MauSac entity = mauSacRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy màu sắc với ID: " + id));
        mauSacRepository.delete(entity);
    }
}
