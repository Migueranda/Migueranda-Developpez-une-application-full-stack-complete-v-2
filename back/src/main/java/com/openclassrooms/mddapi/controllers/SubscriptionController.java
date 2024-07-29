package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * Contrôleur gérant les opérations d'abonnement des utilisateurs aux thèmes.
 * Fournit des points de terminaison pour s'abonner et se désabonner des sujthèmesets.
 */

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    /**
     * Abonne un utilisateur à un thème spécifié.
     *
     * @param userId l'identifiant de l'utilisateur
     * @param subjectId l'identifiant du thème indiquant le succès de l'abonnement
     */

    @PostMapping("/subscriptions/{userId}/{subjectId}")
    public ResponseEntity<?> subscribe(@PathVariable Long userId, @PathVariable  Long subjectId) {
        subscriptionService.subscribeUserToSubject(userId, subjectId);
        return ResponseEntity.ok().body("{\"message\": \"User successfully subscribed to subject.\"}");
    }
    /**
     * Désabonne un utilisateur d'un thème spécifié.
     *
     * @param userId l'identifiant de l'utilisateur
     * @param subjectId l'identifiant du thème
     * @return une ResponseEntity avec un message indiquant le succès du désabonnement
     */
    @DeleteMapping("/subscriptions/{userId}/{subjectId}")
    public ResponseEntity<?> unsubscribe(@PathVariable  Long userId, @PathVariable  Long subjectId) {
        subscriptionService.unsubscribeUserFromSubject(userId, subjectId);
        return ResponseEntity.ok().body("{\"message\": \"User successfully unsubscribed from subject.\"}");
    }
}
