package kr.purred.sp2.spcha.jobs;

import kr.purred.sp2.spcha.domains.User;
import kr.purred.sp2.spcha.enums.UserStatus;
import kr.purred.sp2.spcha.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InactiveItemTasklet implements Tasklet
{
	private final UserRepository userRepository;

	@Override
	public RepeatStatus execute (StepContribution contribution, ChunkContext chunkContext) throws Exception
	{
		Date nowDate = (Date) chunkContext.getStepContext ().getJobParameters ().get ("nowDate");

		LocalDateTime now = LocalDateTime.ofInstant (nowDate.toInstant (), ZoneId.systemDefault ());

		List<User> oldUsers = userRepository.findByUpdatedDateBeforeAndStatusEquals (now.minusYears (1), UserStatus.ACTIVE);

		List<User> collect = oldUsers.stream ().map (User::setInactive).collect (Collectors.toList ());

		userRepository.saveAll (collect);

		return RepeatStatus.FINISHED;
	}
}
