package com.example.datn_teehaven.controller.user;





import com.example.datn_teehaven.Config.PrincipalCustom;
import com.example.datn_teehaven.entyti.*;
import com.example.datn_teehaven.service.*;
import com.example.datn_teehaven.entyti.ChiTietSanPham;
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
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Controller
public class CustomersController {
    //huynh1 //6/4
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
    @Autowired
    private DiaChiService diaChiService;
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private LichSuHoaDonService lichSuHoaDonService;
    @Autowired
    private HoaDonService hoaDonService;

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

    @GetMapping("/chinh-sach")
    public String chinhSach(
            Model model) {
        if (principalCustom.getCurrentUserNameCustomer() != null) {
            TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
            model.addAttribute("checkDangNhap", "true");
            model.addAttribute("soLuongSPGioHangCT",
                    gioHangChiTietService.soLuongSPGioHangCT(khachHang.getGioHang().getId()));
        } else {
            model.addAttribute("checkDangNhap", "false");
        }
        return "/customer-template/chinh-sach";
    }
    @GetMapping("/lien-he")
    public String lienHe(
            Model model) {
        if (principalCustom.getCurrentUserNameCustomer() != null) {
            TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
            model.addAttribute("checkDangNhap", "true");
            model.addAttribute("soLuongSPGioHangCT",
                    gioHangChiTietService.soLuongSPGioHangCT(khachHang.getGioHang().getId()));
        } else {
            model.addAttribute("checkDangNhap", "false");
        }
        return "/customer-template/contact";
    }
    @GetMapping("/about")
    public String about(
            Model model) {
        if (principalCustom.getCurrentUserNameCustomer() != null) {
            TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
            model.addAttribute("checkDangNhap", "true");
            model.addAttribute("soLuongSPGioHangCT",
                    gioHangChiTietService.soLuongSPGioHangCT(khachHang.getGioHang().getId()));
        } else {
            model.addAttribute("checkDangNhap", "false");
        }
        return "/customer-template/about";
    }

    @GetMapping("/user/cart")
    public String cart(
            Model model) {
        TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
        List<GioHangChiTiet> listGioHangChiTiet = gioHangChiTietService
                .findAllByIdGioHang(khachHang.getGioHang().getId());
        model.addAttribute("soLuongSPGioHangCT",
                gioHangChiTietService.soLuongSPGioHangCT(khachHang.getGioHang().getId()));
        model.addAttribute("listGioHangChiTiet", listGioHangChiTiet);
        return "/customer-template/cart";
    }
    @GetMapping("/user/cart/detele/{id}")
    public String deleteCart(
            @PathVariable("id") Long id) {
        gioHangChiTietService.deleteById(id);
        return "redirect:/user/cart";
    }

    @GetMapping("/user/cart/update/{id}")
    public String updateCart(
            @PathVariable("id") Long id,
            @RequestParam("soLuong") String soLuong) {
        GioHangChiTiet gioHangChiTiet = gioHangChiTietService.fillById(id);
        gioHangChiTiet.setSoLuong(Integer.valueOf(soLuong));
        gioHangChiTietService.update(gioHangChiTiet);
        return "redirect:/user/cart";
    }

