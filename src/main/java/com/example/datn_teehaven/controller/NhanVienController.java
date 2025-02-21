package com.example.datn_teehaven.controller;

import com.example.datn_teehaven.entyti.TaiKhoan;
import com.example.datn_teehaven.entyti.VaiTro;
import com.example.datn_teehaven.service.NhanVienService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequestMapping("/admin/nhan-vien")
public class NhanVienController {
    @Autowired
    private NhanVienService nhanVienService;



    TaiKhoan userInfo = new TaiKhoan();

    @GetMapping()
    public String hienThi(Model model) {
        model.addAttribute("listTaiKhoan", nhanVienService.getAll());
        model.addAttribute("nhanVien",new TaiKhoan());
        return "admin-template/nhan_vien/nhan-vien";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("nhanVien") TaiKhoan taiKhoan,
                      BindingResult result,
                      Model model,
                      RedirectAttributes redirectAttributes,
                      HttpServletRequest request,
                      @RequestParam("email") String email
    ) {
        userInfo = taiKhoan;
        TaiKhoan taiKhoanEntity = new TaiKhoan();
        taiKhoanEntity.setNgaySinh(taiKhoan.getNgaySinh());
        if (result.hasErrors()) {
            model.addAttribute("checkModal", "modal");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("listTaiKhoan", nhanVienService.getAll());
            return "/admin-template/nhan_vien/nhan-vien";
        } else if (!taiKhoanEntity.isValidNgaySinh()) {
            model.addAttribute("checkModal", "modal");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("checkNgaySinh", "ngaySinh");
            model.addAttribute("listTaiKhoan", nhanVienService.getAll());
            return "/admin-template/nhan_vien/nhan-vien";
        } else if (!nhanVienService.checkTenTaiKhoanTrung(taiKhoan.getTenTaiKhoan())) {
            model.addAttribute("checkModal", "modal");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("checkTenTrung", "Tên tài khoản đã tồn tại");
            model.addAttribute("checkEmailTrung", "Email đã tồn tại");
            model.addAttribute("listTaiKhoan", nhanVienService.getAll());
            return "/admin-template/nhan_vien/nhan-vien";
        } else if (!nhanVienService.checkEmail(taiKhoan.getEmail())) {
            model.addAttribute("checkModal", "modal");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("checkEmailTrung", "Email đã tồn tại");
            model.addAttribute("listTaiKhoan", nhanVienService.getAll());
            return "/admin-template/nhan_vien/nhan-vien";
        } else {
            redirectAttributes.addFlashAttribute("checkThongBao", "thanhCong");
            String url = request.getRequestURL().toString();
            System.out.println(url);
            url = url.replace(request.getServletPath(), "");
            System.out.println(userInfo);
            userInfo.setNgayTao(new Date());
            userInfo.setNgaySua(new Date());
            VaiTro vaiTro = new VaiTro();
            vaiTro.setId(Long.valueOf(1));
            userInfo.setVaiTro(vaiTro);
            userInfo.setTrangThai(0);
            userInfo.setVaiTro(vaiTro);
            nhanVienService.update(userInfo);
            return "redirect:/admin/nhan-vien";
        }
    }

    @GetMapping("/dang-hoat-dong")
    public String hienThiDangHoatDong(
            Model model
    ) {
        model.addAttribute("listTaiKhoan", nhanVienService.getAllDangHoatDong());
        model.addAttribute("nhanVien", new TaiKhoan());
        return "/admin-template/nhan_vien/nhan-vien";
    }
    @GetMapping("/ngung-hoat-dong")
    public String hienThiNgungHoatDong(Model model) {
        model.addAttribute("listTaiKhoan", nhanVienService.getAllNgungHoatDong());
        model.addAttribute("nhanVien", new TaiKhoan());
        return "/admin-template/nhan_vien/nhan-vien";
    }

    @GetMapping("/view-update-nhan-vien/{id}")
    public String viewUpdate(
            Model model,
            @PathVariable("id") Long id
    ) {
        TaiKhoan taiKhoan = nhanVienService.getById(id);
        model.addAttribute("nhanVien", taiKhoan);
        return "/admin-template/nhan_vien/sua-nhan-vien";

    }
    @PostMapping("/update")
    public String update(
            @Valid
            @ModelAttribute("nhanVien") TaiKhoan taiKhoan,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        TaiKhoan taiKhoanEntity = new TaiKhoan();
        taiKhoanEntity.setNgaySinh(taiKhoan.getNgaySinh());
        if (result.hasErrors()) {
            model.addAttribute("checkModal", "modal");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("listTaiKhoan", nhanVienService.getAll());
            return "/admin-template/nhan_vien/sua-nhan-vien";
        } else if (!taiKhoanEntity.isValidNgaySinh()) {
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("checkNgaySinh", "ngaySinh");
            return "/admin-template/nhan_vien/sua-nhan-vien";
        } else if (!nhanVienService.checkTenTkTrungSua(taiKhoan.getId(), taiKhoan.getTenTaiKhoan())) {
            model.addAttribute("checkModal", "modal");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("checkTenTrung", "Tên tài khoản đã tồn tại");
            model.addAttribute("listTaiKhoan", nhanVienService.getAll());
            return "/admin-template/nhan_vien/sua-nhan-vien";
        } else if (!nhanVienService.checkEmailSua(taiKhoan.getId(), taiKhoan.getEmail())) {
            model.addAttribute("checkModal", "modal");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("checkEmailTrung", "Email đã tồn tại");
            model.addAttribute("listTaiKhoan", nhanVienService.getAll());
            return "/admin-template/nhan_vien/sua-nhan-vien";
        } else {
            redirectAttributes.addFlashAttribute("checkThongBao", "thanhCong");
            taiKhoan.setNgaySua(new Date());
            VaiTro vaiTro = new VaiTro();
            vaiTro.setId(Long.valueOf(1));
            taiKhoan.setVaiTro(vaiTro);
            taiKhoan.setTrangThai(taiKhoan.getTrangThai());
            nhanVienService.update(taiKhoan);
            return "redirect:/admin/nhan-vien";
        }
    }

}
