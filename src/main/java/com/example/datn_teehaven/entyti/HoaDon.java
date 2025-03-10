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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hoa_don")
@Builder
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_hoa_don", length = 20)
    private String maHoaDon;

    @Column(name = "ngay_thanh_toan")
    private Date ngayThanhToan;

    @Column(name = "loai_hoa_don")
    private Integer loaiHoaDon;

    @Column(name = "phi_ship")
    private Long phiShip;

    @Column(name = "tien_giam")
    private Long tienGiam;

    @Column(name = "tong_tien")
    private Long tongTien;

    @Column(name = "tong_tien_khi_giam")
    private Long tongTienKhiGiam;

    @Column(name = "ghi_chu", length = 255)
    private String ghiChu;

    @Column(name = "nguoi_nhan", length = 100)
    private String nguoiNhan;

    @Column(name = "sdt_nguoi_nhan", length = 15)
    private String sdtNguoiNhan;
    @Column(name = "thanh_pho", length = 50)
    private String thanhPho;

    @Column(name = "quan_huyen", length = 50)
    private String quanHuyen;

    @Column(name = "phuong_xa", length = 50)
    private String phuongXa;

    @Column(name = "dia_chi_nguoi_nhan", length = 100)
    private String diaChiNguoiNhan;

    @Column(name = "email_nguoi_nhan", length = 100)
    private String emailNguoiNhan;

    @Column(name = "ngay_nhan")
    private Date ngayNhan;

    @Column(name = "ngay_mong_muon")
    private Date ngayMongMuon;

    @Column(name = "ngay_tao")
    @DateTimeFormat(pattern = "HH:mm dd/MM/yyyy")
    private Date ngayTao;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id", referencedColumnName = "id")
    private Voucher voucher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tai_khoan_id", referencedColumnName = "id")
    private TaiKhoan taiKhoan;

    @OneToMany(mappedBy = "hoaDon")
    private List<HoaDonChiTiet> lstHoaDonChiTiet;

    public Long tongTienHoaDon() {
        Long total = (long) 0;
        for (HoaDonChiTiet hoaDonChiTiet : lstHoaDonChiTiet) {
            total += hoaDonChiTiet.tongTien();
        }
        return total;
    }

    public Long tongTienHoaDonHoanTra() {
        Long total = (long) 0;
        for (HoaDonChiTiet hoaDonChiTiet : lstHoaDonChiTiet) {
            if (hoaDonChiTiet.getTrangThai() == 2) {
                total += hoaDonChiTiet.tongTien();
            }
        }
        return total;
    }



    public Long tongTienHoaDonDaNhan() {
        Long total = (long) 0;
        for (HoaDonChiTiet hoaDonChiTiet : lstHoaDonChiTiet) {
            if (hoaDonChiTiet.getTrangThai() == 0) {
                total += hoaDonChiTiet.tongTien();
            }
        }
        return total;
    }

    public Long tongTienHoaDonKhiGiam() {

        return this.tongTienHoaDonDaNhan() + this.getPhiShip()-this.getGiamGia();
    }

    public Long getGiamGia() {
        return this.tienGiam!=null?this.tienGiam:0;
    }





    public String getStringTrangThai() {
        switch (this.trangThai) {
            case 0:
                return "Chờ xác nhận";
            case 1:
                return "Chờ giao";
            case 2:
                return "Đang giao";

            case 3:
                return "Hoàn thành";
            case 4:
                return "Chờ thanh toán";
            case 5:
                return "Đã hủy";
            case 6:
                return "Hoàn trả";

            case 8:
                return "Đơn đổi trả tạm";
            default:
                break;
        }
        return "";
    }



}
