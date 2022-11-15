package com.shopme.common.metamodel;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Role.class)
public class Role_ {
    public static volatile SingularAttribute<User, String> name;

    public static String NAME = "name";
}
