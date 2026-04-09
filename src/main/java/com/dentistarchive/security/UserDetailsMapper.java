package com.dentistarchive.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsMapper {

    ObjectMapper objectMapper;

    public String serializeToBase64(CustomUserDetails userDetails) {
        return new String(serializeToBase64Bytes(userDetails), UTF_8);
    }

    public CustomUserDetails deserializeFromBase64(String str) {
        return deserializeFromBase64Bytes(str.getBytes(UTF_8));
    }

    public byte[] serializeToBase64Bytes(CustomUserDetails userDetails) {
        String json = serializeToJson(userDetails);
        return Base64.getEncoder().encode(json.getBytes(UTF_8));
    }

    public CustomUserDetails deserializeFromBase64Bytes(byte[] bytes) {
        String json = new String(Base64.getDecoder().decode(bytes), UTF_8);
        return deserializeFromJson(json);
    }

    @SneakyThrows
    public String serializeToJson(CustomUserDetails userDetails) {
        return objectMapper.writeValueAsString(userDetails);
    }

    @SneakyThrows
    public CustomUserDetails deserializeFromJson(String json) {
        return objectMapper.readValue(json, CustomUserDetails.class);
    }
}
