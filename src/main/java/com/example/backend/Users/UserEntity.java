package com.example.backend.Users;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
@TypeAlias("UsersEntity")
public class UserEntity {

    @Id
    private  String id;


   private String email;

   private String phoneNumber;

   private boolean activated;

    @NonNull
    private String otp;

}
