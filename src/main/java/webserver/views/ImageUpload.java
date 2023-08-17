package webserver.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import webserver.StaticResourcesConfiguration;
import webserver.models.Image;
import webserver.models.repos.ImagesRepo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/images-upload")
public class ImageUpload {
    private static final String uploadedImagesDir = StaticResourcesConfiguration.publicFolderPath + "images\\";
    @Autowired
    private ImagesRepo imagesRepo;

    @PostMapping
    String imageUpload(@RequestParam("name") String name,
                       @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                name = saveImage(name, file.getBytes());
            } catch (IOException e) {
                return "fail";
            }
            Image image = new Image();
            image.setName(name);
            imagesRepo.save(image);
            return image.getName();
        } else return "file blank";
    }

    private String saveImage(String name, byte[] bytes) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
        String format = name.split("\\.")[1];
        File imageFile = new File(uploadedImagesDir + name);
        if (imageFile.exists()) {
            if (ImageIO.read(imageFile) == bufferedImage)
                return name;
            else {
                name = name.split("\\.")[0] + "-" + UUID.randomUUID() + "." + format;
                imageFile = new File(uploadedImagesDir + name);
            }
        }
        ImageIO.write(bufferedImage, format, imageFile);
        return name;
    }
}
