package com.shopme.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String userDirName = "user-photos";
        exposeDirectory(registry, userDirName);

        String categoryDirName = "../category-images";
        exposeDirectory(registry, categoryDirName);

        String brandDirName = "../brand-logos";
        exposeDirectory(registry, brandDirName);

        String productDirName = "../product-images";
        exposeDirectory(registry, productDirName);
    }

    private void exposeDirectory(ResourceHandlerRegistry registry,
                                  String pathPattern) {
        Path path = Paths.get(pathPattern);
        String absolutePath  = path.toFile().getAbsolutePath();

        String logicalPath = pathPattern.replace("../", "") + "/**";

        registry
                .addResourceHandler(logicalPath)
                .addResourceLocations("file:/"+absolutePath+"/");
    }
}
