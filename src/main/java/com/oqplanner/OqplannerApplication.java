package com.oqplanner;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@MapperScan(basePackages="com.oqplanner.tripuser.mapper")
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class OqplannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OqplannerApplication.class, args);
	}

}
