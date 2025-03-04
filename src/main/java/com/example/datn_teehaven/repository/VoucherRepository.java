package com.example.datn_teehaven.repository;

import com.example.datn_teehaven.entyti.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    @Query(value = "select * from voucher where ma_voucher = 'vouchernull'",nativeQuery = true)
    Voucher getByMaVoucher();

    @Query(value = "select * from voucher where trang_thai = 0",nativeQuery = true)
    List<Voucher> fillAllDangDienRa();

    @Query(value = "select * from voucher where trang_thai = 1",nativeQuery = true)
    List<Voucher> fillAllDaKetThuc();

    @Query(value = "select * from voucher where trang_thai = 2",nativeQuery = true)
    List<Voucher> fillAllSapDienRa();

    @Query(value = "select * from voucher where trang_thai = 0 or trang_thai = 1 or trang_thai =2 ORDER BY ngay_sua DESC",nativeQuery = true)
    List<Voucher> fillAll();

    @Query(value = "SELECT MAX(CONVERT(varchar, SUBSTRING(ma_voucher,9,10))) from voucher",nativeQuery = true)
    Integer index();

    @Query(value = "select * from voucher where trang_thai = 0 or trang_thai = 2 ORDER BY ngay_tao DESC",nativeQuery = true)
    List<Voucher> fillDangDienRaAndSapDienRa();
}
