package com.huawei.cse.porter.oauth2;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
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
          .authenticated();//閰嶇疆order璁块棶鎺у埗锛屽繀椤昏璇佽繃鍚庢墠鍙互璁块棶

    }
  }


  @Configuration
  @EnableAuthorizationServer
  protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenStore tokenStore;

    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    UserDetailsService userDetailsService;
    
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

      String finalSecret = "123456";
      //        String finalSecret = new BCryptPasswordEncoder().encode("123456");
      //            String finalSecret = "{bcrypt}"+new BCryptPasswordEncoder().encode("123456");
      clients.inMemory()
          .withClient("client_1")
          .resourceIds(DEMO_RESOURCE_ID)
          .authorizedGrantTypes("client_credentials", "refresh_token")
          .scopes("read")
          .authorities("oauth2")
          .secret(finalSecret)
          .and()
          .withClient("client_2")
          .resourceIds(DEMO_RESOURCE_ID)
          .authorizedGrantTypes("password", "refresh_token")
          .scopes("read")
          .authorities("oauth2")
          .secret(finalSecret);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
      endpoints
          .tokenStore(tokenStore)
          .accessTokenConverter(jwtAccessTokenConverter)
          .authenticationManager(authenticationManager)
          .userDetailsService(userDetailsService)
          .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

//      //自定义token生成方式
//      TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//      tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new CustomTokenEnhancer(), jwtAccessTokenConverter));
//      endpoints.tokenEnhancer(tokenEnhancerChain);

      // 配置TokenServices参数
      DefaultTokenServices tokenServices = (DefaultTokenServices) endpoints.getDefaultAuthorizationServerTokenServices();
      tokenServices.setTokenStore(endpoints.getTokenStore());
      tokenServices.setSupportRefreshToken(true);
      tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
      tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
      tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(1));//一天
      endpoints.tokenServices(tokenServices);
    }



    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
      //鍏佽琛ㄥ崟璁よ瘉
      oauthServer.allowFormAuthenticationForClients();
    }

  }

}
