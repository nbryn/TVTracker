package com.niklas.tvtracker.security;

import com.niklas.tvtracker.entities.User;
import com.niklas.tvtracker.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.niklas.tvtracker.security.SecurityCockpit.HEADER;
import static com.niklas.tvtracker.security.SecurityCockpit.TOKEN;

public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        try{
            String jwt = getJWTfromRequest(httpServletRequest);

            if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIDFromJWT(jwt);
                User userDetails = userDetailsService.getUserById(userId);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, Collections.emptyList());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    private String getJWTfromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER);

        if(StringUtils.hasText(bearerToken)&&bearerToken.startsWith(TOKEN)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
