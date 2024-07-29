package com.openclassrooms.mddapi.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.openclassrooms.mddapi.model.dtos.UserDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

/**
 * Composant pour la gestion de l'authentification des utilisateurs via JWT.
 * Fournit des méthodes pour créer et valider des tokens JWT.
 */

@RequiredArgsConstructor
@Component
public class UserAuthProvider {
    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    /**
     * Initialise la clé secrète en la convertissant en une chaîne de caractères Base64.
     */
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * Crée un token JWT pour un utilisateur donné.
     *
     * @param userDto l'objet de transfert de données contenant les informations de l'utilisateur
     * @return le token JWT généré
     */
    public String createToken(UserDto userDto){
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3_600_000);

        String token = JWT.create()
                .withClaim("id", userDto.getId())
                .withIssuer(userDto.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("userName", userDto.getUserName())
                .sign(Algorithm.HMAC256(secretKey));
        return token;
    }

    /**
     * Valide un token JWT et renvoie un objet Authentication.
     *
     * @param token le token JWT à valider
     * @return l'objet Authentication correspondant
     */
    public Authentication validateToken(String token){

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decoded = verifier.verify(token);

        UserDto userDto = UserDto.builder()
                .id(decoded.getClaim("id").asLong())
                .email(decoded.getIssuer())
                .userName(decoded.getClaim("userName").asString())
                .build();

        return new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList());

    }
}
