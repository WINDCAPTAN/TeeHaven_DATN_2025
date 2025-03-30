package com.example.datn_teehaven.repository;



import com.example.datn_teehaven.entyti.ChiTietSanPham;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Long> {

    @Query(value = "select * from chi_tiet_san_pham where trang_thai = 0", nativeQuery = true)
    List<ChiTietSanPham> fillAllDangHoatDong();

    @Query(value = "WITH CTE AS (\n" +
            "    SELECT\n" +
            "        id,\n" +
            "        san_pham_id,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY san_pham_id ORDER BY id DESC) AS rn\n" +
            "    FROM chi_tiet_san_pham\n" +
            "    WHERE trang_thai = 1\n" +
            ")\n" +
            "SELECT \n" +
            "    cts.id,\n" +
            "    cts.so_luong,\n" +
            "    cts.gia_hien_hanh,\n" +
            "    cts.trang_thai,\n" +
            "    cts.san_pham_id,\n" +
//            "    cts.hinh_anh,\n" +
            "    cts.kich_co_id,\n" +
            "    cts.mau_sac_id,\n" +
            "    cts.tay_ao_id,\n" +
            "    cts.ngay_tao,\n" +
            "    cts.ngay_sua\n" +
            "FROM chi_tiet_san_pham cts\n" +
            "JOIN CTE ON cts.id = CTE.id\n" +
            "WHERE CTE.rn = 1  ORDER BY cts.id DESC;", nativeQuery = true)
    List<ChiTietSanPham> fillAllNgungHoatDong();

    @Query(value = "WITH CTE AS (\n" +
            "    SELECT\n" +
            "        id,\n" +
            "        san_pham_id,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY san_pham_id ORDER BY id DESC) AS rn\n" +
            "    FROM chi_tiet_san_pham\n" +
            "    WHERE trang_thai = 0\n" +
            ")\n" +
            "SELECT \n" +
            "    cts.id,\n" +
            "    cts.so_luong,\n" +
            "    cts.gia_hien_hanh,\n" +
            "    cts.trang_thai,\n" +
            "    cts.san_pham_id,\n" +
            "    cts.kich_co_id,\n" +
//            "    cts.hinh_anh,\n" +
            "    cts.mau_sac_id,\n" +
            "    cts.tay_ao_id,\n" +
            "    cts.ngay_tao,\n" +
            "    cts.ngay_sua\n" +
            "FROM chi_tiet_san_pham cts\n" +
            "JOIN CTE ON cts.id = CTE.id\n" +
            "WHERE CTE.rn = 1 ORDER BY cts.id DESC;", nativeQuery = true)
    List<ChiTietSanPham> fillAllCtspOneSanPham();


    @Query(value = "SELECT cts.*\n" +
            "FROM (\n" +
            "    SELECT *,\n" +
            "           ROW_NUMBER() OVER (PARTITION BY san_pham_id, mau_sac_id ORDER BY mau_sac_id) AS RowAsc\n"
            +
            "    FROM chi_tiet_san_pham\n" +
            "    WHERE san_pham_id = :id\n" +
            ") cts\n" +
            "WHERE cts.RowAsc = 1  AND cts.trang_thai = 0;", nativeQuery = true)
    List<ChiTietSanPham> fillAllChiTietSpShop(@Param("id") Long id);

    @Query(value = "select * from chi_tiet_san_pham where san_pham_id = :idSanPham and mau_sac_id = :idMauSac and trang_thai = 0 ORDER BY kich_co_id ASC, id ASC;\n", nativeQuery = true)
    List<ChiTietSanPham> fillAllChiTietSpMauSac(@Param("idSanPham") Long idSanPham,
                                                @Param("idMauSac") Long idMauSac);

    @Query(value = "SELECT *\n" +
            "FROM chi_tiet_san_pham\n" +
            "WHERE san_pham_id = :idSanPham\n" +
            "ORDER BY mau_sac_id, kich_co_id", nativeQuery = true)
    List<ChiTietSanPham> fillAllChiTietSpBySanPham(@Param("idSanPham") Long idSanPham);

    @Query(value = "select * from chi_tiet_san_pham where trang_thai = 0 and so_luong>0", nativeQuery = true)
    List<ChiTietSanPham> fillAllDangHoatDongLonHon0();

    @Query(value = "SELECT p FROM ChiTietSanPham p WHERE p.trangThai = 0 AND p.sanPham.ten LIKE CONCAT('%',:tenSanPham,'%') \n"
            +
            "AND p.mauSac.id in (:tenMauSac) \n" +
            "AND p.kichCo.id in (:idKichCo) \n" +
            "AND p.tayAo.id in (:idTayAo) \n" +
            "AND p.sanPham.thuongHieu.id in (:idThuongHieu) \n" +
            "AND p.giaHienHanh BETWEEN :minGia AND :maxGia")
    Page<List<ChiTietSanPham>> searchAll(Pageable pageable,
                                         @Param("tenSanPham") String tenSanPham,
                                         @Param("tenMauSac") List<Long> idMauSac,
                                         @Param("idKichCo") List<Long> idKichCo,
                                         @Param("idTayAo") List<Long> idTayAo,
                                         @Param("idThuongHieu") List<Long> idThuongHieu,
                                         @Param("minGia") Long minGia,
                                         @Param("maxGia") Long maxGia);

    @Query(value = "select DISTINCT chi_tiet_san_pham.mau_sac_id from chi_tiet_san_pham where trang_thai = 0", nativeQuery = true)
    List<Long> getAllIdMauSacCTSP();

    @Query(value = "select DISTINCT chi_tiet_san_pham.kich_co_id from chi_tiet_san_pham where trang_thai = 0", nativeQuery = true)
    List<Long> getAllIdKichCoCTSP();

    @Query(value = "select DISTINCT chi_tiet_san_pham.tay_ao_id from chi_tiet_san_pham where trang_thai = 0", nativeQuery = true)
    List<Long> getAllIdTayAoCTSP();

    @Query(value = "select min(gia_hien_hanh) from chi_tiet_san_pham where trang_thai = 0", nativeQuery = true)
    Long getAllMinGiaCTSP();

    @Query(value = "select max(gia_hien_hanh) from chi_tiet_san_pham where trang_thai = 0", nativeQuery = true)
    Long getAllMaxGiaCTSP();

    @Query(value = "select DISTINCT th.id,th.ten\n" +
            "from chi_tiet_san_pham c\n" +
            "JOIN san_pham s ON c.san_pham_id = s.id\n" +
            "JOIN thuong_hieu th ON s.thuong_hieu_id = th.id", nativeQuery = true)
    List<Long> getAllIdThuongHieuCTSP();

//    @Query("select ctsp.sanPham.ten,ctsp.kichCo.ten,ctsp.mauSac.ten,ctsp.tayAo.ten,ctsp.soLuong from ChiTietSanPham ctsp where ctsp.soLuong <= :soLuong and ctsp.trangThai = 0")
//    List<Object[]> danhSachHangSapHet(@Param("soLuong") Integer soLuong);

    @Transactional
    @Modifying
    @Query(value = "update chi_tiet_san_pham \r\n" + //
            "set trang_thai=1\r\n" + //
            "where so_luong =0", nativeQuery = true)
    void checkSoLuongBang0();

    @Query(value = "select * from chi_tiet_san_pham where san_pham_id = :idSanPham and mau_sac_id = :idMauSac and tay_ao_id = :idTayAo and kich_co_id = :idKichCo", nativeQuery = true)
    ChiTietSanPham findChiTietSanPham(@Param("idSanPham") Long idSanPham, @Param("idMauSac") Long idMauSac,
                                      @Param("idTayAo") Long idTayAo, @Param("idKichCo") Long idKichCo);

    @Query("SELECT COALESCE(SUM(c.soLuong), 0) FROM ChiTietSanPham c WHERE c.sanPham.id = :sanPhamId")
    Integer sumSoLuongTonBySanPhamId(@Param("sanPhamId") Long sanPhamId);

    @Query(value = "SELECT * FROM chi_tiet_san_pham WHERE trang_thai = 0 ORDER BY ngay_tao DESC LIMIT 5", nativeQuery = true)
    List<ChiTietSanPham> findTop5NewestProducts();

}
