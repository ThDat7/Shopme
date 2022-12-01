package com.shopme.admin.service;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.exception.ImageProcessException;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
public class ProductImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductImageService.class);

    private static final String PRODUCT_MAIN_IMAGE_DIR = "../product-images/";

    private static final String PRODUCT_EXTRA_IMAGE_SUB_DIR = "/extras";

    public void saveImage(MultipartFile mainImageMultipart,
                          MultipartFile[] extraImageMultipart,
                          Product product) {
        setMainImageName(mainImageMultipart, product);
        setExtraImageName(extraImageMultipart, product);

        saveUploadedImages(mainImageMultipart, extraImageMultipart, product);
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
            if (!containsImageName(product, fileName)) {
                product.addExtraImage(fileName);
            }
        }
    }

    private boolean containsImageName(Product product, String fileName) {
        Iterator<ProductImage> iterator = product.getImages().iterator();

        while (iterator.hasNext()) {
            ProductImage image = iterator.next();
            if (image.getName().equals(fileName)) {
                return true;
            }
        }

        return false;
    }

    public void saveUploadedImages(MultipartFile mainImageMultipart,
                                   MultipartFile[] extraImageMultipart, Product product) {
        saveMainImage(mainImageMultipart, product);
        saveExtraImages(extraImageMultipart, product);
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

    public void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
        if (imageIDs == null || imageIDs.length == 0) return;

        Set<ProductImage> images = new HashSet<>();

        for (int i = 0; i < imageIDs.length; i++) {
            Integer id = Integer.valueOf(imageIDs[i]);
            String name = imageNames[i];

            images.add(new ProductImage(id, name, product));
        }

        product.setImages(images);
    }

    public void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
        setExtraImageName(extraImageMultiparts, product);
    }

    public void deleteExtraImagesWereRemovedOnForm(Product product) {
        String extraImageDir = PRODUCT_MAIN_IMAGE_DIR
                + product.getId()
                + PRODUCT_EXTRA_IMAGE_SUB_DIR;

        Path dirPath = Paths.get(extraImageDir);

        try {
            Files.list(dirPath).forEach(file -> {
                String fileName = file.toFile().getName();

                if (!containsImageName(product, fileName)) {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        LOGGER.error("Could not delete extra image: " + fileName);
                    }
                }
            });
        } catch (IOException e) {
            LOGGER.error("Could not list directory: " + dirPath);
        }
    }
}
