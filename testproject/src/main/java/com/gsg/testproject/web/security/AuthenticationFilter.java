package com.gsg.testproject.web.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Log4j2
class AuthenticationFilter extends BasicAuthenticationFilter {

    AuthenticationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        try {
            var path = req.getRequestURI();
            var username = req.getHeader("username");
            var password = req.getHeader("password");

            if (!path.equals("/users/register") && !path.startsWith("/web-socket") && !path.equals("/login")) {
                getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));
            }

            chain.doFilter(req, res);

        } catch (Exception e) {
            log.error(e);
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
