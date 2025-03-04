package com.example.datn_teehaven.service.impl;

import com.example.datn_teehaven.entyti.LichSuHoaDon;
import com.example.datn_teehaven.repository.LichSuHoaDonRepository;
import com.example.datn_teehaven.service.LichSuHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LichSuHoaDonServiceImpl implements LichSuHoaDonService {
    @Autowired
    LichSuHoaDonRepository lichSuHoaDonRepository;
    @Override
    public List<LichSuHoaDon> findAll() {
        return lichSuHoaDonRepository.findAll();
    }

    @Override
    public LichSuHoaDon findById(Long id) {
        return lichSuHoaDonRepository.findById(id).orElse(null);
    }


    @Override
    public void saveOrUpdate(LichSuHoaDon lichSuHoaDon) {
        lichSuHoaDonRepository.save(lichSuHoaDon);
    }

    @Override
    public List<LichSuHoaDon> findByIdhd(Long idhd) {
        return lichSuHoaDonRepository.findByIdHd(idhd);
    }

}
