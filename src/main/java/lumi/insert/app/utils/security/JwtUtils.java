package lumi.insert.app.utils.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lumi.insert.app.entity.Employee;

@Component
public class JwtUtils {

    Algorithm algorithm = Algorithm.HMAC256("SOME_THING-iusedto_knOW");
    
    String issuer = "LUMI-INSERT";

    JWTVerifier jwtVerifier = JWT.require(algorithm)
            .withIssuer(issuer).build();

    public String getAccessToken(Employee employee){
        String accessToken = JWT.create()
        .withIssuer(issuer)
        .withIssuedAt(Instant.now())
        .withClaim("id", employee.getId().toString())
        .withClaim("username", employee.getUsername())
        .withClaim("role", employee.getRole().toString()) 
        .withExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
        .sign(algorithm);

        return accessToken;
    }

    public DecodedJWT parseAccessToken(String accessToken){
        return jwtVerifier.verify(accessToken);
    }

}
