package com.hll.gmall.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GmallManagerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallManagerWebApplication.class, args);
    }

//    // 解决 FastJson @JsonField 注解不生效问题
//    @Bean
//    public HttpMessageConverters fastJsonHttpMessageConverters() {
//        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
//        fastConverter.setFastJsonConfig(fastJsonConfig);
//        HttpMessageConverter<?> converter = fastConverter;
//        return new HttpMessageConverters(converter);
//    }

}
