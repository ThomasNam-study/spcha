package kr.purred.sp2.spcha;

import kr.purred.sp2.spcha.enums.UserStatus;
import kr.purred.sp2.spcha.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@Import (value=TestJobConfig.class)
class InactiveUserJobTest
{
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private UserRepository userRepository;

	@Test
	void changeInactiveTest () throws Exception
	{
		JobExecution jobExecution = jobLauncherTestUtils.launchJob ();

		assertThat (jobExecution.getStatus ()).isEqualTo (BatchStatus.COMPLETED);
		assertThat (userRepository.findByUpdatedDateBeforeAndStatusEquals (LocalDateTime.now ().minusYears (1), UserStatus.ACTIVE).size ()).isEqualTo (0);
	}
}
