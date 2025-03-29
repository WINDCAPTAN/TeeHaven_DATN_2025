package com.example.datn_teehaven.service.impl;


import com.example.datn_teehaven.entyti.GioHangChiTiet;
import com.example.datn_teehaven.repository.GioHangChiTietRepository;
import com.example.datn_teehaven.service.GioHangChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GioHangChiTietServiceImpl implements GioHangChiTietService {

    @Autowired
    GioHangChiTietRepository repository;
    @Override
    public List<GioHangChiTiet> findAll() {
        return null;
    }

    @Override
    public Integer soLuongSPGioHangCT(Long idGioHang) {

        return repository.soLuongSpTrongGioHangCT(idGioHang);

    }
}
