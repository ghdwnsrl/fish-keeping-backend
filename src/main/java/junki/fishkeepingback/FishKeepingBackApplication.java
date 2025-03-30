package junki.fishkeepingback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class FishKeepingBackApplication {

    public static void main(String[] args) {
        log.info("GITHUB ACTIONS TEST");
        SpringApplication.run(FishKeepingBackApplication.class, args);
    }

}
