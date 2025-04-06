package com.example.datn_teehaven.repository;



import com.example.datn_teehaven.entyti.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet,Long> {
    @Query(value = "SELECT COUNT(id) FROM gio_hang_chi_tiet where gio_hang_id = :idGioHang and trang_thai = 0",nativeQuery = true)
    Integer soLuongSpTrongGioHangCT(@Param("idGioHang") Long idGioHang);

    @Query(value = "select * from gio_hang_chi_tiet where gio_hang_id = :idGioHang and trang_thai = 0 order by ngay_tao desc",nativeQuery = true)
    List<GioHangChiTiet> getfindAllByIdGioHang(@Param("idGioHang")Long idGioHang);

    @Query(value = "select * from gio_hang_chi_tiet where gio_hang_id = :idGioHang  and chi_tiet_san_pham_id IN (:idChiTetSP)",nativeQuery = true)
    List<GioHangChiTiet> getByGioHangChiTiet(@Param("idGioHang")Long idGioHang,@Param("idChiTetSP")List<String> idChiTetSP);

    @Query(value = "SELECT * FROM gio_hang_chi_tiet WHERE id IN (:listId) AND gio_hang_id = :idGioHang",nativeQuery = true)
    List<GioHangChiTiet> findAllByIdGHCT(@Param("listId")List<Long> listId,@Param("idGioHang")Long idGioHang);


}
