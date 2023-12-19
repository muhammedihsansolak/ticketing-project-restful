package com.cydeo.entity;

import com.cydeo.repository.UserRepository;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Component
public class BaseEntityListener extends AuditingEntityListener {

    private static UserRepository userRepository;
    private static EntityManager entityManager;

    @Autowired
    public void init(UserRepository userRepository, EntityManager entityManager) {
        BaseEntityListener.userRepository = userRepository;
        BaseEntityListener.entityManager = entityManager;
    }
    //https://stackoverflow.com/questions/12155632/injecting-a-spring-dependency-into-a-jpa-entitylistener

    @PrePersist
    private void onPrePersist(BaseEntity baseEntity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof SimpleKeycloakAccount) {
            SimpleKeycloakAccount details = (SimpleKeycloakAccount) authentication.getDetails();
            String username = details.getKeycloakSecurityContext().getToken().getPreferredUsername();//currently logged in person

            baseEntity.setInsertDateTime(LocalDateTime.now());
            baseEntity.setLastUpdateDateTime(LocalDateTime.now());

            if (!authentication.getName().equals("anonymousUser")) {
                User loggedUser = userRepository.findByUserNameAndIsDeleted(username, false);
                baseEntity.setInsertUserId(loggedUser.getId());
                baseEntity.setLastUpdateUserId(loggedUser.getId());
            }
        }
    }

    @PreUpdate
    private void onPreUpdate(BaseEntity baseEntity) {
        Session session = entityManager.unwrap(Session.class);
        session.setHibernateFlushMode(FlushMode.MANUAL);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof SimpleKeycloakAccount) {
            SimpleKeycloakAccount details = (SimpleKeycloakAccount) authentication.getDetails();
            String username = details.getKeycloakSecurityContext().getToken().getPreferredUsername();

            baseEntity.setLastUpdateDateTime(LocalDateTime.now());

            if (!authentication.getName().equals("anonymousUser")) {
                User loggedUser = userRepository.findByUserNameAndIsDeleted(username, false);
                baseEntity.setLastUpdateUserId(loggedUser.getId());
            }
        }
        session.setHibernateFlushMode(FlushMode.AUTO);
    }

}
