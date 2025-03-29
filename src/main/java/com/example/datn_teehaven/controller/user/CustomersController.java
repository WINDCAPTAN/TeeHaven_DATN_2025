package com.example.datn_teehaven.controller.user;





import com.example.datn_teehaven.Config.PrincipalCustom;
import com.example.datn_teehaven.entyti.TaiKhoan;
import com.example.datn_teehaven.service.GioHangChiTietService;
import com.example.datn_teehaven.service.HoaDonChiTietService;
import com.example.datn_teehaven.service.KhachHangService;
import com.example.datn_teehaven.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class CustomersController {

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

}
