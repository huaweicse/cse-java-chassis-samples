package com.huawei.cse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  @Bean
  @Override
  protected UserDetailsService userDetailsService() {

    String finalPassword = "123456";

    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(User.withUsername("user_1").password(finalPassword).roles("USER").build());
    manager.createUser(User.withUsername("user_2").password(finalPassword).roles("USER").build());
    return manager;
  }



  @Bean
  PasswordEncoder passwordEncoder() {
    // not secure
    return new PasswordEncoder() {

      public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
      }

      public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
      }
    };
  }


  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    AuthenticationManager manager = super.authenticationManagerBean();
    return manager;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .requestMatchers()
        .anyRequest()
        .and()
        .authorizeRequests()
        .antMatchers("/oauth/**")
        .permitAll();
  }
}
