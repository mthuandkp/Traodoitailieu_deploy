package com.sgu.authservice.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sgu.authservice.constant.Constant;
import com.sgu.authservice.dto.request.RefreshTokenRequest;
import com.sgu.authservice.dto.response.HttpResponseObject;
import com.sgu.authservice.dto.request.LoginRequest;
import com.sgu.authservice.dto.response.RefreshTokenResponse;
import com.sgu.authservice.dto.response.TokenResponse;
import com.sgu.authservice.exception.*;
import com.sgu.authservice.model.Account;
import com.sgu.authservice.repository.AuthRepository;
import com.sgu.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AuthServiceImp implements AuthService {
    @Autowired
    private AuthRepository authRepository;

    @Value("${security.app.jwtSecret}")
    private String jwtSecret;

    @Value(("${security.app.jwtAccessExpirationMs}"))
    private int jwtAccessExpirationMs;

    @Value(("${security.app.jwtRefreshExpirationMs}"))
    private int jwtRefreshExpirationMs;

    @Override
    public HttpResponseObject login(LoginRequest loginRequest) {

        Optional<Account> accountOptional = null;
        try{
            accountOptional = authRepository.findByUsername(loginRequest.getUsername());
            if(accountOptional.isPresent()){
                Account account = accountOptional.get();
                if(!new BCryptPasswordEncoder()
                        .matches(loginRequest.getPassword(), account.getPassword())){
                    throw new Fobidden("Password is incorrect");
                }

                if(!account.getIsActive()){
                    throw new Fobidden("Account is not active");
                }

                if(account.getIsBlock()){
                    throw new Fobidden("Account is block");
                }

                //Generate token and return response
                Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
                String access_token = JWT.create()
                        .withSubject(account.getUsername())
                        .withClaim("role",account.getRole().name())
                        .withExpiresAt(new Date(System.currentTimeMillis()+ jwtAccessExpirationMs))
                        .sign(algorithm);
                String refreshtoken = JWT.create()
                        .withSubject(account.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+ jwtRefreshExpirationMs))
                        .sign(algorithm);
                TokenResponse tokenResponse = new TokenResponse().builder()
                        .accessToken(access_token)
                        .refreshToken(refreshtoken)
                        .build();

                //Update refresh token for account
                account.setRefreshToken(refreshtoken);
                authRepository.save(account);

                return new HttpResponseObject().builder()
                        .code(HttpStatus.OK.value())
                        .message(Constant.SUCCESS)
                        .data(Arrays.asList(tokenResponse))
                        .build();
            }
            else{
                throw new NotFound("Account not found");
            }

        }catch (NullPointerException ex){
            throw new BadRequest("Request can't be null");
        }catch (IllegalArgumentException ex){
            throw new BadRequest("Password can't be null");
        }catch (Fobidden ex){
            throw new Fobidden(ex.getMessage());
        }catch (NotFound ex){
            throw new NotFound(ex.getMessage());
        }
        catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }

    }

    @Override
    public HttpResponseObject refreshToken(RefreshTokenRequest request) {
        try{


            DecodedJWT jwt = JWT.decode(request.getRefreshToken());

            if( jwt.getExpiresAt().before(new Date())) {
                throw new RefreshTokenIsExpired("Refresh token is expired");
            }

            Optional<Account> accountOptional = authRepository.findByUsername(jwt.getSubject());
            Account account = accountOptional.get();
            if(accountOptional.isPresent()){

                if(!account.getRefreshToken().equals(request.getRefreshToken())){
                    throw  new NotFound("Refresh token not found");
                }


                //Generate token and return response
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                String access_token = JWT.create()
                        .withSubject(account.getUsername())
                        .withClaim("role",account.getRole().name())
                        .withExpiresAt(new Date(System.currentTimeMillis()+ (1 * 60 * 10000)))
                        .sign(algorithm);

                RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse()
                        .builder()
                        .accessToken(access_token)
                        .build();

                return new HttpResponseObject().builder()
                        .code(HttpStatus.OK.value())
                        .message(Constant.SUCCESS)
                        .data(Arrays.asList(refreshTokenResponse))
                        .build();
            }
            else{
                throw new NotFound("Refresh token not exists");
            }
        }catch (RefreshTokenIsExpired ex){
            throw new RefreshTokenIsExpired(ex.getMessage());
        }catch (NoSuchElementException ex){
            ex.printStackTrace();
            throw new BadRequest("Refresh token invalid");
        }catch(NotFound ex){
            throw new NotFound(ex.getMessage());
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }
}
