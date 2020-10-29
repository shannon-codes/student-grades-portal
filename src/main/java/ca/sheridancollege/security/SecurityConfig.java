package ca.sheridancollege.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private LoggingAccessDeniedHandler accessDeniedHandler;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();	
	}
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
	//in memory username and password and role
	//	auth
	//	.inMemoryAuthentication()
	//	.passwordEncoder(NoOpPasswordEncoder.getInstance())
	//	.withUser("user1").password("password").roles("STUDENT")
	//	.and()
	//	.withUser("user2").password("password").roles("PROFESSOR");

	auth.userDetailsService(userDetailsService)
	.passwordEncoder(passwordEncoder());
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http
			.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/createStudent").hasRole("PROFESSOR")
				.antMatchers("/student/**").hasRole("STUDENT")
				.antMatchers("/professor/**").hasRole("PROFESSOR")
				.antMatchers("/",
						"/js/**",
						"/css/**",
						"/img/**", 
						"/**").permitAll()
				
				.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
			.and()
			.logout()
				.invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/login?logout")
			.permitAll()
			.and()
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler);
	
	}
	
}
