package com.example.datn_teehaven.controller;

import com.example.datn_teehaven.Config.PrincipalCustom;
import com.example.datn_teehaven.Config.UserInfoUserDetails;
import com.example.datn_teehaven.entyti.TayAo;
import com.example.datn_teehaven.service.TayAoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequestMapping("/admin/tay-ao")
public class TayAoController {
    @Autowired
    private TayAoService tayAoService;

    private PrincipalCustom principalCustom = new PrincipalCustom();

    @GetMapping()
    public String hienThi(Model model) {
        UserInfoUserDetails name = principalCustom.getCurrentUserNameAdmin();
        if (name != null) {
            model.addAttribute("tenNhanVien", principalCustom.getCurrentUserNameAdmin().getHoVaTen());
        } else {
            return "redirect:/login";
        }
        model.addAttribute("listTayAo", tayAoService.findAll());
        model.addAttribute("tayAo", new TayAo());
        return "/admin-template/tay_ao/tay-ao";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("tayAo") TayAo tayAo,
                      BindingResult result,
                      Model model,
                      RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            model.addAttribute("checkModal", "modal");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("listtayAo", tayAoService.findAll());
            return "/admin-template/tay_ao/tay-ao";
    } else if (!tayAoService.checkTenTrung(tayAo.getTen())) {
            model.addAttribute("checkModal", "modal");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("checkTenTrung", "Loại tay áo đã tồn tại");
            model.addAttribute("listtayAo", tayAoService.findAll());
            return "/admin-template/tay_ao/tay-ao";
        } else {
            redirectAttributes.addFlashAttribute("checkThongBao", "thanhCong");
            tayAo.setNgayTao(new Date());
            tayAo.setNgaySua(new Date());
            tayAo.setTrangThai(0);
            tayAoService.save(tayAo);
            return "redirect:/admin/tay-ao";
        }
    }

    @PostMapping("/update")
    public String update(@Valid
                         @ModelAttribute("tayAo") TayAo tayAo,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            model.addAttribute("checkThongBao", "thaiBai");
            return "/admin-template/tay_ao/sua-tay-ao";
        } else if (!tayAoService.checkTenTrungSua(tayAo.getId(), tayAo.getTen())) {
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("checkTenTrung", "Loại tay áo đã tồn tại");
            return "/admin-template/tay_ao/sua-tay-ao";
        } else {
            redirectAttributes.addFlashAttribute("checkThongBao", "thanhCong");
            TayAo ld = tayAoService.getById(tayAo.getId());
            tayAo.setNgayTao(ld.getNgayTao());
            tayAo.setNgaySua(new Date());
            tayAoService.update(tayAo);
            return "redirect:/admin/tay-ao";
        }
    }

    @GetMapping("/view-update/{id}")
    public String viewUpdate(
            Model model,
            @PathVariable("id") Long id
    ) {
        TayAo tayAo = tayAoService.getById(id);
        model.addAttribute("tayAo", tayAo);
        return "/admin-template/tay_ao/sua-tay-ao";
}

    @GetMapping("/ngung-hoat-dong")
    public String hienThiNgungHoatDong(Model model) {
        model.addAttribute("listTayAo", tayAoService.getAllNgungHoatDong());
        model.addAttribute("tayAo", new TayAo());
        return "/admin-template/tay_ao/tay-ao";
    }

    @GetMapping("/dang-hoat-dong")
    public String hienThiDangHoatDong(Model model) {
        model.addAttribute("listTayAo", tayAoService.getAllDangHoatDong());
        model.addAttribute("tayAo", new TayAo());
        return "/admin-template/tay_ao/tay-ao";
    }
}


