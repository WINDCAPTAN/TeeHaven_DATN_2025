package com.example.datn_teehaven.entyti;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "kich_co")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KichCo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ten")
    @NotBlank(message = "Tên kích cỡ không được để trống")
    @Size(max = 50, message = "Tên kích cỡ không được vượt quá 50 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "Tên kích cỡ không được chứa ký tự đặc biệt")
    private String ten;

    @Column(name = "ngay_tao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_sua")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngaySua;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @PrePersist
    protected void onCreate() {
        ngayTao = new Date();
        ngaySua = new Date();
        if (trangThai == null) {
            trangThai = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ngaySua = new Date();
    }
}
