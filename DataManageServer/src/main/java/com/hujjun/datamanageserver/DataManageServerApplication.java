package com.hujjun.datamanageserver;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hujjun
 */
@SpringBootApplication
@MapperScan("com.hujjun.datamanageserver.mapper")
public class DataManageServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataManageServerApplication.class, args);
    }

}
