package com.knowledge.microservice.auth;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service("jwtTokenProvider")
@Log4j2(topic = "JwtTokenProvider")
public class JwtTokenProvider {
    private final Environment env;
    private final JwtConfig jwtConfig;
    private final JwtTokenFilter jwtTokenFilter;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public JwtTokenProvider(Environment env, JwtConfig jwtConfig, JwtTokenFilter jwtTokenFilter, CustomUserDetailsService userDetailsService) {
        this.env = env;
        this.jwtConfig = jwtConfig;
        this.jwtTokenFilter = jwtTokenFilter;
        this.userDetailsService = userDetailsService;
    }

    //method to generate token
    public String generateToken(Authentication authentication) throws  IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        //casting to User present in spring-security
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            //calling current class generateJWT() method
            return this.generateJWT(user);

    }

    public String generateJWT(CustomUserDetails user) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + Long.parseLong(Objects.requireNonNull(env.getProperty("jwt.expiryTime"))));
        ConcurrentHashMap<String, Object> claims = new ConcurrentHashMap<>();
        claims.put("username", user.getUsername());
        claims.put("userId", user.isEnabled());
        claims.put("empId", user.isAccountNonLocked());
        claims.put("empNo", user.isCredentialsNonExpired());
        claims.put("roles", user
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(this.jwtConfig.generateJwtKeyEncryption(), SignatureAlgorithm.RS512)
                .compact();
    }

    public String extractingUserNameToken(String refreshToken) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

            return Jwts.
                    parserBuilder()
                    .setSigningKey(this.jwtConfig.generateJwtKeyDecryption())
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody()
                    .getSubject();
    }

    public String generateNewAccessToken(String refreshToken) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        String username = extractingUserNameToken(refreshToken);
        CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        return this.generateJWT(user);
    }

    public boolean validateTokenInformation(String token) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
      Jws<Claims> claims =  jwtTokenFilter.claimsJws(token);

      if(!Strings.isNullOrEmpty(claims.getBody().getSubject()))
      {
          jwtTokenFilter.setupAuthentication(claims);
          return true;
      }

      return false;

    }
}
