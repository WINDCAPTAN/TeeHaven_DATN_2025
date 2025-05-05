package com.example.datn_teehaven.controller;

import com.example.datn_teehaven.Config.PrincipalCustom;
import com.example.datn_teehaven.Config.UserInfoUserDetails;
import com.example.datn_teehaven.entyti.ChiTietSanPham;
import com.example.datn_teehaven.entyti.DiaChi;
import com.example.datn_teehaven.entyti.HoaDon;
import com.example.datn_teehaven.entyti.HoaDonChiTiet;
import com.example.datn_teehaven.entyti.LichSuHoaDon;
import com.example.datn_teehaven.entyti.TaiKhoan;
import com.example.datn_teehaven.entyti.Voucher;
import com.example.datn_teehaven.repository.LichSuHoaDonRepository;
import com.example.datn_teehaven.service.ChiTietSanPhamSerivce;
import com.example.datn_teehaven.service.DiaChiService;
import com.example.datn_teehaven.service.HoaDonChiTietService;
import com.example.datn_teehaven.service.HoaDonService;
import com.example.datn_teehaven.service.KhachHangService;
import com.example.datn_teehaven.service.LichSuHoaDonService;
import com.example.datn_teehaven.service.SanPhamService;
import com.example.datn_teehaven.service.TaiKhoanService;
import com.example.datn_teehaven.service.VoucherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Controller
@RequestMapping("/hoa-don")
public class HoaDonController {

    // dung123
    @Autowired
    HttpServletRequest request;
    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private ChiTietSanPhamSerivce chiTietSanPhamSerivce;

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @Autowired
    private DiaChiService diaChiService;

    @Autowired
    private LichSuHoaDonService lichSuHoaDonService;

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private SanPhamService sanPhamService;
    private PrincipalCustom principalCustom = new PrincipalCustom();

    @Autowired
    LichSuHoaDonRepository lichSuHoaDonRepository;

