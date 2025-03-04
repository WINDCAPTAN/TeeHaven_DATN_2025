package com.example.datn_teehaven.controller;

import com.example.datn_teehaven.entyti.*;
import com.example.datn_teehaven.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequestMapping("/ban-hang-tai-quay")
@RequiredArgsConstructor
public class BanHangController {
    // 123
    @Autowired
    HttpServletRequest request;
    @Autowired
    KhachHangService khachHangService;
    @Autowired
     LichSuHoaDonService lichSuHoaDonService;
    @Autowired
    ChiTietSanPhamSerivce chiTietSanPhamSerivce;
    @Autowired
    HoaDonChiTietService hoaDonChiTietService;
    @Autowired
    TaiKhoanService taiKhoanService;
    @Autowired
    VoucherService voucherService;

    private final HoaDonService hoaDonService;

    @GetMapping("/hoa-don")
    public String home() {
        request.setAttribute("lstHoaDon", hoaDonService.find5ByTrangThai(-1));
        return "/admin-template/ban-hang";
    }

    void addKhachLe() {
        if (khachHangService.findKhachLe() == null) {
            khachHangService.addKhachLe();
        }
    }

    @PostMapping("/hoa-don/add")
    public String taoHoaDon(RedirectAttributes redirectAttributes) {
        addKhachLe();
        if (hoaDonService.countHoaDonTreo() < 5) {
            HoaDon hd = new HoaDon();
            hd.setTrangThai(-1); // view 5 hoa don
//            hd.setNgaySua(new java.util.Date());
            hd.setNgayTao(new Date());
            hd.setTaiKhoan(khachHangService.findKhachLe());
            hd.setPhiShip((long) 0);
            hd.setLoaiHoaDon(2);
            hd.setTongTien((long) 0);
            hd.setTongTienKhiGiam((long) 0);
            hd.setTienGiam((long) 0);
            hoaDonService.saveOrUpdate(hd);
            hd.setMaHoaDon("HD" + hd.getId());
            hoaDonService.saveOrUpdate(hd);


            addLichSuHoaDon(hd.getId(), "", 0);
            thongBao(redirectAttributes, "Thành công", 1);
            return "redirect:/ban-hang-tai-quay/hoa-don/" + hd.getId();
        }
        return "redirect:/ban-hang-tai-quay/hoa-don";
    }

    @GetMapping("/hoa-don/{id}")
    public String hoaDon(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        chiTietSanPhamSerivce.checkSoLuongBang0();

        TaiKhoan tk = new TaiKhoan();
        model.addAttribute("khachHang", tk);
        model.addAttribute("listHoaDon", hoaDonService.find5ByTrangThai(-1));
        model.addAttribute("listHdct", hoaDonChiTietService.findAll());
        model.addAttribute("listCtsp", chiTietSanPhamSerivce.fillAllDangHoatDongLonHon0());
        model.addAttribute("listTaiKhoan", taiKhoanService.getAll());
        model.addAttribute("lstTaiKhoanDc",
                khachHangService.getById(hoaDonService.findById(id).getTaiKhoan().getId()));
        model.addAttribute("listVoucher", voucherService.fillAllDangDienRa());

        HoaDon hd = hoaDonService.findById(id);

        Boolean ctb = false;

        if (hd.getVoucher() != null && hd.getTrangThai() != 6) {
            if (hd.tongTienHoaDonDaNhan() < hd.getVoucher().getGiaTriDonToiThieu().longValue()) {

                hd.setVoucher(null);
                hd.setTongTien(hd.tongTienHoaDonDaNhan());
                hd.setTongTienKhiGiam(hd.tongTienHoaDonDaNhan());
                hoaDonService.saveOrUpdate(hd);
                ctb = true;
                thongBao(redirectAttributes, "Đã xóa mã giảm giá vì chưa đạt giá trị đơn tối thiếu", 0);
            }
        }
        if (ctb) {
            model.addAttribute("thongBao", "Đã xóa mã giảm giá vì chưa đạt giá trị đơn tối thiếu");
            model.addAttribute("checkThongBao", "thatBai");
        }
        model.addAttribute("hoaDon", hd);

        return "admin-template/hoa-don-chi-tiet";
    }

