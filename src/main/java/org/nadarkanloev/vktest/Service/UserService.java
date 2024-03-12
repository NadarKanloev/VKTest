package org.nadarkanloev.vktest.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nadarkanloev.vktest.Enum.Role;
import org.nadarkanloev.vktest.Exception.UserAlreadyExistException;
import org.nadarkanloev.vktest.Model.User;
import org.nadarkanloev.vktest.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collection;


@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    private final UserRepository userRepository;

    /**
     * <p>Сохранение пользователя</p>
     * @param user пользователь
     * @return сохраненный пользователь
     */
    public User save(User user){
        return userRepository.save(user);
    }

    /**
     * Создание пользователя
     * @param user
     * @return Созданный пользователь
     * @throws UserAlreadyExistException
     */
    public User create(User user){
        if (userRepository.existsByUsername(user.getUsername())) {
            log.error("Пользователь с таким именем '{}' уже существует", user.getUsername());
            throw new UserAlreadyExistException("Пользователь с таким именем уже существует");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            log.error("Пользователь с таким Email '{}' уже существует", user.getEmail());
            throw new UserAlreadyExistException("Пользователь с таким Email уже существует");
        }
        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     * @param username имя пользователя
     * @return пользователь
     */
    public User getByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    /**
     * Получение пользователя по имени **для Spring Security**
     * @return
     */
    public UserDetailsService userDetailsService(){
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     * @return текущий пользователь
     */
    public User getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
    public long getid(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return ((User) authentication.getPrincipal()).getId();
        }
        return -1;
    }
    public String getRole(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (!authorities.isEmpty()) {
            return authorities.iterator().next().getAuthority();
        } else {
            return null; // Или любое другое значение по умолчанию, если роль не найдена
        }
    }
}
