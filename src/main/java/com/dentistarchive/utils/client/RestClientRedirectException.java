package com.dentistarchive.utils.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestClientRedirectException extends ResponseStatusException {

    String location;

    public RestClientRedirectException(HttpStatusCode httpStatusCode, String location) {
        super(httpStatusCode);
        this.location = location;
    }
}
