package com.example.datn_teehaven.service.impl;

import com.example.datn_teehaven.entyti.HoaDonChiTiet;
import com.example.datn_teehaven.repository.HoaDonChiTietRepository;
import com.example.datn_teehaven.repository.HoaDonRepository;
import com.example.datn_teehaven.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTietServiceImpl implements HoaDonChiTietService {
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;
    @Override
    public List<HoaDonChiTiet> findAll() {
        return hoaDonChiTietRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        hoaDonChiTietRepository.deleteById(id);
    }

    @Override
    public HoaDonChiTiet findById(Long id) {
        return hoaDonChiTietRepository.findById(id).orElse(null);
    }

    @Override
    public void saveOrUpdate(HoaDonChiTiet hoaDonChiTiet) {
            hoaDonChiTietRepository.save(hoaDonChiTiet);
    }
}
