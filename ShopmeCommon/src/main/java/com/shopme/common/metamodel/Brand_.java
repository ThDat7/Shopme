package com.shopme.common.metamodel;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Set;

@StaticMetamodel(Brand.class)
@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public class Brand_ {
    public static volatile SingularAttribute<Brand, Integer> id;
    public static volatile SingularAttribute<Brand, String> name;
    public static volatile SingularAttribute<Brand, String> logo;
    public static volatile SingularAttribute<Brand, Set<Category>> categories;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String LOGO = "logo";
    public static final String CATEGORIES = "categories";
}
