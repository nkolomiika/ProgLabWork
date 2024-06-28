package org.example.utils.io.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public final class CustomFileReader {

    @Getter
    private File file;
    private BufferedReader fileReader;

    public void setFile(String filename) throws IOException {
        this.file = new File(filename);
        this.fileReader = new BufferedReader(new FileReader(file));
    }

    public String readLine() throws IOException {
        String inputLine = fileReader.readLine();
        if (Objects.isNull(inputLine)) throw new EOFException("File successfully read!");
        return inputLine.trim();
    }
}
