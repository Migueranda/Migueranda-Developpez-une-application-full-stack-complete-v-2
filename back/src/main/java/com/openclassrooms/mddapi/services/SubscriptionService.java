package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.SubscriptionId;
import com.openclassrooms.mddapi.model.entities.Subject;
import com.openclassrooms.mddapi.model.entities.Subscription;
import com.openclassrooms.mddapi.model.entities.UserEntity;
import com.openclassrooms.mddapi.repositories.SubjectRepository;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service fournissant la gestion des abonnements des utilisateurs aux sujets.
 * Gère l'ajout et la suppression des abonnements entre les utilisateurs et les sujets.
 */

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    /**
     * Abonne un utilisateur à un thème.
     *
     * @param userId l'identifiant de l'utilisateur à abonner.
     * @param subjectId l'identifiant du thème auquel l'utilisateur s'abonne
     * @throws EntityNotFoundException si l'utilisateur ou le thème n'existe pas
     * @throws IllegalStateException si l'abonnement existe déjà
     * @throws DataIntegrityViolationException en cas de violation d'intégrité des données lors de la création de l'abonnement
     */

    public void subscribeUserToSubject(Long userId, Long subjectId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found with id: " + userId));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Subject not found with id: " + subjectId));

        SubscriptionId subscriptionId = new SubscriptionId(userId, subjectId);
        Optional<Subscription> existingSubscription = subscriptionRepository.findById(subscriptionId);
        if (existingSubscription.isPresent()) {
            throw new IllegalStateException("Subscription already exists for given user and subject");
        }

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setSubject(subject);

        try {
            subscriptionRepository.save(subscription);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Failed to create subscription due to data integrity violation", e);
        }
    }

    /**
     *Désabonne un utilisateur d'un thème.
     * @param userId l'identifiant de l'utilisateur à désabonner.
     * @param subjectId  l'identifiant du thème dont l'utilisateur se désabonne.
     * @throws EntityNotFoundException si l'abonnement n'existe pas.
     */
    public void unsubscribeUserFromSubject(Long userId, Long subjectId) {
        SubscriptionId subscriptionId = new SubscriptionId();
        subscriptionId.setUser(userId);
        subscriptionId.setSubject(subjectId);
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
        subscriptionRepository.delete(subscription);
    }
}
