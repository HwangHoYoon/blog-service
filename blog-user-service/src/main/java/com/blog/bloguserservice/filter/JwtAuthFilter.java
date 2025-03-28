package com.blog.bloguserservice.filter;

import com.blog.bloguserservice.common.utils.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    private final String[] authorizeUrl;

    public JwtAuthFilter(JwtProvider jwtProvider, String[] authorizeUrl) {
        this.jwtProvider = jwtProvider;
        this.authorizeUrl = authorizeUrl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {
        String url = request.getRequestURI();

        AntPathMatcher pathMatcher = new AntPathMatcher();

        boolean isAuthorized = Arrays.stream(authorizeUrl)
                .anyMatch(pattern -> pathMatcher.match(pattern, url));

        if (!isAuthorized) {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                String userId = jwtProvider.validateToken(token.substring(7));
                UserDetails userDetails = User.withUsername(userId).password("").roles("USER").build();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
