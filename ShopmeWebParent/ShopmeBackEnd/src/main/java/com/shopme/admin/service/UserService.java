package com.shopme.admin.service;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.repository.UserRepository;
import com.shopme.common.entity.User;
import com.shopme.common.exception.ImageProcessException;
import com.shopme.common.exception.ResourceAlreadyExistException;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.common.metamodel.*;
import com.shopme.common.paramFilter.ProductParamFilter;
import com.shopme.common.paramFilter.RequestParamsHelper;
import com.shopme.common.paramFilter.UserParamFilter;
import com.shopme.common.specification.Filter;
import com.shopme.common.specification.SpecificationHelper;
import com.shopme.common.specification.SpecificationOperator;
import com.shopme.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {
    public static final int USER_PER_PAGE = 10;
    public static final String USER_PHOTO_DIR = "user-photos/";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    public Page<User> getAll(HashMap<String, String> requestParams) {

        Specification specification = Specification.not(null);

        for (String key : requestParams.keySet()) {
            String value = requestParams.get(key);

            UserParamFilter enumKey;
            try {
                enumKey = UserParamFilter.valueOf(key);
            } catch(IllegalArgumentException e) { continue; }
            switch (enumKey) {
                case keyword: {
                    String keywordSearch = value;
                    Filter searchLastName = Filter.builder()
                            .field(User_.LASTNAME).build();

                    Filter searchFirstName = Filter.builder()
                            .field(User_.FIRSTNAME).build();

                    Filter searchEmail = Filter.builder()
                            .field(User_.EMAIL).build();

                    List<Filter> searchFilters = Arrays.asList(
                            searchLastName, searchFirstName, searchEmail
                    );

                    searchFilters.forEach(filter -> {
                        filter.setValue(keywordSearch);
                        filter.setOperator(SpecificationOperator.LIKE);
                    });

                    specification = specification.and(SpecificationHelper
                            .filterSpecification(searchFilters));

                    break;
                }

                case roleId: {
                    int roleId = Integer.valueOf(value);

                    Filter matchRoleId = Filter.builder()
                            .joinTables(Arrays.asList(User_.ROLES))
                            .field(Role_.ID)
                            .value(roleId)
                            .operator(SpecificationOperator.IN).build();

                    Specification matchRoleIdSpec = SpecificationHelper
                            .createSpecification(matchRoleId);

                    specification.and(
                            matchRoleIdSpec
                    );
                    break;
                }
            }
        }

        Pageable pageable = RequestParamsHelper.getPageableFromParamRequest(requestParams);

        return userRepository.findAll(specification, pageable);
    }

    public User findById(int id) {
        return userRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }

    public void create(User user, MultipartFile multipartFile) {
        encodePassword(user);
        userRepository.save(user);
        saveImage(multipartFile, user);
    }

    public void edit(int id, User user, MultipartFile multipartFile) {
        user.setId(id);

        if (user.getPassword().isEmpty() || user.getPassword() == null) {
            String oldEncodedPassword = userRepository.getPasswordByEmail(user.getEmail());
            user.setPassword(oldEncodedPassword);
        } else encodePassword(user);
        userRepository.save(user);

        saveImage(multipartFile, user);
    }

    private void saveImage(MultipartFile multipartFile, User user) {
        if (multipartFile == null || multipartFile.isEmpty()) return;

        String fileName = org.springframework.util.StringUtils
                .cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = USER_PHOTO_DIR + user.getId();

        if (user.getPhotos() != null && !user.getPhotos().isEmpty())
            FileUploadUtil.cleanDir(uploadDir);

        try {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            user.setPhotos(fileName);
        } catch (IOException e) {
            throw new ImageProcessException();
        }
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public User getByEmail(String s) {
        return userRepository.findByEmail(s)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void validateEmailUnique(String email) {
        if (userRepository.existsByEmail(email))
            throw new ResourceAlreadyExistException();
    }

    public void updateStatus(int id, Boolean status) {
        if (!userRepository.existsById(id))
            throw new ResourceNotFoundException();
        userRepository.updateStatus(id, status);
    }
}
