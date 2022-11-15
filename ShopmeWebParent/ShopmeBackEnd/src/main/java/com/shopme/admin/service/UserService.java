package com.shopme.admin.service;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.exception.ImageProcessException;
import com.shopme.admin.exception.ResourceAlreadyExistException;
import com.shopme.admin.exception.ResourceNotFoundException;
import com.shopme.admin.repository.UserRepository;
import com.shopme.common.entity.User;
import com.shopme.common.metamodel.User_;
import com.shopme.common.paramFilter.UserParamFilter;
import com.shopme.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

    public List<User> getAll(HashMap<String, String> requestParams) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);

        Integer pageNumber = 0;
        Sort sort = Sort.by(User_.ID);

        for (String key :  requestParams.keySet()) {
            String value = requestParams.get(key);

            UserParamFilter enumKey;
            try {
                enumKey = UserParamFilter.valueOf(key);
            } catch(RuntimeException rte) { continue; }

            switch (enumKey) {
                case keyword: {
                    String keyword = "%" + value + "%";
                    Predicate pd;

                    pd = cb.like(root.get(User_.FIRSTNAME), keyword);
                    pd = cb.or(pd, cb.like(root.get(User_.LASTNAME), keyword));
                    pd = cb.or(pd, cb.like(root.get(User_.EMAIL), keyword));
                    if (StringUtils.isInteger(value))
                            pd = cb.or(pd, cb.equal(root.get(User_.ID),
                                    Integer.valueOf(value)));
                    cq.where(pd);
                    break;
                }

                case order: {
                    if (value.equals("asc")) sort = sort.ascending();
                    else if (value.equals("desc")) sort = sort.descending();
                    break;
                }

                case sortBy: {
                    sort = sort.by(value);
                    break;
                }

                case page: {
                    if (StringUtils.isInteger(value))
                        pageNumber = Integer.valueOf(value) - 1;
                    break;
                }
            }
        }

        List<Order> orders = QueryUtils.toOrders(sort, root, cb);
        cq.orderBy(orders);

        return em.createQuery(cq)
                .setFirstResult(pageNumber * USER_PER_PAGE)
                .setMaxResults(USER_PER_PAGE)
                .getResultList();
    }

    public User findById(int id) {
        return userRepository.findById(id).get();
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }

    public void create(User user, MultipartFile multipartFile) {
        encodePassword(user);
        saveImage(multipartFile, user);
    }

    public void edit(int id, User user, MultipartFile multipartFile) {
        user.setId(id);
        userRepository.save(user);
        saveImage(multipartFile, user);
    }

    private void saveImage(MultipartFile multipartFile, User user) {
        if (multipartFile == null || multipartFile.isEmpty()) return;

        String fileName = org.springframework.util.StringUtils
                .cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = USER_PHOTO_DIR + user.getId();

        if (user.getPhoto() != null && !user.getPhoto().isEmpty())
            FileUploadUtil.cleanDir(uploadDir);

        try {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            user.setPhoto(fileName);
        } catch (IOException e) {
            throw new ImageProcessException();
        }
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public User getByEmail(String s) {
        return userRepository.findByEmail(s).orElseThrow(ResourceNotFoundException::new);
    }

    public void validateEmailUnique(String email) {
        Optional<User> findByEmail = userRepository.findByEmail(email);
        findByEmail.ifPresent(u -> { throw new ResourceAlreadyExistException(); });
    }

    public void updateStatus(int id, Boolean status) {
        userRepository.updateStatus(id, status);
    }
}
