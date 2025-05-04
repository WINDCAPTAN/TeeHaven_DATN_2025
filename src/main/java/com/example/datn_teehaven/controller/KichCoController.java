package com.example.datn_teehaven.controller;

import com.example.datn_teehaven.Config.PrincipalCustom;
import com.example.datn_teehaven.Config.UserInfoUserDetails;
import com.example.datn_teehaven.entyti.KichCo;
import com.example.datn_teehaven.service.KichCoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequestMapping("/admin/kich-co")
public class KichCoController {

    @Autowired
    private KichCoService kichCoService;

    private PrincipalCustom principalCustom = new PrincipalCustom();

    @GetMapping("")
    public String hienThi(Model model) {
        UserInfoUserDetails name = principalCustom.getCurrentUserNameAdmin();
        if (name != null) {
            model.addAttribute("tenNhanVien", name.getHoVaTen());
        } else {
            return "redirect:/login";
        }
        model.addAttribute("listKichCo", kichCoService.findAll());
        model.addAttribute("kichCo", new KichCo());
        return "/admin-template/kich_co/kich-co";
    }

    @GetMapping("/dang-hoat-dong")
    public String hienThiDangHoatDong(Model model) {
        model.addAttribute("listKichCo", kichCoService.getAllDangHoatDong());
        model.addAttribute("kichCo", new KichCo());
        return "/admin-template/kich_co/kich-co";
    }

    @GetMapping("/ngung-hoat-dong")
    public String hienThiNgungHoatDong(Model model) {
        model.addAttribute("listKichCo", kichCoService.getAllNgungHoatDong());
        model.addAttribute("kichCo", new KichCo());
        return "/admin-template/kich_co/kich-co";
    }

    @GetMapping("/view-update/{id}")
    public String viewUpdate(@PathVariable("id") Long id, Model model) {
        KichCo kichCo = kichCoService.getById(id);
        model.addAttribute("kichCo", kichCo);
        model.addAttribute("listKichCo", kichCoService.findAll());
        return "/admin-template/kich_co/sua-kich-co";
    }

    @PostMapping("/update")
    public String update(
            @Valid @ModelAttribute("kichCo") KichCo kichCo,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (!kichCoService.isTenValid(kichCo.getTen())) {
            result.rejectValue("ten", "error.kichCo", "Tên kích cỡ không hợp lệ. Chỉ được chứa chữ cái tiếng Việt có dấu và khoảng trắng, không ký tự đặc biệt.");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("errorMessage", "Tên kích cỡ không hợp lệ. Chỉ được chứa chữ cái tiếng Việt có dấu và khoảng trắng, không ký tự đặc biệt.");
            return "/admin-template/kich_co/sua-kich-co";
        }

        if (!kichCoService.checkTenTrungSua(kichCo.getId(), kichCo.getTen())) {
            model.addAttribute("checkTenTrung", "Kích cỡ đã tồn tại");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("errorMessage", "Tên kích cỡ đã tồn tại.");
            return "/admin-template/kich_co/sua-kich-co";
        }

        kichCo.setNgaySua(new Date());
        kichCoService.update(kichCo);

        redirectAttributes.addFlashAttribute("checkThongBao", "thanhCong");
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật kích cỡ thành công!");
        return "redirect:/admin/kich-co";
    }

    @PostMapping("/add")
    public String add(
            @Valid @ModelAttribute("kichCo") KichCo kichCo,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (!kichCoService.isTenValid(kichCo.getTen())) {
            redirectAttributes.addFlashAttribute("checkThongBao", "thaiBai");
            redirectAttributes.addFlashAttribute("errorMessage", "Tên kích cỡ không hợp lệ. Chỉ được chứa chữ cái tiếng Việt có dấu và khoảng trắng, không ký tự đặc biệt.");
            redirectAttributes.addFlashAttribute("checkModal", "modal");
            return "redirect:/admin/kich-co";
        }

        if (!kichCoService.checkTenTrung(kichCo.getTen())) {
            redirectAttributes.addFlashAttribute("checkThongBao", "thaiBai");
            redirectAttributes.addFlashAttribute("errorMessage", "Tên kích cỡ đã tồn tại.");
            redirectAttributes.addFlashAttribute("checkModal", "modal");
            return "redirect:/admin/kich-co";
        }

        kichCo.setNgayTao(new Date());
        kichCo.setNgaySua(new Date());
        kichCo.setTrangThai(0);
        kichCoService.save(kichCo);

        redirectAttributes.addFlashAttribute("checkThongBao", "thanhCong");
        redirectAttributes.addFlashAttribute("successMessage", "Thêm kích cỡ thành công!");
        return "redirect:/admin/kich-co";
    }
}
