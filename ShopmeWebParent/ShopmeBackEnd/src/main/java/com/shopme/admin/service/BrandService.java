package com.shopme.admin.service;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.exception.ImageProcessException;
import com.shopme.common.exception.ResourceAlreadyExistException;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.admin.repository.BrandRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.metamodel.Brand_;
import com.shopme.common.metamodel.Category_;
import com.shopme.common.metamodel.Product_;
import com.shopme.common.paramFilter.ProductParamFilter;
import com.shopme.common.paramFilter.RequestParamsHelper;
import com.shopme.common.specification.Filter;
import com.shopme.common.specification.SpecificationHelper;
import com.shopme.common.specification.SpecificationOperator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class BrandService {
    private static final String BRAND_PHOTO_DIR = "../brand-logos/";
    private static final Integer BRAND_PER_PAGE = 10;


    private BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Page<Brand> getAll(HashMap<String, String> requestParams) {
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
                            .field(Brand_.NAME).build();

                    searchName.setOperator(SpecificationOperator.LIKE);
                    searchName.setValue(keywordSearch);

                    Filter searchId = Filter.builder()
                            .field(Brand_.ID).build();

                    searchId.setOperator(SpecificationOperator.LIKE);
                    searchId.setValue(keywordSearch);

                    specification = specification.and(SpecificationHelper
                            .filterSpecification(Arrays.asList(searchName, searchId)));

                    break;
                }
            }
        }

        Pageable pageable = RequestParamsHelper.getPageableFromParamRequest(requestParams);

        return brandRepository.findAll(specification, pageable);
    }

    public void create(Brand brand, MultipartFile multipartFile) {
        brandRepository.save(brand);
        saveImage(multipartFile, brand);
    }

    public void edit(int id, Brand brand, MultipartFile multipartFile) {
        brand.setId(id);
        brandRepository.save(brand);
        saveImage(multipartFile, brand);
    }

    private void saveImage(MultipartFile multipartFile, Brand brand) {
        if (multipartFile == null || multipartFile.isEmpty()) return;

        String fileName = org.springframework.util.StringUtils
                .cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = BRAND_PHOTO_DIR + brand.getId();

        if (brand.getLogo() != null && !brand.getLogo().isEmpty())
            FileUploadUtil.cleanDir(uploadDir);

        try {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            brand.setLogo(fileName);
        } catch (IOException e) {
            throw new ImageProcessException();
        }
    }

    public void delete(int id) {
        if (brandRepository.existsById(id)) throw new ResourceNotFoundException();
        brandRepository.deleteById(id);
    }

    public void validateNameUnique(String name) {
        if (brandRepository.existsByName(name)) throw new ResourceAlreadyExistException();
    }

    public List<Category> getCategoriesByBrandId(int id) {
        return brandRepository.getCategoriesById(id);
    }
}
