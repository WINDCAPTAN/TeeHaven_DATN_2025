package com.example.datn_teehaven.controller;

import com.example.datn_teehaven.entyti.*;
import com.example.datn_teehaven.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/ban-hang-tai-quay")
public class BanHangController {
// 123
    @Autowired
    HttpServletRequest request;

    @GetMapping("/hoa-don")
    public String home(Model model) {
        return "admin-template/ban-hang";
    }



}
