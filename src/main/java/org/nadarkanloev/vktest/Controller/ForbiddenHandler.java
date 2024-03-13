package org.nadarkanloev.vktest.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nadarkanloev.vktest.Model.Audition;
import org.nadarkanloev.vktest.Model.User;
import org.nadarkanloev.vktest.Repository.AuditionRepository;
import org.nadarkanloev.vktest.Service.UUIDGenerator;
import org.nadarkanloev.vktest.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Locale;

import static org.nadarkanloev.vktest.Service.UUIDGenerator.generateUniqueId;

@RestControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class ForbiddenHandler  implements AccessDeniedHandler {


    private final UserService userService;
    private final AuditionRepository auditionRepository;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException{
        String method = request.getMethod();
        String body = null;

        try {
            body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual).replace("/api", "").toUpperCase(Locale.ROOT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Audition audition = Audition.builder()
                .UUID(generateUniqueId() + userService.getid())
                .httpMethod(method)
                .errorLogs("Нет доступа")
                .userId(String.valueOf(userService.getid()))
                .timeStamp(LocalDateTime.now())
                .cachingInfo("-")
                .serverResponse("-")
                .responseStatus("403 FORBIDDEN")
                .requestParams(Collections.singletonMap("request params",body))
                .userRole(userService.getRole())
                .section(request.getRequestURI().toString().toLowerCase())
                .errorLogs("Нет доступа")
                .build();
        auditionRepository.save(audition);
        log.info("Обработчик работает");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("Access denied");
    }
}
