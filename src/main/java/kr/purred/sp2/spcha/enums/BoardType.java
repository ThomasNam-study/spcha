package kr.purred.sp2.spcha.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BoardType
{
	notice("공지사항"),

	free("자유게시판"),

	;

	private final String value;
}
