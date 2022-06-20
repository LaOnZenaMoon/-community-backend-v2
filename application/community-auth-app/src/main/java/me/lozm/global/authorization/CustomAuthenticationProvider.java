package me.lozm.global.authorization;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.lozm.domain.user.entity.User;
import me.lozm.domain.user.service.UserHelperService;
import me.lozm.global.exception.CustomOAuth2Exception;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserHelperService userHelperService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userHelperService.findUser(loginId).orElseThrow(() -> new CustomOAuth2Exception("사용자가 존재하지 않습니다."));
        if (!passwordEncoder.matches(password, user.getEncryptedPassword())) {
            throw new CustomOAuth2Exception("비밀번호가 일치하지 않습니다.");
        }

        return new UsernamePasswordAuthenticationToken(loginId, password, Lists.newArrayList(new SimpleGrantedAuthority(user.getRole().toString())));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
