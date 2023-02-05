package com.shopme.admin.controller;

import com.shopme.admin.dto.UserCreateDTO;
import com.shopme.admin.dto.UserListDTO;
import com.shopme.admin.service.UserService;
import com.shopme.common.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.function.Function;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<?> getAll(
            @RequestParam HashMap<String, String> requestParams
    ) {
        return ResponseEntity.ok(
                mapToDTO(userService.getAll(requestParams))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        UserListDTO userListDTO = mapToDTO(userService.findById(id));
        return ResponseEntity.ok(userListDTO);
    }

    @PostMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id") int id) {
        userService.delete(id);
    }

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.OK)
    public void createUser(@RequestPart("user") UserCreateDTO userCreateDTO,
                           @RequestPart(value = "image", required = false)
                                       MultipartFile multipartFile
                           ) {
        User user = mapToEntity(userCreateDTO);
        userService.create(user, multipartFile);
    }

    @PostMapping("/{id}/update-status/{status}")
    public void updateStatus(@PathVariable(name = "id") int id,
                             @PathVariable(name = "status") Boolean status) {
        userService.updateStatus(id, status);
    }

    @PostMapping(value = "/edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void editUser(@RequestPart("user") UserCreateDTO userCreateDTO, @PathVariable int id,
                         @RequestPart(value = "image", required = false)
                                 MultipartFile multipartFile) {
        User user = mapToEntity(userCreateDTO);
        userService.edit(id, user, multipartFile);
    }

    @PostMapping("/check-email")
    @ResponseStatus(HttpStatus.OK)
    public void validateEmailUnique(@RequestParam("email") String email) {
        userService.validateEmailUnique(email);
    }

    private User mapToEntity(UserListDTO userListDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userListDTO, User.class);
    }

    private User mapToEntity(UserCreateDTO userCreateDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userCreateDTO, User.class);
    }

    private Page<UserListDTO> mapToDTO(Page<User> userPage) {
        return userPage.map(new Function<User, UserListDTO>() {
            @Override
            public UserListDTO apply(User user) {
                return mapToDTO(user);
            }
        });
    }

    private UserListDTO mapToDTO(User user) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(user, UserListDTO.class);
    }
}
