package com.example.datn_teehaven.service;



import com.example.datn_teehaven.entyti.TayAo;

import java.util.List;

public interface TayAoService {
    List<TayAo> findAll();
    List<TayAo> getAllDangHoatDong();
    List<TayAo> getAllNgungHoatDong();
    void deleteById(Long id);
    TayAo save(TayAo tayAo);
    TayAo update (TayAo tayAo);
    TayAo getById(Long id);
    boolean isTenValid(String ten);
    boolean checkTenTrung(String ten);
    boolean checkTenTrungSua(Long id, String ten);
}
