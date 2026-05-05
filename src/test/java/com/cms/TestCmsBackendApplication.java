package com.cms;

import org.springframework.boot.SpringApplication;

public class TestCmsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(CmsBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
