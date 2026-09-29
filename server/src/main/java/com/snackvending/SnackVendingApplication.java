package com.snackvending;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SnackVendingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SnackVendingApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  零食自助售货系统启动成功!");
        System.out.println("  访问地址: http://localhost:8080");
        System.out.println("  API 文档: http://localhost:8080/swagger-ui.html");
        System.out.println("  H2 控制台: http://localhost:8080/h2-console");
        System.out.println("========================================\n");
    }
}
