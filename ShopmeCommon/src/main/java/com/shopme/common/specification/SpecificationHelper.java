package com.shopme.common.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

public class SpecificationHelper {

    public static Specification<?> createSpecification(Filter filter) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            String value = filter.getValue().toString();
            Expression rootField = null;

            if (filter.getJoinTables() != null && !filter.getJoinTables().isEmpty())
                rootField = getExpressionJoinTable(filter, root).get(filter.getField());
            else rootField = root.get(filter.getField());

            switch (filter.getOperator()) {
                case EQUALS:
                    return criteriaBuilder.equal(rootField, value);
                case NOT_EQUALS:
                    return criteriaBuilder.notEqual(rootField, value);
                case GREATER_THAN:
                    return criteriaBuilder.greaterThan(rootField, Integer.valueOf(value));
                case LESS_THAN:
                    return criteriaBuilder.lessThan(rootField, value);
                case LIKE:
                    return criteriaBuilder.like(rootField, "%" + value + "%");
                case IN:
                    return criteriaBuilder.in(rootField).value(filter.getValues());
                default:
                    throw new RuntimeException("Not supported this operator");
            }
        };
    }


    private static Join getExpressionJoinTable(Filter filter, Root root) {
        List<String> joinTable = filter.getJoinTables();

        Join join = root.join(joinTable.get(0));

        for (int i = 1; i < joinTable.size(); i++)
            join = join.join(joinTable.get(i));

        return join;
    }

    public static Specification<?> filterSpecification(List<Filter> filters) {
        Specification specification = createSpecification(filters.get(0));

        for (int i = 1; i < filters.size(); i++)
            specification.and(createSpecification(filters.get(i)));

        return specification;
    }
}
