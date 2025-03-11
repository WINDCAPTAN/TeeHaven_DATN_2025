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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/ban-hang-tai-quay")
@RequiredArgsConstructor
public class BanHangController {

    // 123 ///longduong //huynh //123// 1234/  12/3

    // 123 ///longduong //huynh //123 // dung123 // phong10/03

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

    @Autowired
    private DiaChiService diaChiService;

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
    @GetMapping("/hoa-don/detail/{id}")
    public String detailHoaDon(@PathVariable Long id, Model model) {

//        lstHoaDonCtDoiTra = new ArrayList<HoaDonChiTiet>();
        model.addAttribute("lstHoaDon", hoaDonService.find5ByTrangThai(-1));
        model.addAttribute("lstHdct", hoaDonChiTietService.findAll());
        model.addAttribute("lstCtsp", chiTietSanPhamSerivce.fillAllDangHoatDongLonHon0());
        model.addAttribute("lstTaiKhoan", khachHangService.getAll());
        model.addAttribute("lstTaiKhoanDc",
                khachHangService.getById(hoaDonService.findById(id).getTaiKhoan().getId()));
        model.addAttribute("listVoucher", voucherService.fillAllDangDienRa());
        // idhdc = id;

        model.addAttribute("lstLshd", lichSuHoaDonService.findByIdhd(id));
        model.addAttribute("listLichSuHoaDon", lichSuHoaDonService.findByIdhdNgaySuaAsc(id));
        HoaDon hd = hoaDonService.findById(id);
        if (hd.getTrangThai() == 6 && hd.getNgayMongMuon() == null) {
            hd.setNgayMongMuon(new java.util.Date());
//            sendMail(hd);
            hoaDonService.saveOrUpdate(hd);
        }
        if (hd.getVoucher() != null && hd.getTrangThai() != 6) {
            if (hd.tongTienHoaDonDaNhan() < hd.getVoucher().getGiaTriDonToiThieu().longValue()) {
                hd.setVoucher(null);
                hd.setTienGiam((long) 0);
                hd.setTongTien(hd.tongTienHoaDonDaNhan());
                hd.setTongTienKhiGiam(hd.tongTienHoaDonDaNhan());
                hoaDonService.saveOrUpdate(hd);
            }
        }
        List<LichSuHoaDon> lstLshd = lichSuHoaDonService.findByIdhd(id);
        Integer tt = lstLshd.get(0).getTrangThai();
        model.addAttribute("checkRollback", tt);
        // checkVoucher();
        model.addAttribute("hoaDon", hd);
        model.addAttribute("byHoaDon", hd);
        if (hd.getTrangThai() == 4) {
            return "redirect:/ban-hang-tai-quay/hoa-don/" + id;
        }
        return "/admin-template/detail-hoa-don";
    }
    @PostMapping("/hoa-don/rollback/{id}")
    public String rollback(@RequestParam String ghiChu, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        HoaDon hd = hoaDonService.findById(id);

        if (lichSuHoaDonService.findByIdhdNgaySuaAsc(id).size() == 3) {
            updateSL(hd);
        }
        for (LichSuHoaDon lichSuHoaDon : lichSuHoaDonService.findByIdhd(id)) {
            if (lichSuHoaDon.getTrangThai() == hd.getTrangThai()) {
                lichSuHoaDon.setTrangThai(lichSuHoaDon.getTrangThai() + 20);
                lichSuHoaDonService.saveOrUpdate(lichSuHoaDon);
                addLichSuHoaDon(id, ghiChu, 8);
            }
        }

        if (hd.getTrangThai() == 1 && hd.getLoaiHoaDon() == 2) {
            hd.setTrangThai(4);
            updateSoLuongRollBack(hd.getId());

        } else {
            if (hd.getTrangThai() == 5) {
                if (lichSuHoaDonService.findByIdhdNgaySuaAsc(id).size() == 1) {
                    hd.setTrangThai(4);
                } else {
                    Integer tt = lichSuHoaDonService.findByIdhdNgaySuaAsc(hd.getId()).size() - 1;
                    hd.setTrangThai(tt);
                }
            } else {

                if (lichSuHoaDonService.findByIdhdNgaySuaAsc(hd.getId()).size() == 1 && hd.getLoaiHoaDon() == 2) {
                    updateSoLuongRollBack(hd.getId());

                    hd.setTrangThai(4);

                } else if (lichSuHoaDonService.findByIdhdNgaySuaAsc(hd.getId()).size() == 1 && hd.getLoaiHoaDon() == 1) {
                    updateSoLuongRollBack(hd.getId());

                    hd.setTrangThai(0);

                } else {
                    hd.setTrangThai(hd.getTrangThai() - 1);
                }

            }
        }

        hoaDonService.saveOrUpdate(hd);
        if (hd.getTrangThai() == -1) {

            return "redirect:/ban-hang-tai-quay/hoa-don/" + hd.getId();
        } else {

            return "redirect:/ban-hang-tai-quay/hoa-don/detail/" + hd.getId();
        }

    }
    void updateSoLuongRollBack(Long idhdc) {
        HoaDon hd = hoaDonService.findById(idhdc);
        List<ChiTietSanPham> lstCtsp = chiTietSanPhamSerivce.getAll();
        for (HoaDonChiTiet hoaDonChiTiet : hd.getLstHoaDonChiTiet()) {
            for (ChiTietSanPham ctsp : lstCtsp) {
                if (hoaDonChiTiet.getChiTietSanPham().getId() == ctsp.getId()) {
                    ctsp.setSoLuong(ctsp.getSoLuong() + hoaDonChiTiet.getSoLuong());
                    chiTietSanPhamSerivce.update(ctsp);
                }
            }
        }

        if (hd.getVoucher() != null) {
            Voucher v = hd.getVoucher();
            v.setSoLuong(v.getSoLuong().add(new BigDecimal(1)));
            voucherService.save(v);
        }

    }
    private void updateSL(HoaDon hd) {
        List<HoaDonChiTiet> lstHdct = hoaDonService.findById(hd.getId()).getLstHoaDonChiTiet();
        for (HoaDonChiTiet hdct : lstHdct) {
            Long idid = hdct.getChiTietSanPham().getId();
            ChiTietSanPham ctsp = chiTietSanPhamSerivce.getById(idid);
            ctsp.setSoLuong(ctsp.getSoLuong() - hdct.getSoLuong());
            chiTietSanPhamSerivce.update(ctsp);
            if (ctsp.getSoLuong() == 0) {
                ctsp.setTrangThai(1);
                chiTietSanPhamSerivce.update(ctsp);
            }
        }
        if (hd.getVoucher() != null) {
            Voucher v = hd.getVoucher();
            v.setSoLuong(v.getSoLuong().subtract(new BigDecimal(1)));
            voucherService.save(v);
        }
    }
    @PostMapping("/hoa-don/xac-nhan")
    public String xacNhan(
            @RequestParam Long idHoaDon,
            @RequestParam String ghiChu,
            @RequestParam(defaultValue = "") String detail,
            @RequestParam Long phiShip2,
            @RequestParam Long giamGia,
            @RequestParam String voucherID, RedirectAttributes redirectAttributes) {

        HoaDon hd = hoaDonService.findById(idHoaDon);
        if (!checkSlDb(hd)) {
            thongBao(redirectAttributes, "Có sản phẩm vượt quá số lượng vui lòng kiểm tra lại", 0);
            return "redirect:/ban-hang-tai-quay/hoa-don/detail/" + hd.getId();
        }
        if (hd.getTrangThai() == 5) {
            thongBao(redirectAttributes, "Khách hàng đã hủy đơn", 0);
            return "redirect:/ban-hang-tai-quay/hoa-don/detail/" + hd.getId();
        }
        if (voucherID != "") {
            hd.setVoucher(voucherService.findById(Long.parseLong(voucherID)));
            hd.setTienGiam(giamGia);
        }
        if (hd.getTrangThai() == 0 && hd.getLoaiHoaDon() == 1) {
            updateSL(hd);
        }

        hd.setTrangThai(hd.getTrangThai() + 1);

        addLichSuHoaDon(idHoaDon, ghiChu, hd.getTrangThai());
        hoaDonService.saveOrUpdate(hd);
        System.out.println(ghiChu + "ghiChu");

        if (detail.equals("ok")) {
            hd.setTongTien(hd.tongTienHoaDon());
            hd.setTongTienKhiGiam(hd.tongTienHoaDon() - giamGia);
            hoaDonService.saveOrUpdate(hd);
            thongBao(redirectAttributes, "Thành công", 1);
            return "redirect:/ban-hang-tai-quay/hoa-don/detail/" + hd.getId();
        } else {
            return "redirect:/ban-hang-tai-quay/hoa-don/quan-ly";
        }

    }
    Boolean checkSlDb(HoaDon hd) {
        if (hd.getTrangThai() == 0 || hd.getTrangThai() == -1) {
            for (HoaDonChiTiet hdct : hd.getLstHoaDonChiTiet()) {
                if (hdct.getSoLuong() > chiTietSanPhamSerivce.getById(hdct.getChiTietSanPham().getId()).getSoLuong()) {
                    return false;
                }
            }
        }
        return true;
    }
    @PostMapping("/hoa-don/chuyen-nhanh")
    public String chuyenNhanh(
            @RequestParam Long idHoaDon,
            @RequestParam String ghiChu,
            RedirectAttributes redirectAttributes

    ) {

        HoaDon hd = hoaDonService.findById(idHoaDon);
        if (!checkSlDb(hd)) {
            thongBao(redirectAttributes, "Có sản phẩm vượt quá số lượng vui lòng kiểm tra lại", 0);
            return "redirect:/ban-hang-tai-quay/hoa-don/detail/" + idHoaDon;
        }
        hd.setTrangThai(hd.getTrangThai() + 1);
        addLichSuHoaDon(idHoaDon, ghiChu, hd.getTrangThai());
        hoaDonService.saveOrUpdate(hd);

        System.out.println(ghiChu + "ghiChu");

        return "redirect:/ban-hang-tai-quay/hoa-don/quan-ly";
    }
    @PostMapping("/hoa-don/update")
    public String updateHoaDon(@RequestParam Long phiShip, @RequestParam Long idhdc,
                               @RequestParam String inputHoVaTen, @RequestParam String inputSoDienThoai,
                               @RequestParam String inputDcct, @RequestParam String inputGhiChu,
                               @RequestParam(defaultValue = "") String thanhPho,
                               @RequestParam(defaultValue = "") String quanHuyen, @RequestParam(defaultValue = "") String phuongXa) {
        HoaDon hd = hoaDonService.findById(idhdc);
        hd.setPhiShip(phiShip);
        hd.setSdtNguoiNhan(inputSoDienThoai);
        hd.setNguoiNhan(inputHoVaTen);
        hd.setDiaChiNguoiNhan(inputDcct);
        hd.setGhiChu(inputGhiChu);
        hd.setThanhPho(thanhPho);
        hd.setQuanHuyen(quanHuyen);
        hd.setPhuongXa(phuongXa);
        hoaDonService.saveOrUpdate(hd);
        return "redirect:/ban-hang-tai-quay/hoa-don/detail/" + idhdc;
    }
    @GetMapping("/hoa-don/bo-voucher/{id}")
    public String boChonVoucher(@PathVariable Long id) {
        HoaDon hd = hoaDonService.findById(id);
        hd.setVoucher(null);
        hd.setTongTienKhiGiam(hd.tongTienHoaDonDaNhan() + hd.getPhiShip());
        hoaDonService.saveOrUpdate(hd);
        return "redirect:/ban-hang-tai-quay/hoa-don/detail/" + hd.getId();
    }

