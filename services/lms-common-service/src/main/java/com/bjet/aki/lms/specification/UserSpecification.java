package com.bjet.aki.lms.specification;

import com.bjet.aki.lms.domain.Role;
import com.bjet.aki.lms.jpa.UserEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<UserEntity> findUsers(String firstName, String lastName, String email, Role role){
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
            if (role != null) {
                predicate = cb.and(predicate, cb.equal(root.get("role"), role));
            }
            query.orderBy(cb.desc(root.get("id")));
            return predicate;
        };
    }
}
