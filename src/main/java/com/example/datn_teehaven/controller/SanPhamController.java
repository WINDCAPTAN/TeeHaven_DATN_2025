package com.example.datn_teehaven.controller;


import com.example.datn_teehaven.Config.PrincipalCustom;
import com.example.datn_teehaven.Config.UserInfoUserDetails;
import com.example.datn_teehaven.entyti.SanPham;
import com.example.datn_teehaven.service.SanPhamService;
import com.example.datn_teehaven.service.ThuongHieuService;
import com.google.firebase.cloud.StorageClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/san-pham")
public class SanPhamController {
    //huynh
    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private ThuongHieuService thuongHieuService;

    private PrincipalCustom principalCustom = new PrincipalCustom();

    @GetMapping()
    public String getAll(Model model){
        UserInfoUserDetails name = principalCustom.getCurrentUserNameAdmin();
        if (name != null) {
            model.addAttribute("tenNhanVien", principalCustom.getCurrentUserNameAdmin().getHoVaTen());
        } else {
            return "redirect:/login";
        }
        model.addAttribute("listSP",sanPhamService.getAll());
        model.addAttribute("sanPham",new SanPham());
        model.addAttribute("listTH",thuongHieuService.getAllDangHoatDong());
        return "admin-template/san_pham/san-pham";
    }
    @GetMapping("/dang-hoat-dong")
    public String hienThiDangHoatDong(
            Model model
    ) {
        model.addAttribute("listSP", sanPhamService.getAllDangHoatDong());
        model.addAttribute("sanPham", new SanPham());
        return "admin-template/san_pham/san-pham";
    }
    @GetMapping("/ngung-hoat-dong")
    public String hienThiNgungHoatDong(
            Model model
    ) {
        model.addAttribute("listSP", sanPhamService.getAllNgungHoatDong());
        model.addAttribute("sanPham", new SanPham());
        return "admin-template/san_pham/san-pham";
    }

