package com.example.datn_teehaven.repository;

import com.example.datn_teehaven.entyti.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NhanVienRepository extends JpaRepository<TaiKhoan, Long> {
    @Query(value = "SELECT * FROM tai_khoan WHERE vai_tro_id = 1 ORDER BY ngay_sua DESC",nativeQuery = true)
    List<TaiKhoan> fillAllNhanVien();

    @Query(value = "select * from tai_khoan where trang_thai = 0 and vai_tro_id = 1",nativeQuery = true)
    List<TaiKhoan> fillAllDangHoatDong();

    @Query(value = "select *from  tai_khoan where trang_thai = 1 and vai_tro_id = 1", nativeQuery = true)
    List<TaiKhoan> fillAllNgungHoatDong();
}
