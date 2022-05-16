package org.spring.kanban.configuration;

import org.spring.kanban.security.AuthenticationEntryPointJwt;
import org.spring.kanban.security.AuthenticationTokenFilter;
import org.spring.kanban.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter  {	
	
	private final CustomUserDetailService customUserDetailsService;	
	private final AuthenticationEntryPointJwt authenticationEntryPointJwt;
	
	public WebSecurityConfiguration(CustomUserDetailService customUserDetailsService,
			AuthenticationEntryPointJwt authenticationEntryPointJwt) {
		this.customUserDetailsService = customUserDetailsService;
		this.authenticationEntryPointJwt = authenticationEntryPointJwt;
	}
	
	
	
	@Bean
    public AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver(){
        return new AuthenticationPrincipalArgumentResolver();
    }
	
	@Bean
	public AuthenticationTokenFilter authenticationFilter() {
		return new AuthenticationTokenFilter();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}  

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManager();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http			
			.csrf().disable()
			.exceptionHandling().authenticationEntryPoint(authenticationEntryPointJwt)
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests().antMatchers("/api/auth/**", "/admin/**").permitAll()
				.anyRequest().authenticated();

		http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}
