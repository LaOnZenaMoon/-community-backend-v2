package me.lozm.global.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public class CustomOAuth2Exception extends OAuth2Exception {
    public CustomOAuth2Exception(String msg) {
        super(msg);
    }

}
