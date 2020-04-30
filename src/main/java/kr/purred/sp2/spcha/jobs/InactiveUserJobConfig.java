package kr.purred.sp2.spcha.jobs;

import kr.purred.sp2.spcha.domains.User;
import kr.purred.sp2.spcha.enums.UserStatus;
import kr.purred.sp2.spcha.jobs.reader.QueueItemReader;
import kr.purred.sp2.spcha.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class InactiveUserJobConfig
{
	private final UserRepository userRepository;

	@Bean
	public Job inactiveUserJob (JobBuilderFactory jobBuilderFactory, Step inactiveJobStep)
	{
		return jobBuilderFactory.get ("inactiveUserJob")
				.preventRestart ()      // 재실행을 막는다.
				.start (inactiveJobStep)
				.build ();
	}

	@Bean
	public Step inactiveJobStep (StepBuilderFactory stepBuilderFactory)
	{
		return stepBuilderFactory.get ("inactiveUserStep").<User, User> chunk (10)
				.reader (inactiveUserReader ())
				.processor (inactiveUserProcessor ())
				.writer (inactiveUserWriter ())
				.build ();
	}


	@Bean
	@StepScope
	public QueueItemReader<User> inactiveUserReader ()
	{
		// StepScope 해당 메서드는 Step 의 주기에 따라 새로운 빈을 생성
		// 즉 각 Step 의 실행마다 새로 빈을 만들기 때문에 지연 생성이 가능하다.
		List<User> oldUsers = userRepository.findByUpdatedDateBeforeAndStatusEquals (LocalDateTime.now ().minusYears (1), UserStatus.ACTIVE);

		return new QueueItemReader<> (oldUsers);
	}

	public ItemProcessor<User, User> inactiveUserProcessor ()
	{
		return User::setInactive;
	}

	private ItemWriter<User> inactiveUserWriter ()
	{
		return (userRepository::saveAll);
	}
}
