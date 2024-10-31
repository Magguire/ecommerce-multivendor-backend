package com.eshop.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Extract jwt from header
        String jwt = request.getHeader("Authorization");

        if (jwt!=null) {
            jwt = jwt.substring(7); // Extracts the jwt token from Bearer <token>

            try {

                SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes());

                // Validate jwt token
                Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                        .parseClaimsJws(jwt).getBody();   // Generated when the JWT token is generated

                // set security context holder
                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                List<GrantedAuthority> auths = AuthorityUtils
                        .commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
                // Password is null because we do not require it

                SecurityContextHolder.getContext().setAuthentication(authentication);


            }
            catch(Exception e) {

                throw new BadCredentialsException("Invalid JWT Token ...");
            }
        }

        filterChain.doFilter(request, response);

    }
}
