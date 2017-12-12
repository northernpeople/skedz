package online.skedz.scheduler.core.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import online.skedz.scheduler.core.business.ServiceType;

@SuppressWarnings("serial")

@Setter
@Getter
@Accessors(chain=true)
@EqualsAndHashCode(of={"username"})
@Entity
public class User implements UserDetails{

	@Id
	private UUID id;

	@NotNull
	@Size(min=5, max=255)
	@Pattern(regexp = "(\\d|\\w|\\.)+\\@(\\d|\\w|\\.)+\\.(\\d|\\w)+",  message = "example: john.berg@gmail.com")
	private String username;
	
	@NotNull
	@Size(min=7, max=255)
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	private LocalDateTime emailVerified = LocalDateTime.MAX;
	private String verificationCode;
	private LocalDateTime createdDate;
	
	public void verifyEmail(LocalDateTime emailVerified) {
		this.emailVerified = emailVerified;
	}
	
	@PrePersist
	void init() {
		this.id = UUID.randomUUID();
		createdDate = LocalDateTime.now();
		verificationCode = UUID.randomUUID().toString();
	}
	
	@ManyToMany // owner
	private Set<ServiceType> servicesProvided = new HashSet<>();


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(role.toString()));
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {	
		return emailVerified.isBefore(LocalDateTime.now());
	}
}
