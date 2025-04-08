package a.ynov.back;

import a.ynov.back.service.ReadingService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReadingServiceTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Resource resource;

    private ReadingService readingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        readingService = new ReadingService(resourceLoader, objectMapper);
    }

    @Test
    void readInternalFileAsString() throws IOException {
        String filename = "testFile.txt";
        String expectedContent = "Line 1\nLine 2\nLine 3";

        Resource resource = mock(Resource.class);
        when(resourceLoader.getResource("classpath:" + filename)).thenReturn(resource);
        InputStream inputStream = new ByteArrayInputStream(expectedContent.getBytes(StandardCharsets.UTF_8));
        when(resource.getInputStream()).thenReturn(inputStream);

        String content = readingService.readInternalFileAsString(filename);


        assertEquals(expectedContent, content.replace("\r\n", "\n"));
    }

    @Test
    void readInternalFileAsString_FileNotFound() throws IOException {
        String filename = "nonExistentFile.txt";

        when(resourceLoader.getResource("classpath:" + filename)).thenReturn(resource);
        when(resource.getInputStream()).thenThrow(new FileNotFoundException("Le fichier n'a pas été trouvé"));

        String content = readingService.readInternalFileAsString(filename);

        assertEquals("", content);
    }

    @Test
    void readInternalFileAsList() throws IOException {
        String filename = "testFile.txt";
        List<String> expectedLines = Arrays.asList("Line 1", "Line 2", "Line 3");

        when(resourceLoader.getResource("classpath:" + filename)).thenReturn(resource);
        InputStream inputStream = new ByteArrayInputStream(String.join("\n", expectedLines).getBytes(StandardCharsets.UTF_8));
        when(resource.getInputStream()).thenReturn(inputStream);

        List<String> lines = readingService.readInternalFileAsList(filename);

        assertEquals(expectedLines, lines);
    }

    @Test
    void readInternalFileAsList_FileNotFound() throws IOException {
        String filename = "nonExistentFile.txt";

        when(resourceLoader.getResource("classpath:" + filename)).thenReturn(resource);
        when(resource.getInputStream()).thenThrow(new FileNotFoundException("Le fichier n'a pas été trouvé"));

        List<String> lines = readingService.readInternalFileAsList(filename);

        assertTrue(lines.isEmpty());
    }

    @Test
    void findAllFilesInInternalFolder() throws IOException {
        String folderName = "testFolder";
        File folder = mock(File.class);
        File file1 = mock(File.class);
        File file2 = mock(File.class);
        when(folder.exists()).thenReturn(true);
        when(folder.isDirectory()).thenReturn(true);
        when(folder.listFiles()).thenReturn(new File[] { file1, file2 });
        when(file1.isFile()).thenReturn(true);
        when(file2.isFile()).thenReturn(true);
        when(file1.getName()).thenReturn("file1.txt");
        when(file2.getName()).thenReturn("file2.txt");

        when(resourceLoader.getResource("classpath:" + folderName)).thenReturn(resource);
        when(resource.getFile()).thenReturn(folder);

        List<String> files = readingService.findAllFilesInInternalFolder(folderName);

        assertEquals(Arrays.asList("file1.txt", "file2.txt"), files);
    }

    @Test
    void findAllFilesInInternalFolder_FolderNotFound() throws IOException {
        String folderName = "nonExistentFolder";

        when(resourceLoader.getResource("classpath:" + folderName)).thenReturn(resource);
        when(resource.getFile()).thenThrow(new FileNotFoundException("Le dossier n'a pas été trouvé"));

        List<String> files = readingService.findAllFilesInInternalFolder(folderName);

        assertTrue(files.isEmpty());
    }

    @Test
    void readJson() throws IOException {
        String filename = "test.json";
        MyObject expectedObject = new MyObject("value1", "value2");

        when(resourceLoader.getResource("classpath:" + filename)).thenReturn(resource);
        InputStream inputStream = new ByteArrayInputStream("{\"field1\":\"value1\",\"field2\":\"value2\"}".getBytes(StandardCharsets.UTF_8));
        when(resource.getInputStream()).thenReturn(inputStream);
        when(objectMapper.readValue(inputStream, MyObject.class)).thenReturn(expectedObject);

        MyObject result = readingService.readJson(filename, MyObject.class);

        assertEquals(expectedObject, result);
    }

    @Test
    void readJson_InvalidJson() throws IOException {
        String filename = "invalidFile.json";

        when(resourceLoader.getResource("classpath:" + filename)).thenReturn(resource);
        InputStream inputStream = new ByteArrayInputStream("{key1: value1}".getBytes(StandardCharsets.UTF_8)); // JSON mal formé
        when(resource.getInputStream()).thenReturn(inputStream);

        when(objectMapper.readValue(inputStream, new TypeReference<Map<String, String>>() {}))
                .thenThrow(new IOException("Erreur de parsing JSON"));

        Map<String, String> result = readingService.readJsonAsMap(filename);

        assertNull(result);
    }


    static class MyObject {
        private String field1;
        private String field2;

        public MyObject(String field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            MyObject myObject = (MyObject) obj;
            return Objects.equals(field1, myObject.field1) && Objects.equals(field2, myObject.field2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(field1, field2);
        }
    }
}
