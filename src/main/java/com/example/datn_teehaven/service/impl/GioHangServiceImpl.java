package com.example.datn_teehaven.service.impl;



import com.example.datn_teehaven.entyti.GioHang;
import com.example.datn_teehaven.repository.GioHangRepository;
import com.example.datn_teehaven.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GioHangServiceImpl implements GioHangService {

    @Autowired
    private GioHangRepository repository;

    @Override
    public List<GioHang> findAll() {
        return null;
    }

    @Override
    public GioHang save(GioHang gioHang) {
        return repository.save(gioHang);
    }

    @Override

    public Integer genMaTuDong() {

        String maStr = "";
        try {
            if (repository.index() != null) {
                maStr = repository.index().toString();
            } else {
                maStr = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (maStr == null) {
            maStr = "0";
            int ma = Integer.parseInt(maStr);
            return ++ma;
        }
        int ma = Integer.parseInt(maStr);
        return ++ma;

    }
}
