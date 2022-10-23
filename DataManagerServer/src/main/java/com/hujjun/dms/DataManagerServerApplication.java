package com.hujjun.dms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @AUTHOR hujjun
 */
@SpringBootApplication
@MapperScan("com.hujjun.dms.*.mapper")
public class DataManagerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataManagerServerApplication.class, args);
    }

}
