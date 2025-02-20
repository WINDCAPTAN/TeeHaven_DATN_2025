package com.example.datn_teehaven.repository;



import com.example.datn_teehaven.entyti.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet,Long> {



}
