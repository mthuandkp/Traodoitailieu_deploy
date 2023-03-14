package com.sgu.userservice.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("person")
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class Person {
    @Id
    @Getter(AccessLevel.NONE)
    private ObjectId _id;
    @Field("id")
    private Long id;
    @Field("name")
    private String name;
    @Field("address")
    private String address;
    @Field("phone")
    private String phone;
    @Field("birthday")
    private String birthday;
    @Field("gender")
    private Boolean gender;
    @Field("created_at")
    private String createdAt;
    @Field("updated_at")
    private String updatedAt;
}
