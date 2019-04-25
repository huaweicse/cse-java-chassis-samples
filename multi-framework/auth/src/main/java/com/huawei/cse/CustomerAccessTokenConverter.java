package com.huawei.cse;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

public class CustomerAccessTokenConverter extends DefaultAccessTokenConverter {

  public CustomerAccessTokenConverter() {
    super.setUserTokenConverter(new CustomerUserAuthenticationConverter());
  }

  private class CustomerUserAuthenticationConverter extends DefaultUserAuthenticationConverter {
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
      LinkedHashMap<String, Object> response = new LinkedHashMap<>();
//      response.put("details", authentication.getDetails());
      if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
        response.put("authorities", authorityListToSet(authentication.getAuthorities()));
      }
      return response;
    }
  }
  
  public static Set<String> authorityListToSet(
      Collection<? extends GrantedAuthority> userAuthorities) {
  Set<String> set = new LinkedHashSet<>(userAuthorities.size());

  for (GrantedAuthority authority : userAuthorities) {
      set.add(authority.getAuthority());
  }

  return set;
}
}
