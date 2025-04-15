package com.example.datn_teehaven.controller;

import com.example.datn_teehaven.service.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

@Controller
public class VNPayController {

    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("amount") long amount,
                            @RequestParam("orderInfo") String orderInfo,
                            @RequestParam("paymentMethod") String paymentMethod) throws UnsupportedEncodingException {
        if ("VNPAY".equals(paymentMethod)) {
            String paymentUrl = vnPayService.createPaymentUrl(amount, orderInfo);
            return "redirect:" + paymentUrl;
        } else {
            // Handle COD payment
            return "redirect:/thankyou";
        }
    }

    @GetMapping("/vnpay-payment")
    public String paymentResult(@RequestParam("vnp_ResponseCode") String responseCode) {
        if ("00".equals(responseCode)) {
            return "redirect:/thankyou";
        } else {
            return "redirect:/checkout?error=payment_failed";
        }
    }
} 