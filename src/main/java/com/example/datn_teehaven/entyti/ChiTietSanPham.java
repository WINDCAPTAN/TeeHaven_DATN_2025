package com.example.datn_teehaven.entyti;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Entity
@Table(name = "chi_tiet_san_pham")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChiTietSanPham {
    // phong dep trai
    // dung khong dep trai
    // huynh dep trai
    // duong xau trai nhat
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "so_luong")
    @NotNull(message = "Số lượng không được trống.")
    @Min(value = 0, message = "Số lượng nhỏ nhất là 0")
    @Max(value = 99999, message = "Số lượng lớn nhất là 99999")
    private Integer soLuong;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "gia_hien_hanh")
    @NotNull(message = "Giá không được trống.")
    @Min(value = 1, message = "Giá  nhỏ nhất là 1")
    @Max(value = 1000000000, message = "Giá lớn nhất là 1000000000")
    private Long giaHienHanh;

    @Column(name = "ngay_tao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayTao;

    @Column(name = "ngay_sua")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngaySua;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "san_pham_id", referencedColumnName = "id")
    private SanPham sanPham;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kich_co_id", referencedColumnName = "id")
    private KichCo kichCo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mau_sac_id", referencedColumnName = "id")
    private MauSac mauSac;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tay_ao_id", referencedColumnName = "id")
    private TayAo tayAo;

//    @Column(name = "hinh_anh")
//    private String hinhAnh;

}
