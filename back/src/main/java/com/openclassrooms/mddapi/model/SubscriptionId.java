package com.openclassrooms.mddapi.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
/**
 * Classe représentant l'identifiant composite pour l'entité Subscription.
 * Composé de l'identifiant de l'utilisateur et de l'identifiant du sujet.
 */
@Setter
@Getter
public class SubscriptionId implements Serializable {
    private Long user;
    private Long subject;

    public SubscriptionId() {
    }

    public SubscriptionId(Long user, Long subject) {
        this.user = user;
        this.subject = subject;
    }

    /**
     * Vérifie l'égalité entre cet objet SubscriptionId et un autre objet.
     *
     * @param o l'objet à comparer avec cet objet SubscriptionId
     * @return true si les objets sont égaux, false sinon
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionId that = (SubscriptionId) o;
        return Objects.equals(user, that.user) && Objects.equals(subject, that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, subject);
    }
}

