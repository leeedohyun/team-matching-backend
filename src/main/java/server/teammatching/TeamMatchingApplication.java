package server.teammatching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TeamMatchingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamMatchingApplication.class, args);
	}

}
