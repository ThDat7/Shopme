package com.shopme.common.specification;

import com.shopme.common.entity.User;
import com.shopme.common.metamodel.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class UserSpecification{

    private static Specification<User> createSpecification(Filter filter) {
        String field = filter.getField();
        String value = filter.getValue().toString();

        switch (filter.getOperator()) {
            case EQUALS:
                return (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(field),
                                value);
            case NOT_EQUALS:
                return (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(field),
                                value);
            case GREATER_THAN:
                return (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.greaterThan(root.get(field),
                                value);
            case LESS_THAN:
                return (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.lessThan(root.get(field),
                                value);
            case LIKE:
                return (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(field),
                                "%" + value + "%");
            case IN:
                return (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.in(root.get(field))
                                .value(filter.getValues());
            default:
                throw new RuntimeException("Not supported this operator");
        }
    }

    public static Specification<User> FilterSpecification(List<Filter> filters) {
        Specification<User> specification = Specification.where(createSpecification(filters.remove(0)));

        for (Filter input : filters) {
            specification = specification.and(createSpecification(input));
        }

        return specification;
    }

    public static Specification<User> likeEmail(String email) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get(User_.EMAIL),
                        "%" + email + "%");
    }
}
