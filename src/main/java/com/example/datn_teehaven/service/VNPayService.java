package com.example.datn_teehaven.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService {

    private final String vnp_TmnCode = "G5TCSXGB";
    private final String vnp_HashSecret = "UKAMKZ26L3FVBYZWMO368LNB52TU03OV";
    private final String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private final String vnp_ReturnUrl = "http://localhost:8080/vnpay-payment-return";
    private final String vnp_Version = "2.1.0";
    private final String vnp_Command = "pay";

    public String createOrder(int amount, String orderInfo, String baseUrl, String bankCode, 
                            String voucherId, String tienShip, String tienGiam) {
        try {
            String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
            String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String vnp_IpAddr = "127.0.0.1";

            // Build detailed order info with voucher and shipping
            StringBuilder detailedOrderInfo = new StringBuilder(orderInfo);
            if (voucherId != null && !voucherId.isEmpty()) {
                detailedOrderInfo.append("|Voucher:").append(voucherId);
            }
            if (tienShip != null && !tienShip.isEmpty()) {
                detailedOrderInfo.append("|Ship:").append(tienShip);
            }
            if (tienGiam != null && !tienGiam.isEmpty()) {
                detailedOrderInfo.append("|Giam:").append(tienGiam);
            }

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", detailedOrderInfo.toString());
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            if (bankCode != null && !bankCode.isEmpty()) {
                vnp_Params.put("vnp_BankCode", bankCode);
            }

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }

            String queryUrl = query.toString();
            String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = vnp_Url + "?" + queryUrl;
            
            return paymentUrl;
        } catch (Exception e) {
//            logger.error("Error creating VNPay order", e);
            return null;
        }
    }

    public int orderReturn(HttpServletRequest request) {
        try {
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if (fieldValue != null && fieldValue.length() > 0) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
            
            // Remove hash from fields
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }

            // Check checksum
            List<String> fieldNames = new ArrayList<>(fields.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = fields.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        hashData.append('&');
                    }
                }
            }

            String checkSum = hmacSHA512(vnp_HashSecret, hashData.toString());
            
            // Debug information
            System.out.println("Raw hash data: " + hashData.toString());
            System.out.println("Calculated hash: " + checkSum);
            System.out.println("Received hash: " + vnp_SecureHash);

            if (checkSum.equals(vnp_SecureHash)) {
                if ("00".equals(vnp_ResponseCode)) {
                    return 1; // Thành công
                } else {
                    return 0; // Thất bại
                }
            } else {
                return -1; // Sai chữ ký
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Map<String, String> extractReturnFields(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                fields.put(fieldName, fieldValue);
            }
        }
        return fields;
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac.init(secretKeySpec);
        byte[] hmacData = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hmacData) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
