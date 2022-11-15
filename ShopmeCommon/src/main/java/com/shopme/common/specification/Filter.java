package com.shopme.common.specification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Filter {
    public String field;
    public SpecificationOperator operator;
    public Object value;
    public List<Object> values;
}
