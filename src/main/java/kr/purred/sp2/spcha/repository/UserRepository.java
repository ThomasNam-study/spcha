package kr.purred.sp2.spcha.repository;

import kr.purred.sp2.spcha.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>
{
	User findByEmail (String email);
}