    @PostMapping("/hoa-don/add-dia-chi")
    public String addDiaChi(@RequestParam Long idDiaChi, @RequestParam Long idhdc, RedirectAttributes redirectAttributes) {
        System.out.println(idDiaChi + "==========");
        HoaDon hd = hoaDonService.findById(idhdc);
        DiaChi dc = diaChiService.getById(idDiaChi);
        hd.setDiaChiNguoiNhan(dc.getDiaChiCuThe());
        hd.setQuanHuyen(dc.getQuanHuyen());
        hd.setPhuongXa(dc.getPhuongXa());
        hd.setThanhPho(dc.getThanhPho());
        hoaDonService.saveOrUpdate(hd);
        thongBao(redirectAttributes, "Thành công", 1);
        if (hd.getTrangThai() == -1) {
            return "redirect:/ban-hang-tai-quay/hoa-don/" + idhdc;
        } else {
            return "redirect:/ban-hang-tai-quay/hoa-don/detail/" + idhdc;
        }
    }
    @GetMapping("/hoa-don/quan-ly")
    public String quanLyHoaDon(Model model) {
        List<HoaDon> lstHdctAll = hoaDonService.findAllHoaDon();
        List<HoaDon> lstHdChoXacNhan = hoaDonService.find5ByTrangThai(0);
        List<HoaDon> lstHdChoGiao = hoaDonService.find5ByTrangThai(1);
        List<HoaDon> lstHdDangGiao = hoaDonService.find5ByTrangThai(2);
        List<HoaDon> lstHdHoanThanh = hoaDonService.find5ByTrangThai(3);
        List<HoaDon> lstHdChoThanhToan = hoaDonService.find5ByTrangThai(4);
        List<HoaDon> lstHdHuy = hoaDonService.find5ByTrangThai(5);
        List<HoaDon> lstHdTra = hoaDonService.find5ByTrangThai(6);

        model.addAttribute("lstHdctAll", lstHdctAll != null ? lstHdctAll : new ArrayList<>());
        model.addAttribute("lstHdChoXacNhan", lstHdChoXacNhan);
        model.addAttribute("lstHdChoGiao", lstHdChoGiao);
        model.addAttribute("lstHdDangGiao", lstHdDangGiao);
        model.addAttribute("lstHdHoanThanh", lstHdHoanThanh);
        model.addAttribute("lstHdChoThanhToan", lstHdChoThanhToan);
        model.addAttribute("lstHdHuy", lstHdHuy);
        model.addAttribute("lstHdTra", lstHdTra);

        return "admin-template/hoa-don";
    }
    @PostMapping("/hoa-don/thanh-toan")
    public String thanhToan(@RequestParam(defaultValue = "off") String treo,
                            @RequestParam(defaultValue = "off") String giaoHang, @RequestParam Long phiShip, @RequestParam Long idhdc,
                            @RequestParam Long giamGia, @RequestParam String inputHoVaTen, @RequestParam String inputSoDienThoai,
                            @RequestParam String inputDcct, @RequestParam String inputGhiChu,
                            @RequestParam(defaultValue = "") String thanhPho,
                            @RequestParam(defaultValue = "") String quanHuyen, @RequestParam(defaultValue = "") String phuongXa,
                            @RequestParam String voucherID, @RequestParam String ghiChuThanhToan,
                            RedirectAttributes redirectAttributes, @RequestParam(defaultValue = "") String luuDiaChi) {

        HoaDon hd = hoaDonService.findById(idhdc);
        if (!checkSlDb(hd)) {
            thongBao(redirectAttributes, "Có sản phẩm vượt quá số lượng vui lòng kiểm tra lại", 0);
            return "redirect:/ban-hang-tai-quay/hoa-don/" + idhdc;
        }
        thongBao(redirectAttributes, "Thành công", 1);
        chiTietSanPhamSerivce.checkSoLuongBang0();
        System.out.println("ttttttttt" + thanhPho + quanHuyen + phuongXa);
        if (voucherID != "") {
            hd.setVoucher(voucherService.findById(Long.parseLong(voucherID)));
            hd.setTienGiam(giamGia);
        }

        switch (hd.getTrangThai()) {
            case -1:
                if (treo.equals("on")) {
                    hd.setTrangThai(4);

                } else if (giaoHang.equals("on")) {
                    // Giao hàng
                    addLichSuHoaDon(hd.getId(), ghiChuThanhToan, 1);
                    hd.setTrangThai(1);
                    hd.setPhiShip(phiShip);
                    hd.setSdtNguoiNhan(inputSoDienThoai);
                    hd.setNguoiNhan(inputHoVaTen);
                    hd.setDiaChiNguoiNhan(inputDcct);
                    hd.setGhiChu(inputGhiChu);
                    hd.setThanhPho(thanhPho);
                    hd.setQuanHuyen(quanHuyen);
                    hd.setPhuongXa(phuongXa);
                    if (luuDiaChi.equals("on") && hd.getTaiKhoan().getTenTaiKhoan() != null) {
                        if (hd.getTaiKhoan().getLstDiaChi().size() < 5) {
                            DiaChi dc = new DiaChi();
                            dc.setQuanHuyen(quanHuyen);
                            dc.setPhuongXa(phuongXa);
                            dc.setThanhPho(thanhPho);
                            dc.setDiaChiCuThe(inputDcct);
                            dc.setTaiKhoan(hd.getTaiKhoan());
                            dc.setNgaySua(new java.util.Date());
                            dc.setNgayTao(new java.util.Date());
                            dc.setTrangThai(1);
                            diaChiService.save(dc);
                        }
                    }
                } else {
                    // Hoàn thành
                    addLichSuHoaDon(hd.getId(), ghiChuThanhToan, 3);
                    hd.setTrangThai(3);
                    hd.setNgayThanhToan(new java.util.Date());
                    hd.setTongTien(hd.tongTienHoaDon());
                    hd.setTongTienKhiGiam(hd.tongTienHoaDon() - giamGia);
                    hd.setPhiShip((long) 0);
                    hd.setQuanHuyen(null);
                    hd.setThanhPho(null);
                    hd.setPhuongXa(null);
                    if (hd.getNguoiNhan() == null) {
                        hd.setNguoiNhan("Khách lẻ");
                    }

                }
                break;
            case 0:
                // xác nhận đơn
                addLichSuHoaDon(hd.getId(), ghiChuThanhToan, 1);
                hd.setTrangThai(1);
                break;
            case 1:
                // Giao hàng
                addLichSuHoaDon(hd.getId(), ghiChuThanhToan, 2);
                hd.setTrangThai(2);
                break;
            case 2:
                // Giao hàng thành công
                addLichSuHoaDon(hd.getId(), ghiChuThanhToan, 3);
                hd.setTrangThai(3);
                hd.setNgayThanhToan(new java.util.Date());
                System.out.println("updateSoLuong");

                updateSL(hd);
                break;
            case 3:
                addLichSuHoaDon(hd.getId(), ghiChuThanhToan, 7);
                HoaDon hdDoiTra = new HoaDon();
                hdDoiTra.setNguoiNhan(hd.getNguoiNhan());
                hdDoiTra.setEmailNguoiNhan(hd.getEmailNguoiNhan());
                hdDoiTra.setNgayTao(new java.util.Date());
                hdDoiTra.setTaiKhoan(hd.getTaiKhoan());
                hdDoiTra.setQuanHuyen(hd.getQuanHuyen());
                hdDoiTra.setThanhPho(hd.getThanhPho());
                hdDoiTra.setPhuongXa(hd.getPhuongXa());
                hdDoiTra.setLoaiHoaDon(2);
                hdDoiTra.setTrangThai(7);
                hdDoiTra.setTongTien((long) 0);
                hdDoiTra.setTongTienKhiGiam((long) 0);
                hdDoiTra.setPhiShip((long) 0);
                hoaDonService.saveOrUpdate(hdDoiTra);
                hdDoiTra.setMaHoaDon("HD-DOITRA" + hdDoiTra.getId());
                hoaDonService.saveOrUpdate(hdDoiTra);
                for (HoaDonChiTiet hdctf : hd.getLstHoaDonChiTiet()) {
                    if (hdctf.getTrangThai() == 2) {
                        HoaDonChiTiet hdctn = hdctf;
                        hdctn.setHoaDon(hdDoiTra);
                        hoaDonChiTietService.saveOrUpdate(hdctn);
                    }
                }

                break;
            case 4:
                if (giaoHang.equals("on")) {
                    // Giao hàng
                    addLichSuHoaDon(hd.getId(), ghiChuThanhToan, 1);
                    hd.setTrangThai(1);
                    hd.setPhiShip(phiShip);
                    hd.setSdtNguoiNhan(inputSoDienThoai);
                    hd.setNguoiNhan(inputHoVaTen);
                    hd.setDiaChiNguoiNhan(inputDcct);
                    hd.setGhiChu(inputGhiChu);
                    hd.setThanhPho(thanhPho);
                    hd.setQuanHuyen(quanHuyen);
                    hd.setPhuongXa(phuongXa);

                    if (luuDiaChi.equals("on") && hd.getTaiKhoan().getTenTaiKhoan() != null) {
                        if (hd.getTaiKhoan().getLstDiaChi().size() < 5) {
                            DiaChi dc = new DiaChi();
                            dc.setQuanHuyen(quanHuyen);
                            dc.setPhuongXa(phuongXa);
                            dc.setThanhPho(thanhPho);
                            dc.setDiaChiCuThe(inputDcct);
                            dc.setTaiKhoan(hd.getTaiKhoan());
                            dc.setNgaySua(new java.util.Date());
                            dc.setNgayTao(new java.util.Date());
                            dc.setTrangThai(1);
                            diaChiService.save(dc);
                        }
                    }
                } else {
                    // Hoàn thành
                    addLichSuHoaDon(hd.getId(), ghiChuThanhToan, 3);
                    hd.setTrangThai(3);
                    hd.setNgayThanhToan(new java.util.Date());
                    hd.setTongTien(hd.tongTienHoaDon());
                    hd.setTongTienKhiGiam(hd.tongTienHoaDon() - giamGia);
//                    sendMail(hd);
                    if (hd.getNguoiNhan() == null) {
                        hd.setNguoiNhan("Khách lẻ");
                    }

                }
                // addLichSuHoaDon(hd.getId(), ghiChuThanhToan, 3);
                // hd.setTrangThai(3);
                // hd.setNgaySua(new Date());
                // hd.setNgayThanhToan(new Date());
                // updateSl(hd);
                break;
            case 6:
                hd.setTrangThai(7);
            default:
                break;

        }
        hd.setTongTien(hd.tongTienHoaDon());
        hd.setTongTienKhiGiam(hd.tongTienHoaDon() - giamGia);

        hoaDonService.saveOrUpdate(hd);
        updateSL(hd);
        if (hd.getTrangThai() == 4) {
            return "redirect:/ban-hang-tai-quay/hoa-don";
        }
        return "redirect:/ban-hang-tai-quay/hoa-don/detail/" + idhdc;
    }

}
// dung ngu
// phong oc cho
// phong 12345