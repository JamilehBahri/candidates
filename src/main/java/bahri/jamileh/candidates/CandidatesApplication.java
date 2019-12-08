package bahri.jamileh.candidates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CandidatesApplication {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CandidatesApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(CandidatesApplication.class, args);

        LOGGER.info("Candidate Wait To Received Genesis And Votes . . . . . ");
    }

}
