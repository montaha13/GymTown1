package services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageService {
    private static final String UPLOAD_DIR = "uploads"; // Dossier où stocker les images

    public String uploadImage(File imageFile) throws IOException {
        // Créer le dossier s'il n'existe pas
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générer un nom de fichier unique
        String fileName = System.currentTimeMillis() + "_" + imageFile.getName();
        Path destination = uploadPath.resolve(fileName);

        // Copier le fichier dans le dossier
        Files.copy(imageFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

        // Retourner l'URL de l'image (chemin relatif ou absolu)
        return destination.toUri().toString();
    }
}