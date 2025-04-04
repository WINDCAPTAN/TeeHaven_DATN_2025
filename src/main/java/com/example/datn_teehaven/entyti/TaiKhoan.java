package com.example.datn_teehaven.entyti;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tai_khoan")

@Builder

public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ho_va_ten", length = 100)
    @NotBlank(message = "Họ và tên không được trống")
    private String hoVaTen;

    @Column(name = "ngay_sinh")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Ngày sinh không được trống")
    @PastOrPresent(message = "Phải là một ngày trong quá khứ hoặc hiện tại")
    private Date ngaySinh;


    // Kiem tra ngay sinh >= 1923
    public boolean isValidNgaySinh() {
        if (ngaySinh != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(ngaySinh);
            int year = cal.get(Calendar.YEAR);
            return year >= 1900;
        }
        return true; // Truong ngaySinh co the de trong
    }

    @Column(name = "gioi_tinh")
    @NotNull(message = "Giới tính không được để trống")
    private Integer gioiTinh;

    @Column(name = "so_dien_thoai", length = 15)
    @NotBlank(message = "Số điện thoai không được trống")
    @Pattern(regexp = "^(0[35789][0-9]{8,9})$", message = "Số điện thoại không hợp lệ")
    private String soDienThoai;

    @Column(name = "email", length = 255)
    @NotBlank(message = "Email không được trống")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Email không hợp lệ")
    private String email;

    @Column(name = "ten_tai_khoan", length = 100)
    @NotBlank(message = "Tên tài khoản không được trống")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Tên tài khoản không hợp lệ")
    private String tenTaiKhoan;

    @Column(name = "mat_khau")
//    @NotBlank(message = "Mật khẩu không được trống")
    private String matKhau;

    public String getConfirmMatKhau() {
        return confirmMatKhau;
    }

    public void setConfirmMatKhau(String confirmMatKhau) {
        this.confirmMatKhau = confirmMatKhau;
    }

    @Transient // Không lưu vào database
    private String confirmMatKhau;

    @Column(name = "ngay_tao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayTao;

    @Column(name = "ngay_sua")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngaySua;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vai_tro_id", referencedColumnName = "id")
    private VaiTro vaiTro;

    @OneToMany(mappedBy = "taiKhoan")
    List<DiaChi> lstDiaChi;


    @OneToOne(mappedBy = "taiKhoan")
    GioHang gioHang;


}
