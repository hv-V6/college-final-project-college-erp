package com.college.erp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public UserDetailsService users() {
		return new InMemoryUserDetailsManager(
				  User.withDefaultPasswordEncoder()
							 .username("admin")
							 .password("admin123")
							 .roles("ADMIN")
							 .build()
		);
	}

	@Bean
	public SecurityFilterChain security(HttpSecurity http)
			  throws Exception {
		http
				  .csrf(csrf -> csrf.disable())
				  .authorizeHttpRequests(auth -> auth
							 // Allow static files and login page — NO auth needed
						  .requestMatchers(
								  "/login", "/css/**", "/js/**",
								  "/plugins/**", "/images/**", "/webjars/**", "/static/**"
						  ).permitAll()
							 // Everything else needs login
							 .anyRequest().authenticated()
				  )
				  .formLogin(form -> form
							 .loginPage("/login")
							 .loginProcessingUrl("/login")
							 .defaultSuccessUrl("/dashboard", true)
							 .failureUrl("/login?error")
							 .permitAll()
				  )
				  .logout(logout -> logout
							 .logoutUrl("/logout")
							 .logoutSuccessUrl("/login?logout")
							 .invalidateHttpSession(true)
							 .deleteCookies("JSESSIONID")
							 .permitAll()
				  );
		return http.build();
	}
}