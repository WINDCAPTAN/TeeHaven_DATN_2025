package com.example.datn_teehaven.controller.user;





import com.example.datn_teehaven.Config.PrincipalCustom;
import com.example.datn_teehaven.entyti.ChiTietSanPham;
import com.example.datn_teehaven.entyti.TaiKhoan;
import com.example.datn_teehaven.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/user/shop-single/{id}")
    public String shopSingle(
            @PathVariable("id") String id,
            Model model) {
        ChiTietSanPham ChiTietSanPham = chiTietSanPhamSerivce.getAllById(Long.valueOf(id)).get(0);
        List<ChiTietSanPham> listChiTietSanPham = chiTietSanPhamSerivce.getAllById(Long.valueOf(id));
        TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
        model.addAttribute("soLuongSPGioHangCT",
                gioHangChiTietService.soLuongSPGioHangCT(khachHang.getGioHang().getId()));
        model.addAttribute("chiTietSp", ChiTietSanPham);
        model.addAttribute("listChiTietSp", listChiTietSanPham);
        model.addAttribute("listTop5HDCT", hoaDonChiTietService.finTop5HDCT());
        return "/customer-template/shop-single";
    }

}
