// KhachHangService.java
package com.example.datn_teehaven.service;

import com.example.datn_teehaven.entyti.TaiKhoan;

import java.util.List;

public interface KhachHangService {

    List<TaiKhoan> getAll();

    List<TaiKhoan> getAllDangHoatDong();

    List<TaiKhoan> getAllNgungHoatDong();

    TaiKhoan add(TaiKhoan taiKhoan);

    TaiKhoan update(TaiKhoan taiKhoan);

    void remove(Long id);

    TaiKhoan getById(Long id);

    boolean checkTenTrung(String ten);

    boolean checkTenTaiKhoanTrung(String ten);

    boolean checkEmail(String email);

    boolean checkTenTkTrungSua(Long id, String ten);

    boolean checkEmailSua(Long id, String email);

    boolean checkSoDienThoaiTrung(String soDienThoai);

    boolean checkSoDienThoaiTrungSua(Long id, String soDienThoai);

    TaiKhoan findKhachLe();

    void addKhachLe();

    void sendEmail(TaiKhoan taiKhoan, String path, String random);

    void guiLieuHe(String hoTen, String email, String chuDe, String tinNhan);
}