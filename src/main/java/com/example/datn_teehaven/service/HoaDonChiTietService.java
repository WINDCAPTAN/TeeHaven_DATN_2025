package com.example.datn_teehaven.service;


import com.example.datn_teehaven.entyti.HoaDonChiTiet;

import java.util.Date;
import java.util.List;


public interface HoaDonChiTietService {

    List<HoaDonChiTiet> findAll();

    HoaDonChiTiet findById(Long id);

    void deleteById(Long id);

    void saveOrUpdate(HoaDonChiTiet hoaDonChiTiet);

    List<HoaDonChiTiet> findByIdHoaDon(Long idHoaDon);

    List<HoaDonChiTiet> finTop5HDCT();

    List<Object[]> findByTongSoLuongBetween(
            Date startDate,
            Date endDate);

    List<Object[]> findByTongSoLuongNgay(Date ngayTao);

    List<Object[]> findByTongSoLuongThang(Date ngayTao);

    Integer sumSanPhamHoaDonThang(Date ngayTao);

    Integer sumSanPhamHoaDonNgay(Date ngayTao);

    Integer sumSanPhamHoaDonBetween(Date startDate,
                                    Date endDate);

    List<Object[]> thongKeSanPhamTheoNgay(
            Date startDateChart,
            Date endDateChart
    );

    Integer sumSanPhamHoaDonAll();

    List<Object[]> findByTongSoLuongAll();

    List<Object[]> thongKeSanPhamTheoNgayMacDinh30Ngay(
    );


}
