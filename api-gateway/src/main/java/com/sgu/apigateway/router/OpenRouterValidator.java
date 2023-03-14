package com.sgu.apigateway.router;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class OpenRouterValidator {

    public static final List<String> openApiEndpoints= List.of(
            //auth
            "/api/v1/auth/",
            //user
            "/api/v1/user/register",
            //account
            "/api/v1/account/send-otp-code",
            "/api/v1/account/active-account",
            //person
            "/api/v1/person/get-person-by-id",
            //category
            "/api/v1/category/get-all",
            "/api/v1/category/get-all-with-pagination",
            //posts
            "/api/v1/posts/get-all",
            "/api/v1/posts/get-by-id",
            "/api/v1/posts/get-by-account-id",
            "/api/v1/posts/get-by-category-id",
            "/api/v1/posts/get-by-category-slug",
            "/api/v1/posts/get-all-with-pagination",
            "/api/v1/posts/get-all-available",
            "/api/v1/posts/get-all-available-with-pagination",
            "/api/v1/posts/search-by-keyword",
            "/api/v1/posts/search",
            "/api/v1/posts/get-by-posts-slug"

    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
