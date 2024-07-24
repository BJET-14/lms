package com.bjet.aki.lms.Listener;

import com.bjet.aki.lms.jpa.BaseEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LMSAuditingListener{

    @PrePersist
    public void onPrePersist(BaseEntity baseEntity) {
//        autowire();
        baseEntity.setCreatedBy("Public");
        baseEntity.setCreateDateTime(LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate(BaseEntity baseEntity) {
//        autowire();
        baseEntity.setUpdatedBy("Public");
        baseEntity.setUpdateDateTime(LocalDateTime.now());
    }
}
