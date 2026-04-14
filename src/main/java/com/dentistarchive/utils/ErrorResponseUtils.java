package com.dentistarchive.utils;

import com.dentistarchive.exception.ErrorResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

public class ErrorResponseUtils {

    public static Mono<Void> buildErrorCodeResponse(
            ServerWebExchange exchange,
            String errorCode,
            ServerCodecConfigurer serverCodecConfigurer
    ) {
        ErrorResponse responseDto = ErrorResponse.builder()
                .errorCode(errorCode)
                .build();
        return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(responseDto))
                .flatMap(serverResponse -> serverResponse.writeTo(exchange, new ServerResponse.Context() {

                    @Override
                    @NonNull
                    public List<HttpMessageWriter<?>> messageWriters() {
                        return serverCodecConfigurer.getWriters();
                    }

                    @Override
                    @NonNull
                    public List<ViewResolver> viewResolvers() {
                        return Collections.emptyList();
                    }
                }));
    }
}
