package com.example.datn_teehaven.service.impl;


import com.example.datn_teehaven.entyti.*;
import com.example.datn_teehaven.repository.ChiTietSanPhamRepository;
import com.example.datn_teehaven.repository.GioHangChiTietRepository;
import com.example.datn_teehaven.repository.HoaDonChiTietRepository;
import com.example.datn_teehaven.repository.HoaDonRepository;
import com.example.datn_teehaven.repository.LichSuHoaDonRepository;
import com.example.datn_teehaven.service.GioHangChiTietService;
import com.example.datn_teehaven.service.LichSuHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GioHangChiTietServiceImpl implements GioHangChiTietService {

    @Autowired
    GioHangChiTietRepository repository;

    @Autowired
    private HoaDonRepository repositoryHoaDon;

    @Autowired
    private HoaDonChiTietRepository repositoryHoaDonChiTiet;

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;
    @Autowired
    private LichSuHoaDonService lichSuHoaDonService;

    @Autowired
    private LichSuHoaDonRepository lichSuHoaDonRepository;
    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Override
    public GioHangChiTiet fillByIdCTSP(Long idCTSP) {
        return repository.fillByIdCTSP(idCTSP);
    }

    @Override
    public List<GioHangChiTiet> findAll() {
        return null;
    }

    @Override
    public Integer soLuongSPGioHangCT(Long idGioHang) {

        return repository.soLuongSpTrongGioHangCT(idGioHang);

    }
    @Override
    public List<GioHangChiTiet> findAllByIdGioHang(Long idGioHang) {

        return repository.getfindAllByIdGioHang(idGioHang);

    }
    @Override
    public List<GioHangChiTiet> save(Long idGioHang, List<String> idChiTietSp, Integer soLuong) {
        List<GioHangChiTiet> gioHangChiTietList = repository.getByGioHangChiTiet(idGioHang, idChiTietSp);

        List<GioHangChiTiet> gioHangChiTietNewList = new ArrayList<>();

        // Nếu chưa có, thêm mới
        if (gioHangChiTietList.isEmpty()) {
            for (String idChiTiet : idChiTietSp) {
                GioHangChiTiet gioHangChiTietNew = new GioHangChiTiet();
                gioHangChiTietNew.setSoLuong(soLuong);
                gioHangChiTietNew.setNgayTao(new Date());
                gioHangChiTietNew.setNgaySua(new Date());
                ChiTietSanPham chiTietSanPham = ChiTietSanPham.builder().id(Long.valueOf(idChiTiet)).build();
                gioHangChiTietNew.setChiTietSanPham(chiTietSanPham);
                gioHangChiTietNew.setGioHang(GioHang.builder().id(idGioHang).build());
                gioHangChiTietNew.setTrangThai(0);

                gioHangChiTietNewList.add(gioHangChiTietNew);
            }
        } else {
            for (GioHangChiTiet gioHangChiTiet : gioHangChiTietList) {
                for (String idChiTiet : idChiTietSp) {
                    GioHangChiTiet gioHangChiTietNew = new GioHangChiTiet();
                    gioHangChiTietNew.setId(gioHangChiTiet.getId());
                    gioHangChiTietNew.setSoLuong(gioHangChiTiet.getSoLuong() + soLuong);
                    gioHangChiTietNew.setGhiChu(gioHangChiTiet.getGhiChu());
                    gioHangChiTietNew.setNgayTao(gioHangChiTiet.getNgayTao());
                    gioHangChiTietNew.setNgaySua(new Date());
                    ChiTietSanPham chiTietSanPham = ChiTietSanPham.builder().id(Long.valueOf(idChiTiet)).build();
                    gioHangChiTietNew.setChiTietSanPham(chiTietSanPham);
                    gioHangChiTietNew.setGioHang(gioHangChiTiet.getGioHang());
                    gioHangChiTietNew.setTrangThai(0);

                    gioHangChiTietNewList.add(gioHangChiTietNew);
                }
            }
        }

        return repository.saveAll(gioHangChiTietNewList);
    }

    @Override
    public GioHangChiTiet fillById(Long id) {

        return repository.findById(id).get();

    }
    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);

    } @Override
    public GioHangChiTiet update(GioHangChiTiet gioHangChiTiet) {

        return repository.save(gioHangChiTiet);

    }
    @Override
    public List<GioHangChiTiet> findAllById(List<String> listIdString, Long idGioHang) {
        List<Long> listIdLong = new ArrayList<>();
        for (String str : listIdString) {
            try {
                Long value = Long.parseLong(str);
                listIdLong.add(value);
            } catch (NumberFormatException e) {
                e.fillInStackTrace();
                // Xử lý ngoại lệ nếu có giá trị không hợp lệ
            }
        }

        return repository.findAllByIdGHCT(listIdLong, idGioHang);

    }
    @Override
    public HoaDonChiTiet addHoaDon(List<String> listStringIdGioHangCT, Long tongTien, Long tongTienSale,
                                   String hoVaTen, String soDienThoai, String tienShip, String tienGiam, String email,
                                   String voucher, String diaChiCuThe, String ghiChu, TaiKhoan taiKhoan,
                                   String phuongXaID, String quanHuyenID, String thanhPhoID, Long idGioHang) {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon("HD" + hoaDon.getId());
        hoaDon.setLoaiHoaDon(1);
        hoaDon.setPhiShip(Long.valueOf(tienShip));
        hoaDon.setTienGiam(Long.valueOf(tienGiam));
        hoaDon.setTongTien(tongTien);
        hoaDon.setTongTienKhiGiam(tongTienSale);
        hoaDon.setGhiChu(ghiChu);
        hoaDon.setNguoiNhan(hoVaTen);
        hoaDon.setSdtNguoiNhan(soDienThoai);
        hoaDon.setDiaChiNguoiNhan(diaChiCuThe);
        hoaDon.setEmailNguoiNhan(email);
        hoaDon.setNgayTao(new Date());
        hoaDon.setTrangThai(0);
        hoaDon.setPhuongXa(phuongXaID);
        hoaDon.setQuanHuyen(quanHuyenID);
        hoaDon.setThanhPho(thanhPhoID);
        if (voucher != "") {
            hoaDon.setVoucher(Voucher.builder().id(Long.valueOf(voucher)).build());
        }

        hoaDon.setTaiKhoan(taiKhoan);
        repositoryHoaDon.save(hoaDon);

        lichSuHoaDonService.saveOrUpdate(LichSuHoaDon.builder()
                .ghiChu(ghiChu)
                .ngayTao(new Date())
                .ngaySua(new Date())
                .trangThai(0)
                .hoaDon(hoaDon)
                .build());

        hoaDon.setMaHoaDon("HD" + hoaDon.getId());

        repositoryHoaDon.save(hoaDon);


        List<GioHangChiTiet> listGioHangChiTiet = this.findAllById(listStringIdGioHangCT, idGioHang);

        for (GioHangChiTiet gioHangChiTiet : listGioHangChiTiet) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setSoLuong(gioHangChiTiet.getSoLuong());
            hoaDonChiTiet.setDonGia(gioHangChiTiet.getChiTietSanPham().getGiaHienHanh());
            hoaDonChiTiet.setHoaDon(HoaDon.builder().id(hoaDon.getId()).build());
            hoaDonChiTiet.setChiTietSanPham(gioHangChiTiet.getChiTietSanPham());
            hoaDonChiTiet.setTrangThai(0);
            hoaDonChiTiet.setNgayTao(new Date());
            repositoryHoaDonChiTiet.save(hoaDonChiTiet);
            repository.delete(gioHangChiTiet);
        }

        return null;

    }

    public Long addHoaDonVNPay(List<String> listIdString, Long tongTien, Long tongTienAndSale, Integer loaiHoaDon,
                               String hoVaTen, String soDienThoai, String tienShip, String tienGiam,
                               String email, String voucher, String diaChiCuThe, String ghiChu,
                               TaiKhoan khachHang, String phuongXaID, String quanHuyenID,
                               String thanhPhoID, Long idGioHang, Integer trangThai) {
        // Create a new order with the specified status
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon("HD"+ hoaDon.getId());
        hoaDon.setNgayTao(new Date());
        hoaDon.setNgayThanhToan(new Date());
        hoaDon.setLoaiHoaDon(1);
        hoaDon.setTongTien(tongTien);
        hoaDon.setTongTienKhiGiam(tongTienAndSale);
        hoaDon.setPhiShip(Long.valueOf(tienShip));
        hoaDon.setTienGiam(Long.valueOf(tienGiam));
        hoaDon.setNguoiNhan(hoVaTen);
        hoaDon.setSdtNguoiNhan(soDienThoai);
        hoaDon.setEmailNguoiNhan(email);
        hoaDon.setDiaChiNguoiNhan(diaChiCuThe);
        hoaDon.setPhuongXa(phuongXaID);
        hoaDon.setQuanHuyen(quanHuyenID);
        hoaDon.setThanhPho(thanhPhoID);
        hoaDon.setGhiChu(ghiChu);
        hoaDon.setTrangThai(trangThai); // Set the specified status
        hoaDon.setTaiKhoan(khachHang);

        // Save voucher if provided
        if (voucher != null && !voucher.isEmpty() && !voucher.equals("null")) {
            hoaDon.setVoucher(Voucher.builder().id(Long.valueOf(voucher)).build());
        }

        // Save the order
        hoaDon = repositoryHoaDon.save(hoaDon);
        hoaDon.setMaHoaDon("HD" + hoaDon.getId());
        // Create order details
        for (String id : listIdString) {
            GioHangChiTiet gioHangChiTiet = gioHangChiTietRepository.findById(Long.valueOf(id)).orElse(null);
            if (gioHangChiTiet != null) {
                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                hoaDonChiTiet.setHoaDon(hoaDon);
                hoaDonChiTiet.setChiTietSanPham(gioHangChiTiet.getChiTietSanPham());
                hoaDonChiTiet.setSoLuong(gioHangChiTiet.getSoLuong());
                hoaDonChiTiet.setDonGia(gioHangChiTiet.getChiTietSanPham().getGiaHienHanh());
                hoaDonChiTiet.setNgayTao(new Date());
                repositoryHoaDonChiTiet.save(hoaDonChiTiet);

                // Reduce product quantity
                ChiTietSanPham chiTietSanPham = gioHangChiTiet.getChiTietSanPham();
                chiTietSanPham.setSoLuong(chiTietSanPham.getSoLuong() - gioHangChiTiet.getSoLuong());
                chiTietSanPhamRepository.save(chiTietSanPham);

                // Delete cart item
                gioHangChiTietRepository.deleteById(gioHangChiTiet.getId());
            }
        }

        // Create order history
        hoaDon.setMaHoaDon("HD" + hoaDon.getId());
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(hoaDon);
        lichSuHoaDon.setTrangThai(trangThai);
        lichSuHoaDon.setGhiChu("Đơn hàng đang chờ thanh toán VNPay");
        lichSuHoaDon.setNgayTao(new Date());
        lichSuHoaDon.setNgaySua(new Date());
        lichSuHoaDonRepository.save(lichSuHoaDon);

        return hoaDon.getId();
    }

}
