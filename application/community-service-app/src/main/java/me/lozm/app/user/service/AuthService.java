package me.lozm.app.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import lombok.RequiredArgsConstructor;
import me.lozm.app.user.client.AuthClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthClient authClient;
    private final ObjectMapper objectMapper;


    public void createToken() throws JsonProcessingException {
        Response response = authClient.createToken("password", "system", "asdfasdf1234",
                "read", true);

        final HttpStatus.Series httpStatusSeries = HttpStatus.Series.valueOf(response.status());
        String responseBody = response.body().toString();
        System.out.println("responseBody = " + responseBody);
    }

}
