package org.nadarkanloev.vktest.Service.Auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nadarkanloev.vktest.DTO.JwtAuthenticationResponse;
import org.nadarkanloev.vktest.DTO.SignInRequest;
import org.nadarkanloev.vktest.DTO.SignUpRequest;
import org.nadarkanloev.vktest.Enum.Role;
import org.nadarkanloev.vktest.Model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     *
     * @param signUpRequest Данные пользователя
     * @return
     */
    public JwtAuthenticationResponse signUp(SignUpRequest signUpRequest){
        var user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);
        log.info("Создан пользователь " + user.getUsername() + " с айди" + user.getId());
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        log.info("Пользователь с именем " + user.getUsername() + " успешно вошел в систему");

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
