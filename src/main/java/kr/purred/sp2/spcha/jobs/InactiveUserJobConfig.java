package kr.purred.sp2.spcha.jobs;

import kr.purred.sp2.spcha.domains.User;
import kr.purred.sp2.spcha.enums.UserStatus;
import kr.purred.sp2.spcha.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class InactiveUserJobConfig
{
	private final UserRepository userRepository;

	private final static int CHUNK_SIZE = 15;

	private final EntityManagerFactory entityManagerFactory;

	@Bean
	public Job inactiveUserJob (JobBuilderFactory jobBuilderFactory, Step inactiveJobStep)
	{
		return jobBuilderFactory.get ("inactiveUserJob")
				.preventRestart ()      // 재실행을 막는다.
				.start (inactiveJobStep)
				.build ();
	}

	@Bean
	public Step inactiveJobStep (StepBuilderFactory stepBuilderFactory, ListItemReader<User> inactiveUserReader)
	{
		return stepBuilderFactory.get ("inactiveUserStep").<User, User> chunk (CHUNK_SIZE)
				// .reader (inactiveUserJpaReader)
				.reader (inactiveUserReader)
				.processor (inactiveUserProcessor ())
				.writer (inactiveUserWriter ())
				.build ();
	}

	@Bean(destroyMethod = "")
	@StepScope
	public JpaPagingItemReader<User> inactiveUserJpaReader ()
	{
		JpaPagingItemReader<User> jpaPagingItemReader = new JpaPagingItemReader<User> () {
			@Override
			public int getPage ()
			{
				return 0;
			}
		};

		jpaPagingItemReader.setQueryString ("select u from User as u where u.updatedDate < :updatedDate and u.status = :status");

		Map<String, Object> map = new HashMap<> ();

		LocalDateTime now = LocalDateTime.now ();

		map.put ("updatedDate", now.minusYears (1));
		map.put ("status", UserStatus.ACTIVE);

		jpaPagingItemReader.setParameterValues (map);
		jpaPagingItemReader.setEntityManagerFactory (entityManagerFactory);
		jpaPagingItemReader.setPageSize (CHUNK_SIZE);

		return jpaPagingItemReader;
	}


	@Bean
	@StepScope
	public ListItemReader<User> inactiveUserReader (@Value("#{jobParameters[nowDate]}") Date nowDate)
	{
		LocalDateTime now = LocalDateTime.ofInstant (nowDate.toInstant (), ZoneId.systemDefault ());

		// StepScope 해당 메서드는 Step 의 주기에 따라 새로운 빈을 생성
		// 즉 각 Step 의 실행마다 새로 빈을 만들기 때문에 지연 생성이 가능하다.
		List<User> oldUsers = userRepository.findByUpdatedDateBeforeAndStatusEquals (now.minusYears (1), UserStatus.ACTIVE);

		return new ListItemReader<> (oldUsers);
	}

	public ItemProcessor<User, User> inactiveUserProcessor ()
	{
		return User::setInactive;
	}

	private JpaItemWriter<User> inactiveUserWriter ()
	{
		JpaItemWriter<User> jpaItemWriter = new JpaItemWriter<> ();

		jpaItemWriter.setEntityManagerFactory (entityManagerFactory);

		return jpaItemWriter;
	}

	/*private ItemWriter<User> inactiveUserWriter ()
	{
		return (userRepository::saveAll);
	}*/
}
