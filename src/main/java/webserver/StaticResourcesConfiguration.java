package webserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourcesConfiguration implements WebMvcConfigurer {
    public final static String publicFolderPath = System.getProperty("user.dir") + "\\src\\main\\resources\\public\\";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/public/**")
                .addResourceLocations("file:" + publicFolderPath)
                .setCachePeriod(3600);
    }
}