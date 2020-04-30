package kr.purred.sp2.spcha.repository;

import kr.purred.sp2.spcha.domains.User;
import kr.purred.sp2.spcha.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>
{
	User findByEmail (String email);

	List<User> findByUpdatedDateBeforeAndStatusEquals (LocalDateTime localDateTime, UserStatus status);
}
