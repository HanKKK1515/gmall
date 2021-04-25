package com.hll.gmall.payment.conf;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:alipay.properties")
public class AlipayConfig {
    @Value("${app_id}")
    private String appId;
    @Value("${merchant_private_key}")
    private String merchantPrivateKey;

    @Value("${gateway_host}")
    private String gatewayHost;

    public final static String PROTOCOL = "https";
    public final static String SIGN_TYPE = "RSA2";

    public static String alipayPublicKey;
    public static String notifyUrl;
    public static String returnUrl;

    @Value("${alipay_public_key}")
    public void setAlipayPublicKey(String alipayPublicKey) {
        AlipayConfig.alipayPublicKey = alipayPublicKey;
    }

    @Value("${notify_url}")
    public void setNotifyUrl(String notifyUrl) {
        AlipayConfig.notifyUrl = notifyUrl;
    }

    @Value("${return_url}")
    public void setReturnUrl(String returnUrl) {
        AlipayConfig.returnUrl = returnUrl;
    }

    @Bean
    public Config getConfig() {
        Config config = new Config();
        config.appId = appId;
        config.merchantPrivateKey = merchantPrivateKey;
        // 采用非证书模式
        config.alipayPublicKey = alipayPublicKey;

        config.gatewayHost = gatewayHost;
        config.protocol = PROTOCOL;
        config.signType = SIGN_TYPE;

        config.notifyUrl = notifyUrl;
        // Factory全局只需配置一次
        Factory.setOptions(config);

        // 注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
        // config.merchantCertPath = "<-- 请填写您的应用公钥证书文件路径，例如：/foo/appCertPublicKey_2019051064521003.crt -->";
        // config.alipayCertPath = "<-- 请填写您的支付宝公钥证书文件路径，例如：/foo/alipayCertPublicKey_RSA2.crt -->";
        // config.alipayRootCertPath = "<-- 请填写您的支付宝根证书文件路径，例如：/foo/alipayRootCert.crt -->";
        return config;
    }
}