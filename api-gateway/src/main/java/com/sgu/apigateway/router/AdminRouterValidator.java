package com.sgu.apigateway.router;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class AdminRouterValidator {

    public static final List<String> adminEndpoints= List.of(
            //auth
            //user
            //account
            "/api/v1/account/get-all-account",
            "/api/v1/account/get-all-account-with-pagination",
            "/api/v1/account/get-account-by-person-id/",
	        "/api/v1/account/get-account-by-username",

            //person
            "/api/v1/person/get-all-person",
            "/api/v1/person/get-all-person-with-pagination",
            //account
            "/api/v1/account/block-account",
            "/api/v1/account/unblock-account",
            //category
            "/api/v1/category/get-by-id/",
            "/api/v1/category/create",
            //posts
            "/api/v1/posts/admin-delete"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> adminEndpoints
                    .stream()
                    .anyMatch(uri -> request.getURI().getPath().contains(uri));

}
