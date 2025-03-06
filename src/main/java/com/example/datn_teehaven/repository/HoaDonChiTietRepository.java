package com.example.datn_teehaven.repository;

import com.example.datn_teehaven.entyti.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Long> {
    @Query("Select hdct from HoaDonChiTiet hdct where hdct.hoaDon.id=:idHoaDon")
    List<HoaDonChiTiet> findByIdHoaDon(@Param("idHoaDon") Long idHoaDon);

    @Query(value = "WITH RankedHoaDonChiTiet AS (\n" +
            "  SELECT\n" +
            "    h.*,\n" +
            "    ROW_NUMBER() OVER (PARTITION BY h.chi_tiet_san_pham_id ORDER BY h.so_luong DESC) AS RowNum\n" +
            "  FROM\n" +
            "    hoa_don_chi_tiet h\n" +
            "    INNER JOIN chi_tiet_san_pham c ON h.chi_tiet_san_pham_id = c.id\n" +
            "  WHERE\n" +
            "    h.trang_thai = 0\n" +
            "    AND h.hoa_don_id IN (SELECT hd.id FROM hoa_don hd WHERE hd.trang_thai = 3)\n" +
            "    AND c.trang_thai = 0\n" +
            ")\n" +
            ", FilteredResults AS (\n" +
            "  SELECT\n" +
            "    id,\n" +
            "\tso_luong,\n" +
            "\tdon_gia,\n" +
            "\tghi_chu,\n" +
            "\tngay_tao,\n" +
            "\tnguoi_tao,\n" +
//            "\tnguoi_sua,\n" +
            "    hoa_don_id,\n" +
            "    chi_tiet_san_pham_id,\n" +
            "    trang_thai\n" +
            "  FROM RankedHoaDonChiTiet\n" +
            "  WHERE RowNum = 1\n" +
            ")\n" +
            "SELECT TOP 5 *\n" +
            "FROM FilteredResults\n" +
            "ORDER BY so_luong DESC", nativeQuery = true)
    List<HoaDonChiTiet> fillAllIdHoaDonTrangThaiHoanThanh(@Param("listIdHoaDon") List<Long> listIdHoaDon);
}
