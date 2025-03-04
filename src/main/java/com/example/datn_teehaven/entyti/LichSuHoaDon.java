package com.example.datn_teehaven.entyti;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "lich_su_hoa_don")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LichSuHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ngay_tao")
    @DateTimeFormat(pattern = "HH:mm dd/MM/yyyy")
    private Date ngayTao;

//    @Column(name = "ngay_sua")
//    @DateTimeFormat(pattern = "HH:mm dd/MM/yyyy")
//    private Date ngaySua;

    @Column(name = "ghi_chu", length = 255)
    private String ghiChu;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hoa_don_id", referencedColumnName = "id")
    private HoaDon hoaDon;

//    private String nguoiSua;
}
