package com.openclassrooms.mddapi.services;

public interface ISubscriptionService {

    void subscribeUserToSubject(Long userId, Long subjectId);

    void unsubscribeUserFromSubject(Long userId, Long subjectId);
}
