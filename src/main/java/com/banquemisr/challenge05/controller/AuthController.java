package com.banquemisr.challenge05.controller;

import com.banquemisr.challenge05.payload.user.request.UserLoginDTO;
import com.banquemisr.challenge05.payload.user.request.UserRegistrationDTO;
import com.banquemisr.challenge05.payload.user.response.LoginResponseDTO;
import com.banquemisr.challenge05.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("${api.prefix.auth}")
@AllArgsConstructor
@Validated
@Slf4j
public class AuthController {
    private static final Logger logger = Logger.getLogger(AuthController.class.getName());
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> register(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        userService.signup(userRegistrationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        LoginResponseDTO loginResponseDTO = userService.authenticate(userLoginDTO);
        return ResponseEntity.ok(loginResponseDTO);
    }
}
