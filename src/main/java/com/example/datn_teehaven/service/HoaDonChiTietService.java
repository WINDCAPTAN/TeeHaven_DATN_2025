package com.example.datn_teehaven.service;

import com.example.datn_teehaven.entyti.HoaDonChiTiet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HoaDonChiTietService {
    List<HoaDonChiTiet> findAll();

    HoaDonChiTiet findById(Long id);
    void saveOrUpdate(HoaDonChiTiet hoaDonChiTiet);
}
