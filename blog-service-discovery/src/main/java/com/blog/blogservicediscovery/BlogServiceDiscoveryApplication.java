package com.blog.blogservicediscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class BlogServiceDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogServiceDiscoveryApplication.class, args);
	}

}
