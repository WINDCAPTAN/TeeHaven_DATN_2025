package com.example.datn_teehaven.service.impl;



import com.example.datn_teehaven.entyti.Voucher;
import com.example.datn_teehaven.repository.VoucherRepository;
import com.example.datn_teehaven.service.VoucherService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        voucherRepository.deleteById(id);
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
        for (Voucher sp : voucherRepository.findAll()) {
            if (sp.getMaVoucher().equalsIgnoreCase(ma)) {
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean checkTenTrung(String ten) {
        for (Voucher sp : voucherRepository.findAll()) {
            if (sp.getTenVoucher().equalsIgnoreCase(ten)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkTenTrungSua(String ma, String ten) {
        for (Voucher sp : voucherRepository.findAll()) {
            if (sp.getTenVoucher().equalsIgnoreCase(ten)) {
                if (!sp.getMaVoucher().equalsIgnoreCase(ma)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkName(Long id, String ten) {
        for (Voucher sp : voucherRepository.findAll()) {
            if (sp.getTenVoucher().equalsIgnoreCase(ten)) {
                if (!sp.getId().equals(id)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkCode(Long id, String ma) {
        for (Voucher sp : voucherRepository.findAll()) {
            if (sp.getMaVoucher().equalsIgnoreCase(ma)) {
                if (!sp.getId().equals(id)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Voucher update(Voucher voucher) {

        return voucherRepository.save(voucher);

    }

    @Override
    public Voucher getById(Long id) {

        return voucherRepository.findById(id).get();

    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 5000)
    public void updateVoucherStatus() {
        List<Voucher> vouchers = voucherRepository.fillDangDienRaAndSapDienRa();
        LocalDateTime currentDateTime = LocalDateTime.now();
        for (Voucher voucher : vouchers) {
            if (currentDateTime.isBefore(voucher.getNgayBatDau())) {
                voucher.setTrangThai(2);
            } else if (currentDateTime.isAfter(voucher.getNgayKetThuc())) {
                voucher.setTrangThai(1);
            } else {
                voucher.setTrangThai(0);
            }
            update(voucher);
        }
    }
}
