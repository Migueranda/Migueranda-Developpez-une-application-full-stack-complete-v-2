package com.openclassrooms.mddapi.model.entities;

import com.openclassrooms.mddapi.model.SubscriptionId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "subscription")
@IdClass(SubscriptionId.class)
public class Subscription {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Subject subject;

}
