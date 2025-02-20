package com.example.datn_teehaven.service;


import com.example.datn_teehaven.entyti.MauSac;

import java.util.List;

public interface MauSacService {

    List<MauSac> findAll();

    List<MauSac> getAllDangHoatDong();

    List<MauSac> getAllNgungHoatDong();


    MauSac save(MauSac mauSac);

    boolean checkTenTrung(String ten);

    boolean checkTenTrungSua(Long id, String ten);

    MauSac update(MauSac mauSac);

    MauSac getById(Long id);

    Integer genMaTuDong();
    boolean isTenValid(String ten);
}
