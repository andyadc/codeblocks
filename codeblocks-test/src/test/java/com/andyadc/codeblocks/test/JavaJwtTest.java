package com.andyadc.codeblocks.test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

/**
 * @author andy.an
 * @since 2018/4/12
 */
public class JavaJwtTest {

    // Create and Sign a Token
    // Example using HS256
    @Test
    public void createHS256Token() {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            String token = JWT.create()
                    .withIssuer("adc")
                    .sign(algorithm);

            System.out.println(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Verify a Token
    // Example using HS256
    @Test
    public void verifyHS256Token() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhZGMifQ.JCPY1-hw2zzqx1rcmemU0PaIzOmJKbPp_td5DDps350";
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("adc")
                    .build();

            DecodedJWT jwt = verifier.verify(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Decode a Token
    @Test
    public void testDecodeToken() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhZGMifQ.JCPY1-hw2zzqx1rcmemU0PaIzOmJKbPp_td5DDps350";

        DecodedJWT jwt = JWT.decode(token);

        System.out.println(jwt.getPayload());
    }
}
