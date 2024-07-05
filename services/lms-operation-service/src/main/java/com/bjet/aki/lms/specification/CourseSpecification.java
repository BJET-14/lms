package com.bjet.aki.lms.specification;

import com.bjet.aki.lms.jpa.CourseEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;


public class CourseSpecification {

    public static Specification<CourseEntity> findCourses(String title, LocalDate startDateFrom, LocalDate startDateTo, Boolean isComplete){
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (title != null) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (startDateFrom != null) {
                predicate =
                        cb.and(
                                predicate,
                                cb.greaterThanOrEqualTo(
                                        root.get("startDate"), startDateFrom));
            }

            if (startDateTo != null) {
                predicate =
                        cb.and(
                                predicate,
                                cb.lessThanOrEqualTo(root.get("startDate"), startDateTo));
            }

            if(isComplete != null){
                predicate = cb.and(predicate, cb.equal(root.get("isComplete"), isComplete));
            } else {
                predicate = cb.and(predicate, cb.isFalse(root.get("isComplete")));
            }
            query.orderBy(cb.desc(root.get("id")));
            return predicate;
        };
    }

}
