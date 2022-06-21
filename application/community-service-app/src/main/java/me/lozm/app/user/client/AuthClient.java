package me.lozm.app.user.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import me.lozm.global.feign.FeignContractConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "auth-client", url = "${feign.auth-client.url}", configuration = {FeignContractConfig.class})
public interface AuthClient {
    @Headers("Authorization: Basic dGVzdENsaWVudElkOnRlc3RTZWNyZXQ=")
    @RequestLine("POST /oauth/token?grant_type={grant_type}&username={username}&password={password}&scope={scope}&force={force}")
    Response createToken(@Param("grant_type") String grantType,
                         @Param("username") String username,
                         @Param("password") String password,
                         @Param("scope") String scope,
                         @Param("force") Boolean force);

    @Headers("Authorization: Basic dGVzdENsaWVudElkOnRlc3RTZWNyZXQ=")
    @RequestLine("POST /oauth/token?grant_type={grant_type}&refresh_token={refresh_token}")
    Response refreshToken(@Param("grant_type") String grantType,
                         @Param("refresh_token") String refreshToken);

}
