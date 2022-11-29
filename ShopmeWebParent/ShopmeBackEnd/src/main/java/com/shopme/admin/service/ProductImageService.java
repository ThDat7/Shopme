package com.shopme.admin.service;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.exception.ImageProcessException;
import com.shopme.common.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Service
public class ProductImageService {
    private static final String PRODUCT_MAIN_IMAGE_DIR = "../product-images/";

    private static final String PRODUCT_EXTRA_IMAGE_SUB_DIR = "/extras";

    public void saveImage(MultipartFile mainImageMultipart,
                          MultipartFile[] extraImageMultipart,
                          Product product) {
        setMainImageName(mainImageMultipart, product);
        setExtraImageName(extraImageMultipart, product);

        saveMainImage(mainImageMultipart, product);
        saveExtraImages(extraImageMultipart, product);
    }

    public void setMainImageName(MultipartFile mainImageMultipart, Product product) {
        if (! hasFile(mainImageMultipart))
            return;

        String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
        product.setMainImage(fileName);
    }

    public void setExtraImageName(MultipartFile[] extraImageName, Product product) {
        for (MultipartFile imageFile : extraImageName) {
            if (! hasFile(imageFile)) continue;

            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            product.addExtraImage(fileName);
        }
    }

    private void saveMainImage(MultipartFile mainImageMultipart,
                               Product product) {
        if (! hasFile(mainImageMultipart)) return;

        String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
        String uploadDir = PRODUCT_MAIN_IMAGE_DIR + product.getId();
        FileUploadUtil.cleanDir(uploadDir);

        try {
            FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
        } catch (IOException e) {
            throw new ImageProcessException();
        }
    }

    private void saveExtraImages(MultipartFile[] extraImageMultipart,
                                 Product product) {
        String uploadDir = PRODUCT_MAIN_IMAGE_DIR
                + product.getId()
                + PRODUCT_EXTRA_IMAGE_SUB_DIR;

        for (MultipartFile imageFile : extraImageMultipart) {
            if (! hasFile(imageFile)) continue;

            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            try {
                FileUploadUtil.saveFile(uploadDir, fileName, imageFile);
            } catch (IOException e) {
                throw new ImageProcessException();
            }
        }
    }

    private boolean hasFile(MultipartFile multipartFile) {
        return (!multipartFile.isEmpty() && multipartFile != null);
    }

    public void removeImageDir(int id) {
        String productImageDir = PRODUCT_MAIN_IMAGE_DIR + id;
        String productExtraImageDir = productImageDir + PRODUCT_EXTRA_IMAGE_SUB_DIR;

        FileUploadUtil.removeDir(productExtraImageDir);
        FileUploadUtil.removeDir(productImageDir);
    }
}
