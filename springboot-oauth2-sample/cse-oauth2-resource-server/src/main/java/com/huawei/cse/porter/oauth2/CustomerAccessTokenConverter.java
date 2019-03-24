package com.huawei.cse.porter.oauth2;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.util.StringUtils;

public class CustomerAccessTokenConverter extends DefaultAccessTokenConverter {

  public CustomerAccessTokenConverter() {
    super.setUserTokenConverter(new CustomerUserAuthenticationConverter());
  }

  private class CustomerUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
      Collection<? extends GrantedAuthority> authorities = this.getAuthorities(map);
      return new UsernamePasswordAuthenticationToken(map, "N/A", authorities);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
      if (!map.containsKey("authorities")) {
        return AuthorityUtils
            .commaSeparatedStringToAuthorityList(StringUtils.arrayToCommaDelimitedString(new String[] {"USER"}));
      } else {
        Object authorities = map.get("authorities");
        if (authorities instanceof String) {
          return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        } else if (authorities instanceof Collection) {
          return AuthorityUtils.commaSeparatedStringToAuthorityList(
              StringUtils.collectionToCommaDelimitedString((Collection) authorities));
        } else {
          throw new IllegalArgumentException("Authorities must be either a String or a Collection");
        }
      }
    }
  }
}
