package com.example.backend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class BackendApplication {

	private static final Logger log = LoggerFactory.getLogger(BackendApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Component
	static class PropertiesLoggerRunner implements ApplicationRunner {

		private static final Logger runnerLog = LoggerFactory.getLogger(PropertiesLoggerRunner.class);

		@Value("${spring.jpa.hibernate.ddl-auto:not-set}")
		private String ddlAutoValue;

		@Value("${spring.profiles.active:none}")
		private String activeProfiles;

		@Override
		public void run(ApplicationArguments args) throws Exception {
			runnerLog.warn("----------------------------------------------------------");
			runnerLog.warn("VERIFICANDO PROPRIEDADES EFETIVAS (via ApplicationRunner):");
			runnerLog.warn("O valor REALMENTE USADO para 'spring.jpa.hibernate.ddl-auto' é: '{}'", ddlAutoValue);
			runnerLog.warn("Perfis ativos ('spring.profiles.active'): '{}'", activeProfiles);
			runnerLog.warn("----------------------------------------------------------");
		}
	}

}