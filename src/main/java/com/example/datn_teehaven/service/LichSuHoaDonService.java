package com.example.datn_teehaven.service;

import com.example.datn_teehaven.entyti.LichSuHoaDon;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface LichSuHoaDonService {
    List<LichSuHoaDon> findAll();
    LichSuHoaDon findById(Long id);

    void saveOrUpdate(LichSuHoaDon lichSuHoaDon);
    List<LichSuHoaDon> findByIdhd(Long idhd);


}
