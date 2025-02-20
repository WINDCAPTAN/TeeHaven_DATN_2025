package com.example.datn_teehaven.repository;

import com.example.datn_teehaven.entyti.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MauSacRepository extends JpaRepository<MauSac, Long> {

    @Query(value = "SELECT MAX(CONVERT(varchar, SUBSTRING(ma_mau,3,10))) from mau_sac", nativeQuery = true)
    Integer index();


    @Query(value = "select * from mau_sac where trang_thai = 0", nativeQuery = true)
    List<MauSac> fillAllDangHoatDong();

    @Query(value = "select * from mau_sac where trang_thai = 1", nativeQuery = true)
    List<MauSac> fillAllNgungHoatDong();

    @Query(value = "SELECT * FROM mau_sac WHERE LOWER(ten) = LOWER(:name)", nativeQuery = true)
    MauSac findMauSacByTen(@Param("name") String name);
}
