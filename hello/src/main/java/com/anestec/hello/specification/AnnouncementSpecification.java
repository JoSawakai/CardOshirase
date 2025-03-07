package com.anestec.hello.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.anestec.hello.model.Announcement;

import jakarta.persistence.criteria.Predicate;

public class AnnouncementSpecification {

    public static Specification<Announcement> filterByConditions(String title, String category, String registrationDate, String startDate, String endDate) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (category != null && !category.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("category")), "%" + category.toLowerCase() + "%"));
            }
            if (registrationDate != null) {
                predicates.add(criteriaBuilder.equal(root.get("registrationDate"), registrationDate));
            }
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate));
            }

            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.notEqual(root.get("deleteFlg"), "1"),
                    criteriaBuilder.isNull(root.get("deleteFlg")),
                    criteriaBuilder.equal(root.get("deleteFlg"), "")
            ));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
