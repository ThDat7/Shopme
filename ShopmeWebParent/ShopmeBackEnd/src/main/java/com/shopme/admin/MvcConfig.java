package com.shopme.admin;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MvcConfig implements WebMvcConfigurer {
    private static final String LOCATION = "file:/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String userDirName = "user-photos";
        registerResource(registry, userDirName);

        String categoryDirName = "../category-images";
        registerResource(registry, categoryDirName);

        String brandDirName = "../brand-logos";
        registerResource(registry, brandDirName);
    }

    private void registerResource(ResourceHandlerRegistry registry,
                                  String pathPattern) {
        Path path = Paths.get(pathPattern);
        String absolutePath  = path.toFile().getAbsolutePath();

        String logicalPath = pathPattern.replace("../", "") + "/**";

        registry
                .addResourceHandler(logicalPath)
                .addResourceLocations("file:/"+absolutePath+"/");
    }
}
