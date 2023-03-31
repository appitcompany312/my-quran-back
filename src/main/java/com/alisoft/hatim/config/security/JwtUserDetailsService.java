package com.alisoft.hatim.config.security;

import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.config.security.jwt.JwtUserFactory;
import com.alisoft.hatim.domain.User;
import com.alisoft.hatim.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (isNull(user)) {
            throw new UsernameNotFoundException("Пользователь не найден.");
        }
        JwtUser jwtUser = JwtUserFactory.create(user);

        log.info("LoadByUserName: success - {}", user);
        return jwtUser;
    }
}
