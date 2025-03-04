package com.example.datn_teehaven.service.impl;

import com.example.datn_teehaven.entyti.TaiKhoan;
import com.example.datn_teehaven.repository.NhanVienRepository;
import com.example.datn_teehaven.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NhanVienServiceImpl implements NhanVienService {
    @Autowired
    private NhanVienRepository nhanVienRepositry;
    @Override
    public List<TaiKhoan> getAll() {
        return nhanVienRepositry.fillAllNhanVien();
    }

    @Override
    public List<TaiKhoan> getAllDangHoatDong() {
        return nhanVienRepositry.fillAllDangHoatDong();
    }

    @Override
    public List<TaiKhoan> getAllNgungHoatDong() {
        return nhanVienRepositry.fillAllNgungHoatDong();
    }

    @Override
    public TaiKhoan add(TaiKhoan nhanVien) {
        return nhanVienRepositry.save(nhanVien);
    }

    @Override
    public TaiKhoan update(TaiKhoan sanPham) {
        return nhanVienRepositry.save(sanPham);
    }

    @Override
    public TaiKhoan getById(Long id) {
        return nhanVienRepositry.findById(id).get();
    }

    @Override
    public boolean checkTenTrung(String ten) {
        for (TaiKhoan sp : nhanVienRepositry.findAll()) {
            if (sp.getGioiTinh() == null) {
                continue;
            }
            if (sp.getTenTaiKhoan().equalsIgnoreCase(ten)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkTenTrungSua(String id, String ten) {
        for (TaiKhoan tk : nhanVienRepositry.findAll()) {
            if (tk.getGioiTinh() == null) {
                continue;
            }
            if (tk.getTenTaiKhoan().equalsIgnoreCase(ten)) {
                if (!tk.getId().equals(id)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkTenTkTrungSua(Long id, String ten) {
        for (TaiKhoan sp : nhanVienRepositry.findAll()) {
            if (sp.getGioiTinh() == null) {
                continue;
            }
            if (sp.getTenTaiKhoan().equalsIgnoreCase(ten)) {
                if (!sp.getId().equals(id)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkEmailSua(Long id, String email) {
        for (TaiKhoan sp : nhanVienRepositry.findAll()) {
            if (sp.getGioiTinh() == null) {
                continue;
            }
            if (sp.getEmail().equalsIgnoreCase(email)) {
                if (!sp.getId().equals(id)) {
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public boolean checkTenTaiKhoanTrung(String ten) {
        for (TaiKhoan sp : nhanVienRepositry.findAll()) {
            if (sp.getGioiTinh() == null) {
                continue;
            }
            if (sp.getTenTaiKhoan().equalsIgnoreCase(ten)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkEmail(String email) {
        for (TaiKhoan sp : nhanVienRepositry.findAll()) {
            if (sp.getGioiTinh() == null) {
                continue;
            }
            if (sp.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }
}
