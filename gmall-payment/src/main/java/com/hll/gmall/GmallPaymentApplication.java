package com.hll.gmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.hll.gmall.payment.service.mapper")
public class GmallPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallPaymentApplication.class, args);
    }

}
