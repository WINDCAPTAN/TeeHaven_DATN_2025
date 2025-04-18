package com.example.datn_teehaven.controller;

import com.example.datn_teehaven.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Controller
public class VNPayController {
    private static final Logger logger = LoggerFactory.getLogger(VNPayController.class);

    @Autowired
    private VNPayService vnPayService;

    @GetMapping("/vnpay-payment")
    public String submitOrder(@RequestParam("amount") int amount,
                            @RequestParam("orderInfo") String orderInfo,
                            @RequestParam(value = "bankCode", required = false) String bankCode,
                            HttpServletRequest request) {
        try {
            logger.info("Initiating VNPay payment - Amount: {}, OrderInfo: {}, BankCode: {}", amount, orderInfo, bankCode);
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnPayService.createOrder(amount, orderInfo, baseUrl, bankCode);
            
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
            String bankCode = fields.get("vnp_BankCode");
            String bankTranNo = fields.get("vnp_BankTranNo");
            String payDate = fields.get("vnp_PayDate");
            String transactionNo = fields.get("vnp_TransactionNo");
            String responseCode = fields.get("vnp_ResponseCode");

            logger.info("Payment return - TransactionId: {}, Amount: {}, ResponseCode: {}", 
                       transactionId, amount, responseCode);

            // Add common attributes
            model.addAttribute("transactionId", transactionId);
            model.addAttribute("amount", amount != null ? Long.parseLong(amount) : 0);
            model.addAttribute("bankCode", bankCode);
            model.addAttribute("bankTranNo", bankTranNo);
            model.addAttribute("payDate", payDate);
            model.addAttribute("transactionNo", transactionNo);

            if (paymentStatus == 1) {
                logger.info("Payment successful for transaction: {}", transactionId);
                return "customer-template/payment-success";
            } else {
                // Payment failed
                String errorMessage;
                switch (responseCode) {
                    case "24":
                        errorMessage = "Giao dịch không thành công do: Khách hàng hủy giao dịch";
                        break;
                    case "09":
                        errorMessage = "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng";
                        break;
                    case "10":
                        errorMessage = "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
                        break;
                    case "11":
                        errorMessage = "Giao dịch không thành công do: Đã hết hạn chờ thanh toán";
                        break;
                    case "12":
                        errorMessage = "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa";
                        break;
                    case "13":
                        errorMessage = "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP)";
                        break;
                    case "51":
                        errorMessage = "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch";
                        break;
                    case "65":
                        errorMessage = "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày";
                        break;
                    case "75":
                        errorMessage = "Ngân hàng thanh toán đang bảo trì";
                        break;
                    case "79":
                        errorMessage = "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định";
                        break;
                    case "99":
                        errorMessage = "Các lỗi khác";
                        break;
                    default:
                        if (paymentStatus == -1) {
                            errorMessage = "Chữ ký không hợp lệ";
                        } else {
                            errorMessage = "Có lỗi xảy ra trong quá trình xử lý";
                        }
                }
                logger.error("Payment failed for transaction: {} with error: {}", transactionId, errorMessage);
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
