package com.banquemisr.challenge05.service;

import com.banquemisr.challenge05.payload.user.request.UserLoginDTO;
import com.banquemisr.challenge05.payload.user.request.UserRegistrationDTO;
import com.banquemisr.challenge05.payload.user.response.LoginResponseDTO;

public interface UserService {
    void signup(UserRegistrationDTO userRegistrationDTO);

    LoginResponseDTO authenticate(UserLoginDTO userLoginDTO);
}
