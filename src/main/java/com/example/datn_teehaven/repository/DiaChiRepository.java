package com.example.datn_teehaven.repository;

import com.example.datn_teehaven.entyti.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiaChiRepository extends JpaRepository<DiaChi, Long> {
    @Query(value = "select*from dia_chi where tai_khoan_id = :idTaiKhoan order by trang_thai asc",nativeQuery = true)
    List<DiaChi> getAllByIdTaiKhoan(@Param("idTaiKhoan") Long id);

    @Query(value = "select*from dia_chi where trang_thai = :trangThai",nativeQuery = true)
    List<DiaChi> fillAllByTrangThai(@Param("trangThai") Integer trangThai);
}
