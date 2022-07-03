package perosnal.spotifymatcher.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import perosnal.spotifymatcher.util.HttpRequestSender;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenValidator {

    @Autowired
    private HttpRequestSender httpRequestSender;

    @Value("${auth.server.url}")
    private String AUTH_SERVER_URL;


    public void validateJwt(String jwt) {
        Jwts.parserBuilder()
                .setSigningKey(publicKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    @SneakyThrows
    public PublicKey publicKey() {
        String rsaPublicKey = httpRequestSender.genericRequest(AUTH_SERVER_URL+"/api/key");
        String publicKey = rsaPublicKey;

        publicKey = publicKey.replace("\n", "");
        publicKey = publicKey.replace("-----BEGIN PUBLIC KEY-----", "");
        publicKey = publicKey.replace("-----END PUBLIC KEY-----", "");
        byte publicKeyData[] = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyData);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pk = kf.generatePublic(spec);
        return pk;
    }

    public String getUserSpotifyId(String jwt) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(publicKey())
                .build()
                .parseClaimsJws(jwt);
        System.out.println(claims.getBody().get("user_id"));

        return claims.getBody().get("user_id").toString();
    }

}
