package kr.co.eceris.demojava11.files;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesTest {

    @Test
    public void readStringTest() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/filestest.txt");
        Path path = Path.of(classPathResource.getURI());
        String text = Files.readString(path);
        System.out.println(text);
    }

    @Test
    public void writeStringTest() throws IOException {
        Path tempFile = Files.createTempFile("temp", "txt");
        Files.writeString(tempFile, "파일에 \n String을 \n 써볼까? \n");
        String text = Files.readString(tempFile);
        System.out.println(text);
    }
}
