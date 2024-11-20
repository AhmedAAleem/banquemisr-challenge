package com.banquemisr.challenge05.mapper;

import com.banquemisr.challenge05.payload.user.request.UserRegistrationDTO;
import com.banquemisr.challenge05.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    User toUserEntity(UserRegistrationDTO dto);
}