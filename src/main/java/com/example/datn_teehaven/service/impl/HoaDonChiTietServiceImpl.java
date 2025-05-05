package com.example.datn_teehaven.service.impl;

import com.example.datn_teehaven.entyti.HoaDonChiTiet;
import com.example.datn_teehaven.repository.HoaDonChiTietRepository;
import com.example.datn_teehaven.repository.HoaDonRepository;
import com.example.datn_teehaven.repository.ChiTietSanPhamRepository;
import com.example.datn_teehaven.service.HoaDonChiTietService;
import com.example.datn_teehaven.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HoaDonChiTietServiceImpl implements HoaDonChiTietService {

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    SanPhamService sanPhamService;

    @Override
    public List<HoaDonChiTiet> findAll() {
        return hoaDonChiTietRepository.findAll();
    }

    @Override
    public HoaDonChiTiet findById(Long id) {
        return hoaDonChiTietRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        hoaDonChiTietRepository.deleteById(id);
    }

    @Override
    public void saveOrUpdate(HoaDonChiTiet hoaDonChiTiet) {
        // Lưu hoặc cập nhật chi tiết hóa đơn
        hoaDonChiTietRepository.save(hoaDonChiTiet);
        
        // Cập nhật số lượng tồn của sản phẩm
        if (hoaDonChiTiet.getChiTietSanPham() != null && hoaDonChiTiet.getChiTietSanPham().getSanPham() != null) {
            sanPhamService.updateSoLuongTon(hoaDonChiTiet.getChiTietSanPham().getSanPham().getId());
        }
    }

    @Override
    public List<HoaDonChiTiet> findByIdHoaDon(Long idHoaDon) {
        return hoaDonChiTietRepository.findByIdHoaDon(idHoaDon);
    }

    @Override
    public List<HoaDonChiTiet> finTop5HDCT() {
        return hoaDonChiTietRepository.fillAllIdHoaDonTrangThaiHoanThanh(null);
    }

    public List<Object[]> findByTongSoLuongBetween(Date startDate, Date endDate) {
        return hoaDonChiTietRepository.findByTongSoLuongBetween(startDate,endDate);
    }

    @Override
    public List<Object[]> findByTongSoLuongNgay(Date ngayTao) {
        return hoaDonChiTietRepository.findByTongSoLuongNgay(ngayTao);
    }

    @Override
    public List<Object[]> findByTongSoLuongThang(Date ngayTao) {
        return hoaDonChiTietRepository.findByTongSoLuongThang(ngayTao);
    }

    @Override
    public Integer sumSanPhamHoaDonThang(Date ngayTao) {
        return hoaDonChiTietRepository.sumSanPhamHoaDonThang(ngayTao);
    }

    @Override
    public Integer sumSanPhamHoaDonNgay(Date ngayTao) {
        return hoaDonChiTietRepository.sumSanPhamHoaDonNgay(ngayTao);
    }

    @Override
    public Integer sumSanPhamHoaDonBetween(Date startDate, Date endDate) {
        return hoaDonChiTietRepository.sumSanPhamHoaDonBetween(startDate,endDate);
    }

    @Override
    public List<Object[]> thongKeSanPhamTheoNgay(Date startDateChart, Date endDateChart) {
        return hoaDonChiTietRepository.thongKeSanPhamTheoNgay(startDateChart, endDateChart);
    }

    @Override
    public Integer sumSanPhamHoaDonAll() {
        return hoaDonChiTietRepository.sumSanPhamHoaDonAll();
    }

    @Override
    public List<Object[]> findByTongSoLuongAll() {
        return hoaDonChiTietRepository.findByTongSoLuongAll();
    }

    @Override
    public List<Object[]> thongKeSanPhamTheoNgayMacDinh30Ngay() {
        Date ngayHienTai = new Date();
        Date ngayTru30 = new Date();
        ngayTru30.setDate(ngayHienTai.getDate()-30);
        return hoaDonChiTietRepository.thongKeSanPhamTheoNgay(ngayTru30,ngayHienTai);
    }

    @Override
    public List<HoaDonChiTiet> findTop5BanChay() {
        return hoaDonChiTietRepository.findTop5BanChay();
    }

    @Override
    public List<HoaDonChiTiet> findTop10SanPhamMoi() {
        return hoaDonChiTietRepository.findTop10SanPhamMoi();
    }

}
