package com.bjet.aki.lms.specification;

import com.bjet.aki.lms.jpa.FileManagementEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class FileManagementSpecification {

    public static Specification<FileManagementEntity> findFiles(Long courseId, Long moduleId){
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (courseId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("courseId"), courseId));
            }
            if (moduleId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("moduleId"), moduleId));
            }
            query.orderBy(cb.desc(root.get("id")));
            return predicate;
        };
    }
}
