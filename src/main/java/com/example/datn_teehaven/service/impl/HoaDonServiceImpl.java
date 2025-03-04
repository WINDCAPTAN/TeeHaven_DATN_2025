package com.example.datn_teehaven.service.impl;

import com.example.datn_teehaven.entyti.HoaDon;
import com.example.datn_teehaven.repository.HoaDonRepository;
import com.example.datn_teehaven.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HoaDonServiceImpl implements HoaDonService {

    @Autowired
    HoaDonRepository hoaDonRepository;
    @Override
    public List<HoaDon> findAll() {
        return hoaDonRepository.findAll();
    }

    @Override
    public HoaDon findById(Long id) {
        return hoaDonRepository.findById(id).orElse(null);
    }

    @Override
    public List<HoaDon> find5ByTrangThai(Integer trangThai) {
        return hoaDonRepository.find5ByTrangThai(trangThai);
    }

    @Override
    public Integer countHoaDonTreo() {
        return  hoaDonRepository.countHoaDonTreo();
    }

    @Override
    public void saveOrUpdate(HoaDon hoaDon) {
        hoaDonRepository.save(hoaDon);
    }
}
