package bahri.jamileh.candidates;


import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@TestPropertySource(value = "/application1.properties")
@SpringBootApplication

public class CandidatesApplicationTests1 {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CandidatesApplicationTests1.class);



    public static void main(String[] args) {

        SpringApplication.run(CandidatesApplicationTests1.class, args);

        LOGGER.info("Candidate 1 Wait To Received Genesis And Votes From BallotBox  . . . . ");
    }



}
