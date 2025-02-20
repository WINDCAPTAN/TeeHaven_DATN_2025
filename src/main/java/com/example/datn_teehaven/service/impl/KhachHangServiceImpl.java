package com.example.datn_teehaven.service.impl;


import com.example.datn_teehaven.entyti.TaiKhoan;
import com.example.datn_teehaven.repository.KhachHangRepository;
import com.example.datn_teehaven.service.KhachHangService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KhachHangServiceImpl implements KhachHangService {

    @Autowired
    KhachHangRepository repository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public List<TaiKhoan> getAll() {

        return repository.fillAllKhachHang();

    }

    @Override
    public List<TaiKhoan> getAllDangHoatDong() {

        return repository.fillAllDangHoatDong();

    }

    @Override
    public List<TaiKhoan> getAllNgungHoatDong() {

        return repository.fillAllNgungHoatDong();

    }

    @Override
    public TaiKhoan add(TaiKhoan taiKhoan) {

        return repository.save(taiKhoan);

    }

    @Override
    public TaiKhoan update(TaiKhoan taiKhoan) {

        return repository.save(taiKhoan);

    }

    @Override
    public void remove(Long id) {

        repository.deleteById(id);

    }

    @Override
    public TaiKhoan getById(Long id) {

        return repository.findById(id).get();

    }


    @Override
    public boolean checkTenTrung(String ten) {
//        for (TaiKhoan sp : repository.findAll()) {
//            if (sp.getTenTaiKhoan().equalsIgnoreCase(ten)) {
//                return false;
//            }
//        }
        return true;
    }

    @Override
    public boolean checkTenTaiKhoanTrung(String ten) {
        for (TaiKhoan sp : repository.findAll()) {
            if(sp.getGioiTinh()==null){
                continue;
            }
            if (sp.getTenTaiKhoan().equalsIgnoreCase(ten)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkEmail(String email) {
        for (TaiKhoan sp : repository.findAll()) {
            if(sp.getGioiTinh()==null){
                continue;
            }
            if (sp.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkTenTkTrungSua(Long id, String ten) {
        for (TaiKhoan sp : repository.findAll()) {
            if(sp.getGioiTinh()==null){
                continue;
            }
            if (sp.getTenTaiKhoan().equalsIgnoreCase(ten)) {
                if (!sp.getId().equals(id)) {
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public boolean checkEmailSua(Long id, String email) {
        for (TaiKhoan sp : repository.findAll()) {
            if(sp.getGioiTinh()==null){
                continue;
            }
            if (sp.getEmail().equalsIgnoreCase(email)) {
                if (!sp.getId().equals(id)) {
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public TaiKhoan findKhachLe() {
        return repository.findKhachLe();
    }

    public void sendEmail(TaiKhoan taiKhoan, String path, String random) {
        String from = "bachdung004@gmail.com";
        String to = taiKhoan.getEmail();
        String subject = "Khôi Phục Mật Khẩu Tài Khoản Glacat của Bạn";
        String content = "<p class=\"email-content\" style=\"font-family: 'Arial', sans-serif;font-size: 16px;color: #333;line-height: 1.5;\">\n" +
                "Chào [[name]], <br>\n" +
                "Chúc mừng! Bạn đã yêu cầu hướng dẫn khôi phục mật khẩu cho tài khoản của mình trên Glacat. Để tiếp tục quá trình này, vui lòng nhấn vào liên kết dưới đây:\n" +
                "</p>\n" +

                "<p class=\"email-content\">\n" +
                "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" +
                "</p>\n" +

                "<p class=\"email-content\">\n" +
                "<p>Tên tài khoản của bạn: " + taiKhoan.getTenTaiKhoan() + "</p>" +
                "<p>Email của bạn: " + taiKhoan.getEmail() + "</p>" +
                "Nếu bạn không yêu cầu hướng dẫn khôi phục mật khẩu hoặc không nhớ việc này, hãy bỏ qua email này. Liên kết xác nhận sẽ hết hạn sau 24 giờ.\n" +
                "<br>\n" +
                "Chân thành cảm ơn,\n" +
                "<br>\n" +
                "Đội ngũ TeeHaven\n" +
                "</p>";
        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from, "Glacat");
            helper.setTo(to);
            helper.setSubject(subject);
            content = content.replace("[[name]]", taiKhoan.getTenTaiKhoan());
            String siteUrl = "Mật khẩu" + random + "Tài khoản" + taiKhoan.getTenTaiKhoan();

            System.out.println(siteUrl);

            content = content.replace("[[URL]]", siteUrl);

            helper.setText(content, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void guiLieuHe(String hoTen, String email, String chuDe, String tinNhan) {
        String from = email;
        String to = "glacatshopshoes@gmail.com";
        String subject = chuDe;
        String content = tinNhan;
        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from, hoTen);
            helper.setTo(to);
            helper.setSubject(subject);

            helper.setText(content, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addKhachLe() {
        repository.addKhachLe();
    }
}
