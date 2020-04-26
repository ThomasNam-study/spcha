package kr.purred.sp2.spcha.domains;

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
	private LocalDateTime createdDate;

	@Column
	private LocalDateTime updatedDate;
}
