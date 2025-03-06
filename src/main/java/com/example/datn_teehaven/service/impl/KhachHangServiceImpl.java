package com.example.datn_teehaven.service.impl;


import com.example.datn_teehaven.entyti.TaiKhoan;
import com.example.datn_teehaven.repository.KhachHangRepository;
import com.example.datn_teehaven.service.KhachHangService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class KhachHangServiceImpl implements KhachHangService {

    @Autowired
    KhachHangRepository repository;

    @Autowired
    private JavaMailSender javaMailSender;

    // Regex patterns
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]{6,20}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0[3|5|7|8|9])[0-9]{8}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-ZÀ-ỹ\\s]{1,100}$");

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

    @Transactional
    @Override
    public TaiKhoan add(TaiKhoan taiKhoan) {
        try {
            System.out.println("Bắt đầu thêm tài khoản: " + taiKhoan.getTenTaiKhoan());
            TaiKhoan saved = repository.save(taiKhoan);
            repository.flush(); // Đẩy dữ liệu vào DB ngay lập tức
            System.out.println("Tài khoản đã lưu thành công! ID: " + saved.getId());
            return saved;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi thêm tài khoản: " + e.getMessage());
            return null;
        }
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
        if (!USERNAME_PATTERN.matcher(ten).matches()) {
            return false; // Không đúng định dạng
        }
        return repository.findAll().stream()
                .filter(tk -> tk.getGioiTinh() != null)
                .noneMatch(tk -> tk.getTenTaiKhoan().equalsIgnoreCase(ten));
    }

    @Override
    public boolean checkEmail(String email) {
        return repository.findAll()
                .stream()
                .filter(tk -> tk.getEmail() != null)
                .noneMatch(tk -> tk.getEmail().equalsIgnoreCase(email));
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
    public boolean checkSoDienThoaiTrung(String soDienThoai) {
        return false;
    }

    @Override
    public boolean checkSoDienThoaiTrungSua(Long id, String soDienThoai) {
        return false;
    }

    @Override
    public TaiKhoan findKhachLe() {
        return repository.findKhachLe();
    }

    public void sendEmail(TaiKhoan taiKhoan, String path, String random) {
        String from = "bachdung004@gmail.com";
        String to = taiKhoan.getEmail();
        String subject = "Chào mừng bạn đến với TeeHaven - Tài khoản Khách Hàng mới đã được tạo";
        String content =
                "Chào bạn," + "<br>" +
                        "Chúc mừng! Tài khoản Khách Hàng mới của bạn tại TeeHaven đã được tạo thành công. Dưới đây là thông tin đăng nhập của bạn:" + "<br>" +
                        "- Tài khoản:  " + taiKhoan.getTenTaiKhoan() + "<br>" +
                        "- Mật khẩu:   " + random +
                        "<br>" +
                        "Cảm ơn bạn đã chọn TeeHaven! Nếu bạn có bất kỳ câu hỏi hoặc cần hỗ trợ, đừng ngần ngại liên hệ với chúng tôi.";
        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from, "TeeHaven");
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
        String to = "bachdung004@gmail.com";
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
