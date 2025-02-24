package com.example.datn_teehaven.service;

import com.example.datn_teehaven.entyti.TaiKhoan;

import java.util.List;

public interface NhanVienService {
    List<TaiKhoan> getAll();
    List<TaiKhoan> getAllDangHoatDong();
    List<TaiKhoan> getAllNgungHoatDong();
    TaiKhoan add(TaiKhoan nhanVien);
    TaiKhoan update(TaiKhoan sanPham);
    TaiKhoan getById(Long id);
    boolean checkTenTrung(String ten);
    boolean checkTenTrungSua(String id,String ten);
    boolean checkTenTkTrungSua(Long id,String ten);
    boolean checkEmailSua(Long id,String email);
    boolean checkTenTaiKhoanTrung(String ten);
    boolean checkEmail(String email);
}
