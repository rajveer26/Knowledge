package com.knowledge.microservice.auth;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.List;

@Component
@Log4j2(topic = "JwtTokenFilter")
public class JwtTokenFilter extends OncePerRequestFilter {

    private final AuthAbstract authAbstract;
    private final JwtConfig jwtConfig;

    //constructor
    @Autowired
    public JwtTokenFilter(AuthAbstract authAbstract, JwtConfig jwtConfig) {
        super();
        this.authAbstract = authAbstract;
        this.jwtConfig = jwtConfig;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);
        if (!Strings.isNullOrEmpty(token)) {
            try {
                Jws<Claims> claims = claimsJws(token);

                this.setupAuthentication(claims);
                //setting token in authAbstract
                this.authAbstract.setBearerToken("Bearer " + token);
            } catch (Exception e) {

                //logging exception
                log.error(e.fillInStackTrace());
                log.catching(e);

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        filterChain.doFilter(request, response);
    }


    public Jws<Claims> claimsJws(String token) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

        return Jwts.parserBuilder()
                .setSigningKey(this.jwtConfig.generateJwtKeyDecryption())
                .build()
                .parseClaimsJws(token);
    }

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }


    public void setupAuthentication(Jws<Claims> claimsJws) {

        authenticate(claimsJws);
    }

    public void authenticate(Jws<Claims> claims) throws BadCredentialsException {
        try {

            String username = claims.getBody().getSubject();
            ObjectMapper objectMapper = new ObjectMapper();

            List<String> roles = objectMapper.convertValue(claims.getBody().get("roles"), new TypeReference<>() {
            });

            UsernamePasswordAuthenticationToken authenticationToken = null;
            if (username != null && roles != null && !roles.isEmpty()) {
                Collection<SimpleGrantedAuthority> authorities = roles.parallelStream().map(SimpleGrantedAuthority::new).toList();
                // Create an Authentication object and set it in the SecurityContextHolder
                authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        } catch (Exception e) {
            throw new BadCredentialsException("Invalid JWT token", e);
        }
    }

}