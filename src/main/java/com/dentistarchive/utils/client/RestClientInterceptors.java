package com.dentistarchive.utils.client;

import com.dentistarchive.exception.ErrorResponse;
import com.dentistarchive.exception.RestClientErrorResponseException;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.UserDetailsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static com.dentistarchive.security.UserDetailsConstants.USER_DETAILS_HEADER_NAME;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

@Slf4j
public class RestClientInterceptors {

    private static final int MAX_BODY_LENGTH = 1000;

    public static ClientHttpRequestInterceptor actorDetailsInterceptor(UserDetailsMapper mapper) {
        return (request, body, execution) -> {
            AuthHolder.getUserDetails().ifPresent(actorDetails ->
                    request.getHeaders().add(USER_DETAILS_HEADER_NAME, mapper.serializeToBase64(actorDetails)));
            return execution.execute(request, body);
        };
    }

    public static ClientHttpRequestInterceptor loggingInterceptor() {
        return (request, body, execution) -> {
            if (!log.isTraceEnabled()) {
                return execution.execute(request, body);
            }

            log.trace(
                    "HTTP Request: {} {}\nHeaders: {}\nBody: {}\nActor: {}",
                    request.getMethod(),
                    request.getURI(),
                    toString(request.getHeaders()),
                    toString(body),
                    AuthHolder.getUserId().map(UUID::toString).orElse(Strings.EMPTY)
            );

            ClientHttpResponse response = new BufferingClientHttpResponseWrapper(execution.execute(request, body));

            log.trace(
                    "HTTP Response: {}\nHeaders: {}\nBody: {}",
                    response.getStatusCode(),
                    toString(response.getHeaders()),
                    StreamUtils.copyToString(response.getBody(), UTF_8)
            );

            return response;
        };
    }

    public static ResponseErrorHandler responseErrorHandler(ObjectMapper objectMapper) {
        return new ResponseErrorHandler() {

            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                HttpStatusCode statusCode = response.getStatusCode();
                return !statusCode.is2xxSuccessful();
            }


            public void handleError(ClientHttpResponse response) throws IOException {
                ErrorResponse errorResponse;
                if (response.getStatusCode().is3xxRedirection()) {
                    throw new RestClientRedirectException(
                            response.getStatusCode(),
                            getLocationHeaderValue(response)
                    );
                }
                try {
                    errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);
                } catch (Exception e) {
                    try {
                        String responseBody = new String(response.getBody().readAllBytes(), UTF_8);
                        throw new ResponseStatusException(response.getStatusCode(), responseBody);
                    } catch (IOException ignored) {
                        throw new ResponseStatusException(response.getStatusCode());
                    }
                }
                throw new RestClientErrorResponseException(response.getStatusCode().value(), errorResponse);
            }
        };
    }

    private static String toString(HttpHeaders headers) {
        return headers.headerSet().stream()
                .map(it -> it.getKey() + ": " + String.join(", ", it.getValue()))
                .collect(joining(", "));
    }

    private static String toString(byte[] body) {
        if (ArrayUtils.isEmpty(body)) {
            return Strings.EMPTY;
        }
        if (body.length > MAX_BODY_LENGTH) {
            return new String(body, 0, MAX_BODY_LENGTH, UTF_8);
        }
        return new String(body, UTF_8);
    }

    private static String getLocationHeaderValue(ClientHttpResponse response) {
        try {
            return Objects.requireNonNull(response.getHeaders().get(HttpHeaders.LOCATION)).getFirst();
        } catch (Exception e) {
            return null;
        }
    }
}
