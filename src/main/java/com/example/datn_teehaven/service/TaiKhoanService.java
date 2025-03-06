package com.example.datn_teehaven.service;


import com.example.datn_teehaven.entyti.TaiKhoan;

import java.util.List;

public interface TaiKhoanService {

    List<TaiKhoan> getAll();
    void addKhachLe();

    TaiKhoan findKhachLe();

    public String addUser(TaiKhoan userInfo);

    public String updateUser(TaiKhoan userInfo);

    public void sendEmail(TaiKhoan taiKhoan, String path);

    public void sendEmail1(TaiKhoan taiKhoan, String url, String random);

    boolean verifyAccount(String verificationPassWord, String resetPass);

    TaiKhoan saveUser(TaiKhoan user, String url);

    TaiKhoan getTaiKhoanByName(String name);
}
