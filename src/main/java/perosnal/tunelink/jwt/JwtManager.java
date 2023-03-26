package perosnal.tunelink.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import perosnal.tunelink.exceptions.InvalidJwtException;
import perosnal.tunelink.exceptions.TuneLinkException;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static java.sql.Date.valueOf;
import static java.time.LocalDate.now;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtManager {

    @Value("${rsa.public.key}")
    private String publicKey;

    @Value("${rsa.private.key}")
    private String privateKey;

    @SneakyThrows
    public String generateJwt(String userId) {
        if (userId == null || userId.isEmpty() || userId.isBlank())
            throw new TuneLinkException("User ID can't be empty");
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(valueOf(now().plusDays(1)))
                .signWith(getPrivateKey())
                .compact();
    }

    public String extractSub(String jwt) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey())
                    .build()
                    .parseClaimsJws(jwt);
            return claims.getBody().get("sub").toString();
        } catch (Exception e) {
            throw new InvalidJwtException();
        }
    }

    @SneakyThrows
    private PublicKey publicKey() {
        publicKey = publicKey.replace("\n", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "");

        final byte[] publicKeyData = Base64.getDecoder().decode(publicKey);
        var spec = new X509EncodedKeySpec(publicKeyData);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    private PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        privateKey = privateKey.replace("\n", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "");

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }
}
