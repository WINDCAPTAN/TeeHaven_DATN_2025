package com.example.datn_teehaven.controller;

import com.example.datn_teehaven.Config.PrincipalCustom;
import com.example.datn_teehaven.Config.UserInfoUserDetails;
import com.example.datn_teehaven.service.ChiTietSanPhamSerivce;
import com.example.datn_teehaven.service.HoaDonChiTietService;
import com.example.datn_teehaven.service.HoaDonService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/thong-ke")
public class ThongKeController {

    @Autowired
    HoaDonService hoaDonService;

    @Autowired
    HoaDonChiTietService hoaDonChiTietService;

    @Autowired
    ChiTietSanPhamSerivce chiTietSanPhamSerivce;

    private PrincipalCustom principalCustom = new PrincipalCustom();

    @GetMapping()
    public String hienThi(
            Model model) {
        UserInfoUserDetails name = principalCustom.getCurrentUserNameAdmin();
        if (name != null) {
            model.addAttribute("tenNhanVien", principalCustom.getCurrentUserNameAdmin().getHoVaTen());
        } else {
            return "redirect:/login";
        }
        Date ngayTao1 = new Date();
        Integer sumSanPham = (Integer) model.asMap().get("sumSanPham");
        Integer countHoaDon = (Integer) model.asMap().get("countHoaDon");
        Long sumHoaDon = (Long) model.asMap().get("sumHoaDon");
        Integer countHoaDonChoXacNhan = (Integer) model.asMap().get("countHoaDonChoXacNhanBetween");
        Integer countHoaDonChoGiao = (Integer) model.asMap().get("countHoaDonChoGiaoBetween");
        Integer countHoaDonDangGiao = (Integer) model.asMap().get("countHoaDonDangGiaoBetween");
        Integer countHoaDonHoanThanh = (Integer) model.asMap().get("countHoaDonHoanThanhBetween");
        Integer countHoaDonDaHuy = (Integer) model.asMap().get("countHoaDonDaHuyBetween");
        Integer countHoaDonTra = (Integer) model.asMap().get("countHoaDonTraBetween");
        List<Object[]> thongKeSanPham = (List<Object[]>) model.asMap().get("thongKeBetween");
        List<Object[]> danhSachSapHetHangAll = (List<Object[]>) model.asMap().get("danhSachSapHetHang");
        List<Object[]> bieuDoCot = (List<Object[]>) model.asMap().get("thongKeSP");
        Integer countHoaDonDay = hoaDonService.countHoaDonDay(ngayTao1);
        Long sumHoaDonDay = hoaDonService.sumHoaDonDay(ngayTao1);
        Integer countHoaDonMonth = hoaDonService.countHoaDonMonth(ngayTao1);
        Long sumHoaDonMonth = hoaDonService.sumHoaDonMonth(ngayTao1);
        Integer sumSanPhamHoaDonMonth = hoaDonChiTietService.sumSanPhamHoaDonThang(ngayTao1);
        Integer sumSanPhamHoaDonDay = hoaDonChiTietService.sumSanPhamHoaDonNgay(ngayTao1);
        Integer countHoaDon0 = hoaDonService.countHoaDon(0);
        Integer countHoaDon1 = hoaDonService.countHoaDon(1);
        Integer countHoaDon2 = hoaDonService.countHoaDon(2);
        Integer countHoaDon3 = hoaDonService.countHoaDon(3);
        Integer countHoaDon5 = hoaDonService.countHoaDon(5);
        Integer countHoaDon6 = hoaDonService.countHoaDon(6);
        Integer countHoaDonChoXacNhanNgay = hoaDonService.countHoaDonTrangThaiNgay(ngayTao1,0);
        Integer countHoaDonChoGiaoNgay = hoaDonService.countHoaDonTrangThaiNgay(ngayTao1,1);
        Integer countHoaDonDangGiaoNgay = hoaDonService.countHoaDonTrangThaiNgay(ngayTao1,2);
        Integer countHoaDonHoanThanhNgay = hoaDonService.countHoaDonTrangThaiNgay(ngayTao1,3);
        Integer countHoaDonDaHuyNgay = hoaDonService.countHoaDonTrangThaiNgay(ngayTao1,5);
        Integer countHoaDonTraNgay = hoaDonService.countHoaDonTrangThaiNgay(ngayTao1,6);
        Integer countHoaDonChoXacNhanThang = hoaDonService.countHoaDonTrangThaiThang(ngayTao1,0);
        Integer countHoaDonChoGiaoThang = hoaDonService.countHoaDonTrangThaiThang(ngayTao1,1);
        Integer countHoaDonDangGiaoThang = hoaDonService.countHoaDonTrangThaiThang(ngayTao1,2);
        Integer countHoaDonHoanThanhThang = hoaDonService.countHoaDonTrangThaiThang(ngayTao1,3);
        Integer countHoaDonDaHuyThang = hoaDonService.countHoaDonTrangThaiThang(ngayTao1,5);
        Integer countHoaDonTraThang = hoaDonService.countHoaDonTrangThaiThang(ngayTao1,6);
        Integer sumSanPhamAll = hoaDonChiTietService.sumSanPhamHoaDonAll();
        Integer countHoaDonAll = hoaDonService.countHoaDonAll();
        Long sumHoaDonAll = hoaDonService.sumGiaTriHoaDonAll();
        List<Object[]> thongKeSanPhamAll = hoaDonChiTietService.findByTongSoLuongAll();
        List<Object[]> bieuDoCotTru30 = hoaDonChiTietService.thongKeSanPhamTheoNgayMacDinh30Ngay();
        Integer soLuong = 10;
        List<Object[]> danhSachSapHetHang10 = chiTietSanPhamSerivce.danhSachHangSapHet(soLuong);
        model.addAttribute("countHoaDonDay", countHoaDonDay);
        model.addAttribute("sumHoaDonDay", sumHoaDonDay);
        model.addAttribute("countHoaDonMonth", countHoaDonMonth);
        model.addAttribute("sumHoaDonMonth", sumHoaDonMonth);
        model.addAttribute("sumSanPhamHoaDonMonth", sumSanPhamHoaDonMonth);
        if (sumSanPham == null) {
            model.addAttribute("sumSanPham", sumSanPhamAll);
        }else{
            model.addAttribute("sumSanPham", sumSanPham);
        }
        if (countHoaDon == null) {
            model.addAttribute("countHoaDon", countHoaDonAll);
        }else{
            model.addAttribute("countHoaDon", countHoaDon);
        }
        if (sumHoaDon == null) {
            model.addAttribute("sumHoaDon", sumHoaDonAll);
        }else{
            model.addAttribute("sumHoaDon", sumHoaDon);
        }
        if (countHoaDonChoXacNhan == null) {
            model.addAttribute("countHoaDonChoXacNhanBetween", countHoaDon0);
        }else{
            model.addAttribute("countHoaDonChoXacNhanBetween", countHoaDonChoXacNhan);
        }
        if (countHoaDonChoGiao == null) {
            model.addAttribute("countHoaDonChoGiaoBetween", countHoaDon1);
        }else{
            model.addAttribute("countHoaDonChoGiaoBetween", countHoaDonChoGiao);
        }
        if (countHoaDonDangGiao == null) {
            model.addAttribute("countHoaDonDangGiaoBetween", countHoaDon2);
        }else{
            model.addAttribute("countHoaDonDangGiaoBetween", countHoaDonDangGiao);
        }
        if (countHoaDonHoanThanh == null) {
            model.addAttribute("countHoaDonHoanThanhBetween", countHoaDon3);
        }else{
            model.addAttribute("countHoaDonHoanThanhBetween", countHoaDonHoanThanh);
        }
        if (countHoaDonDaHuy == null) {
            model.addAttribute("countHoaDonDaHuyBetween", countHoaDon5);
        }else{
            model.addAttribute("countHoaDonDaHuyBetween", countHoaDonDaHuy);
        }
//        if (countHoaDonTra == null) {
//            model.addAttribute("countHoaDonTraBetween", countHoaDon6);
//        }else{
//            model.addAttribute("countHoaDonTraBetween", countHoaDonTra);
//        }
        if (thongKeSanPham == null) {
            model.addAttribute("thongKeBetween", thongKeSanPhamAll);
        }else{
            model.addAttribute("thongKeBetween", thongKeSanPham);
        }
        if (danhSachSapHetHangAll == null){
            model.addAttribute("danhSachSapHetHang",danhSachSapHetHang10);
        }else{
            model.addAttribute("danhSachSapHetHang",danhSachSapHetHangAll);
        }
        if (bieuDoCot == null){
            model.addAttribute("thongKeSP",bieuDoCotTru30);
        }else{
            model.addAttribute("thongKeSP",bieuDoCot);
        }
        model.addAttribute("sumSanPhamHoaDonDay", sumSanPhamHoaDonDay);
        model.addAttribute("countHoaDonChoXacNhanNgay", countHoaDonChoXacNhanNgay);
        model.addAttribute("countHoaDonChoGiaoNgay", countHoaDonChoGiaoNgay);
        model.addAttribute("countHoaDonDangGiaoNgay", countHoaDonDangGiaoNgay);
        model.addAttribute("countHoaDonHoanThanhNgay", countHoaDonHoanThanhNgay);
        model.addAttribute("countHoaDonDaHuyNgay", countHoaDonDaHuyNgay);
        model.addAttribute("countHoaDonTraNgay", countHoaDonTraNgay);
        model.addAttribute("countHoaDonChoXacNhanThang", countHoaDonChoXacNhanThang);
        model.addAttribute("countHoaDonChoGiaoThang", countHoaDonChoGiaoThang);
        model.addAttribute("countHoaDonDangGiaoThang", countHoaDonDangGiaoThang);
        model.addAttribute("countHoaDonHoanThanhThang", countHoaDonHoanThanhThang);
        model.addAttribute("countHoaDonDaHuyThang", countHoaDonDaHuyThang);
        model.addAttribute("countHoaDonTraThang", countHoaDonTraThang);
        model.addAttribute("countHoaDon0", countHoaDon0);
        model.addAttribute("countHoaDon1", countHoaDon1);
        model.addAttribute("countHoaDon2", countHoaDon2);
        model.addAttribute("countHoaDon3", countHoaDon3);
        model.addAttribute("countHoaDon5", countHoaDon5);
        model.addAttribute("countHoaDon6", countHoaDon6);
        List<Object[]> result = hoaDonChiTietService.findByTongSoLuongThang(ngayTao1);
        List<Object[]> top5Ngay = hoaDonChiTietService.findByTongSoLuongNgay(ngayTao1);
        model.addAttribute("top5Thang", result);
        model.addAttribute("top5Ngay", top5Ngay);
        model.addAttribute("listSapHetHang",danhSachSapHetHang10);
        return "/admin-template/thong_ke/thong-ke";
    }

