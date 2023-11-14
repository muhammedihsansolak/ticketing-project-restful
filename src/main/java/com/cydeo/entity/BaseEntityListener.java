package com.cydeo.entity;

import com.cydeo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntityListener extends AuditingEntityListener {

    private UserRepository userRepository;

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
    }

}
