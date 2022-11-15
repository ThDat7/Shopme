package com.shopme.common.metamodel;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Category.class)
@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public class Category_ {
    public static volatile SingularAttribute<Category, Integer> id;
    public static volatile SingularAttribute<Category, String> name;
    public static volatile SingularAttribute<Category, String> alias;
    public static volatile SingularAttribute<Category, Category> parent;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ALIAS = "alias";
    public static final String PARENT = "parent";
}
