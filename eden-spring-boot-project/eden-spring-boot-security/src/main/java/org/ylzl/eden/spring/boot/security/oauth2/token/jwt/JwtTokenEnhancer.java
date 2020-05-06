package org.ylzl.eden.spring.boot.security.oauth2.token.jwt;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import org.ylzl.eden.spring.boot.security.jwt.JwtConstants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JWT 令牌增强器
 *
 * @author gyl
 * @since 0.0.1
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {

  @Override
  public OAuth2AccessToken enhance(
      OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    addClaims((DefaultOAuth2AccessToken) accessToken);
    return accessToken;
  }

  private void addClaims(DefaultOAuth2AccessToken accessToken) {
    DefaultOAuth2AccessToken token = accessToken;
    Map<String, Object> additionalInformation = token.getAdditionalInformation();
    if (additionalInformation.isEmpty()) {
      additionalInformation = new LinkedHashMap<>();
    }
    additionalInformation.put(
        JwtConstants.ISSUED_AT, new Integer((int) (System.currentTimeMillis() / 1000L)));
    token.setAdditionalInformation(additionalInformation);
  }
}
