package kr.purred.sp2.spcha.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType
{
	FACEBOOK("facebook"),

	GOOGLE("google"),

	KAKAO("kakao"),

	;

	private final String ROLE_PREFIX = "ROLE_";

	private final String name;

	public String getRoleType ()
	{
		return ROLE_PREFIX + name.toUpperCase ();
	}

	public boolean isEquals (String authority)
	{
		return this.getRoleType ().equals (authority);
	}
}
