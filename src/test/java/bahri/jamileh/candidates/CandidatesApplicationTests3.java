package bahri.jamileh.candidates;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;


@RunWith(SpringRunner.class)
@TestPropertySource(value = "/application3.properties")
@SpringBootApplication

public class CandidatesApplicationTests3 {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CandidatesApplicationTests3.class);

    public static void main(String[] args) {

        SpringApplication.run(CandidatesApplicationTests3.class, args);

        LOGGER.info("Candidate 3 Wait To Received Genesis And Votes From BallotBox  . . . . ");
    }



}