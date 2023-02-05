package com.shopme.admin.service;

import com.shopme.common.exception.ResourceAlreadyExistException;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.admin.repository.CategoryRepository;
import com.shopme.common.entity.Category;
import com.shopme.common.metamodel.Category_;
import com.shopme.common.paramFilter.ProductParamFilter;
import com.shopme.common.paramFilter.RequestParamsHelper;
import com.shopme.common.specification.Filter;
import com.shopme.common.specification.SpecificationHelper;
import com.shopme.common.specification.SpecificationOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
public class CategoryService {
    private static final int CATEGORY_PER_PAGE = 10;
    private final String PREFIX_SUB_CATEGORY = "--";

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Page<Category> getAll(HashMap<String, String> requestParams) {
        Specification specification = Specification.not(null);

        for (String key : requestParams.keySet()) {
            String value = requestParams.get(key);

            ProductParamFilter enumKey;
            try {
                enumKey = ProductParamFilter.valueOf(key);
            } catch(IllegalArgumentException e) { continue; }
            switch (enumKey) {
                case keyword: {
                    String keywordSearch = value;
                    Filter searchName = Filter.builder()
                            .field(Category_.NAME).build();

                    Filter searchId = Filter.builder()
                            .field(Category_.ID).build();

                    List<Filter> searchFilters = Arrays.asList(
                            searchName, searchId
                    );

                    searchFilters.forEach(filter -> {
                        filter.setValue(keywordSearch);
                        filter.setOperator(SpecificationOperator.LIKE);
                    });

                    specification = specification.and(SpecificationHelper
                            .filterSpecification(searchFilters));

                    break;
                }
            }
        }

        Pageable pageable = RequestParamsHelper.getPageableFromParamRequest(requestParams);

        return categoryRepository.findAll(specification, pageable);
    }

    public Category getById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void create(Category category) {
        setAllParentIds(category);
        categoryRepository.save(category);
    }

    public void edit(int id, Category category) {
        category.setId(id);
        setAllParentIds(category);
        categoryRepository.save(category);
    }

    public void delete(int id) {
        categoryRepository.deleteById(id);
    }

    private void setAllParentIds(Category category) {
        Category parent = category.getParent();

        if (parent == null) return;

        String allParentIds = parent.getAllParentIds() == null ? "-" : parent.getAllParentIds();
        allParentIds += parent.getId() + "-";
        category.setAllParentIds(allParentIds);
    }

    public void validateNameUnique(String name) {
        boolean exists = categoryRepository.existsByName(name);
        if (exists) throw new ResourceAlreadyExistException();
    }

    public void validateAliasUnique(String alias) {
        boolean exists = categoryRepository.existsByAlias(alias);
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
                .name(origin.getName())
                .alias(origin.getAlias())
                .image(origin.getImage())
                .enabled(origin.isEnabled())
                .build();

        copy.setId(origin.getId());
        return copy;
    }
    private Category copyFull(Category origin, String name) {
        Category copy = copyFull(origin);
        copy.setName(name);
        return copy;
    }

    public List<Category> listCategoryUsedInForm() {
        List<Category> roots = categoryRepository.getParent();

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
                .name(origin.getName())
                .build();

        copy.setId(origin.getId());
        copy.setName(name);
        return copy;
    }
}


