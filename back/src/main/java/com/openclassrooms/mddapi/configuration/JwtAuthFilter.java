package com.openclassrooms.mddapi.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * Filtre pour l'authentification JWT.
 * Ce filtre est exécuté une fois par requête pour vérifier le token JWT dans l'en-tête de la requête.
 */

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserAuthProvider userAuthProvider;

    /**
     * Filtre les requêtes HTTP pour vérifier l'authentification JWT.
     *
     * @param request  l'objet HttpServletRequest contenant la requête du client
     * @param response l'objet HttpServletResponse contenant la réponse du serveur
     * @param filterChain l'objet FilterChain pour passer la requête/response au filtre suivant
     * @throws ServletException si une erreur de servlet survient
     * @throws IOException si une erreur d'entrée/sortie survient
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null){
            String[] authElements = header.split(" ");

            if (authElements.length == 2 && "Bearer".equals(authElements[0])){
                try {
                    SecurityContextHolder.getContext().setAuthentication(userAuthProvider.validateToken(authElements[1]));
                }catch (RuntimeException e){
                    SecurityContextHolder.clearContext();
                    throw e;
                }
            }
        }

        filterChain.doFilter(request,response);
    }
}
