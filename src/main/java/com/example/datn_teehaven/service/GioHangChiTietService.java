package com.example.datn_teehaven.service;



import com.example.datn_teehaven.entyti.GioHangChiTiet;
import com.example.datn_teehaven.entyti.HoaDonChiTiet;
import com.example.datn_teehaven.entyti.TaiKhoan;

import java.util.List;

public interface GioHangChiTietService {

    List<GioHangChiTiet> findAll();

    Integer soLuongSPGioHangCT(Long idGioHang);
    
    List<GioHangChiTiet> findAllByIdGioHang(Long idGioHang);

    List<GioHangChiTiet> save(Long idGioHang, List<String> idChiTietSp, Integer soLuong);

    GioHangChiTiet fillById(Long id);

    void deleteById(Long id);

    GioHangChiTiet update(GioHangChiTiet gioHangChiTiet);

    List<GioHangChiTiet> findAllById(List<String> listIdString, Long idGioHang);

    HoaDonChiTiet addHoaDon(List<String> listStringIdGioHangCT, Long tongTien, Long tongTienSale,
                            String hoVaTen, String soDienThoai, String tienShip, String tienGiam, String email,
                            String voucher, String diaChiCuThe, String ghiChu, TaiKhoan taiKhoan,
                            String phuongXaID, String quanHuyenID, String thanhPhoID, Long idGioHang);
    GioHangChiTiet fillByIdCTSP(Long idCTSP);
}