    @PostMapping("/user/gio-hang-chi-tiet/add/{idChiTietSpAdd}/{soLuongAdd}")
    public String addGioHangChiTiet(
            @PathVariable String idChiTietSpAdd,
            @PathVariable String soLuongAdd) {
        String[] optionArray = idChiTietSpAdd.split(",");
        List<String> listIdString = Arrays.asList(optionArray);
        TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
        gioHangChiTietService.save(khachHang.getGioHang().getId(), listIdString, Integer.valueOf(soLuongAdd));
        return "redirect:/shop";
    }
    @PostMapping("/user/gio-hang-chi-tiet/add-fast/{idChiTietSpAdd}/{soLuongAdd}")
    public String addGioHangChiTietNhanh(
            @PathVariable String idChiTietSpAdd,
            @PathVariable String soLuongAdd) {
        String[] optionArray = idChiTietSpAdd.split(",");
        List<String> listIdString = Arrays.asList(optionArray);
        TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
        gioHangChiTietService.save(khachHang.getGioHang().getId(), listIdString, Integer.valueOf(soLuongAdd));
        return "redirect:/user/cart";
    }
    @GetMapping("/user/checkout")
    public String checkout(
            @RequestParam String options,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
        String[] optionArray = options.split(",");
        List<String> listIdString = Arrays.asList(optionArray);

        for (GioHangChiTiet gioHangChiTiet : gioHangChiTietService.findAllById(listIdString, khachHang.getGioHang().getId())) {
            if (gioHangChiTiet.getSoLuong() > chiTietSanPhamSerivce.getById(gioHangChiTiet.getChiTietSanPham().getId()).getSoLuong()) {
                redirectAttributes.addFlashAttribute("checkSoLuongDB","true");
                return "redirect:/user/cart";
            }
        }

        List<GioHangChiTiet> listGioHangChiTiet = gioHangChiTietService.findAllById(listIdString,
                khachHang.getGioHang().getId());
        model.addAttribute("listGioHangChiTiet", listGioHangChiTiet);
        List<DiaChi> diaChi = diaChiService.getAllByTaiKhoan(idTaiKhoan);
        model.addAttribute("listVoucher", voucherService.fillAllDangDienRa());
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("soLuongSPGioHangCT",
                gioHangChiTietService.soLuongSPGioHangCT(khachHang.getGioHang().getId()));
        if (khachHang.getLstDiaChi() == null || khachHang.getLstDiaChi().size() == 0) {
            model.addAttribute("checkDiaChi", "DiaChiNull");
        } else {
            model.addAttribute("checkDiaChi", "DiaChi");
            model.addAttribute("listDiaChi", diaChi);
            if (diaChi.size() == 5) {
                model.addAttribute("checkButtonAdd", "true");
                model.addAttribute("soDiaChi", diaChi.size());
            } else {
                model.addAttribute("checkButtonAdd", "false");
                model.addAttribute("soDiaChi", diaChi.size());
            }
        }
        return "/customer-template/checkout";
    }
    @PostMapping("/user/checkout/add")
    public String addHoaDon(
            @RequestParam("idGioHangChiTiet") String idGioHangChiTiet,
            @RequestParam("tongTien") String tongTien,
            @RequestParam("tienGiam") String tienGiam,
            @RequestParam("tongTienAndSale") String tongTienAndSale,
            @RequestParam("hoVaTen") String hoVaTen,
            @RequestParam("soDienThoai") String soDienThoai,
            @RequestParam("tienShip") String tienShip,
            @RequestParam("email") String email,
            @RequestParam("voucher") String voucher,
            @RequestParam("diaChiCuThe") String diaChiCuThe,
            @RequestParam("ghiChu") String ghiChu,
            @RequestParam("phuongXaID") String phuongXaID,
            @RequestParam("quanHuyenID") String quanHuyenID,
            @RequestParam("thanhPhoID") String thanhPhoID,
            @RequestParam("trangThaiLuuDC") String trangThaiLuuDC,
            RedirectAttributes redirectAttributes) {
        String[] optionArray = idGioHangChiTiet.split(",");

        TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
        List<String> listIdString = Arrays.asList(optionArray);
        for (GioHangChiTiet gioHangChiTiet : gioHangChiTietService.findAllById(listIdString, khachHang.getGioHang().getId())) {
            if (gioHangChiTiet.getSoLuong() > chiTietSanPhamSerivce.getById(gioHangChiTiet.getChiTietSanPham().getId()).getSoLuong()) {
                redirectAttributes.addFlashAttribute("checkSoLuongDB","true");
                return "redirect:/user/checkout?options="+idGioHangChiTiet;
            }
        }
        if (trangThaiLuuDC.equals("0")) {
            Date date = new Date();
            DiaChi diaChi = new DiaChi();
            diaChi.setPhuongXa(phuongXaID);
            diaChi.setQuanHuyen(quanHuyenID);
            diaChi.setThanhPho(thanhPhoID);
            diaChi.setDiaChiCuThe(diaChiCuThe);
            diaChi.setTrangThai(0);
            diaChi.setNgayTao(date);
            diaChi.setNgaySua(date);
            diaChi.setTaiKhoan(TaiKhoan.builder().id(idTaiKhoan).build());
            diaChiService.save(diaChi);
        }

//        // Trừ số lượng sản phẩm trong kho
//        for (GioHangChiTiet gioHangChiTiet : gioHangChiTietService.findAllById(listIdString, khachHang.getGioHang().getId())) {
//            ChiTietSanPham chiTietSanPham = chiTietSanPhamSerivce.getById(gioHangChiTiet.getChiTietSanPham().getId());
//            chiTietSanPham.setSoLuong(chiTietSanPham.getSoLuong() - gioHangChiTiet.getSoLuong());
//            chiTietSanPhamSerivce.update(chiTietSanPham);
//        }
//
        gioHangChiTietService.addHoaDon(listIdString, Long.valueOf(tongTien), Long.valueOf(tongTienAndSale), hoVaTen,
                soDienThoai, tienShip,tienGiam, email, voucher, diaChiCuThe, ghiChu, khachHang, phuongXaID, quanHuyenID,
                thanhPhoID, khachHang.getGioHang().getId());
        return "redirect:/user/thankyou";
    }
    @GetMapping("/user/thankyou")
    public String thankYou(
            Model model) {
        TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
        model.addAttribute("soLuongSPGioHangCT",
                gioHangChiTietService.soLuongSPGioHangCT(khachHang.getGioHang().getId()));
        return "/customer-template/thankyou";
    }

