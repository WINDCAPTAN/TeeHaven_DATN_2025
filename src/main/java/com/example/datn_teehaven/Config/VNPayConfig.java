package com.example.datn_teehaven.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VNPayConfig {
    @Value("${vnp.payUrl}")
    private String payUrl;

    @Value("${vnp.returnUrl}")
    private String returnUrl;

    @Value("${vnp.tmnCode}")
    private String tmnCode;

    @Value("${vnp.secretKey}")
    private String secretKey;

    @Value("${vnp.version}")
    private String version;

    public String getPayUrl() {
        return payUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public String getTmnCode() {
        return tmnCode;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getVersion() {
        return version;
    }
} 