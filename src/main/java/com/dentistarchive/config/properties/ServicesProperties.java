//package com.dentistarchive.config.properties;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotEmpty;
//import lombok.AccessLevel;
//import lombok.Data;
//import lombok.experimental.FieldDefaults;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.http.HttpMethod;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.validation.annotation.Validated;
//
//import java.util.*;
//
//import static java.util.stream.Collectors.toSet;
//import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
//import static org.apache.commons.lang3.ObjectUtils.notEqual;
//
//@ConfigurationProperties
//@Validated
//@Data
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class ServicesProperties {
//
//    public static final String GATEWAY_NAME = "gateway";
//
//    @NotEmpty
//    Map<@NotBlank String, @Valid ServiceSettings> services;
//
//    @PostConstruct
//    protected void validateProperties() {
//        assertThatRoutesDoNotHaveDuplicates();
//    }
//
//    public Set<String> getServicesWithRoutes() {
//        Set<String> serviceNames = new HashSet<>();
//        services.forEach((name, settings) -> {
//            if (isNotEmpty(settings.routes())) {
//                serviceNames.add(name);
//            }
//        });
//        return serviceNames;
//    }
//
//    public Optional<ServiceSettings> getService(String path, HttpMethod method) {
//        return services.values().stream()
//                .filter(settings -> settings.anyMatch(path, method))
//                .findFirst();
//    }
//
//
//    public boolean matchesAnyRoute(String path, HttpMethod method) {
//        return services.values().stream()
//                .map(ServiceSettings::routes)
//                .filter(Objects::nonNull)
//                .flatMap(Collection::stream)
//                .anyMatch(route -> route.matches(path, method));
//    }
//
//    public String getGatewayBaseUrl() {
//        return services.get(GATEWAY_NAME).baseUrl();
//    }
//
//    private void assertThatRoutesDoNotHaveDuplicates() {
//        Set<Route> allRoutes = services.values().stream()
//                .map(ServiceSettings::routes)
//                .filter(Objects::nonNull)
//                .flatMap(Collection::stream)
//                .collect(toSet());
//        for (Route route : allRoutes) {
//            allRoutes.stream()
//                    .filter(it -> notEqual(route, it))
//                    .filter(it -> route.matches(it.path(), it.method()))
//                    .findFirst()
//                    .ifPresent(it -> {
//                        throw new IllegalStateException("Routes can be matched with the same urls: %s and %s".formatted(route, it));
//                    });
//        }
//    }
//
//    public record ServiceSettings(@NotEmpty String baseUrl, List<Route> routes) {
//
//        public boolean anyMatch(String path, HttpMethod method) {
//            return routes != null && routes.stream().anyMatch(route -> route.matches(path, method));
//        }
//    }
//
//    public record Route(@NotBlank String path,
//                        HttpMethod method,
//                        List<@Valid ExcludedRoute> excludedRoutes) {
//
//        public Route {
//            if (path != null) {
//                path = path.trim();
//            }
//        }
//
//        public boolean matches(String path, HttpMethod method) {
//            return new AntPathMatcher().match(this.path.trim(), path)
//                    && (this.method == null || this.method.equals(method))
//                    && (excludedRoutes == null || excludedRoutes.stream().noneMatch(it -> it.matches(path, method)));
//        }
//
//    }
//
//    public record ExcludedRoute(@NotBlank String path, HttpMethod method) {
//
//        public ExcludedRoute {
//            if (path != null) {
//                path = path.trim();
//            }
//        }
//
//        public boolean matches(String path, HttpMethod method) {
//            return new AntPathMatcher().match(this.path, path)
//                    && (this.method == null || this.method.equals(method));
//        }
//    }
//}
