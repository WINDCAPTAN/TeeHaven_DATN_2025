package com.example.datn_teehaven.service;

import com.example.datn_teehaven.entyti.Voucher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VoucherService {
    Voucher fillByMaVoucher();

    List<Voucher> findAll();

    Voucher findById(Long id);

    void deleteById(Long id);

    List<Voucher> fillAllDangDienRa();

    List<Voucher> fillAll();



    List<Voucher> fillAllDaKetThuc();

    List<Voucher> fillAllSapDienRa();

    Voucher save(Voucher voucher);

    boolean checkMaTrung(String ma);
}
