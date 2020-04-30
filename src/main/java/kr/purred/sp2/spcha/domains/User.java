package kr.purred.sp2.spcha.domains;

import kr.purred.sp2.spcha.enums.Grade;
import kr.purred.sp2.spcha.enums.SocialType;
import kr.purred.sp2.spcha.enums.UserStatus;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"idx", "email"})
public class User implements Serializable
{
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;

	@Column
	private String name;

	@Column
	private String password;

	@Column
	private String email;

	@Column
	private String principal;

	@Column
	@Enumerated(EnumType.STRING)
	private SocialType socialType;

	@Column
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@Column
	@Enumerated(EnumType.STRING)
	private Grade grade;

	@Column
	private LocalDateTime createdDate;

	@Column
	private LocalDateTime updatedDate;

	public User setInactive ()
	{
		status = UserStatus.INACTIVE;
		return this;
	}
}
