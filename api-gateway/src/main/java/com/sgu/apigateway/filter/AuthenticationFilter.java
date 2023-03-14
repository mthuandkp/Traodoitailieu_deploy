package com.sgu.apigateway.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.sgu.apigateway.dto.request.HttpResponseObject;
import com.sgu.apigateway.router.OpenRouterValidator;
import com.sgu.apigateway.router.AdminRouterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

    @Value("${security.app.jwtSecret}")
    private String secret;
    @Autowired
    private OpenRouterValidator openRouterValidator;//custom route validator

    @Autowired
    private AdminRouterValidator adminRouterValidator;//custom route validator

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (openRouterValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request)) {
                HttpResponseObject httpResponseObject = HttpResponseObject.builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .message(Arrays.asList("Authorization header is missing in request"))
                        .build();
                String jsonHttp = new Gson().toJson(httpResponseObject);
                return this.onError(exchange, jsonHttp, HttpStatus.UNAUTHORIZED);
            }
            String token = this.getAuthHeader(request);

            //Incorrect authorization structure
            if(!token.startsWith("Bearer ")){
                HttpResponseObject httpResponseObject = HttpResponseObject.builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .message(Arrays.asList("Incorrect authorization structure"))
                        .build();
                String jsonHttp = new Gson().toJson(httpResponseObject);
                return this.onError(exchange, jsonHttp, HttpStatus.UNAUTHORIZED);
            }

            token = token.substring("Bearer ".length());

            DecodedJWT jwt = JWT.decode(token);

            if( jwt.getExpiresAt().before(new Date())) {
                HttpResponseObject httpResponseObject = HttpResponseObject.builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .message(Arrays.asList("Token is expired"))
                        .build();
                String jsonHttp = new Gson().toJson(httpResponseObject);
                return this.onError(exchange, jsonHttp, HttpStatus.UNAUTHORIZED);
            }

            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            Claim role = decodedJWT.getClaims().get("role");
            Claim sub = decodedJWT.getClaims().get("sub");


            if(role == null || role.isNull()){
                return this.onError(exchange, "'Role' not found", HttpStatus.UNAUTHORIZED);
            }

            String sessionROle = role.asString();

            //Check user call admin api
            if(role.asString().compareToIgnoreCase("USER") == 0
            && adminRouterValidator.isSecured.test(request)){
                HttpResponseObject httpResponseObject = HttpResponseObject.builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .message(Arrays.asList("Not authorization admin"))
                        .build();
                String jsonHttp = new Gson().toJson(httpResponseObject);
                return this.onError(exchange, jsonHttp, HttpStatus.UNAUTHORIZED);
            }

            exchange.getRequest().mutate()
                .header("role", String.valueOf(role))
                .header("user", String.valueOf(sub))

                .build();


        }
        return chain.filter(exchange);
    }


    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}