    @GetMapping("/admin/thong-ke")
    @ResponseBody
    public Map<String, Object> getThongKe(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        Map<String, Object> response = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Long> doanhThuList = new ArrayList<>();
        List<Integer> donHoanThanhList = new ArrayList<>();
        List<Integer> soSanPhamBanList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            Date currentDate = calendar.getTime();
            labels.add(currentDate.toString());

            Long doanhThu = hoaDonService.sumGiaTriHoaDonBetween(currentDate, currentDate);
            Integer donHoanThanh = hoaDonService.countHoaDonTrangThaiNgay(currentDate, 3); // Giả sử 3 là trạng thái hoàn thành
            Integer soSanPhamBan = hoaDonService.countHoaDonDay(currentDate); // Tổng số hóa đơn có thể tượng trưng cho số sản phẩm

            doanhThuList.add(doanhThu != null ? doanhThu : 0);
            donHoanThanhList.add(donHoanThanh != null ? donHoanThanh : 0);
            soSanPhamBanList.add(soSanPhamBan != null ? soSanPhamBan : 0);

            calendar.add(Calendar.DAY_OF_MONTH, 1); // Tăng ngày lên 1
        }

        response.put("labels", labels);
        response.put("doanhThu", doanhThuList);
        response.put("donHoanThanh", donHoanThanhList);
        response.put("soSanPhamBan", soSanPhamBanList);

