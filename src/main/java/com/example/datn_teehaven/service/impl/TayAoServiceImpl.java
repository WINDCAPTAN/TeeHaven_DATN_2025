package com.example.datn_teehaven.service.impl;



import com.example.datn_teehaven.entyti.TayAo;
import com.example.datn_teehaven.repository.TayAoRepository;
import com.example.datn_teehaven.service.TayAoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TayAoServiceImpl implements TayAoService {
    @Autowired
    private TayAoRepository tayAoRepository;

    @Override
    public List<TayAo> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "ngaySua");
        return tayAoRepository.findAll(sort);
    }

    @Override
    public List<TayAo> getAllDangHoatDong() {
        return tayAoRepository.fillAllDangHoatDong();
    }

    @Override
    public List<TayAo> getAllNgungHoatDong() {
        return tayAoRepository.fillAllNgungHoatDong();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public TayAo save(TayAo tayAo) {
        return tayAoRepository.save(tayAo);
    }

    @Override
    public TayAo update(TayAo tayAo) {
        return tayAoRepository.save(tayAo);
    }

    @Override
    public TayAo getById(Long id) {
        return tayAoRepository.findById(id).get();
    }

    @Override
    public boolean isTenValid(String ten) {
        return ten != null && ten.matches("^[a-zA-Z0-9\\sàáạảãâầấậẩẫêềếệểễôồốộổỗưừứựửữ]+$");
    }

    @Override
    public boolean checkTenTrung(String ten) {
        for (TayAo sp : tayAoRepository.findAll()) {
            if (sp.getTen().equalsIgnoreCase(ten)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkTenTrungSua(Long id, String ten) {
        if (ten == null){
            return false;
        }
        for (TayAo sp : tayAoRepository.findAll()){
            if (sp.getTen().equals(ten)){
                if(!sp.getId().equals(id)){
                    return false;
                }
            }
        }
        return true;
    }
}
