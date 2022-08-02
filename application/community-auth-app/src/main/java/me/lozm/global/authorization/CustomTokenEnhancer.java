package me.lozm.global.authorization;

import lombok.RequiredArgsConstructor;
import me.lozm.app.CustomUserDetailService;
import me.lozm.app.CustomUserDetails;
import me.lozm.exception.CustomExceptionType;
import me.lozm.exception.InternalServerException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomTokenEnhancer implements TokenEnhancer {

    private final CustomUserDetailService customUserDetailService;


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        CustomUserDetails customUserDetails;
        Object principal = authentication.getUserAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            customUserDetails = (CustomUserDetails) principal;
        } else if (principal instanceof String) {
            final String loginId = (String) principal;
            UserDetails userDetails = customUserDetailService.loadUserByUsername(loginId);
            customUserDetails = (CustomUserDetails) userDetails;
        } else {
            throw new InternalServerException(CustomExceptionType.INTERNAL_SERVER_ERROR);
        }

        Map<String, Object> additionalInformationMap = new HashMap<>();
        additionalInformationMap.put("userId", customUserDetails.getUserId());
        additionalInformationMap.put("loginId", customUserDetails.getLoginId());
        additionalInformationMap.put("name", customUserDetails.getName());
        additionalInformationMap.put("role", customUserDetails.getRole());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformationMap);
        return accessToken;
    }

}
