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
import java.util.Random;

@Controller
@RequestMapping("/ban-hang-tai-quay")
@RequiredArgsConstructor
public class BanHangController {
    // 123 ///longduong //huynh //123 // dung123
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

    @PostMapping("/hoa-don/add-khach-hang")
    public String addKhachHang(@RequestParam Long idTaiKhoan, @RequestParam Long idhdc, RedirectAttributes redirectAttributes) {
        HoaDon hd = hoaDonService.findById(idhdc);
        if (idTaiKhoan == -1) {
            hd.setTaiKhoan(khachHangService.findKhachLe());
            hd.setDiaChiNguoiNhan(null);
            hd.setThanhPho(null);
            hd.setQuanHuyen(null);
            hd.setPhuongXa(null);
            hd.setNguoiNhan(null);
            hd.setSdtNguoiNhan(null);
        } else {
            TaiKhoan kh = khachHangService.getById(idTaiKhoan);
            hd.setTaiKhoan(kh);
            hd.setNguoiNhan(kh.getHoVaTen());
            hd.setSdtNguoiNhan(kh.getSoDienThoai());

        }

        hoaDonService.saveOrUpdate(hd);
        thongBao(redirectAttributes, "Thành công", 1);
        return "redirect:/ban-hang-tai-quay/hoa-don/" + idhdc;
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

    @PostMapping("/khach-hang/them-nhanh")
    public String add(@ModelAttribute("khachHang") TaiKhoan taiKhoan, @RequestParam Long idhdc,
                      Model model,
                      RedirectAttributes redirectAttributes,
                      HttpServletRequest request,
                      @RequestParam("email") String email) {
        String random3 = ranDom1();
        TaiKhoan userInfo = taiKhoan;
        TaiKhoan taiKhoanEntity = new TaiKhoan();
        taiKhoanEntity.setNgaySinh(taiKhoan.getNgaySinh());
        if (!taiKhoanEntity.isValidNgaySinh()) {
            redirectAttributes.addFlashAttribute("checkModal", "modal");
            redirectAttributes.addFlashAttribute("checkThongBao", "thaiBai");
            redirectAttributes.addFlashAttribute("checkNgaySinh", "ngaySinh");
            return "redirect:/ban-hang-tai-quay/hoa-don/" + idhdc;
        } else if (!khachHangService.checkTenTaiKhoanTrung(taiKhoan.getTenTaiKhoan())) {
            redirectAttributes.addFlashAttribute("checkModal", "modal");
            redirectAttributes.addFlashAttribute("checkThongBao", "thaiBai");
            redirectAttributes.addFlashAttribute("checkTenTrung", "Tên tài khoản đã tồn tại");
            redirectAttributes.addFlashAttribute("checkEmailTrung", "Email đã tồn tại");
            return "redirect:/ban-hang-tai-quay/hoa-don/" + idhdc;
        } else if (!khachHangService.checkEmail(taiKhoan.getEmail())) {
            redirectAttributes.addFlashAttribute("checkModal", "modal");
            redirectAttributes.addFlashAttribute("checkThongBao", "thaiBai");
            redirectAttributes.addFlashAttribute("checkEmailTrung", "Email đã tồn tại");
            return "redirect:/ban-hang-tai-quay/hoa-don/" + idhdc;
        } else {
            redirectAttributes.addFlashAttribute("checkThongBao", "thanhCong");
            String url = request.getRequestURL().toString();
            System.out.println(url);
            url = url.replace(request.getServletPath(), "");
            khachHangService.sendEmail(userInfo, url, random3);
            System.out.println(userInfo);
            userInfo.setNgayTao(new java.util.Date());
            userInfo.setNgaySua(new java.util.Date());
            VaiTro vaiTro = new VaiTro();
            vaiTro.setId(Long.valueOf(2));
            userInfo.setVaiTro(vaiTro);
            userInfo.setTrangThai(0);
            userInfo.setVaiTro(vaiTro);
            khachHangService.update(userInfo);


            return "redirect:/ban-hang-tai-quay/hoa-don/" + idhdc;
        }
    }

    public String ranDom1() {
        // Khai báo một mảng chứa 6 số nguyên ngẫu nhiên
        String ran = "";
        int[] randomNumbers = new int[6];

        // Tạo một đối tượng Random
        Random random = new Random();

        // Đổ số nguyên ngẫu nhiên vào mảng
        for (int i = 0; i < 6; i++) {
            randomNumbers[i] = random.nextInt(100); // Giới hạn số ngẫu nhiên từ 0 đến 99
        }

        // In ra các số nguyên ngẫu nhiên trong mảng
        System.out.println("Dãy 6 số nguyên ngẫu nhiên:");
        for (int number : randomNumbers) {
            ran = ran + number;
            System.out.println(number);
        }
        return ran;
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

    @GetMapping("/hoa-don-chi-tiet/delete/{id}")
    public String deleteHdct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Lấy chi tiết hóa đơn từ service
        HoaDonChiTiet hdct = hoaDonChiTietService.findById(id);
        if (hdct == null) {
            thongBao(redirectAttributes, "Không tìm thấy chi tiết hóa đơn.", 0);
            return "redirect:/ban-hang-tai-quay/hoa-don/detail/" + hdct.getHoaDon().getId();
        }
        // Lấy hóa đơn tương ứng
        HoaDon hd = hdct.getHoaDon();
        // Lấy sản phẩm chi tiết và tăng số lượng
        ChiTietSanPham ctsp = hdct.getChiTietSanPham();
        ctsp.setSoLuong(ctsp.getSoLuong() + hdct.getSoLuong());
        // Đảm bảo trạng thái sản phẩm vẫn là đang hoạt động (0)
        ctsp.setTrangThai(0);
        chiTietSanPhamSerivce.update(ctsp); // Cập nhật số lượng sản phẩm

        hoaDonChiTietService.deleteById(id);

        // Thông báo thành công
        thongBao(redirectAttributes, "Xóa chi tiết hóa đơn thành công.", 1);

        if (hd.getTrangThai() == -1) {
            return "redirect:/ban-hang-tai-quay/hoa-don/" + hd.getId();
        } else {
            return "redirect:/ban-hang-tai-quay/hoa-don/detail/" + hd.getId();
        }
    }
    @PostMapping("/hoa-don-chi-tiet/update")
    public String updateSoLuong(RedirectAttributes redirectAttributes,
                                @RequestParam(defaultValue = "") Integer soLuongEdit,
                                @RequestParam(defaultValue = "") Integer soLuongEditTra, 
                                @RequestParam Long idHdct) {
        HoaDonChiTiet hdct = hoaDonChiTietService.findById(idHdct);
        HoaDon hd = hdct.getHoaDon();
        HoaDonChiTiet hdctnew = new HoaDonChiTiet();
        ChiTietSanPham ctsp = hdct.getChiTietSanPham();

        hdctnew.setSoLuong(0);
        System.out.println(hd.getTrangThai() + "tthd");
        if (hd.getTrangThai() == 3) {
            // Xử lý trường hợp đổi trả
            for (HoaDonChiTiet hdctf : hd.getLstHoaDonChiTiet()) {
                if (hdctf.getChiTietSanPham() == hdct.getChiTietSanPham() && hdctf.getTrangThai() == 2) {
                    hdctnew = hdctf;
                    break;
                }
            }

            hdct.setSoLuong(hdct.getSoLuong() - soLuongEditTra);
            hdctnew.setHoaDon(hdct.getHoaDon());
            hdctnew.setChiTietSanPham(hdct.getChiTietSanPham());
            hdctnew.setSoLuong(hdctnew.getSoLuong() + soLuongEditTra);
            hdctnew.setTrangThai(2);
            hdctnew.setDonGia(hdct.getDonGia());
            hoaDonChiTietService.saveOrUpdate(hdctnew);
            hoaDonChiTietService.saveOrUpdate(hdct);

            if (hd.getTrangThai() == -1) {
                thongBao(redirectAttributes, "Thành công", 1);
                return "redirect:/ban-hang-tai-quay/hoa-don/" + hd.getId();
            } else {
                thongBao(redirectAttributes, "Thành công", 1);
                return "redirect:/ban-hang-tai-quay/hoa-don/detail/" + hd.getId();
            }
        }

        // Xử lý trường hợp cập nhật số lượng thông thường
        if (soLuongEdit == 0) {
            // Nếu số lượng mới là 0, hoàn lại số lượng vào kho và xóa chi tiết hóa đơn
            ctsp.setSoLuong(ctsp.getSoLuong() + hdct.getSoLuong());
            // Đảm bảo trạng thái sản phẩm vẫn là đang hoạt động (0)
            ctsp.setTrangThai(0);
            chiTietSanPhamSerivce.update(ctsp);
            hoaDonChiTietService.deleteById(idHdct);
            thongBao(redirectAttributes, "Đã xóa sản phẩm khỏi hóa đơn", 1);
        } else {
            // Tính số lượng tăng thêm
            int soLuongTangThem = soLuongEdit - hdct.getSoLuong();
            
            if (soLuongTangThem > 0) {
                // Nếu tăng số lượng, trừ đi số lượng trong kho
                if (ctsp.getSoLuong() >= soLuongTangThem) {
                    ctsp.setSoLuong(ctsp.getSoLuong() - soLuongTangThem);
                    // Đảm bảo trạng thái sản phẩm vẫn là đang hoạt động (0)
                    ctsp.setTrangThai(0);
                    chiTietSanPhamSerivce.update(ctsp);
                    thongBao(redirectAttributes, "Đã tăng số lượng sản phẩm", 1);
                } else {
                    thongBao(redirectAttributes, "Số lượng trong kho không đủ! Số lượng hiện tại: " + ctsp.getSoLuong(), 0);
                    return "redirect:/ban-hang-tai-quay/hoa-don/" + hd.getId();
                }
            } else if (soLuongTangThem < 0) {
                // Nếu giảm số lượng, hoàn lại số lượng vào kho
                ctsp.setSoLuong(ctsp.getSoLuong() + Math.abs(soLuongTangThem));
                // Đảm bảo trạng thái sản phẩm vẫn là đang hoạt động (0)
                ctsp.setTrangThai(0);
                chiTietSanPhamSerivce.update(ctsp);
                thongBao(redirectAttributes, "Đã giảm số lượng sản phẩm", 1);
            }
            
            hdct.setSoLuong(soLuongEdit);
            hdct.setTrangThai(0);
            hoaDonChiTietService.saveOrUpdate(hdct);
        }

        if (hd.getTrangThai() == -1) {
            return "redirect:/ban-hang-tai-quay/hoa-don/" + hd.getId();
        } else if (hd.getTrangThai() == 3) {
            return "redirect:/ban-hang-tai-quay/doi-tra/" + hd.getId();
        } else {
            return "redirect:/ban-hang-tai-quay/hoa-don/detail/" + hd.getId();
        }
    }

}