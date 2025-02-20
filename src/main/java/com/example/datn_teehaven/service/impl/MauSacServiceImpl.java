package com.example.datn_teehaven.service.impl;




import com.example.datn_teehaven.entyti.MauSac;
import com.example.datn_teehaven.repository.MauSacRepository;
import com.example.datn_teehaven.service.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MauSacServiceImpl implements MauSacService {

    @Autowired
    MauSacRepository mauSacRepository;

    @Override
    public List<MauSac> findAll() {

        Sort sort = Sort.by(Sort.Direction.DESC, "ngaySua");
        return mauSacRepository.findAll(sort);

    }

    @Override
    public List<MauSac> getAllDangHoatDong() {

        return mauSacRepository.fillAllDangHoatDong();

    }

    @Override
    public List<MauSac> getAllNgungHoatDong() {

        return mauSacRepository.fillAllNgungHoatDong();

    }


    @Override
    public MauSac save(MauSac mauSac) {

        return mauSacRepository.save(mauSac);

    }

    @Override
    public boolean checkTenTrung(String ten) {
        if (ten == null) {
            return false;
        }
        for (MauSac sp : mauSacRepository.findAll()) {
            if (sp.getTen().equals(ten)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkTenTrungSua(Long id, String ten) {
        if (ten == null) {
            return false;
        }
        for (MauSac ms : mauSacRepository.findAll()) {
            if (ms.getTen().equals(ten)) {
                if (!ms.getId().equals(id)) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public MauSac update(MauSac mauSac) {

        return mauSacRepository.save(mauSac);
    }

    @Override
    public MauSac getById(Long id) {

        return mauSacRepository.findById(id).get();
    }


    @Override
    public Integer genMaTuDong() {
        String maStr = "";
        try {
            if (mauSacRepository.index() != null) {
                maStr = mauSacRepository.index().toString();
            } else {
                maStr = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (maStr == null) {
            maStr = "0";
            int ma = Integer.parseInt(maStr);
            return ++ma;
        }
        int ma = Integer.parseInt(maStr);
        return ++ma;
    }
    public boolean isTenValid(String ten) {
        return ten != null && ten.matches("^[a-zA-Z0-9\\s]+$");
    }
}
