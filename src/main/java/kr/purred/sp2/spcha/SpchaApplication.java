package kr.purred.sp2.spcha;

import kr.purred.sp2.spcha.domains.Board;
import kr.purred.sp2.spcha.domains.User;
import kr.purred.sp2.spcha.enums.BoardType;
import kr.purred.sp2.spcha.repository.BoardRepository;
import kr.purred.sp2.spcha.repository.UserRepository;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootApplication
@EnableBatchProcessing
public class SpchaApplication
{

    public static void main (String[] args)
    {
        SpringApplication.run (SpchaApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(UserRepository userRepository, BoardRepository boardRepository)
    {
        return (args) -> {
            User user = userRepository.save (User.builder ().name ("havi").password ("test").email ("purred@gmail.com").createdDate (LocalDateTime.now ()).build ());

            IntStream.rangeClosed (1, 200).forEach ((index) -> {
                boardRepository.save (Board.builder ()
                        .title ("게시글" + index)
                        .subTitle ("순서" + index)
                        .content ("내용")
                        .boardType (BoardType.free)
                        .createdDate (LocalDateTime.now ())
                        .updatedDate (LocalDateTime.now ())
                        .user (user)
                        .build ());
            });
        };
    }
}
