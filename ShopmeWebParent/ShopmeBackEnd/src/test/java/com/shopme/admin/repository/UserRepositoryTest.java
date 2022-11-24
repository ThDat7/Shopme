package com.shopme.admin.repository;

import com.shopme.admin.repository.UserRepository;
import com.shopme.admin.service.UserService;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DataJpaTest
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void createUser() {
//        Role role = new Role();
//
//        List<User> userList = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            String s = Integer.toString(i+1);
//            role.setId(i+1);
//
//            User user = new User(s, "12345", s, s,
//                    "", true, Stream.of(role).collect(Collectors.toSet()));
//            userList.add(user);
//        }
//
//
//        userRepository.saveAll(userList);
//    }
//
//    @Test
//    public void saveDuplicate() {
//        Role role = new Role();
//        role.setId(1);
//
//        User user = new User("1", "12345", "1", "1",
//                "", true, Stream.of(role).collect(Collectors.toSet()));
//        userRepository.save(user);
//    }
//
//    @Test
//    public void saveSucces() {
//        Role role = new Role();
//        role.setId(1);
//
//        User user = new User("succes", "12345", "1", "1",
//                "", true, Stream.of(role).collect(Collectors.toSet()));
//        user.setId(1);
//        userRepository.save(user);
//    }
//
//    @Test
//    public void deleteSucces() {
//        userRepository.deleteByEmail("succes");
//    }

}