    @Value("${app.firebase.bucket.name}")
    private String bucketName;
    @PostMapping("/add")
    public String add(@ModelAttribute("sanPham") @Valid SanPham sanPham,
                      BindingResult result,
                      Model model,
                      @RequestParam("fileImage") List<MultipartFile> listHinhAnh,
                      RedirectAttributes redirectAttributes) throws IOException {

        // Kiểm tra ký tự đặc biệt trong tên sản phẩm
        if (sanPham.getTen() != null && sanPham.getTen().matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+.*")) {
            model.addAttribute("checkModal", "modal");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("checkTenTrung", "Tên sản phẩm không được chứa ký tự đặc biệt");
            model.addAttribute("listSP", sanPhamService.getAll());
            model.addAttribute("listTH", thuongHieuService.getAllDangHoatDong());
            return "admin-template/san_pham/san-pham";
        }

        // Kiểm tra số trong tên sản phẩm
        if (sanPham.getTen() != null && sanPham.getTen().matches(".*\\d+.*")) {
            model.addAttribute("checkModal", "modal");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("checkTenTrung", "Tên sản phẩm không được chứa số");
            model.addAttribute("listSP", sanPhamService.getAll());
            model.addAttribute("listTH", thuongHieuService.getAllDangHoatDong());
            return "admin-template/san_pham/san-pham";
        }

        // Kiểm tra lỗi validate
        if (result.hasErrors()) {
            model.addAttribute("checkModal", "modal");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("listSP", sanPhamService.getAll());
            model.addAttribute("listTH", thuongHieuService.getAllDangHoatDong());
            return "admin-template/san_pham/san-pham";
        }

        // Kiểm tra tên sản phẩm có bị trùng không
        if (!sanPhamService.checkTenTrung(sanPham.getTen())) {
            model.addAttribute("checkModal", "modal");
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("checkTenTrung", "Sản phẩm đã tồn tại");
            model.addAttribute("listSP", sanPhamService.getAll());
            model.addAttribute("listTH", thuongHieuService.getAllDangHoatDong());
            return "admin-template/san_pham/san-pham";
        }

        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile hinhAnh : listHinhAnh) {
            // Upload hình ảnh lên Firebase Storage và lấy URL
            String imageUrl = uploadImageToFirebase(hinhAnh);
            if (imageUrl == null) {
                redirectAttributes.addFlashAttribute("checkThongBao", "uploadHinhAnhThatBai");
                return "redirect:/admin/san-pham";
            }
            imageUrls.add(imageUrl); // Lưu URL vào danh sách
        }
        sanPham.setMa("SP" + sanPhamService.genMaTuDong());
        sanPham.setSoLuongTon(0);
        sanPham.setNgayTao(Date.valueOf(LocalDate.now()));
        sanPham.setTrangThai(0);
        sanPham.setHinhAnh(imageUrls.toString());
        sanPham.setHinhAnh(String.join(",", imageUrls));
        sanPhamService.add(sanPham);
        redirectAttributes.addFlashAttribute("checkThongBao", "thanhCong");
        return "redirect:/admin/san-pham";
    }

    private String uploadImageToFirebase(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        // Upload file to Firebase Storage
        StorageClient.getInstance().bucket(bucketName).create(fileName, file.getBytes(), file.getContentType());

        // Retrieve the download URL
        String imageUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", bucketName, fileName);

        return imageUrl;
    }
    @GetMapping("/detail/{id}")
    public String Detail(@PathVariable("id")Long id,
                         Model model){
        SanPham sanPham = sanPhamService.getById(id);
        model.addAttribute("sanPham",sanPham);
        model.addAttribute("listTH",thuongHieuService.getAllDangHoatDong());
        return "admin-template/san_pham/sua-san-pham";
    }

    @GetMapping("/detail-redirect/{id}")
    public String redirectToDetail(@PathVariable("id") Long id) {
        return "redirect:/admin/san-pham-chi-tiet/view-update/" + id;
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("sanPham") @Valid SanPham sanPham,
                         BindingResult result,
                         Model model,
                         @RequestParam(value = "fileImage", required = false) List<MultipartFile> listHinhAnh,
                         RedirectAttributes redirectAttributes) throws IOException {

        // Kiểm tra ký tự đặc biệt trong tên sản phẩm
        if (sanPham.getTen() != null && sanPham.getTen().matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+.*")) {
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("checkTenTrung", "Tên sản phẩm không được chứa ký tự đặc biệt");
            model.addAttribute("listTH", thuongHieuService.getAllDangHoatDong());
            return "admin-template/san_pham/sua-san-pham";
        }

        // Kiểm tra số trong tên sản phẩm
        if (sanPham.getTen() != null && sanPham.getTen().matches(".*\\d+.*")) {
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("checkTenTrung", "Tên sản phẩm không được chứa số");
            model.addAttribute("listTH", thuongHieuService.getAllDangHoatDong());
            return "admin-template/san_pham/sua-san-pham";
        }

        if (result.hasErrors()) {
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("listTH", thuongHieuService.getAllDangHoatDong());
            return "admin-template/san_pham/sua-san-pham";
        }

        if (!sanPhamService.checkTenTrungSua(sanPham.getId(), sanPham.getTen())) {
            model.addAttribute("checkThongBao", "thaiBai");
            model.addAttribute("checkTenTrung", "Sản phẩm đã tồn tại");
            model.addAttribute("listSP", sanPhamService.getAll());
            model.addAttribute("listTH", thuongHieuService.getAllDangHoatDong());
            return "admin-template/san_pham/sua-san-pham";
        }

        // Lấy thông tin sản phẩm cũ từ database
        SanPham sanPhamCu = sanPhamService.getById(sanPham.getId());

        // Kiểm tra nếu có ảnh mới được tải lên
        if (listHinhAnh != null && !listHinhAnh.isEmpty() && !listHinhAnh.get(0).isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile hinhAnh : listHinhAnh) {
                String imageUrl = uploadImageToFirebase(hinhAnh);
                if (imageUrl == null) {
                    redirectAttributes.addFlashAttribute("checkThongBao", "uploadHinhAnhThatBai");
                    return "redirect:/admin/san-pham";
                }
                imageUrls.add(imageUrl);
            }
//             Cập nhật danh sách ảnh mới vào sản phẩm
            sanPham.setHinhAnh(String.join(",", imageUrls));
        } else {
            // Nếu không có ảnh mới, giữ nguyên ảnh cũ
            sanPham.setHinhAnh(sanPhamCu.getHinhAnh());
        }
        sanPham.setNgayTao(sanPhamCu.getNgayTao());
        sanPham.setMa(sanPhamCu.getMa());
        sanPham.setSoLuongTon(sanPhamCu.getSoLuongTon());

        sanPhamService.update(sanPham);
        redirectAttributes.addFlashAttribute("checkThongBao", "thanhCong");

        return "redirect:/admin/san-pham";
    }
}
