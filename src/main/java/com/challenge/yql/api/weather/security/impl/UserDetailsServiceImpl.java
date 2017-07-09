package com.challenge.yql.api.weather.security.impl;

import com.challenge.yql.api.weather.model.User;
import com.challenge.yql.api.weather.repository.UserRepository;
import com.challenge.yql.api.weather.security.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * Created by springfield-home on 7/8/17.
 */
@Service("customDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private LoginAttemptService loginAttemptService;

    private final UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }

        User user = userRepository.findByUsername(username);
        if(user != null) {
            return new UserDetailsImpl(user);
        } else {
            throw new UsernameNotFoundException("could not find the user '"
                    + username + "'");
        }
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private static class UserDetailsImpl extends User implements UserDetails {

        public UserDetailsImpl(User user) {
            super(user);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.createAuthorityList("USER");
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return isActive();
        }
    }
}
