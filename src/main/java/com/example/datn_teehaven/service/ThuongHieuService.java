package com.example.datn_teehaven.service;



import com.example.datn_teehaven.entyti.ThuongHieu;

import java.util.List;

public interface ThuongHieuService {
    List<ThuongHieu> getAll();
    List<ThuongHieu> getAllDangHoatDong();
    List<ThuongHieu> getAllNgungHoatDong();
    ThuongHieu save(ThuongHieu thuongHieu);
    boolean checkTenTrung(String ten);
    boolean checkTenTrungSua(Long id, String ten);
    ThuongHieu update(ThuongHieu thuongHieu);
    ThuongHieu getById(Long id);

}
