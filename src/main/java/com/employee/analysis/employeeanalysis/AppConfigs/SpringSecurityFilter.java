package com.employee.analysis.employeeanalysis.AppConfigs;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.employee.analysis.employeeanalysis.User.UserService;

public class SpringSecurityFilter extends OncePerRequestFilter {

    @Autowired 
    SpringSecurityJWT jwt;

    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7), username = this.jwt.extractUsername(jwt);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails user = this.userService.loadUserByUsername(username);
                    if (user == null) throw new UsernameNotFoundException("User not found");
                    if (this.jwt.validateToken(jwt)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    } else throw new Exception("JWT token vailidation failed!");
                }
            } else {
                String url = request.getRequestURI();
                boolean condition = url.equals("/auth/signin") || url.equals("/auth/signup") || url.equals("/") || url.equals("/error");
                if (!condition) throw new Exception("Please supply a JWT token");
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        String response_json = "{\"message\" : \"" + e.getMessage() + "\", \"result\" : \"" + e +"\", \"code\": \"403\"}";
            System.out.println(e.getMessage());
            response.setContentType("application/json");
            response.setStatus(403);
            response.getWriter().write(response_json);
        }
    }
}
