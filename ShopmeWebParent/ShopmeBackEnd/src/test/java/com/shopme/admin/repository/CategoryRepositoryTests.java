package com.shopme.admin.repository;

import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {

//    @Autowired
//    private CategoryRepository repo;
//
//    @Test
//    public void testCreateRootCategory() {
//        Category category = new Category("Electronics");
//        Category savedCategory = repo.save(category);
//
//        assertThat(savedCategory.getId()).isGreaterThan(0);
//    }
//
//    @Test
//    public void testCreateSubCategory() {
//        Category parent = new Category(1);
//        Category subCategory = new Category("iPhone", parent);
//        Category savedCategory = repo.save(subCategory);
//
//        assertThat(savedCategory.getId()).isGreaterThan(0);
//    }
//
//    @Test
//    public void testGetCategory() {
//        Category category = repo.findById(1).get();
//        System.out.println(category.getName());
//
//        Set<Category> children = category.getChildren();
//
//        for (Category subCategory : children) {
//            System.out.println(subCategory.getName());
//        }
//
//        assertThat(children.size()).isGreaterThan(0);
//    }
//
//    @Test
//    public void testPrintHierarchicalCategories() {
//        Iterable<Category> categories = repo.findAll();
//
//        for (Category category : categories) {
//            if (category.getParent() == null) {
//                System.out.println(category.getName());
//
//                Set<Category> children = category.getChildren();
//
//                for (Category subCategory : children) {
//                    System.out.println("--" + subCategory.getName());
//                    printChildren(subCategory, 1);
//                }
//            }
//        }
//    }
//
//    private void printChildren(Category parent, int subLevel) {
//        int newSubLevel = subLevel + 1;
//        Set<Category> children = parent.getChildren();
//
//        for (Category subCategory : children) {
//            for (int i = 0; i < newSubLevel; i++) {
//                System.out.print("--");
//            }
//
//            System.out.println(subCategory.getName());
//
//            printChildren(subCategory, newSubLevel);
//        }
//    }
//
//    @Test
//    public void createChildren() {
//        Category parent1 = new Category(1);
//        Category child11 = new Category("c11", parent1);
//        Category child12 = new Category("c12", parent1);
//        Category child13 = new Category("c13", parent1);
//        Category child14 = new Category("c14", parent1);
//        Category child15 = new Category("c15", parent1);
//
//        Category parent2 = new Category(2);
//        Category child21 = new Category("c21", parent2);
//        Category child22 = new Category("c22", parent2);
//        Category child23 = new Category("c23", parent2);
//
//        Category parent3 = new Category(3);
//        Category child31 = new Category("c31", parent3);
//        Category child32 = new Category("c32", parent3);
//
//        repo.saveAll(Arrays.asList(child11, child12, child13, child14, child15));
//        repo.saveAll(Arrays.asList(child21, child22, child23));
//        repo.saveAll(Arrays.asList(child31, child32));
//    }
//
//    @Test
//    public void createChildren2() {
//        Category parent11 = new Category(4);
//        Category child111 = new Category("c111", parent11);
//        Category child112 = new Category("c112", parent11);
//
//        Category parent13 = new Category(6);
//        Category child131 = new Category("c131", parent13);
//        Category child132 = new Category("c132", parent13);
//
//        Category parent32 = new Category(13);
//        Category child321 = new Category("c321", parent32);
//        Category child322 = new Category("c322", parent32);
//
//        repo.saveAll(Arrays.asList(child111, child112));
//        repo.saveAll(Arrays.asList(child131, child132));
//        repo.saveAll(Arrays.asList(child321, child322));
//    }
//
//    @Test
//    public void testFetchLazy() {
//        Category cate = repo.findById(13).get();
//        Category parent = cate.getParent();
//        assertThat(parent.getId()).isGreaterThan(0);
//    }
}