package me.lozm.app;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.user.entity.User;
import me.lozm.domain.user.service.UserHelperService;
import me.lozm.global.exception.CustomOAuth2Exception;
import me.lozm.utils.exception.CustomExceptionType;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserHelperService userHelperService;
    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userHelperService.findUser(loginId).orElseThrow(() -> new CustomOAuth2Exception(CustomExceptionType.USER_NOT_FOUND.getMessage()));
        final CustomUserDetails customUserDetails = new CustomUserDetails(user);
        detailsChecker.check(customUserDetails);
        return customUserDetails;
    }

}
