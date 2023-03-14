package com.sgu.userservice.model;

import com.sgu.userservice.constant.Role;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("account")
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class Account{
    @Id
    @Getter(AccessLevel.NONE)
    private ObjectId id;
    @Field("person_id")
    private Long personId;
    @Field("username")
    private String username;
    @Field("password")
    private String password;
    @Field("role")
    private Role role;
    @Field("avatar")
    private String avatar;
    @Field("is_block")
    private Boolean isBlock;
    @Field("is_active")
    private Boolean isActive;
    @Field("otp_code")
    private String otpCode;
    @Field("refresh_token")
    private String refreshToken;
    @Field("vnpay_url")
    private String vnpayURL;
    @Field("created_at")
    private String createdAt;
    @Field("updated_at")
    private String updatedAt;
    //reasonForBlock
    @Field("reason_for_blocking")
    private String reasonForBlock;
    @Field("otp_created_at")
    private String otpCreatedAt;
}