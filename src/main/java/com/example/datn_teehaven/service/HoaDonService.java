package com.example.datn_teehaven.service;


import com.example.datn_teehaven.entyti.HoaDon;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;



@Service
public interface HoaDonService {
    List<HoaDon> findAll();

    HoaDon findById(Long id);


    void deleteById(Long id);

    void saveOrUpdate(HoaDon hoaDon);


    Integer countHoaDonTreo();

    List<HoaDon> find5ByTrangThai(Integer trangThai);

    List<HoaDon> findAllHoaDon();
    HoaDon finByHoaDonMaHDSdt(String maDonHang, String sdt);

    List<HoaDon> findAllOrderByNgaySua();

    List<HoaDon> getAllHoaDonByTaiKhoanOrderByNgaySua(Long idTaiKhoan);

    List<HoaDon> getHoaDonByTaiKhoanByTrangThaiOrderByNgaySua(Long idTaiKhoan, Integer trangThai);

    Integer countHoaDonDay(Date ngayTao);

    Long sumHoaDonDay(Date ngayTao);

    Integer countHoaDonMonth(Date ngayTao);

    Long sumHoaDonMonth(Date ngayTao);

    Integer countHoaDon(Integer trangThai);

    Integer countHoaDonBetween(Date startDate,
                               Date endDate);

    Long sumGiaTriHoaDonBetween(Date startDate,
                                Date endDate);

    Integer countHoaDonTrangThaiBetween(Date startDate,
                                        Date endDate,
                                        Integer trangThai);

    Integer countHoaDonTrangThaiNgay(Date ngayTao,
                                     Integer trangThai);

    Integer countHoaDonTrangThaiThang(Date ngayTao,
                                      Integer trangThai);



    Integer countHoaDonAll();

    Long sumGiaTriHoaDonAll();








}
