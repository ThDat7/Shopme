package com.shopme.admin.service;

import com.shopme.admin.exception.ResourceAlreadyExistException;
import com.shopme.admin.exception.ResourceNotFoundException;
import com.shopme.admin.repository.CategoryRepository;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;
import com.shopme.common.metamodel.Category_;
import com.shopme.common.metamodel.User_;
import com.shopme.common.paramFilter.CategoryParamFilter;
import com.shopme.common.paramFilter.UserParamFilter;
import com.shopme.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {
    private static final int CATEGORY_PER_PAGE = 10;
    private final String PREFIX_SUB_CATEGORY = "--";

    @Autowired
    private CategoryRepository repository;

    @PersistenceContext
    private EntityManager em;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> getAll(HashMap<String, String> requestParams) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Category> cq = cb.createQuery(Category.class);
        Root<Category> root = cq.from(Category.class);
        cq.select(root);

        Boolean isSearch = false;

        Integer pageNumber = 0;
        Sort sort = Sort.by(User_.ID);

        for (String key :  requestParams.keySet()) {
            String value = requestParams.get(key);

            CategoryParamFilter enumKey;
            try {
                enumKey = CategoryParamFilter.valueOf(key);
            } catch(RuntimeException rte) { continue; }

            switch (enumKey) {
                case keyword: {
                    String keyword = "%" + value + "%";

                    Predicate pd;
                    pd = cb.like(root.get(Category_.NAME), keyword);
                    pd = cb.or(pd, cb.like(root.get(Category_.ALIAS), keyword));
                    if (StringUtils.isInteger(value))
                        pd = cb.or(pd, cb.equal(root.get(Category_.ID),
                                Integer.valueOf(value)));
                    cq.where(pd);
                    isSearch = true;
                    break;
                }

//                case order: {
//                    if (value.equals("asc")) sort = sort.ascending();
//                    else if (value.equals("desc")) sort = sort.descending();
//                    break;
//                }
//
//                case sortBy: {
//                    sort = sort.by(value);
//                    break;
//                }
//
//                case page: {
//                    if (StringUtils.isInteger(value))
//                        pageNumber = Integer.valueOf(value) - 1;
//                    break;
//                }
            }
            if (isSearch) break;
        }

//        List<Order> orders = QueryUtils.toOrders(sort, root, cb);
//        cq.orderBy(orders);

        if (!isSearch) {
            Predicate pd;
            pd = cb.isNull(root.get(Category_.PARENT));
            cq.where(pd);
        }

        List<Category> origin =  em.createQuery(cq)
                .setFirstResult(pageNumber * CATEGORY_PER_PAGE)
                .setMaxResults(CATEGORY_PER_PAGE)
                .getResultList();

        List<Category> result;

        if (isSearch) {
            result = new ArrayList<>();
            for (Category copy : origin)
                result.add(copyFull(copy));
        } else {
            result = listHierarchicalCategories(origin);
        }

        return result;
    }

    public Category getById(int id) {
        return repository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void create(Category category) {
        repository.save(category);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public void edit(int id, Category category) {
        repository.save(category);
    }

    public void validateNameUnique(String name) {
        boolean exists = repository.existsByName(name);
        if (exists) throw new ResourceAlreadyExistException();
    }

    public void validateAliasUnique(String alias) {
        boolean exists = repository.existsByAlias(alias);
        if (exists) throw new ResourceAlreadyExistException();
    }

    private List<Category> listHierarchicalCategories(List<Category> roots) {
        List<Category> result = new ArrayList<>();

        for (Category root : roots) {
            if (root.getParent() != null) continue;

            result.add(copyFull(root));
            Set<Category> children = root.getChildren();

            for (Category child : children) {
                String name = PREFIX_SUB_CATEGORY + child.getName();
                result.add(copyFull(child, name));
                listChildren(result, child, 1);
            }
        }

        return result;
    }

    private void listChildren(List<Category> result, Category parent, int level) {
        int newLevel = level + 1;
        Set<Category> children = parent.getChildren();

        if (children == null) return;

        for (Category child : children) {
            String name = "";
            for (int i = 0; i < newLevel; i++)
                name += "--";
            name += child.getName();

            result.add(copyFull(child, name));
            listChildren(result, child, newLevel);
        }
    }

    private Category copyFull(Category origin) {
        Category copy = Category.builder()
                .id(origin.getId())
                .name(origin.getName())
                .alias(origin.getAlias())
                .image(origin.getImage())
                .enabled(origin.isEnabled())
                .build();


        return copy;
    }
    private Category copyFull(Category origin, String name) {
        Category copy = copyFull(origin);
        copy.setName(name);
        return copy;
    }

    public List<Category> listCategoryUsedInForm() {
        List<Category> roots = repository.getParent();

        List<Category> cateUsedInForm = new ArrayList<>();

        for (Category root : roots) {
            if (root.getParent() != null) continue;

            cateUsedInForm.add(copyIdAndName(root, root.getName()));
            Set<Category> children = root.getChildren();

            for (Category child : children) {
                String name = PREFIX_SUB_CATEGORY + child.getName();
                cateUsedInForm.add(copyIdAndName(child, name));
                listSubCategoriesUsedInForm(cateUsedInForm, child, 1);
            }
        }

        return cateUsedInForm;
    }

    private void listSubCategoriesUsedInForm(List<Category> cateUsedInForm, Category parent, int level) {
        int newLevel = level + 1;
        Set<Category> children = parent.getChildren();

        if (children == null) return;

        for (Category child : children) {
            String name = "";
            for (int i = 0; i < newLevel; i++)
                name += "--";
            name += child.getName();

            cateUsedInForm.add(copyIdAndName(child, name));
            listSubCategoriesUsedInForm(cateUsedInForm, child, newLevel);
        }
    }

    private Category copyIdAndName(Category origin, String name) {
        Category copy = Category.builder()
                .id(origin.getId())
                .name(origin.getName())
                .build();

        copy.setName(name);
        return copy;
    }
}


