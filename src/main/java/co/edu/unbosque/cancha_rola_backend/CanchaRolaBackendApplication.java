package co.edu.unbosque.cancha_rola_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "co.edu.unbosque")
@EntityScan("co.edu.unbosque.entity")
@EnableJpaRepositories("co.edu.unbosque.repository")
public class CanchaRolaBackendApplication {
    public static void main(String[] args) {
		SpringApplication.run(CanchaRolaBackendApplication.class, args);
	}
}
