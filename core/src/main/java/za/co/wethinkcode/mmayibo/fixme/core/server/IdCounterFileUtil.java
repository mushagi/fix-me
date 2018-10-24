package za.co.wethinkcode.mmayibo.fixme.core.server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;
import java.io.IOException;

public class IdCounterFileUtil {
    private static final String filePath = "counter.txt";
    private static File file = new File(filePath);;


    public static int getCounter() {
        int counter = 100000;

        if (!Files.exists(file.toPath())){
            try {
                Files.createFile(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return counter;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            counter = Integer.parseInt(line);
        } catch (NumberFormatException | IOException e) {
            //Error
        }
        return counter;
    }

    public static void saveCounter(int idCounter) {
        Path path = Paths.get(filePath);
        byte[] strToBytes = String.valueOf(idCounter).getBytes();

        try {
            Files.write(path, strToBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
