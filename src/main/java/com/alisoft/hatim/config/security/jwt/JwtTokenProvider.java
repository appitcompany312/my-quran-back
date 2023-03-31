package com.alisoft.hatim.config.security.jwt;

import com.alisoft.hatim.config.security.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.access.token.expired}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh.token.expired}")
    private long refreshTokenValidityInMilliseconds;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createAccessTokenLogin(String username) {
        return create(username, accessTokenValidityInMilliseconds);
    }

    public String createRefreshTokenLogin(String username) {
        return create(username, refreshTokenValidityInMilliseconds);
    }

    public String createAccessTokenRefresh(String refreshToken) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(refreshToken).getBody();
        String username = claims.getSubject();

        return create(username, accessTokenValidityInMilliseconds);
    }

    public String createRefreshTokenRefresh(String refreshToken) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(refreshToken).getBody();
        String username = claims.getSubject();

        return create(username, refreshTokenValidityInMilliseconds);
    }


    public String create(String username, Long timeout) {
        Claims claims = Jwts.claims().setSubject(username);
        Date date = new Date();
        Date validity = new Date(date.getTime() + timeout);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
//                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }



    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
//        return !claims.getBody().getExpiration().before(new Date());
        return !claims.getBody().isEmpty();
    }


}
