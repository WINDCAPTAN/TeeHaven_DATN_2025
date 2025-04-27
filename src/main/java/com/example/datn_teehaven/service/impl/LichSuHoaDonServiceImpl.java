package com.example.datn_teehaven.service.impl;

import com.example.datn_teehaven.entyti.HoaDon;
import com.example.datn_teehaven.entyti.LichSuHoaDon;
import com.example.datn_teehaven.repository.LichSuHoaDonRepository;
import com.example.datn_teehaven.service.LichSuHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class LichSuHoaDonServiceImpl implements LichSuHoaDonService {
    @Autowired
    LichSuHoaDonRepository lichSuHoaDonRepository;

    @Override
    public List<LichSuHoaDon> findAll() {
        return lichSuHoaDonRepository.findAll();
    }

    @Override
    public LichSuHoaDon findById(Long id) {
        return lichSuHoaDonRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        lichSuHoaDonRepository.deleteById(id);
    }

    @Override
    public void saveOrUpdate(LichSuHoaDon lichSuHoaDon) {
        lichSuHoaDonRepository.save(lichSuHoaDon);
    }

    @Override
    public List<LichSuHoaDon> findByIdhd(Long idhd) {
        return lichSuHoaDonRepository.findByIdHd(idhd);
    }

    @Override
    public List<LichSuHoaDon> findByIdhdNgaySuaAsc(Long idhd) {
        return lichSuHoaDonRepository.findByIdhdNgaySuaAsc(idhd);
    }

    // Thêm phương thức lưu lịch sử trạng thái hóa đơn
    @Override
    public void saveLichSuHoaDon(HoaDon hoaDon) {
        // Lấy trạng thái hiện tại của hóa đơn
        Integer trangThaiMoi = hoaDon.getTrangThai();

        // Lấy lịch sử trạng thái gần nhất của hóa đơn
        LichSuHoaDon lichSuHoaDonCuoi = lichSuHoaDonRepository.findTopByHoaDonOrderByNgaySuaDesc(hoaDon);

        // Kiểm tra nếu trạng thái mới khác trạng thái của lần cập nhật cuối cùng
        if (lichSuHoaDonCuoi == null || !lichSuHoaDonCuoi.getTrangThai().equals(trangThaiMoi)) {
            // Nếu trạng thái đã thay đổi, tạo mới bản ghi lịch sử
            LichSuHoaDon lichSu = LichSuHoaDon.builder()
                    .hoaDon(hoaDon)  // Liên kết với hóa đơn
                    .trangThai(trangThaiMoi)  // Trạng thái hiện tại của hóa đơn
                    .ghiChu("Cập nhật trạng thái hóa đơn") // Thêm ghi chú cho thay đổi
                    .ngaySua(new Date())  // Lưu thời gian thay đổi trạng thái
                    .build();

            // Lưu lịch sử thay đổi trạng thái vào cơ sở dữ liệu
            lichSuHoaDonRepository.save(lichSu);
        }
    }

}

