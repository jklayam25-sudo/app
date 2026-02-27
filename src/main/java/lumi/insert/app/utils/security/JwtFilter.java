package lumi.insert.app.utils.security;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier; 
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; 
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils; 
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lumi.insert.app.entity.nondatabase.EmployeeLogin;
import lumi.insert.app.entity.nondatabase.EmployeeRole;

@Component
public class JwtFilter extends OncePerRequestFilter{

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = parseBearer(request);

        if(token == null) resolver.resolveException(request, response, token, new BadCredentialsException("Missing access token, try to request token!"));

        DecodedJWT accessToken;

        try {
            accessToken = jwtUtils.parseAccessToken(token);    
        } catch (JWTVerificationException e) {
            resolver.resolveException(request, response, token, e);
            return;
        }
        
        if(accessToken.getExpiresAtAsInstant().isBefore(Instant.now())) {
            resolver.resolveException(request, response, token, new BadCredentialsException("Access token is expired, try to request token"));
        }

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(accessToken.getClaim("role").asString());
 
        EmployeeLogin employeeLogin = EmployeeLogin.builder()
        .id(UUID.fromString(accessToken.getClaim("id").asString()))
        .username(accessToken.getClaim("username").asString())
        .role(EmployeeRole.valueOf(accessToken.getClaim("role").asString()))
        .build();

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(employeeLogin, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private String parseBearer(HttpServletRequest request){
         String token = request.getHeader("Authorization").split("Bearer ")[1];
         if(token.length() < 1) return null;
         return token;
    }

    
}
