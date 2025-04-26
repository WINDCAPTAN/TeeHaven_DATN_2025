package com.example.datn_teehaven.repository;


import com.example.datn_teehaven.entyti.HoaDon;
import com.example.datn_teehaven.entyti.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon, Long> {

    @Query("select lshd from LichSuHoaDon lshd where lshd.hoaDon.id =:idhd order by lshd.ngaySua desc")
    List<LichSuHoaDon> findByIdHd(@Param("idhd") Long idhd);

    @Query(value = "select * from lich_su_hoa_don where hoa_don_id = :idhd and trang_thai in(0,1,2,3,4,5,6) order by ngay_sua asc",nativeQuery = true)

    List<LichSuHoaDon> findByIdhdNgaySuaAsc(@Param("idhd") Long idhd);

    LichSuHoaDon findTopByHoaDonOrderByNgaySuaDesc(HoaDon hoaDon);
}
