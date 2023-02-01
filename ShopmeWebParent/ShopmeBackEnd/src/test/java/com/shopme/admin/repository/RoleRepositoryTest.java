package com.shopme.admin.repository;

import com.shopme.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTest {

//    @Autowired
//    private RoleRepository repository;
//
//    @Test
//    public void createAllRole() {
//        Role admin = new Role("Admin", "Quan ly moi thu");
//        Role editor = new Role("Editor", "Quan ly articles, categories, brands, products va menus");
//        Role saler = new Role("Salesperson", "Quan ly product price" +
//                ", customers, shipping, orders va sales report");
//        Role  shipper = new Role("Shipper", "Xem products, orders va update order status");
//        Role assist = new Role("Assistant", "Quan ly questions va reviews");
//
//        List<Role> roles = Arrays.asList(admin, editor, saler, shipper, assist);
//
//        repository.saveAll(roles);
//    }
}
