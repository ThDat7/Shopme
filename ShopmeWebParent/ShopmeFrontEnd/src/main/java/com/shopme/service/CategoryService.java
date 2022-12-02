package com.shopme.service;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listNoChildren() {
        List<Category> listNoChildren = new ArrayList<>();

        List<Category> listEnabled = categoryRepository.findAllEnabled();

        listEnabled.forEach(category -> {
            Set<Category> children = category.getChildren();
            if (children == null || children.size() == 0)
                listNoChildren.add(category);
        });

        return listNoChildren;
    }

    public List<Category> getCategoryParents(String category_alias) {
        Category child = categoryRepository.findByAlias(category_alias)
                .orElseThrow(ResourceNotFoundException::new);

        List<Category> listParent = new ArrayList<>();

        Category parent = child.getParent();

        while(parent != null) {
            listParent.add(0, parent);
            parent = parent.getParent();
        }

        listParent.add(child);

        return listParent;
    }
}
