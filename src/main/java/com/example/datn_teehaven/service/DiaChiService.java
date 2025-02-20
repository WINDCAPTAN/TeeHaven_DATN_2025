package com.example.datn_teehaven.service;




import com.example.datn_teehaven.entyti.DiaChi;

import java.util.List;

public interface DiaChiService {
    List<DiaChi> getAll();

    List<DiaChi> getAllByTaiKhoan(Long id);

    void deleteById(Long id);

    DiaChi save(DiaChi diaChi);

    boolean checkTenTrung(String ten,Long idTaiKhoan);

    boolean checkTenTrungSua(Long idDiaChi, String ten,Long idTaiKhoan);

    DiaChi update(DiaChi diaChi);

    DiaChi getById(Long id);

    List<DiaChi> getAllTrangThai(Integer trangThai);
}
