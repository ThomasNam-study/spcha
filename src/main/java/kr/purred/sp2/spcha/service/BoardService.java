package kr.purred.sp2.spcha.service;

import kr.purred.sp2.spcha.domains.Board;
import kr.purred.sp2.spcha.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService
{
	private final BoardRepository boardRepository;

	public Page<Board> findBoardList (Pageable pageable)
	{
		pageable = PageRequest.of (pageable.getPageNumber () <= 0 ? 0 : pageable.getPageNumber () - 1, pageable.getPageSize (), Sort.by (Sort.Direction.DESC, "createdDate"));

		return boardRepository.findAll (pageable);
	}

	public Board findBoardByIdx (Long idx)
	{
		return boardRepository.findById (idx).orElse (new Board ());
	}
}
