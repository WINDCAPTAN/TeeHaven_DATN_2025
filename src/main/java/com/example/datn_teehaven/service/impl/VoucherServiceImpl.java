package com.example.datn_teehaven.service.impl;

import com.example.datn_teehaven.entyti.Voucher;
import com.example.datn_teehaven.repository.VoucherRepository;
import com.example.datn_teehaven.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    VoucherRepository voucherRepository;

    @Override
    public Voucher fillByMaVoucher() {
        return voucherRepository.getByMaVoucher();
    }

    @Override
    public List<Voucher> findAll() {
        return voucherRepository.findAll();
    }

    @Override
    public Voucher findById(Long id) {
        return voucherRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Voucher> fillAllDangDienRa() {
        return voucherRepository.fillAllDangDienRa();
    }

    @Override
    public List<Voucher> fillAll() {
        return voucherRepository.fillAll();
    }

    @Override
    public List<Voucher> fillAllDaKetThuc() {
        return voucherRepository.fillAllDaKetThuc();
    }

    @Override
    public List<Voucher> fillAllSapDienRa() {
        return voucherRepository.fillAllSapDienRa();
    }

    @Override
    public Voucher save(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    @Override
    public boolean checkMaTrung(String ma) {
        return false;
    }
}
