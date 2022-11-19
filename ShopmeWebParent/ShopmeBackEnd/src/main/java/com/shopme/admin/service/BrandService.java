package com.shopme.admin.service;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.exception.ImageProcessException;
import com.shopme.admin.exception.ResourceAlreadyExistException;
import com.shopme.admin.exception.ResourceNotFoundException;
import com.shopme.admin.repository.BrandRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.metamodel.Brand_;
import com.shopme.common.paramFilter.BrandParamFilter;
import com.shopme.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

@Service
public class BrandService {
    private static final String BRAND_PHOTO_DIR = "";
    private static final Integer BRAND_PER_PAGE = 10;


    private BrandRepository repo;

    public BrandService(BrandRepository repo) {
        this.repo = repo;
    }

    public Page<Brand> getAll(HashMap<String, String> requestParams) {
        Sort sort = Sort.by(Brand_.NAME).ascending();
        int pageIndex = 0;

        boolean isSearch = false;
        String keywordSearch = "";

        for (String key : requestParams.keySet()) {
            String value = requestParams.get(key);

            BrandParamFilter enumKey;
            try {
                enumKey = BrandParamFilter.valueOf(key);
            } catch(IllegalArgumentException e) { continue; }
            switch (enumKey) {
                case keyword: {
                    isSearch = true;
                    keywordSearch = value;
                    break;
                }

                case order: {
                    if (value.equals("desc")) sort = sort.descending();
                    else sort = sort.ascending();
                    break;
                }

                case sortBy: {
                    sort = sort.by(value);
                    break;
                }

                case page: {
                    if (StringUtils.isInteger(value))
                        pageIndex = Integer.valueOf(value) - 1;
                    break;
                }
            }
        }

        Pageable pageable =  PageRequest.of(pageIndex, BRAND_PER_PAGE, sort);

        if (isSearch) {
            return repo.search(keywordSearch, pageable);
        }

        return repo.findAll(pageable);
    }

    public void create(Brand brand, MultipartFile multipartFile) {
        repo.save(brand);
        saveImage(multipartFile, brand);
    }

    public void edit(int id, Brand brand, MultipartFile multipartFile) {
        brand.setId(id);
        repo.save(brand);
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
        if (repo.existsById(id)) throw new ResourceNotFoundException();
        repo.deleteById(id);
    }

    public void validateNameUnique(String name) {
        if (repo.existsByName(name)) throw new ResourceAlreadyExistException();
    }
}
