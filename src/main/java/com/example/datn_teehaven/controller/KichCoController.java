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
            model.addAttribute("tenNhanVien", principalCustom.getCurrentUserNameAdmin().getHoVaTen());
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
    public String viewUpdate(Model model, @PathVariable("id") Long id) {
        KichCo kichCo = kichCoService.getById(id);
        model.addAttribute("listKichCo", kichCoService.findAll());
        model.addAttribute("kichCo", kichCo);
        return "/admin-template/kich_co/sua-kich-co";
    }

    @PostMapping("/update")
    public String update(
            @Valid @ModelAttribute("kichCo") KichCo kichCo,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // Kiểm tra trường trống
        if (kichCo.getTen() == null || kichCo.getTen().trim().isEmpty()) {
            result.rejectValue("ten", "error.kichCo", "Tên kích cỡ không được để trống.");
            model.addAttribute("listKichCo", kichCoService.findAll());
            return "/admin-template/kich_co/sua-kich-co"; // Giữ nguyên trang
        }

        // Kiểm tra lỗi trong quá trình nhập dữ liệu
        if (result.hasErrors()) {
            model.addAttribute("listKichCo", kichCoService.findAll());
            return "/admin-template/kich_co/sua-kich-co"; // Giữ nguyên trang
        }

        // Kiểm tra ký tự đặc biệt trong tên kích cỡ
        if (!kichCoService.isTenValid(kichCo.getTen())) {
            result.rejectValue("ten", "error.kichCo", "Tên kích cỡ không được chứa ký tự đặc biệt.");
            model.addAttribute("listKichCo", kichCoService.findAll());
            return "/admin-template/kich_co/sua-kich-co"; // Giữ nguyên trang
        }

        // Kiểm tra trùng tên khi cập nhật
        if (!kichCoService.checkTenTrungSua(kichCo.getId(), kichCo.getTen())) {
            model.addAttribute("checkTenTrung", "Kích cỡ đã tồn tại");
            model.addAttribute("listKichCo", kichCoService.findAll());
            return "/admin-template/kich_co/sua-kich-co"; // Giữ nguyên trang
        }

        // Cập nhật dữ liệu kích cỡ
        kichCo.setNgaySua(new Date());
        kichCoService.update(kichCo);

        // Lưu thông báo và chuyển hướng
        redirectAttributes.addFlashAttribute("checkThongBaoUpdate", "thanhCongUpdate");
        return "redirect:/admin/kich-co"; // Chuyển hướng
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("kichCo", new KichCo());
        return "/admin-template/kich_co/them-kich-co"; // Đường dẫn tới template cho form thêm
    }

    @PostMapping("/add")
    public String add(
            @Valid @ModelAttribute("kichCo") KichCo kichCo,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // Kiểm tra trường trống
        if (kichCo.getTen() == null || kichCo.getTen().trim().isEmpty()) {
            result.rejectValue("ten", "error.kichCo", "Tên kích cỡ không được để trống.");
            model.addAttribute("listKichCo", kichCoService.findAll());
            return "/admin-template/kich_co/kich-co"; // Giữ nguyên trang
        }

        // Kiểm tra lỗi trong quá trình nhập dữ liệu
        if (result.hasErrors()) {
            model.addAttribute("listKichCo", kichCoService.findAll());
            return "/admin-template/kich_co/kich-co"; // Giữ nguyên trang
        }

        // Kiểm tra ký tự đặc biệt trong tên kích cỡ
        if (!kichCoService.isTenValid(kichCo.getTen())) {
            result.rejectValue("ten", "error.kichCo", "Tên kích cỡ không được chứa ký tự đặc biệt.");
            model.addAttribute("listKichCo", kichCoService.findAll());
            return "/admin-template/kich_co/kich-co"; // Giữ nguyên trang
        }

        // Kiểm tra trùng tên khi thêm
        if (!kichCoService.checkTenTrung(kichCo.getTen())) {
            model.addAttribute("checkTenTrung", "Kích cỡ đã tồn tại");
            model.addAttribute("listKichCo", kichCoService.findAll());
            return "/admin-template/kich_co/them-kich-co"; // Giữ nguyên trang
        }

        // Lưu thông báo và thêm kích cỡ
        kichCo.setNgayTao(new Date());
        kichCo.setNgaySua(new Date());
        kichCo.setTrangThai(0);
        kichCoService.save(kichCo);

        redirectAttributes.addFlashAttribute("checkThongBaoAdd", "thanhCongAdd"); // Thêm thông báo cho việc thêm
        return "redirect:/admin/kich-co";
    }
}
