package com.example.shoes_store_backend.service;

import com.example.shoes_store_backend.dto.NhanVienDTO;
import com.example.shoes_store_backend.entity.ChucVu;
import com.example.shoes_store_backend.entity.NhanVien;
import com.example.shoes_store_backend.repository.ChucVuRepository; // Cần tạo cái này nếu chưa có
import com.example.shoes_store_backend.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class NhanVienService {

    @Autowired
    private NhanVienRepository nvRepo;

    @Autowired
    private ChucVuRepository cvRepo;

    public List<NhanVien> getAll() {
        return nvRepo.findAll();
    }

    public NhanVien getById(Integer id) {
        return nvRepo.findById(id).orElse(null);
    }

    public NhanVien add(NhanVienDTO dto) {
        if (nvRepo.existsByMaNhanVien(dto.getMaNhanVien())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã nhân viên đã tồn tại");
        }
        if (nvRepo.existsByTaiKhoan(dto.getTaiKhoan())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản đã tồn tại");
        }

        ChucVu chucVu = cvRepo.findById(dto.getIdChucVu())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chức vụ không tồn tại"));

        NhanVien nv = new NhanVien();
        nv.setChucVu(chucVu);
        nv.setMaNhanVien(dto.getMaNhanVien());
        nv.setHoTen(dto.getHoTen());
        nv.setGioiTinh(dto.getGioiTinh());
        nv.setNgaySinh(dto.getNgaySinh());
        nv.setEmail(dto.getEmail());
        nv.setTaiKhoan(dto.getTaiKhoan());
        nv.setMatKhau(dto.getMatKhau());
        nv.setLuong(dto.getLuong());
        nv.setDiaChi(dto.getDiaChi());
        nv.setSdt(dto.getSdt());
        nv.setHinhAnh(dto.getHinhAnh());
        nv.setTrangThai(dto.getTrangThai());
        nv.setNgayTao(LocalDate.now());

        return nvRepo.save(nv);
    }

    public NhanVien update(Integer id, NhanVienDTO dto) {
        NhanVien existing = nvRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên"));

        ChucVu chucVu = cvRepo.findById(dto.getIdChucVu())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chức vụ không tồn tại"));

        existing.setChucVu(chucVu);
        existing.setHoTen(dto.getHoTen());
        existing.setGioiTinh(dto.getGioiTinh());
        existing.setNgaySinh(dto.getNgaySinh());
        existing.setEmail(dto.getEmail());
        existing.setLuong(dto.getLuong());
        existing.setDiaChi(dto.getDiaChi());
        existing.setSdt(dto.getSdt());
        existing.setHinhAnh(dto.getHinhAnh());
        existing.setTrangThai(dto.getTrangThai());

        return nvRepo.save(existing);
    }

    // Xóa
    public void delete(Integer id) {
        if (!nvRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên");
        }
        nvRepo.deleteById(id);
    }
}