        return response;
    }

    @PostMapping("/count-range")
    public String countHoaDonTrongKhoangThoiGian(
            @RequestParam(name = "startDateChart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDateChart,
            @RequestParam(name = "endDateChart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDateChart,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("startDateChart", startDateChart);
        redirectAttributes.addFlashAttribute("endDateChart", endDateChart);
        List<Object[]> thongKeSP = hoaDonChiTietService.thongKeSanPhamTheoNgay(startDateChart, endDateChart);
        redirectAttributes.addFlashAttribute("thongKeSP", thongKeSP);
        return "redirect:/admin/thong-ke";
    }

    @PostMapping("/count-between")
    public String countHoaDonBetween(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            RedirectAttributes redirectAttributes) {
        Integer sumSanPham = hoaDonChiTietService.sumSanPhamHoaDonBetween(startDate, endDate);
        Integer countHoaDon = hoaDonService.countHoaDonBetween(startDate, endDate);
        Long sumHoaDon = hoaDonService.sumGiaTriHoaDonBetween(startDate, endDate);
        Integer countHoaDonChoXacNhanBetween = hoaDonService.countHoaDonTrangThaiBetween(startDate,endDate,0);
        Integer countHoaDonChoGiaoBetween = hoaDonService.countHoaDonTrangThaiBetween(startDate,endDate,1);
        Integer countHoaDonDangGiaoBetween = hoaDonService.countHoaDonTrangThaiBetween(startDate,endDate,2);
        Integer countHoaDonHoanThanhBetween = hoaDonService.countHoaDonTrangThaiBetween(startDate,endDate,3);
        Integer countHoaDonDaHuyBetween = hoaDonService.countHoaDonTrangThaiBetween(startDate,endDate,5);
        Integer countHoaDonTraBetween = hoaDonService.countHoaDonTrangThaiBetween(startDate,endDate,6);
        redirectAttributes.addFlashAttribute("sumSanPham", sumSanPham);
        redirectAttributes.addFlashAttribute("startDate", startDate);
        redirectAttributes.addFlashAttribute("endDate", endDate);
        redirectAttributes.addFlashAttribute("countHoaDon", countHoaDon);
        redirectAttributes.addFlashAttribute("sumHoaDon", sumHoaDon);
        redirectAttributes.addFlashAttribute("countHoaDonChoXacNhanBetween", countHoaDonChoXacNhanBetween);
        redirectAttributes.addFlashAttribute("countHoaDonChoGiaoBetween", countHoaDonChoGiaoBetween);
        redirectAttributes.addFlashAttribute("countHoaDonDangGiaoBetween", countHoaDonDangGiaoBetween);
        redirectAttributes.addFlashAttribute("countHoaDonHoanThanhBetween", countHoaDonHoanThanhBetween);
        redirectAttributes.addFlashAttribute("countHoaDonDaHuyBetween", countHoaDonDaHuyBetween);
        redirectAttributes.addFlashAttribute("countHoaDonTraBetween", countHoaDonTraBetween);
        List<Object[]> thongKeSanPhamBetween = hoaDonChiTietService.findByTongSoLuongBetween(startDate, endDate);
        redirectAttributes.addFlashAttribute("thongKeBetween", thongKeSanPhamBetween);
        return "redirect:/admin/thong-ke";
    }

    @PostMapping("/sapHetHang")
    public String countSapHetHang(
            @RequestParam(name = "soLuong", required = false) Integer soLuong,
            @RequestParam(name = "outputFormat", defaultValue = "table") String outputFormat,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response) {

        List<Object[]> danhSachSapHetHang = chiTietSanPhamSerivce.danhSachHangSapHet(soLuong);

        redirectAttributes.addFlashAttribute("soLuong", soLuong);
        redirectAttributes.addFlashAttribute("danhSachSapHetHang", danhSachSapHetHang);

        if ("excel".equals(outputFormat)) {
            // Export to Excel
            response.setHeader("Content-Disposition", "attachment; filename=SapHetHang.xlsx");
            exportToExcel(response, danhSachSapHetHang);
            return null;  // Returning null to indicate that the response is already handled
        } else {
            return "redirect:/admin/thong-ke";  // Redirect to the table view
        }
    }

    private void exportToExcel(HttpServletResponse response, List<Object[]> listSapHetHang) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("SapHetHang");

            // Create header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("STT");
            header.createCell(1).setCellValue("Sản phẩm");
            header.createCell(2).setCellValue("Kích cỡ");
            header.createCell(3).setCellValue("Màu sắc");
            header.createCell(4).setCellValue("Loại đế");
            header.createCell(5).setCellValue("Số Lượng");

            // Populate data rows
            int rowNum = 1;
            for (Object[] row : listSapHetHang) {
                Row dataRow = sheet.createRow(rowNum++);
                dataRow.createCell(0).setCellValue(rowNum - 1); // STT
                for (int i = 1; i < row.length + 1; i++) {
                    Cell cell = dataRow.createCell(i);
                    cell.setCellValue(String.valueOf(row[i - 1]));
                }
            }

            // Write the workbook to the response output stream
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.close();

        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }


}