    @PostMapping("/user/dia-chi/add")
    public String adđDiaChi(
            @RequestParam("phuongXaID") String phuongXa,
            @RequestParam("quanHuyenID") String quanHuyen,
            @RequestParam("thanhPhoID") String thanhPho,
            @RequestParam("diaChiCuThe") String diaChiCuThe) {
        Date date = new Date();
        DiaChi diaChi = new DiaChi();
        diaChi.setPhuongXa(phuongXa);
        diaChi.setQuanHuyen(quanHuyen);
        diaChi.setThanhPho(thanhPho);
        diaChi.setDiaChiCuThe(diaChiCuThe);
        diaChi.setTrangThai(1);
        diaChi.setNgayTao(date);
        diaChi.setNgaySua(date);
        diaChi.setTaiKhoan(TaiKhoan.builder().id(idTaiKhoan).build());
        diaChiService.save(diaChi);
        return "redirect:/user/cart";
    }

    @PostMapping("/user/dia-chi/update")
    public String updateDiaChi(
            @RequestParam("idDiaChi") Long idDiaChi,
            @RequestParam("phuongXa") String phuongXa,
            @RequestParam("quanHuyen") String quanHuyen,
            @RequestParam("thanhPho") String thanhPho,
            @RequestParam("diaChiCuThe") String diaChiCuThe,
            @RequestParam("trangThai") Integer trangThai) {
        if (trangThai == 0) {
            List<DiaChi> listDiaChi = diaChiService.getAllTrangThai(0);
            DiaChi diaChiNew = new DiaChi();
            for (DiaChi diaChiUpdate : listDiaChi) {
                diaChiNew.setId(diaChiUpdate.getId());
                diaChiNew.setPhuongXa(diaChiUpdate.getPhuongXa());
                diaChiNew.setQuanHuyen(diaChiUpdate.getQuanHuyen());
                diaChiNew.setThanhPho(diaChiUpdate.getThanhPho());
                diaChiNew.setDiaChiCuThe(diaChiUpdate.getDiaChiCuThe());
                diaChiNew.setTrangThai(1);
                diaChiNew.setNgayTao(diaChiUpdate.getNgayTao());
                diaChiNew.setNgaySua(diaChiUpdate.getNgaySua());
                diaChiNew.setTaiKhoan(diaChiUpdate.getTaiKhoan());
                diaChiService.update(diaChiNew);
            }
        }
        Date date = new Date();
        DiaChi diaChi = new DiaChi();
        diaChi.setId(idDiaChi);
        diaChi.setPhuongXa(phuongXa);
        diaChi.setQuanHuyen(quanHuyen);
        diaChi.setThanhPho(thanhPho);
        diaChi.setDiaChiCuThe(diaChiCuThe);
        diaChi.setTrangThai(trangThai);
        diaChi.setNgayTao(date);
        diaChi.setNgaySua(date);
        diaChi.setTaiKhoan(TaiKhoan.builder().id(idTaiKhoan).build());
        diaChiService.update(diaChi);

        return "redirect:/user/cart";
    }

    @GetMapping("/user/shop-single/get-so-luong")
    @ResponseBody
    public Integer getSoLuongGHCT() {
        TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
        Integer soLuong = gioHangChiTietService.soLuongSPGioHangCT(khachHang.getGioHang().getId());
        return soLuong;
    }

