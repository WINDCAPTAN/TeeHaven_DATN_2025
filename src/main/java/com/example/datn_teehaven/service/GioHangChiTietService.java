package com.example.datn_teehaven.service;



import com.example.datn_teehaven.entyti.GioHangChiTiet;

import java.util.List;

public interface GioHangChiTietService {

    List<GioHangChiTiet> findAll();

    Integer soLuongSPGioHangCT(Long idGioHang);

}
