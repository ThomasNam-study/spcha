package kr.purred.sp2.spcha.repository;

import kr.purred.sp2.spcha.domains.Board;
import kr.purred.sp2.spcha.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>
{
	Board findByUser (User user);
}
