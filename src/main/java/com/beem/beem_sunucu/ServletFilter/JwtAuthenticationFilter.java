package com.beem.beem_sunucu.ServletFilter;

import com.beem.beem_sunucu.Users.User_service;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final User_service service;


    public JwtAuthenticationFilter(JwtUtil jwtUtil, User_service service) {
        this.jwtUtil = jwtUtil;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header= request.getHeader("Authorization");
        String token=null;
        if(header!=null&&header.startsWith("Bearer ")){
            token=header.substring(7);
        }
        if(token!=null&&jwtUtil.validateToken(token)){
            String username=jwtUtil.getUsername(token);
            UserDetails user = service.loadUserByUsername(username);
            Long userId = service.getUserIdByUsername(username);
            UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(user,null,null);

            auth.setDetails(userId);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request,response);
    }
}
