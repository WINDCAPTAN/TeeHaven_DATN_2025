package com.example.datn_teehaven.service.impl;



import com.example.datn_teehaven.entyti.KichCo;
import com.example.datn_teehaven.repository.KichCoRepository;
import com.example.datn_teehaven.service.KichCoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class KichCoServiceImpl implements KichCoService {

    @Autowired
    KichCoRepository kichCoRepository;

    @Override
    public List<KichCo> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "ngaySua");
        return kichCoRepository.findAll(sort);
    }

    @Override
    public List<KichCo> getAllDangHoatDong() {
        return kichCoRepository.fillAllDangHoatDong();
    }

    @Override
    public List<KichCo> getAllNgungHoatDong() {
        return kichCoRepository.fillAllNgungHoatDong();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public KichCo save(KichCo kichCo) {
        return kichCoRepository.save(kichCo);
    }

    @Override
    public boolean checkTenTrung(String ten) {
        if (ten == null) {
            return false;
        }
        for (KichCo sp : kichCoRepository.findAll()) {
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
        for (KichCo sp : kichCoRepository.findAll()) {
            if (sp.getTen().equals(ten)) {
                if (!sp.getId().equals(id)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public KichCo update(KichCo kichCo) {
        return kichCoRepository.save(kichCo);
    }

    @Override
    public KichCo getById(Long id) {
        return kichCoRepository.findById(id).get();
    }

    @Override
    public boolean isTenValid(String ten) {
        // Kiểm tra null hoặc rỗng
        if (ten == null || ten.trim().isEmpty()) {
            return false;
        }

        // Regex cho phép chữ cái tiếng Việt có dấu và khoảng trắng
        // Không cho phép số và ký tự đặc biệt
        String vietnameseRegex = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+$";

        return Pattern.matches(vietnameseRegex, ten);
    }

}
