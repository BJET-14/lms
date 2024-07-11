package com.bjet.aki.lms.specification;

import com.bjet.aki.lms.jpa.TeacherEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class TeacherSpecification {

    public static Specification<TeacherEntity> findTeachers(String firstName, String lastName, String email){
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (firstName != null) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"));
            }
            if(lastName != null){
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%"));
            }
            if (email != null) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            query.orderBy(cb.desc(root.get("id")));
            return predicate;
        };
    }
}
