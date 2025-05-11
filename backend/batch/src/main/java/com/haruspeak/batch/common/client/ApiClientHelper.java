package com.haruspeak.batch.common.client;

import com.haruspeak.batch.common.exception.ApiException;
import com.haruspeak.batch.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Slf4j
public class ApiClientHelper {

    public static <T, R> R post(RestTemplate restTemplate, String url, T requestBody, Class<R> responseType) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<T> entity = new HttpEntity<>(requestBody, headers);

            log.debug("API 요청 URL: {}", url);

            ResponseEntity<R> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    responseType
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("[{}] {}", response.getStatusCode(), ErrorCode.API_CALL_FAILED.getMessage());
                throw new ApiException(ErrorCode.API_CALL_FAILED);
            }

            if(response.getBody() == null) {
                throw new ApiException(ErrorCode.API_RESPONSE_FORMAT_ERROR);
            }

            return response.getBody();

        } catch (Exception e) {
            log.error(ErrorCode.API_CLIENT_EXCEPTION.getMessage());
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            throw new ApiException(ErrorCode.API_CLIENT_EXCEPTION, e);
        }
    }

}

