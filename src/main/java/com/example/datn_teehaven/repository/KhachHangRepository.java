package com.example.datn_teehaven.repository;


import com.example.datn_teehaven.entyti.TaiKhoan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface KhachHangRepository extends JpaRepository<TaiKhoan, Long> {

    Optional<TaiKhoan> findByEmail(String email);
    Optional<TaiKhoan> findByTenTaiKhoan(String tenTaiKhoan);
    Optional<TaiKhoan> findBySoDienThoai(String soDienThoai);

    @Query(value = "SELECT * FROM tai_khoan WHERE vai_tro_id = 2 ORDER BY ngay_sua DESC", nativeQuery = true)
    List<TaiKhoan> fillAllKhachHang();

    @Query(value = "select * from tai_khoan where trang_thai = 0 and vai_tro_id = 2", nativeQuery = true)
    List<TaiKhoan> fillAllDangHoatDong();

    @Query(value = "select * from tai_khoan where trang_thai = 1 and vai_tro_id = 2", nativeQuery = true)
    List<TaiKhoan> fillAllNgungHoatDong();

    @Query(value = "select top(1) * from tai_khoan where ho_va_ten =N'Khách lẻ'", nativeQuery = true)
    TaiKhoan findKhachLe();

    @Transactional
    @Modifying
    @Query(value = "INSERT into tai_khoan ( ho_va_ten, ngay_sinh, gioi_tinh, so_dien_thoai, email, ten_tai_khoan, mat_khau, ngay_tao, ngay_sua, trang_thai, vai_tro_id) values ( N'Khách lẻ', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, -1, NULL)", nativeQuery = true)
    void addKhachLe();
}
