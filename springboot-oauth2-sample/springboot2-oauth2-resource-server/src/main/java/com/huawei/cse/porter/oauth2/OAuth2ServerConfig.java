package com.huawei.cse.porter.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class OAuth2ServerConfig {

  private static final String DEMO_RESOURCE_ID = "order";

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setSigningKey("123");
    converter.setAccessTokenConverter(new CustomerAccessTokenConverter());
    return converter;
  }

  @Bean
  public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
    return new JwtTokenStore(jwtAccessTokenConverter);
  }

  
  @Configuration
  @EnableResourceServer

  protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    TokenStore tokenStore;
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
      DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
      defaultTokenServices.setTokenStore(tokenStore);
      resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
      resources.tokenServices(defaultTokenServices);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
      http
          .authorizeRequests()
          .antMatchers("/order/**")
          .authenticated();//配置order访问控制，必须认证过后才可以访问

    }
  }

}
