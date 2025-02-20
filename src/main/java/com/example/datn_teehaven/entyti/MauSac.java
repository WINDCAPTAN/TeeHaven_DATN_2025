package com.example.datn_teehaven.entyti;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "mau_sac")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MauSac {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ma_mau", length = 30)
    private String maMau;

    @Column(name = "ten", length = 100)
    @NotBlank(message = "Không được trống tên")
    private String ten;

    @Column(name = "ngay_tao")
    private Date ngayTao;

    @Column(name = "ngay_sua")
    private Date ngaySua;

    @Column(name = "trang_thai")
    private Integer trangThai;


}
