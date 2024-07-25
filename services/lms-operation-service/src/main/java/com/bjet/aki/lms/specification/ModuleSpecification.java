package com.bjet.aki.lms.specification;

import com.bjet.aki.lms.jpa.ModuleEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


public class ModuleSpecification {

    public static Specification<ModuleEntity> findModules(String title) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (title != null) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            query.orderBy(cb.desc(root.get("id")));
            return predicate;
        };
    }

}
