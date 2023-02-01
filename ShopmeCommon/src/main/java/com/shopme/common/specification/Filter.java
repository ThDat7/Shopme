package com.shopme.common.specification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class Filter {
    private String field;
    private SpecificationOperator operator;
    private Object value;
    private List<Object> values;
    private List<String> joinTables;

}
