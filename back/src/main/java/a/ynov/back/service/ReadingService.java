package a.ynov.back.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReadingService {

    private static final Logger logger = LoggerFactory.getLogger(ReadingService.class);

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @Autowired
    public ReadingService(@Qualifier("webApplicationContext") ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    /**
     * Lit un fichier interne et retourne son contenu sous forme de chaîne de caractères.
     */
    public String readInternalFileAsString(String filename) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getInternalResource(filename).getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            logger.error("Erreur lors de la lecture du fichier {}", filename, e);
            return "";
        }
    }

    /**
     * Lit un fichier interne ligne par ligne et retourne les lignes sous forme de liste de chaînes.
     */
    public List<String> readInternalFileAsList(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getInternalResource(filename).getInputStream(), StandardCharsets.UTF_8))) {
            reader.lines().forEach(lines::add);
        } catch (IOException e) {
            logger.error("Erreur lors de la lecture du fichier {}", filename, e);
        }
        return lines;
    }

    /**
     * Trouve tous les fichiers dans un dossier interne et retourne leurs noms sous forme de liste.
     */
    public List<String> findAllFilesInInternalFolder(String folderName) {
        List<String> fileNames = new ArrayList<>();
        try {
            File folder = getInternalResource(folderName).getFile();
            if (folder.exists() && folder.isDirectory()) {
                File[] listOfFiles = folder.listFiles();
                if (listOfFiles != null) {
                    for (File file : listOfFiles) {
                        if (file.isFile()) {
                            fileNames.add(file.getName());
                        }
                    }
                } else {
                    logger.warn("Le dossier est vide ou n'existe pas: {}", folderName);
                }
            }
        } catch (IOException e) {
            logger.error("Erreur lors de l'accès au dossier {}", folderName, e);
        }
        return fileNames;
    }

    /**
     * Permet d'extraire le contenu d'un fichier JSON et de le convertir en objet.
     */
    public <T> T readJson(String filename, Class<T> toMapClass) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + filename);
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, toMapClass);
        }
    }

    /**
     * Lit un fichier JSON et retourne son contenu sous forme de Map<String, String>.
     */
    public Map<String, String> readJsonAsMap(String filename) {
        Resource resource = resourceLoader.getResource("classpath:" + filename);
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            logger.error("Erreur lors de la lecture du fichier JSON {}", filename, e);
            return Collections.emptyMap();
        }
    }

    /**
     * Charge une ressource interne en utilisant le ResourceLoader.
     */
    private Resource getInternalResource(String fileName) {
        return resourceLoader.getResource("classpath:" + fileName);
    }
}
