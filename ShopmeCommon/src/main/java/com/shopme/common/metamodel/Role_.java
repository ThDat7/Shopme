package com.shopme.common.metamodel;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Role.class)
public class Role_ {
    public static volatile SingularAttribute<Role, Integer> id;
    public static volatile SingularAttribute<Role, String> name;

    public static final String ID = "id";
    public static final String NAME = "name";
}
