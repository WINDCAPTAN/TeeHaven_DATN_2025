package com.example.datn_teehaven.repository;

import com.example.datn_teehaven.entyti.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository

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
            "    hoa_don_id,\n" +
            "    chi_tiet_san_pham_id,\n" +
            "    trang_thai\n" +
            "  FROM RankedHoaDonChiTiet\n" +
            "  WHERE RowNum = 1\n" +
            ")\n" +
            "SELECT TOP 10 *\n" +
            "FROM FilteredResults\n" +
            "ORDER BY so_luong DESC", nativeQuery = true)
    List<HoaDonChiTiet> fillAllIdHoaDonTrangThaiHoanThanh(@Param("listIdHoaDon") List<Long> listIdHoaDon);

    @Query("select SUM(hd.soLuong) from HoaDonChiTiet hd where hd.trangThai=0 and (hd.hoaDon.trangThai =3 or hd.hoaDon.trangThai = 6) and CAST(hd.hoaDon.ngayTao AS DATE) = :ngayTao")
    Integer sumSanPhamHoaDonNgay(@Param("ngayTao") Date ngayTao);

    @Query("select COALESCE(SUM(hd.soLuong), 0) from HoaDonChiTiet hd where hd.trangThai=0 and (hd.hoaDon.trangThai =3 or hd.hoaDon.trangThai = 6) and CAST(hd.hoaDon.ngayTao AS DATE) BETWEEN :startDate AND :endDate")
    Integer sumSanPhamHoaDonBetween(@Param("startDate") Date startDate,
                                    @Param("endDate") Date endDate);

    @Query("SELECT CAST(hd.hoaDon.ngayTao AS DATE) AS ngay, \n" +
            "       SUM(DISTINCT hd.hoaDon.tongTienKhiGiam) AS sumHoaDon,\n" +
            "       COUNT(DISTINCT hd.hoaDon) AS countHoaDon,\n" +
            "       SUM(CASE WHEN hd.trangThai = 0 THEN hd.soLuong ELSE 0 END) AS sumSoLuong\n" +
            "FROM HoaDonChiTiet hd\n" +
            "WHERE (hd.hoaDon.trangThai = 3 OR hd.hoaDon.trangThai = 6) AND \n" +
            "      CAST(hd.hoaDon.ngayTao AS DATE) BETWEEN :startDateChart AND :endDateChart\n" +
            "GROUP BY CAST(hd.hoaDon.ngayTao AS DATE)\n")
    List<Object[]> thongKeSanPhamTheoNgay(
            @Param("startDateChart") Date startDateChart,
            @Param("endDateChart") Date endDateChart
    );


    @Query("SELECT h.chiTietSanPham.sanPham.ten, SUM(h.soLuong),SUM(h.donGia)" +
            "FROM HoaDonChiTiet h " +
            "WHERE MONTH(h.hoaDon.ngayTao) = MONTH(:ngayTao) and h.trangThai = 0 and (h.hoaDon.trangThai = 3 or h.hoaDon.trangThai = 6)" +
            "GROUP BY h.chiTietSanPham.sanPham.ten " +
            "ORDER BY SUM(h.soLuong) DESC ")
    List<Object[]> findByTongSoLuongThang(@Param("ngayTao") Date ngayTao);


    @Query("SELECT h.chiTietSanPham.sanPham.ten, SUM(h.soLuong),SUM(h.donGia)" +
            "FROM HoaDonChiTiet h " +
            "WHERE CAST(h.hoaDon.ngayTao AS DATE) = :ngayTao and h.trangThai = 0 and (h.hoaDon.trangThai = 3 or h.hoaDon.trangThai = 6) " +
            "GROUP BY h.chiTietSanPham.sanPham.ten " +
            "ORDER BY SUM(h.soLuong) DESC ")
    List<Object[]> findByTongSoLuongNgay(@Param("ngayTao") Date ngayTao);


    @Query("SELECT h.chiTietSanPham.sanPham.ten, SUM(h.soLuong),SUM(h.donGia)" +
            "FROM HoaDonChiTiet h " +
            "WHERE h.trangThai = 0 and (h.hoaDon.trangThai = 3 or h.hoaDon.trangThai = 6) and CAST(h.hoaDon.ngayTao AS DATE) BETWEEN :startDate AND :endDate " +
            "GROUP BY h.chiTietSanPham.sanPham.ten " +
            "ORDER BY SUM(h.soLuong) DESC ")
    List<Object[]> findByTongSoLuongBetween(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    @Query("select SUM(hd.soLuong) from HoaDonChiTiet hd where hd.trangThai=0 and (hd.hoaDon.trangThai = 3 or hd.hoaDon.trangThai = 6)")
    Integer sumSanPhamHoaDonAll();

    @Query("SELECT h.chiTietSanPham.sanPham.ten, SUM(h.soLuong),SUM(h.donGia)" +
            "FROM HoaDonChiTiet h " +
            "WHERE h.trangThai = 0 and (h.hoaDon.trangThai = 3 or h.hoaDon.trangThai = 6)" +
            "GROUP BY h.chiTietSanPham.sanPham.ten " +
            "ORDER BY SUM(h.soLuong) DESC ")
    List<Object[]> findByTongSoLuongAll();

    @Query("SELECT SUM(hd.soLuong) FROM HoaDonChiTiet hd WHERE hd.trangThai=0 and (hd.hoaDon.trangThai =3 or hd.hoaDon.trangThai = 6) and MONTH(hd.hoaDon.ngayTao) = MONTH(:ngayTao)")
    Integer sumSanPhamHoaDonThang(@Param("ngayTao") Date ngayTao);

    @Query(value = "WITH RankedSales AS (" +
            "    SELECT" +
            "        hdct.chi_tiet_san_pham_id," +
            "        SUM(hdct.so_luong) as total_sold," +
            "        ROW_NUMBER() OVER(ORDER BY SUM(hdct.so_luong) DESC) as sales_rank" +
            "    FROM" +
            "        hoa_don_chi_tiet hdct" +
            "    JOIN" +
            "        hoa_don hd ON hd.id = hdct.hoa_don_id" +
            "    JOIN" +
            "        chi_tiet_san_pham ctsp ON ctsp.id = hdct.chi_tiet_san_pham_id" +
            "    JOIN" +
            "        san_pham sp ON ctsp.san_pham_id = sp.id" +
            "    WHERE" +
            "        hd.trang_thai = 3" + // Consider if status 6 should also be included
            "        AND hdct.trang_thai = 0" +
            "        AND sp.trang_thai = 0" + // Ensure parent product is active
            "    GROUP BY" +
            "        hdct.chi_tiet_san_pham_id" +
            "), Top5Variants AS (" +
            "    SELECT chi_tiet_san_pham_id, total_sold" +
            "    FROM RankedSales" +
            "    WHERE sales_rank <= 10" + // Changed from 10 to 5
            "), RepresentativeHDCT AS (" +
            "    SELECT" +
            "        hdct.*," +
            "        ROW_NUMBER() OVER(PARTITION BY hdct.chi_tiet_san_pham_id ORDER BY hdct.id DESC) as rn" +
            "    FROM" +
            "        hoa_don_chi_tiet hdct" +
            "    JOIN" +
            "        Top5Variants t5 ON hdct.chi_tiet_san_pham_id = t5.chi_tiet_san_pham_id" +
            "    WHERE " +
            "        hdct.trang_thai = 0" + // Redundant check? No, ensure the chosen representative is active
            ")" +
            "SELECT" +
            "    r.id, r.so_luong, r.don_gia, r.ghi_chu, r.ngay_tao, r.nguoi_tao, r.hoa_don_id, r.chi_tiet_san_pham_id, r.trang_thai " +
            "FROM" +
            "    RepresentativeHDCT r " +
            "JOIN " +
            "    Top5Variants t5 ON r.chi_tiet_san_pham_id = t5.chi_tiet_san_pham_id " + // Join to order by total_sold
            "WHERE" +
            "    r.rn = 1 " +
            "ORDER BY" +
            "    t5.total_sold DESC",
            nativeQuery = true)
    List<HoaDonChiTiet> findTop5BanChay();

    @Query(value = "WITH RankedHoaDonChiTiet AS (" +
            "    SELECT" +
            "        hdct.*," +
            "        sp.ngay_tao AS product_ngay_tao," +
            "        ROW_NUMBER() OVER(PARTITION BY hdct.chi_tiet_san_pham_id ORDER BY sp.ngay_tao DESC, hdct.id DESC) as rn" +
            "    FROM" +
            "        hoa_don_chi_tiet hdct" +
            "    JOIN" +
            "        chi_tiet_san_pham ctsp ON hdct.chi_tiet_san_pham_id = ctsp.id" +
            "    JOIN" +
            "        san_pham sp ON ctsp.san_pham_id = sp.id" +
            "    WHERE" +
            "        hdct.trang_thai = 0" +
            "        AND sp.trang_thai = 0" + // Ensure parent product is active
            ")" +
            "SELECT TOP 10" +
            "    rhdct.id," +
            "    rhdct.so_luong," +
            "    rhdct.don_gia," +
            "    rhdct.ghi_chu," +
            "    rhdct.ngay_tao," +
            "    rhdct.nguoi_tao," +
            "    rhdct.hoa_don_id," +
            "    rhdct.chi_tiet_san_pham_id," +
            "    rhdct.trang_thai " +
            "FROM" +
            "    RankedHoaDonChiTiet rhdct " +
            "WHERE" +
            "    rhdct.rn = 1 " +
            "ORDER BY" +
            "    rhdct.product_ngay_tao DESC",
            nativeQuery = true)
    List<HoaDonChiTiet> findTop10SanPhamMoi();
}
