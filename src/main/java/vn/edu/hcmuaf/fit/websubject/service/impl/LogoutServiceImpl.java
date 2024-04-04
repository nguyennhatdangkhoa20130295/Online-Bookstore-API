package vn.edu.hcmuaf.fit.websubject.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.edu.hcmuaf.fit.websubject.repository.TokenRepository;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutHandler {
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String headerAuth = request.getHeader("Authorization");
        String jwt;
        if (!StringUtils.hasText(headerAuth) && !headerAuth.startsWith("Bearer ")) {
            return;
        }
        jwt = headerAuth.substring(7);
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if(storedToken != null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }
    }
}
