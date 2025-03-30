package com.example.datn_teehaven.controller.user;





import com.example.datn_teehaven.Config.PrincipalCustom;
import com.example.datn_teehaven.entyti.TaiKhoan;
import com.example.datn_teehaven.entyti.TayAo;
import com.example.datn_teehaven.service.ChiTietSanPhamSerivce;
import com.example.datn_teehaven.service.GioHangChiTietService;
import com.example.datn_teehaven.service.HoaDonChiTietService;
import com.example.datn_teehaven.service.KhachHangService;
import com.example.datn_teehaven.service.KichCoService;
import com.example.datn_teehaven.service.MauSacService;
import com.example.datn_teehaven.service.TaiKhoanService;
import com.example.datn_teehaven.service.TayAoService;
import com.example.datn_teehaven.service.ThuongHieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class CustomersController {
//huynh
    private Long idTaiKhoan;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private PrincipalCustom principalCustom = new PrincipalCustom();
    @Autowired
    private KhachHangService khachHangService;
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;
    @Autowired
    private GioHangChiTietService gioHangChiTietService;
    @Autowired
    private ChiTietSanPhamSerivce chiTietSanPhamSerivce;
    @Autowired
    private MauSacService mauSacService;
    @Autowired
    private KichCoService kichCoService;
    @Autowired
    private TayAoService tayAoService;
    @Autowired
    private ThuongHieuService thuongHieuService;

    @GetMapping("/home")
    public String home(Model model) {
        if (principalCustom.getCurrentUserNameCustomer() != null) {
            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanByName(principalCustom.getCurrentUserNameCustomer());
            idTaiKhoan = taiKhoan.getId();
        } else {
            idTaiKhoan = null;
        }

        // Lấy sản phẩm nổi bật
        model.addAttribute("listTop5HDCT", hoaDonChiTietService.finTop5HDCT());

        model.addAttribute("listTop10SPM", hoaDonChiTietService.findTop10SanPhamMoi());

        // Lấy sản phẩm bán chạy
        model.addAttribute("listTopBanChay", hoaDonChiTietService.findTop5BanChay());

        if (principalCustom.getCurrentUserNameCustomer() != null) {
            TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
            model.addAttribute("checkDangNhap", "true");
            model.addAttribute("soLuongSPGioHangCT",
                    gioHangChiTietService.soLuongSPGioHangCT(khachHang.getGioHang().getId()));
        } else {
            model.addAttribute("checkDangNhap", "false");
        }

        return "/customer-template/ban-hang-customer";
    }

    @GetMapping("/shop")
    // @ResponseBody
    public String search(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "MauSac", required = false) List<Long> MauSac,
            @RequestParam(value = "KichCo", required = false) List<Long> KichCo,
            @RequestParam(value = "TayAo", required = false) List<Long> TayAo,
            @RequestParam(value = "ThuongHieu", required = false) List<Long> ThuongHieu,
            @RequestParam(value = "minPrice", defaultValue = "") Long minPrice,
            @RequestParam(value = "maxPrice", defaultValue = "") Long maxPrice,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "28") Integer size,
            Model model) {
        if (MauSac == null) {
            MauSac = chiTietSanPhamSerivce.getAllIdMauSacCTSP();
        }
        if (KichCo == null) {
            KichCo = chiTietSanPhamSerivce.getAllIdKichCoCTSP();
        }
        if (TayAo == null) {
            TayAo = chiTietSanPhamSerivce.getAllIdTayAoCTSP();
        }
        if (ThuongHieu == null) {
            ThuongHieu = chiTietSanPhamSerivce.getAllIdThuongHieuCTSP();
        }
        if (minPrice == null) {
            minPrice = chiTietSanPhamSerivce.getAllMinGiaCTSP();
        }
        if (maxPrice == null) {
            maxPrice = chiTietSanPhamSerivce.getAllMaxGiaCTSP();
        }

        if (principalCustom.getCurrentUserNameCustomer() != null) {
            TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
            model.addAttribute("checkDangNhap", "true");
            model.addAttribute("soLuongSPGioHangCT",
                    gioHangChiTietService.soLuongSPGioHangCT(khachHang.getGioHang().getId()));
        } else {
            model.addAttribute("checkDangNhap", "false");
        }
        if (chiTietSanPhamSerivce.searchAll(page, size, keyword, MauSac, KichCo, TayAo, ThuongHieu, minPrice, maxPrice)
                .isEmpty()) {
            model.addAttribute("checkListChiTietSP", "true");
        } else {
            model.addAttribute("listChiTietSP",
                    chiTietSanPhamSerivce
                            .searchAll(page, size, keyword, MauSac, KichCo, TayAo, ThuongHieu, minPrice, maxPrice)
                            .stream().toList());
        }
        model.addAttribute("pageCount",
                chiTietSanPhamSerivce
                        .searchAll(page, size, keyword, MauSac, KichCo, TayAo, ThuongHieu, minPrice, maxPrice)
                        .getTotalPages());
        model.addAttribute("listMauSac", mauSacService.findAll());
        model.addAttribute("listKichCo", kichCoService.findAll());
        model.addAttribute("listTayAo", tayAoService.findAll());
        model.addAttribute("listThuongHieu", thuongHieuService.getAllDangHoatDong());
        return "/customer-template/shop";
    }
}
