package com.banquemisr.challenge05.service.impl;

import com.banquemisr.challenge05.exception.userException.UserLoginException;
import com.banquemisr.challenge05.exception.userException.UserRegistrationException;
import com.banquemisr.challenge05.repository.UserRepository;
import com.banquemisr.challenge05.mapper.UserMapper;
import com.banquemisr.challenge05.model.User;
import com.banquemisr.challenge05.payload.user.request.UserLoginDTO;
import com.banquemisr.challenge05.payload.user.request.UserRegistrationDTO;
import com.banquemisr.challenge05.payload.user.response.LoginResponseDTO;
import com.banquemisr.challenge05.security.jwt.JwtUtils;
import com.banquemisr.challenge05.service.UserService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtService;

    @Override
    public void signup(UserRegistrationDTO userRegistrationDTO) {
        log.info("Start Sign up process with details :{}", userRegistrationDTO);

        try {
            if (userRepository.findByEmail(userRegistrationDTO.getEmail()).isPresent()) {
                String errorMessage = "Email is existing, Please insert a valid email";
                log.error(errorMessage);
                throw new UserRegistrationException(errorMessage);
            }

            User user = userMapper.toUserEntity(userRegistrationDTO);
            user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
            userRepository.save(user);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserRegistrationException("Error while trying to register user with details :" + userRegistrationDTO);
        }

    }

    @Override
    public LoginResponseDTO authenticate(UserLoginDTO userLoginDTO) {
        log.info("Start Attempting to authenticate user: {}", userLoginDTO.getEmail());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));

            User authenticatedUser = userRepository.findByEmail(userLoginDTO.getEmail()).orElseThrow(() -> new NotFoundException("Invalid username or password"));

            String jwtToken = jwtService.generateToken(authenticatedUser);

            log.info("Authentication successful for user: {}", userLoginDTO.getEmail());

            return new LoginResponseDTO(jwtToken);

        } catch (NotFoundException ex) {
            throw new UserLoginException("Authentication failed for user Not Found " + userLoginDTO);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserLoginException("Authentication failed for user Details :{} " + userLoginDTO);
        }

    }
}
