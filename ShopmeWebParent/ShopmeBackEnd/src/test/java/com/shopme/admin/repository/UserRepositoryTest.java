package com.shopme.admin.repository;

import com.shopme.common.entity.User;
import com.shopme.common.metamodel.Role_;
import com.shopme.common.metamodel.User_;
import com.shopme.common.specification.Filter;
import com.shopme.common.specification.SpecificationOperator;
import com.shopme.common.specification.SpecificationHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

//    @Test
//    public void testPredicateFilterUser() {
//        Filter emailFilter = Filter.builder()
//                .field(User_.EMAIL)
//                .value("aecllc.bnk@gmail.com")
//                .operator(SpecificationOperator.LIKE)
//                .build();
//
//        Filter idFilter = Filter.builder()
//                .field(User_.ID)
//                .value(2)
//                .operator(SpecificationOperator.GREATER_THAN)
//                .build();
//
//        Filter lastNameFilter = Filter.builder()
//                .field(User_.LASTNAME)
//                .value("Kitchell")
//                .operator(SpecificationOperator.LIKE)
//                .build();
//
//        Filter roleNameFilter = Filter.builder()
//                .joinTables(Arrays.asList(User_.ROLES))
//                .field(Role_.NAME)
//                .value("Salesperson")
//                .operator(SpecificationOperator.LIKE)
//                .build();
//
//        Specification<User> spec = (Specification<User>) SpecificationHelper
////                .createSpecification(roleNameFilter);
//                .filterSpecification(Arrays.asList(idFilter, emailFilter, lastNameFilter, roleNameFilter));
//
//        List<User> user = repository.findAll(spec);
//
//        assertThat(user.get(0).getId()).isEqualTo(3);
//
//    }
//
//    @Test
//    public void testGetAllBySpecification() {
//        Specification spec = Specification.not(null);
//
//        List<User> users = repository.findAll(spec);
//
//        assertThat(users.size()).isEqualTo(26);
//    }

    @Test
    public void testAndOrFilter() {

    }

//    email = aecllc.bnk@gmail.com
    // id > 2
    // lastname Like Kitchell

    // role name = Salesperson

}