    @GetMapping("/user/shop-single/check-so-luong/{idCTSP}")
    @ResponseBody
    public Integer checkSoLuongSpEndGHCT(
            @PathVariable String idCTSP) {
        Integer soLuongCheck;
        GioHangChiTiet gioHangChiTiet = gioHangChiTietService.fillByIdCTSP(Long.valueOf(idCTSP));
        if (gioHangChiTiet != null) {
            soLuongCheck = gioHangChiTiet.getSoLuong();
        } else {
            soLuongCheck = 0;
        }
        return soLuongCheck;
    }
    @GetMapping("/user/chi-tiet-san-pham/{idSanPham}/{idMauSac}")
    @ResponseBody
    public List<ChiTietSanPham> getAllbyIdSPAndIdMS(
            @PathVariable String idSanPham,
            @PathVariable String idMauSac) {
        List<ChiTietSanPham> listChiTietSanPham1 = chiTietSanPhamSerivce.getAllbyIdSPAndIdMS(Long.valueOf(idSanPham),
                Long.valueOf(idMauSac));
        return listChiTietSanPham1;
    }
    @GetMapping("/user/thong-tin-khach-hang")
    public String info(
            Model model) {
        TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("soLuongSPGioHangCT",
                gioHangChiTietService.soLuongSPGioHangCT(khachHang.getGioHang().getId()));
        return "/customer-template/thong-tin-khach-hang";
    }

    @GetMapping("/user/don-mua")
    public String donMua(
            Model model) {
        TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
        model.addAttribute("soLuongSPGioHangCT",
                gioHangChiTietService.soLuongSPGioHangCT((khachHang.getGioHang() != null ? khachHang.getGioHang().getId() : null)));
        model.addAttribute("listAllHoaDon", hoaDonService.getAllHoaDonByTaiKhoanOrderByNgaySua(idTaiKhoan));
        model.addAttribute("listHDChoXacNhan", hoaDonService.getHoaDonByTaiKhoanByTrangThaiOrderByNgaySua(idTaiKhoan, 0));
        model.addAttribute("listHDChoGiao", hoaDonService.getHoaDonByTaiKhoanByTrangThaiOrderByNgaySua(idTaiKhoan, 1));
        model.addAttribute("listHDDangGiao", hoaDonService.getHoaDonByTaiKhoanByTrangThaiOrderByNgaySua(idTaiKhoan, 2));
        model.addAttribute("listHDHoanThanh", hoaDonService.getHoaDonByTaiKhoanByTrangThaiOrderByNgaySua(idTaiKhoan, 3));
        model.addAttribute("listHDDaHuy", hoaDonService.getHoaDonByTaiKhoanByTrangThaiOrderByNgaySua(idTaiKhoan, 5));
        model.addAttribute("listHDTraHang", hoaDonService.getHoaDonByTaiKhoanByTrangThaiOrderByNgaySua(idTaiKhoan, 6));
        return "customer-template/don-mua";
    }

    @PostMapping("/user/don-mua/mua-lai")
    public String muaLaiDonMua(
            @RequestParam String options) {
        String[] optionArray = options.split(",");
        List<String> listIdString = Arrays.asList(optionArray);
        TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
        gioHangChiTietService.save((khachHang.getGioHang() != null ? khachHang.getGioHang().getId() : null), listIdString, 1);
        return "redirect:/user/cart";
    }

    @GetMapping("/user/huy-don/{idHoaDon}")
    public String huyDon(
            @PathVariable("idHoaDon") Long idHoaDon,
            @RequestParam("ghiChu") String ghiChu,
            Model model,
            RedirectAttributes redirectAttributes) {
        TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
        model.addAttribute("soLuongSPGioHangCT",
                gioHangChiTietService.soLuongSPGioHangCT((khachHang.getGioHang() != null ? khachHang.getGioHang().getId() : null)));
        HoaDon hoaDon = hoaDonService.findById(idHoaDon);
        hoaDon.setNgayTao(new Date());
        hoaDon.setTrangThai(5);

        lichSuHoaDonService.saveOrUpdate(LichSuHoaDon.builder()
                .ghiChu(ghiChu)
                .ngayTao(new Date())
                .ngaySua(new Date())
                .trangThai(5)
                .hoaDon(hoaDon)
                .build());

        hoaDonService.saveOrUpdate(hoaDon);
        return "redirect:/user/don-mua";
    }

