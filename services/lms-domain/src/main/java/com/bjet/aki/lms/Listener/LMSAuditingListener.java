package com.bjet.aki.lms.Listener;

import com.bjet.aki.lms.jpa.BaseEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.time.LocalDateTime;

@Configurable
public class LMSAuditingListener implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @PrePersist
    public void onPrePersist(BaseEntity baseEntity) {
//        autowire();
        baseEntity.setCreatedBy(null);
        baseEntity.setCreateDateTime(LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate(BaseEntity baseEntity) {
//        autowire();
        baseEntity.setUpdatedBy(null);
        baseEntity.setUpdateDateTime(LocalDateTime.now());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LMSAuditingListener.applicationContext = applicationContext;
    }
}
