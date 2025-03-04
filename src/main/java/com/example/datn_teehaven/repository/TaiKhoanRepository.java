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
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan,Long> {
    @Query(value = "SELECT * FROM tai_khoan WHERE vai_tro_id = 2 ORDER BY ngay_sua DESC", nativeQuery = true)
    List<TaiKhoan> fillAllKhachHang();
    @Query(value = "select top(1) * from tai_khoan where ho_va_ten =N'Khách lẻ'", nativeQuery = true)
    TaiKhoan findKhachLe();
    @Transactional
    @Modifying
    @Query(value = "INSERT into NguoiDung( ho_va_ten, email, ten_tai_khoan, mat_khau, so_dien_thoai, ngay_sinh, gioi_tinh, trang_thai, ngay_tao, ngay_sua, vai_tro_id) \n" +
            "values \n" +
            "( N'Khách lẻ', NULL, NULL, NULL, NULL, NULL, NULL, NULL,-1, NULL,  NULL)", nativeQuery = true)
    void addKhachLe();

    TaiKhoan findByMatKhau(String code);

    Optional<TaiKhoan> findByTenTaiKhoan(String name);

    TaiKhoan findByEmail(String email);

}
