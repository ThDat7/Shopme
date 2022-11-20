package com.shopme.admin.controller;

import com.shopme.admin.service.UserService;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<?> getAll(
            @RequestParam HashMap<String, String> requestParams
    ) {
        List<User> userList = userService.getAll(requestParams);
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id") int id) {
        userService.delete(id);
    }

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.OK)
    public void createUser(User user, @RequestParam(value = "image", required = false)
                                       MultipartFile multipartFile
                           ) {
        userService.create(user, multipartFile);
    }

    @PostMapping("/{id}/update-status/{status}")
    public void updateStatus(@PathVariable(name = "id") int id,
                             @PathVariable(name = "status") Boolean status) {
        userService.updateStatus(id, status);
    }

    @PostMapping(value = "/edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void editUser(User user, @PathVariable int id,
                         @RequestParam(value = "image", required = false)
                                 MultipartFile multipartFile) {
        userService.edit(id, user, multipartFile);
    }

    @PostMapping("/check-email")
    @ResponseStatus(HttpStatus.OK)
    public void validateEmailUnique(@RequestParam("email") String email) {
        userService.validateEmailUnique(email);
    }
}
