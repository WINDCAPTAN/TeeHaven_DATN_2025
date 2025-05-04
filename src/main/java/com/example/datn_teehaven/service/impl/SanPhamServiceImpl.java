package com.example.datn_teehaven.service.impl;


import com.example.datn_teehaven.entyti.SanPham;
import com.example.datn_teehaven.repository.ChiTietSanPhamRepository;
import com.example.datn_teehaven.repository.SanPhamRepository;
import com.example.datn_teehaven.service.SanPhamService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanPhamServiceImpl implements SanPhamService {
    @Autowired
    private SanPhamRepository sanPhamRepo;


    @Override
    public List<SanPham> getAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "ngayTao");
        return sanPhamRepo.findAll(sort);
    }

    @Override
    public SanPham add(SanPham sanPham) {
        return sanPhamRepo.save(sanPham);
    }


    @Override
    public List<SanPham> getAllDangHoatDong() {
        return sanPhamRepo.fillAllDangHoatDong();
    }

    @Override
    public List<SanPham> getAllNgungHoatDong() {
        return sanPhamRepo.fillAllNgungHoatDong();
    }



    @Override
    public Integer genMaTuDong() {
        Long count = sanPhamRepo.count();
        return count.intValue() + 1;
    }

    @Override
    public SanPham update(SanPham sanPham) {
        return sanPhamRepo.save(sanPham);
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public SanPham getById(Long id) {
        return sanPhamRepo.findById(id).get();
    }


    @Override
    public boolean checkTenTrung(String ten) {
        for (SanPham sp : sanPhamRepo.findAll()) {
            if (sp.getTen().equalsIgnoreCase(ten)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkTenTrungSua(Long id, String ten) {
        for (SanPham sp : sanPhamRepo.findAll()) {
            if (sp.getTen().equalsIgnoreCase(ten)) {
                if (!sp.getId().equals(id)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Autowired
    private ChiTietSanPhamRepository repository;

    public void updateSoLuongTon(Long sanPhamId) {
        Integer tongSoLuong = repository.sumSoLuongTonBySanPhamId(sanPhamId);
        SanPham sanPham = sanPhamRepo.findById(sanPhamId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm!"));
        sanPham.setSoLuongTon(tongSoLuong);
        sanPhamRepo.save(sanPham);
    }
}
