
package com.kishan.reporadar.repository;

import com.kishan.reporadar.entity.RepositoryEntity;

import org.springframework.data.jpa.domain.Specification;

public final class RepositorySpecifications {

    private RepositorySpecifications() {
    }

    public static Specification<RepositoryEntity> hasLanguage(
            String language) {

        return (root, query, criteriaBuilder) -> {

            if (language == null || language.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("language")),
                    language.trim().toLowerCase()
            );
        };
    }

    public static Specification<RepositoryEntity> hasMinimumStars(
            Integer minStars) {

        return (root, query, criteriaBuilder) -> {

            if (minStars == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("stars"),
                    minStars
            );
        };
    }
}