    @PostMapping("/hoa-don-chi-tiet/add")
    public String addHdct(@RequestParam Long idHoaDon, @RequestParam Long idCtsp,
                          RedirectAttributes redirectAttributes) {

        Boolean cr = true;
        HoaDonChiTiet hdct = new HoaDonChiTiet();
        HoaDon hoaDon = hoaDonService.findById(idHoaDon);
        hoaDonService.saveOrUpdate(hoaDon);
        ChiTietSanPham ctsp = chiTietSanPhamSerivce.getById(idCtsp);
        // Kiểm tra nếu sản phẩm còn đủ số lượng để thêm
        if (ctsp.getSoLuong() <= 0) {
            // Sản phẩm hết hàng, không cho thêm
            thongBao(redirectAttributes, "Sản phẩm không còn hàng.", 0);
            return "redirect:/ban-hang-tai-quay/hoa-don/detail/" + idHoaDon;
        }
        // Kiểm tra xem sản phẩm đã có trong hóa đơn chi tiết hay chưa
        for (HoaDonChiTiet obj : hoaDonChiTietService.findAll()) {
            if (obj.getHoaDon().equals(hoaDon) && obj.getChiTietSanPham().equals(ctsp)) {
                hdct = obj;
                cr = false;
                break;
            }
        }
        if (cr) {
            // Nếu sản phẩm chưa có trong hóa đơn chi tiết, tạo mới và thêm vào
            hdct = new HoaDonChiTiet();
            hdct.setHoaDon(hoaDon);
            hdct.setChiTietSanPham(ctsp);
            hdct.setSoLuong(1);
            hdct.setDonGia(ctsp.getGiaHienHanh());
            // Cập nhật lại số lượng sản phẩm
            ctsp.setSoLuong(ctsp.getSoLuong() - 1);
            chiTietSanPhamSerivce.update(ctsp); // Cập nhật số lượng sản phẩm
        } else {
            // Nếu sản phẩm đã có trong hóa đơn chi tiết, chỉ tăng số lượng lên 1 đơn vị
            if (ctsp.getSoLuong() > 0) {
                hdct.setSoLuong(hdct.getSoLuong() + 1);
                // Trừ số lượng sản phẩm khi đã tồn tại trong hóa đơn chi tiết
                ctsp.setSoLuong(ctsp.getSoLuong() - 1);
                chiTietSanPhamSerivce.update(ctsp); // Cập nhật số lượng sản phẩm
            }
        }

        hdct.setTrangThai(0);
        hoaDonChiTietService.saveOrUpdate(hdct);
        thongBao(redirectAttributes, "Thành công", 1);
        redirectAttributes.addFlashAttribute("batModal", "ok");

        // Điều hướng tùy vào trạng thái hóa đơn
        if (hoaDon.getTrangThai() == -1) {
            return "redirect:/ban-hang-tai-quay/hoa-don/" + idHoaDon;
        } else {
            return "redirect:/ban-hang-tai-quay/hoa-don/detail/" + idHoaDon;
        }
    }

    void thongBao(RedirectAttributes redirectAttributes, String thongBao, int trangThai) {
        if (trangThai == 0) {
            redirectAttributes.addFlashAttribute("checkThongBao", "thatBai");
            redirectAttributes.addFlashAttribute("thongBao", thongBao);
        } else if (trangThai == 1) {
            redirectAttributes.addFlashAttribute("checkThongBao", "thanhCong");
            redirectAttributes.addFlashAttribute("thongBao", thongBao);
        } else {

            redirectAttributes.addFlashAttribute("checkThongBao", "canhBao");
            redirectAttributes.addFlashAttribute("thongBao", thongBao);
        }

    }


    public void addLichSuHoaDon(Long idHoaDon, String ghiChu, Integer trangThai) {
        HoaDon hd = hoaDonService.findById(idHoaDon);
        LichSuHoaDon lshd = new LichSuHoaDon();
        lshd.setHoaDon(hd);
        lshd.setGhiChu(ghiChu);
        lshd.setTrangThai(trangThai);
//        lshd.setNgaySua(new Date());
        lichSuHoaDonService.saveOrUpdate(lshd);
    }
}