    @GetMapping("/quan-ly")
    public String quanLyHoaDon(Model model) {
        UserInfoUserDetails name = principalCustom.getCurrentUserNameAdmin();
        if (name != null) {
            model.addAttribute("tenNhanVien", principalCustom.getCurrentUserNameAdmin().getHoVaTen());
        } else {
            return "redirect:/login";
        }
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
    public void addLichSuHoaDon(Long idHoaDon, String ghiChu, Integer trangThaiMoi) {
        HoaDon hoaDon = hoaDonService.findById(idHoaDon);
        if (hoaDon == null) return;

        LichSuHoaDon cuoi = lichSuHoaDonRepository.findTopByHoaDonOrderByNgaySuaDesc(hoaDon);
        if (cuoi == null || !cuoi.getTrangThai().equals(trangThaiMoi)) {
            LichSuHoaDon lichSu = new LichSuHoaDon();
            lichSu.setHoaDon(hoaDon);
            lichSu.setTrangThai(trangThaiMoi);
            lichSu.setGhiChu(ghiChu);
            lichSu.setNgaySua(new Date());

            lichSuHoaDonRepository.save(lichSu);
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
            return "redirect:/hoa-don/" + idhdc;
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
//                    sendMail(hd);
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
//                    sendMail(hd);
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
//                 addLichSuHoaDon(hd.getId(), ghiChuThanhToan, 3);
//                 hd.setTrangThai(3);
//                 hd.setNgaySua(new Date());
//                 hd.setNgayThanhToan(new Date());
//                 updateSl(hd);
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
            return "redirect:/hoa-don";
        }
        return "redirect:/hoa-don/detail/" + idhdc;
    }
    @PostMapping("/chuyen-nhanh")
    public String chuyenNhanh(
            @RequestParam Long idHoaDon,
            @RequestParam String ghiChu,
            RedirectAttributes redirectAttributes

    ) {

        HoaDon hd = hoaDonService.findById(idHoaDon);
        if (!checkSlDb(hd)) {
            thongBao(redirectAttributes, "Có sản phẩm vượt quá số lượng vui lòng kiểm tra lại", 0);
            return "redirect:/hoa-don/detail/" + idHoaDon;
        }
        if (hd.getTrangThai() == 0) {
            // If order is moving from "waiting for confirmation" to "confirmed"
            updateSL(hd); // Reduce product quantities in inventory
        }

        hd.setTrangThai(hd.getTrangThai() + 1);
        addLichSuHoaDon(idHoaDon, ghiChu, hd.getTrangThai());
        hoaDonService.saveOrUpdate(hd);

        System.out.println(ghiChu + "ghiChu");

        return "redirect:/hoa-don/quan-ly";
    }
    @Transactional
    public void updateSoLuongRollBack(Long idhdc) {
        HoaDon hd = hoaDonService.findById(idhdc);
        Integer trangThai = hd.getTrangThai();

        System.out.println("Trạng thái hóa đơn: " + trangThai); // Kiểm tra trạng thái thực tế

        if (trangThai != null && (trangThai.equals(1) || trangThai.equals(2))) {

            // Rollback sản phẩm
            for (HoaDonChiTiet hoaDonChiTiet : hd.getLstHoaDonChiTiet()) {
                ChiTietSanPham ctsp = hoaDonChiTiet.getChiTietSanPham();
                ctsp.setSoLuong(ctsp.getSoLuong() + hoaDonChiTiet.getSoLuong());
                chiTietSanPhamSerivce.update(ctsp);
            }

            // Rollback voucher nếu có
            if (hd.getVoucher() != null) {
                Voucher v = hd.getVoucher();
                v.setSoLuong(v.getSoLuong().add(BigDecimal.ONE));
                voucherService.save(v);
            }

            // Đặt trạng thái thành hủy (5)
            hd.setTrangThai(5);
            hoaDonService.saveOrUpdate(hd);

        } else {
            System.out.println("Hóa đơn không thể rollback vì không ở trạng thái 1 hoặc 2.");
        }
    }



    @PostMapping("/delete/{id}")
    public String delete(RedirectAttributes redirectAttributes, @PathVariable Long id, @RequestParam String ghiChu) {
        HoaDon hd = hoaDonService.findById(id);
        if (hd.getTrangThai() == 1) {
            updateSoLuongRollBack(id);
        }

        Integer oldStatus = hd.getTrangThai();
        Integer newStatus = 5; // Status for canceled orders

        hd.setTrangThai(newStatus);
        hoaDonService.saveOrUpdate(hd);

        // Only add history if status actually changed
        if (!oldStatus.equals(newStatus)) {
            addLichSuHoaDon(id, ghiChu, newStatus);
        }

        thongBao(redirectAttributes, "Thành công", 1);
        if (hd.getTrangThai() == -1) {
            return "redirect:/hoa-don/" + id;
        } else {
            return "redirect:/hoa-don/detail/" + id;
        }
    }
    @GetMapping("/detail/{id}")
    public String detailHoaDon(@PathVariable Long id, Model model) {

//        lstHoaDonCtDoiTra = new ArrayList<HoaDonChiTiet>();
        model.addAttribute("lstHoaDon", hoaDonService.find5ByTrangThai(-1));
        model.addAttribute("lstHdct", hoaDonChiTietService.findAll());
        model.addAttribute("lstCtsp", chiTietSanPhamSerivce.fillAllDangHoatDongLonHon0());
        model.addAttribute("lstTaiKhoan", khachHangService.getAll());
        model.addAttribute("sanpham", sanPhamService.getAll());
        model.addAttribute("lstTaiKhoanDc",
                khachHangService.getById(hoaDonService.findById(id).getTaiKhoan().getId()));
//        model.addAttribute("listVoucher", voucherService.fillAllDangDienRa());
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
            return "redirect:/hoa-don/" + id;
        }
        return "/admin-template/detail-hoa-don";
    }


}
