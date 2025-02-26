package com.example.datn_teehaven.service;

import com.example.datn_teehaven.entyti.SanPham;

import java.util.List;


public interface SanPhamService {



    List<SanPham> getAll();


    SanPham getById(Long id);


    boolean checkTenTrung(String ten);

    Integer genMaTuDong();

    SanPham add(SanPham sanPham);

    List<SanPham> getAllDangHoatDong();


    List<SanPham> getAllNgungHoatDong();



    SanPham update(SanPham sanPham);

    void remove(Long id);




    boolean checkTenTrungSua(Long id, String ten);

}
