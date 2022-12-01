package com.shopme.common.metamodel;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Product.class)
@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public class Product_ {

    public static volatile SingularAttribute<Product, Integer> id;
    public static volatile SingularAttribute<Product, String> name;
    public static volatile SingularAttribute<Product, String> shortDescription;
    public static volatile SingularAttribute<Product, String> fullDescription;
    public static volatile SingularAttribute<Product, Category> category;
    public static volatile SingularAttribute<Product, Brand> brand;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String SHORT_DESCRIPTION = "shortDescription";
    public static final String FULL_DESCRIPTION = "fullDescription";
    public static final String CATEGORY = "category";
    public static final String BRAND = "brand";
}
