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
        if (!mauSacService.isTenValid(mauSac.getTen())) {
            result.rejectValue("ten", "error.mauSac", "Tên màu sắc chỉ được chứa chữ cái tiếng Việt có dấu.");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("errorMessage", "Tên màu sắc không hợp lệ.");
            return "/admin-template/mau_sac/sua-mau-sac";
        }

        if (!mauSacService.checkTenTrungSua(mauSac.getId(), mauSac.getTen())) {
            model.addAttribute("checkTenTrung", "Màu sắc đã tồn tại");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("errorMessage", "Tên màu sắc đã tồn tại.");
            return "/admin-template/mau_sac/sua-mau-sac";
        }

        // Cập nhật dữ liệu màu sắc
        mauSac.setNgaySua(new Date());
        mauSacService.update(mauSac);

        redirectAttributes.addFlashAttribute("checkThongBao", "thanhCong");
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật màu sắc thành công!");
        return "redirect:/admin/mau-sac";
    }

    @PostMapping("/add")
    public String add(
            @Valid @ModelAttribute("mauSac") MauSac mauSac,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // Kiểm tra tên có hợp lệ không
        if (!mauSacService.isTenValid(mauSac.getTen())) {
            redirectAttributes.addFlashAttribute("checkThongBao", "thaiBai");
            redirectAttributes.addFlashAttribute("errorMessage", "Tên màu sắc chỉ được chứa chữ cái tiếng Việt có dấu.");
            return "redirect:/admin/mau-sac"; // Chuyển hướng về trang danh sách
        }

        if (!mauSacService.checkTenTrung(mauSac.getTen())) {
            redirectAttributes.addFlashAttribute("checkThongBao", "thaiBai");
            redirectAttributes.addFlashAttribute("errorMessage", "Tên màu sắc đã tồn tại.");
            return "redirect:/admin/mau-sac"; // Chuyển hướng về trang danh sách
        }

        // Lưu màu sắc nếu hợp lệ
        mauSac.setMaMau("MS" + mauSacService.genMaTuDong());
        mauSac.setNgayTao(new Date());
        mauSac.setNgaySua(new Date());
        mauSac.setTrangThai(0);
        mauSacService.save(mauSac);

        redirectAttributes.addFlashAttribute("checkThongBao", "thanhCong");
        redirectAttributes.addFlashAttribute("successMessage", "Thêm màu sắc thành công!");
        return "redirect:/admin/mau-sac"; // Chuyển hướng về danh sách màu sắc
    }


}

