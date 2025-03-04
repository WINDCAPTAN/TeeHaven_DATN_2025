package com.example.datn_teehaven.repository;

import com.example.datn_teehaven.entyti.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    @Query("Select hd from HoaDon hd where hd.trangThai=:tt order by hd.ngayTao asc")
    List<HoaDon> findByTrangThai(@Param("tt") Integer trangThai);

    @Query("Select hd from HoaDon hd where hd.trangThai !=-1 and hd.trangThai !=8")
    List<HoaDon> findAllOrderByNgaySua();

    @Query("Select hd from HoaDon hd where hd.maHoaDon=:ma")
    HoaDon findByMa(@Param("ma") String ma);

    @Query("Select hd from HoaDon hd where hd.trangThai=:tt")
    List<HoaDon> find5ByTrangThai(@Param("tt") Integer trangThai);

    @Query("select COUNT(hd) from HoaDon hd where hd.trangThai = -1")
    Integer countHoaDonTreo();
}
