package com.example.datn_teehaven.repository;

import com.example.datn_teehaven.entyti.PhuongThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhuongThucThanhToanRepository extends JpaRepository<PhuongThucThanhToan, Long> {

    Optional<PhuongThucThanhToan> findByMaPhuongThuc(String maPhuongThuc);

    Optional<PhuongThucThanhToan> findByTen(String ten);

    boolean existsById(Long id);

    Optional<PhuongThucThanhToan> findById(Long id);
}