    @GetMapping("/user/don-mua/{idHoaDon}")
    public String donMuaChiTiet(
            @PathVariable("idHoaDon") Long idHoaDon,
            Model model) {

        model.addAttribute("soLuongSPGioHangCT",
                gioHangChiTietService.soLuongSPGioHangCT(null));

        model.addAttribute("byHoaDon", hoaDonService.findById(idHoaDon));
        model.addAttribute("listLichSuHoaDon", lichSuHoaDonService.findByIdhdNgaySuaAsc(idHoaDon));

        return "/customer-template/don-mua-chi-tiet";
    }
    @GetMapping("/user/dia-chi")
    public String diaChiKhachHang(
            Model model) {
        TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
        model.addAttribute("soLuongSPGioHangCT",
                gioHangChiTietService.soLuongSPGioHangCT((khachHang.getGioHang() != null ? khachHang.getGioHang().getId() : null)));
        List<DiaChi> diaChi = diaChiService.getAllByTaiKhoan(idTaiKhoan);
        model.addAttribute("listDiaChi", diaChi);
        if (diaChi.size() == 5) {
            model.addAttribute("checkButtonAdd", "true");
            model.addAttribute("soDiaChi", diaChi.size());
        } else {
            model.addAttribute("checkButtonAdd", "false");
            model.addAttribute("soDiaChi", diaChi.size());
        }
        return "/customer-template/dia-chi";
    }
    @GetMapping("/user/dia-chi/delete/{id}")
    public String deleteDiaChiKhachHang(
            @PathVariable("id") Long idDiaChi,
            RedirectAttributes redirectAttributes) {
        diaChiService.deleteById(idDiaChi);
        redirectAttributes.addFlashAttribute("checkModal", "modal");
        return "redirect:/user/dia-chi";
    }
    @GetMapping("/user/mat-khau")
    public String doiMatKhau(
            Model model) {
        TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
        model.addAttribute("soLuongSPGioHangCT",
                gioHangChiTietService.soLuongSPGioHangCT((khachHang.getGioHang() != null ? khachHang.getGioHang().getId() : null)));
        return "/customer-template/doi-mat-khau-khach-hang";
    }
    @GetMapping("/user/mat-khau/update")
    public String updateMatKhau(
            @RequestParam("matKhauCu") String matKhauCu,
            @RequestParam("xacNhanmatKhauMoi") String xacNhanmatKhauMoi,
            Model model,
            RedirectAttributes redirectAttributes) {
        TaiKhoan khachHang = khachHangService.getById(idTaiKhoan);
        if (!passwordEncoder.matches(matKhauCu, khachHang.getMatKhau())) {
            model.addAttribute("messages", "Mật khẩu cũ không chính xác, vui lòng thử lại");
        } else {
            khachHang.setNgaySua(new Date());
            khachHang.setMatKhau(passwordEncoder.encode(xacNhanmatKhauMoi));
            khachHangService.update(khachHang);
            redirectAttributes.addFlashAttribute("checkModal", "modal");
            return "redirect:/user/mat-khau";
        }
        return "/customer-template/doi-mat-khau-khach-hang";
    }
    @PostMapping("/user/thong-tin-khach-hang/update")
    public String updateInfo(
            @Valid @ModelAttribute("khachHang") TaiKhoan khachHang,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        TaiKhoan kh = khachHangService.getById(idTaiKhoan);

        // Kiểm tra lỗi validate
        if (result.hasErrors()) {
            // Kiểm tra null trước khi truy cập GioHang
            if (kh.getGioHang() != null) {
                model.addAttribute("soLuongSPGioHangCT", gioHangChiTietService.soLuongSPGioHangCT(kh.getGioHang().getId()));
            } else {
                model.addAttribute("soLuongSPGioHangCT", 0);  // Hoặc một giá trị mặc định nếu không có giỏ hàng
            }
            return "/customer-template/thong-tin-khach-hang";
        }

        // Kiểm tra ngày sinh
        if (!khachHang.isValidNgaySinh()) {
            if (kh.getGioHang() != null) {
                model.addAttribute("soLuongSPGioHangCT", gioHangChiTietService.soLuongSPGioHangCT(kh.getGioHang().getId()));
            } else {
                model.addAttribute("soLuongSPGioHangCT", 0);  // Giỏ hàng null
            }
            model.addAttribute("checkNgaySinh", "Năm sinh phải lớn 1900");
            return "/customer-template/thong-tin-khach-hang";
        }

        // Kiểm tra email
        if (!khachHangService.checkEmailSua(khachHang.getId(), khachHang.getEmail())) {
            if (kh.getGioHang() != null) {
                model.addAttribute("soLuongSPGioHangCT", gioHangChiTietService.soLuongSPGioHangCT(kh.getGioHang().getId()));
            } else {
                model.addAttribute("soLuongSPGioHangCT", 0);  // Giỏ hàng null
            }
            model.addAttribute("checkEmailTrung", "Email đã được đăng ký");
            return "/customer-template/thong-tin-khach-hang";
        }

        // Thực hiện cập nhật tài khoản
        khachHang.setNgaySua(new Date());
        khachHang.setVaiTro(VaiTro.builder().id(Long.valueOf(2)).build());
        redirectAttributes.addFlashAttribute("checkModal", "modal");
        khachHangService.update(khachHang);

        return "redirect:/user/thong-tin-khach-hang";
    }



}
//huynh18/04