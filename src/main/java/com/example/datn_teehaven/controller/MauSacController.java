package com.example.datn_teehaven.controller;


import com.example.datn_teehaven.entyti.MauSac;
import com.example.datn_teehaven.service.MauSacService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequestMapping("/admin/mau-sac")
public class MauSacController {
    @Autowired
    private MauSacService mauSacService;

    @GetMapping("")
    public String hienThi(Model model) {
        model.addAttribute("listMauSac", mauSacService.findAll());
        model.addAttribute("mauSac", new MauSac());
        return "/admin-template/mau_sac/mau-sac";
    }

    @GetMapping("/dang-hoat-dong")
    public String hienThiDangHoatDong(Model model) {
        model.addAttribute("listMauSac", mauSacService.getAllDangHoatDong());
        model.addAttribute("mauSac", new MauSac());
        return "/admin-template/mau_sac/mau-sac";
    }

    @GetMapping("/ngung-hoat-dong")
    public String hienThiNgungHoatDong(Model model) {
        model.addAttribute("listMauSac", mauSacService.getAllNgungHoatDong());
        model.addAttribute("mauSac", new MauSac());
        return "/admin-template/mau_sac/mau-sac";
    }

    @GetMapping("/view-update/{id}")
    public String viewUpdate(Model model, @PathVariable("id") Long id) {
        MauSac mauSac = mauSacService.getById(id);
        model.addAttribute("listMauSac", mauSacService.findAll());
        model.addAttribute("mauSac", mauSac);
        return "/admin-template/mau_sac/sua-mau-sac";
    }

    @PostMapping("/update")
    public String update(
            @Valid @ModelAttribute("mauSac") MauSac mauSac,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // Kiểm tra trường trống
        if (mauSac.getTen() == null || mauSac.getTen().trim().isEmpty()) {
            result.rejectValue("ten", "error.mauSac", "Tên màu sắc không được để trống.");
            model.addAttribute("listMauSac", mauSacService.findAll());
            return "/admin-template/mau_sac/sua-mau-sac"; // Giữ nguyên trang
        }

        // Kiểm tra lỗi trong quá trình nhập dữ liệu
        if (result.hasErrors()) {
            model.addAttribute("listMauSac", mauSacService.findAll());
            return "/admin-template/mau_sac/sua-mau-sac"; // Giữ nguyên trang
        }

        // Kiểm tra ký tự đặc biệt trong tên màu sắc
        if (!mauSacService.isTenValid(mauSac.getTen())) {
            result.rejectValue("ten", "error.mauSac", "Tên màu sắc không được chứa ký tự đặc biệt.");
            model.addAttribute("listMauSac", mauSacService.findAll());
            return "/admin-template/mau_sac/sua-mau-sac"; // Giữ nguyên trang
        }

        // Kiểm tra trùng tên khi cập nhật
        if (!mauSacService.checkTenTrungSua(mauSac.getId(), mauSac.getTen())) {
            model.addAttribute("checkTenTrung", "Màu sắc đã tồn tại");
            model.addAttribute("listMauSac", mauSacService.findAll());
            return "/admin-template/mau_sac/sua-mau-sac"; // Giữ nguyên trang
        }

        // Cập nhật dữ liệu màu sắc
        mauSac.setNgaySua(new Date());
        mauSacService.update(mauSac);

        // Lưu thông báo và chuyển hướng
        redirectAttributes.addFlashAttribute("checkThongBaoUpdate", "thanhCongUpdate");
        return "redirect:/admin/mau-sac"; // Chuyển hướng
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("mauSac", new MauSac());
        return "/admin-template/mau_sac/them-mau-sac"; // Đường dẫn tới template cho form thêm
    }

    @PostMapping("/add")
    public String add(
            @Valid @ModelAttribute("mauSac") MauSac mauSac,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // Kiểm tra trường trống
        if (mauSac.getTen() == null || mauSac.getTen().trim().isEmpty()) {
            result.rejectValue("ten", "error.mauSac", "Tên màu sắc không được để trống.");
            model.addAttribute("listMauSac", mauSacService.findAll());
            return "/admin-template/mau_sac/mau-sac"; // Giữ nguyên trang
        }

        // Kiểm tra lỗi trong quá trình nhập dữ liệu
        if (result.hasErrors()) {
            model.addAttribute("listMauSac", mauSacService.findAll());
            return "/admin-template/mau_sac/mau-sac"; // Giữ nguyên trang
        }

        // Kiểm tra ký tự đặc biệt trong tên màu sắc
        if (!mauSacService.isTenValid(mauSac.getTen())) {
            result.rejectValue("ten", "error.mauSac", "Tên màu sắc không được chứa ký tự đặc biệt.");
            model.addAttribute("listMauSac", mauSacService.findAll());
            return "/admin-template/mau_sac/mau-sac"; // Giữ nguyên trang
        }

        // Kiểm tra trùng tên khi thêm
        if (!mauSacService.checkTenTrung(mauSac.getTen())) {
            model.addAttribute("checkTenTrung", "Màu sắc đã tồn tại");
            model.addAttribute("listMauSac", mauSacService.findAll());
            return "/admin-template/mau_sac/them-mau-sac"; // Giữ nguyên trang
        }

        // Lưu thông báo và thêm màu sắc
        mauSac.setMaMau("MS" + mauSacService.genMaTuDong());
        mauSac.setNgayTao(new Date());
        mauSac.setNgaySua(new Date());
        mauSac.setTrangThai(0);
        mauSacService.save(mauSac);

        redirectAttributes.addFlashAttribute("checkThongBaoAdd", "thanhCongAdd"); // Thêm thông báo cho việc thêm
        return "redirect:/admin/mau-sac";
    }
}

