package com.example.datn_teehaven.service.impl;




import com.example.datn_teehaven.entyti.ChiTietSanPham;
import com.example.datn_teehaven.entyti.HoaDon;
import com.example.datn_teehaven.entyti.HoaDonChiTiet;
import com.example.datn_teehaven.entyti.LichSuHoaDon;
import com.example.datn_teehaven.entyti.SanPham;
import com.example.datn_teehaven.repository.HoaDonChiTietRepository;
import com.example.datn_teehaven.repository.HoaDonRepository;
import com.example.datn_teehaven.service.HoaDonService;
import com.example.datn_teehaven.service.LichSuHoaDonService;

import com.example.datn_teehaven.entyti.HoaDon;
import com.example.datn_teehaven.repository.HoaDonRepository;
import com.example.datn_teehaven.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class HoaDonServiceImpl implements HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    LichSuHoaDonService lichSuHoaDonService;

    @Autowired
    private HoaDonChiTietRepository chiTietHoaDonRepository;
    @Override
    public List<HoaDon> findAll() {
        return hoaDonRepository.findAll();
    }

    @Override
    public HoaDon findById(Long id) {
        return hoaDonRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        hoaDonRepository.deleteById(id);}

    public List<HoaDon> find5ByTrangThai(Integer trangThai) {
        return hoaDonRepository.find5ByTrangThai(trangThai);
    }

    @Override
    public Integer countHoaDonTreo() {
        return  hoaDonRepository.countHoaDonTreo();
    }

    @Override
    public void saveOrUpdate(HoaDon hoaDon) {
        // Lưu hoặc cập nhật hóa đơn
        hoaDonRepository.save(hoaDon);

        // Lưu lịch sử trạng thái sau khi thay đổi trạng thái hóa đơn
        lichSuHoaDonService.saveLichSuHoaDon(hoaDon);
    }


    @Override
    public List<HoaDon> findAllHoaDon() {
        return hoaDonRepository.findAllHoaDon();
    }

    @Override
    public HoaDon finByHoaDonMaHDSdt(String maDonHang, String sdt) {
        return hoaDonRepository.finByHoaDonMaHDSdt(maDonHang, sdt);
    }

    @Override
    public List<HoaDon> findAllOrderByNgaySua() {
        return hoaDonRepository.findAllOrderByNgaySua();
    }

    @Override
    public List<HoaDon> getHoaDonByTaiKhoanByTrangThaiOrderByNgaySua(Long idTaiKhoan, Integer trangThai) {

        return hoaDonRepository.findAllHoaDonByTaiKhoanAndTrangThaiOrderByNgaySua(idTaiKhoan, trangThai);

    }

    @Override
    public Integer countHoaDonDay(Date ngayTao) {
        return hoaDonRepository.countHoaDonNgay(ngayTao);
    }

    @Override
    public List<HoaDon> getAllHoaDonByTaiKhoanOrderByNgaySua(Long idTaiKhoan) {

        return hoaDonRepository.findAllHoaDonByTaiKhoanOrderByNgaySua(idTaiKhoan);

    }


    @Override
    public Long sumHoaDonDay(Date ngayTao) {
        return hoaDonRepository.sumGiaTriHoaDonNgay(ngayTao);
    }

    @Override
    public Integer countHoaDonMonth(Date ngayTao) {
        return hoaDonRepository.countHoaDonThang(ngayTao);
    }

    @Override
    public Long sumHoaDonMonth(Date ngayTao) {
        return hoaDonRepository.sumGiaTriHoaDonThang(ngayTao);
    }

    @Override
    public Integer countHoaDon(Integer trangThai) {
        return hoaDonRepository.countHoaDon(trangThai);
    }

    @Override
    public Integer countHoaDonBetween(Date startDate, Date endDate) {
        return hoaDonRepository.countHoaDonBetween(startDate, endDate);
    }

    @Override
    public Long sumGiaTriHoaDonBetween(Date startDate, Date endDate) {
        return hoaDonRepository.sumGiaTriHoaDonBetween(startDate, endDate);
    }

    @Override
    public Integer countHoaDonTrangThaiBetween(Date startDate, Date endDate, Integer trangThai) {
        return hoaDonRepository.countHoaDonTrangThaiBetween(startDate, endDate, trangThai);
    }

    @Override
    public Integer countHoaDonTrangThaiNgay(Date ngayTao, Integer trangThai) {
        return hoaDonRepository.countHoaDonTrangThaiNgay(ngayTao, trangThai);
    }

    @Override
    public Integer countHoaDonTrangThaiThang(Date ngayTao, Integer trangThai) {
        return hoaDonRepository.countHoaDonTrangThaiThang(ngayTao, trangThai);
    }

    @Override
    public Integer countHoaDonAll() {
        return hoaDonRepository.countHoaDonAll();
    }

    @Override
    public Long sumGiaTriHoaDonAll() {
        return hoaDonRepository.sumGiaTriHoaDonAll();
    }


}
