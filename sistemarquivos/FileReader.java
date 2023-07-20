package org.example;

import java.io.IOException;
import java.nio.file.Path;

public class FileReader {

    public void read(Path path) {
        try(final java.io.FileReader file = new java.io.FileReader(path.toFile())){
            int conteudo = file.read();
            while (conteudo != -1){
                System.out.print((char) conteudo);
                conteudo = file.read();
            }
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }
}
