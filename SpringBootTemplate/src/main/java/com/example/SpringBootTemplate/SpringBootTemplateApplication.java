package com.example.SpringBootTemplate;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootTemplateApplication {

	public static void main(String[] args) {
		// java-dotenv 라이브러리를 사용해 .env 파일을 로드하기 (로컬환경에서만)
		// .env 파일 로드
		Dotenv dotenv = Dotenv.configure()
				.directory("./") // .env 파일 경로 설정 (기본: 프로젝트 루트)
				.load();

		// 환경변수를 시스템 프로퍼티에 추가
		dotenv.entries().forEach(entry -> {
			System.out.println(entry.getKey() + "=" + entry.getValue());
			System.setProperty(entry.getKey(), entry.getValue());
		});


		SpringApplication.run(SpringBootTemplateApplication.class, args);
	}

}
