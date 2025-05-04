package com.example.datn_teehaven.controller;

import com.example.datn_teehaven.entyti.HoaDon;
import com.example.datn_teehaven.service.VNPayService;
import com.example.datn_teehaven.service.GioHangChiTietService;
import com.example.datn_teehaven.service.HoaDonService;
import com.example.datn_teehaven.service.TaiKhoanService;
import com.example.datn_teehaven.Config.PrincipalCustom;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class VNPayController {
    private static final Logger logger = LoggerFactory.getLogger(VNPayController.class);

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private GioHangChiTietService gioHangChiTietService;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    private PrincipalCustom principalCustom = new PrincipalCustom();

    @GetMapping("/vnpay-payment")
    public String submitOrder(@RequestParam("amount") int amount,
                              @RequestParam("orderInfo") String orderInfo,
                              @RequestParam(value = "bankCode", required = false) String bankCode,
                              @RequestParam(value = "voucherId", required = false) String voucherId,
                              @RequestParam(value = "tienShip", required = false) String tienShip,
                              @RequestParam(value = "tienGiam", required = false) String tienGiam,
                              @RequestParam(value = "selectedItems", required = false) String selectedItems,
                              HttpServletRequest request) {
        try {
            logger.info("Initiating VNPay payment - Amount: {}, OrderInfo: {}, BankCode: {}, VoucherId: {}, TienShip: {}, TienGiam: {}, SelectedItems: {}",
                    amount, orderInfo, bankCode, voucherId, tienShip, tienGiam, selectedItems);

            // Xử lý cộng phí ship vào amount
            int shipFee = 0;
            if (tienShip != null && !tienShip.isEmpty()) {
                try {
                    shipFee = Integer.parseInt(tienShip);
                } catch (NumberFormatException e) {
                    logger.warn("Invalid tienShip format: {}", tienShip);
                }
            }
            amount += shipFee;

            // Lưu thông tin vào session
            request.getSession().setAttribute("voucherId", voucherId);
            request.getSession().setAttribute("tienShip", tienShip);
            request.getSession().setAttribute("tienGiam", tienGiam);
            request.getSession().setAttribute("selectedItems", selectedItems);

            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnPayService.createOrder(amount, orderInfo, baseUrl, bankCode, voucherId, tienShip, tienGiam);

            if (vnpayUrl != null) {
                logger.info("Redirecting to VNPay URL: {}", vnpayUrl);
                return "redirect:" + vnpayUrl;
            } else {
                logger.error("Failed to create VNPay payment URL");
                return "redirect:/checkout?error=payment_error";
            }
        } catch (Exception e) {
            logger.error("Error creating VNPay payment", e);
            return "redirect:/checkout?error=system_error";
        }
    }


    @GetMapping("/vnpay-payment-return")
    public String paymentReturn(HttpServletRequest request, Model model) {
        try {
            Map<String, String> fields = vnPayService.extractReturnFields(request);
            int paymentStatus = vnPayService.orderReturn(request);

            // Get transaction details
            String transactionId = fields.get("vnp_TxnRef");
            String amount = fields.get("vnp_Amount");
            String responseCode = fields.get("vnp_ResponseCode");

            logger.info("Payment return - TransactionId: {}, Amount: {}, ResponseCode: {}",
                    transactionId, amount, responseCode);

            model.addAttribute("transactionId", transactionId);
            model.addAttribute("amount", amount != null ? Long.parseLong(amount) : 0);

            String phuongThucThanhToanId = "2"; // Giá trị 2 cho VNPay

            if (paymentStatus == 1) {
                logger.info("Payment successful for transaction: {}", transactionId);

                // Get current user's cart items
                String username = principalCustom.getCurrentUserNameCustomer();
                if (username != null) {
                    // Get user's account
                    var taiKhoan = taiKhoanService.getTaiKhoanByName(username);
                    if (taiKhoan != null && taiKhoan.getGioHang() != null) {
                        // Get selected items from session
                        String selectedItemsStr = (String) request.getSession().getAttribute("selectedItems");
                        List<String> selectedItems = new ArrayList<>();

                        if (selectedItemsStr != null && !selectedItemsStr.isEmpty()) {
                            selectedItems = Arrays.asList(selectedItemsStr.split(","));
                            logger.info("Processing selected items: {}", selectedItems);
                        }

                        // Get user's address if available
                        String phuongXaID = "";
                        String quanHuyenID = "";
                        String thanhPhoID = "";
                        String diaChiCuThe = "";

                        if (taiKhoan.getLstDiaChi() != null && !taiKhoan.getLstDiaChi().isEmpty()) {
                            var diaChi = taiKhoan.getLstDiaChi().get(0); // Get first address
                            phuongXaID = diaChi.getPhuongXa();
                            quanHuyenID = diaChi.getQuanHuyen();
                            thanhPhoID = diaChi.getThanhPho();
                            diaChiCuThe = diaChi.getDiaChiCuThe();
                        }

                        // Get voucher and shipping information from session
                        String voucherId = (String) request.getSession().getAttribute("voucherId");
                        String tienShip = (String) request.getSession().getAttribute("tienShip");
                        String tienGiam = (String) request.getSession().getAttribute("tienGiam");

                        // Clear session after use
                        request.getSession().removeAttribute("voucherId");
                        request.getSession().removeAttribute("tienShip");
                        request.getSession().removeAttribute("tienGiam");
                        request.getSession().removeAttribute("selectedItems");

                        if (voucherId == null) voucherId = "";
                        if (tienShip == null) tienShip = "0";
                        if (tienGiam == null) tienGiam = "0";

                        logger.info("Creating order with voucherId: {}, tienShip: {}, tienGiam: {}, selectedItems: {}",
                                voucherId, tienShip, tienGiam, selectedItems);

                        // Calculate total amount for selected items
                        var cartItems = gioHangChiTietService.findAllById(selectedItems, taiKhoan.getGioHang().getId());
                        long tongTien = 0;
                        for (var item : cartItems) {
                            tongTien += item.tongTien();
                        }

                        // Create the order with the payment method
                        gioHangChiTietService.addHoaDon(
                                selectedItems,
                                tongTien,                                // Total amount of selected items
                                tongTien - Long.parseLong(tienGiam),     // Total after discount
                                taiKhoan.getHoVaTen(),                   // Full name
                                taiKhoan.getSoDienThoai(),               // Phone number
                                tienShip,                                // Shipping cost
                                tienGiam,                                // Discount
                                taiKhoan.getEmail(),                     // Email
                                voucherId,                               // Voucher code
                                diaChiCuThe,                             // Specific address
                                "Thanh toán qua VNPay",                  // Note (payment method)
                                taiKhoan,                                // User account
                                phuongXaID,                              // PhuongXaID
                                quanHuyenID,                             // QuanHuyenID
                                thanhPhoID,                              // ThanhPhoID
                                taiKhoan.getGioHang().getId(),           // Cart ID
                                phuongThucThanhToanId                    // Payment method ID
                        );

                        // Remove selected items from cart
                        for (var item : cartItems) {
                            gioHangChiTietService.deleteById(item.getId());
                        }
                    }
                }

                return "customer-template/payment-success";
            } else {
                // Handle payment failure
                String errorMessage = "Payment failed";
                model.addAttribute("paymentError", errorMessage);
                return "customer-template/payment-failed";
            }
        } catch (Exception e) {
            logger.error("Error processing payment return", e);
            model.addAttribute("paymentError", "Có lỗi xảy ra trong quá trình xử lý thanh toán");
            return "customer-template/payment-failed";
        }
    }

}
