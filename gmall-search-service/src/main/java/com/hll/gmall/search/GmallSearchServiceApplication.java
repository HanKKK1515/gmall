package com.hll.gmall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.hll.gmall.search.mapper")
public class GmallSearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallSearchServiceApplication.class, args);
    }

}
