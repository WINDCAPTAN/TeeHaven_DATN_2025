package com.example.datn_teehaven.repository;


import com.example.datn_teehaven.entyti.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang,Long> {

    @Query(value = "SELECT MAX(CONVERT(INT, SUBSTRING(ma_gio_hang,3,10))) from gio_hang",nativeQuery = true)
    Integer index();

}
