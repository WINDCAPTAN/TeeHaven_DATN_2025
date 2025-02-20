package com.example.datn_teehaven.entyti;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gio_hang")
@Builder
public class GioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_gio_hang", length = 20)
    private String maGioHang;

    @Column(name = "ghi_chu", length = 255)
    private String ghiChu;

    @Column(name = "ngay_tao")
    private Date ngayTao;

    @Column(name = "ngay_sua")
    private Date ngaySua;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @OneToOne
    @JoinColumn(name = "nguoi_so_huu", referencedColumnName = "id")
    private TaiKhoan taiKhoan;


}
