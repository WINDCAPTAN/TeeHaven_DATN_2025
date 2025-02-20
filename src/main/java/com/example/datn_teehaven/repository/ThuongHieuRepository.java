package com.example.datn_teehaven.repository;


import com.example.datn_teehaven.entyti.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Long> {
    @Query(value = "select * from thuong_hieu where trang_thai = 0",nativeQuery = true)
    List<ThuongHieu> fillAllDangHoatDong();

    @Query(value = "select * from thuong_hieu where trang_thai = 1",nativeQuery = true)
    List<ThuongHieu> fillAllNgungHoatDong();

}
