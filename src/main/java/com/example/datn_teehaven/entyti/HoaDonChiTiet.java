package com.example.datn_teehaven.entyti;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "hoa_don_chi_tiet")
public class HoaDonChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "don_gia")
    private Long donGia;

    @Column(name = "ghi_chu", length = 255)
    private String ghiChu;

    @Column(name = "ngay_tao")
    private Date ngayTao;

//    @Column(name = "ngay_sua")
//    private Date ngaySua;

    @Column(name = "nguoi_tao", length = 100)
    private String nguoiTao;

//    @Column(name = "nguoi_sua", length = 100)
//    private String nguoiSua;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hoa_don_id", referencedColumnName = "id")
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chi_tiet_san_pham_id", referencedColumnName = "id")
    private ChiTietSanPham chiTietSanPham;

    public Long tongTien(){
        return this.donGia*this.soLuong;
    }
}
