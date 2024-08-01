package com.bjet.aki.lms.jpa;

import com.bjet.aki.lms.Listener.LMSAuditingListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, LMSAuditingListener.class})
public class BaseEntity {

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @Column(name = "CREATE_DATE_TIME", updatable = false)
    private LocalDateTime createDateTime;

    @LastModifiedBy
    private String updatedBy;

    @Column(name = "UPDATE_DATE_TIME")
    private LocalDateTime updateDateTime;
}
