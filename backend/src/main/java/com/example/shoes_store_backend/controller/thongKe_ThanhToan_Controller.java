package com.example.shoes_store_backend.controller;

import com.example.shoes_store_backend.service.thongKe_ThanhToan_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/quan_ly_thong_ke")
public class thongKe_ThanhToan_Controller {
    @Autowired
    private thongKe_ThanhToan_Service tk_service;


    @GetMapping()
    public ResponseEntity<?> getAllTable() {
        return ResponseEntity.ok(tk_service.ggetAllThongKe_ThanhToan());
    }
    @PutMapping("/search")
    public ResponseEntity<?> searchByDate(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
    ) {
        return ResponseEntity.ok(tk_service.search(startDate, endDate));
    }


}
