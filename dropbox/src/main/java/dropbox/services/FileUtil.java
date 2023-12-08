package dropbox.services;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Component
public class FileUtil {
    // To compress the size of the file and save it in the database
    public static byte[] compressFile(byte[] file) {
        if (file == null) {
            // Handle the case where the input array is null
            throw new IllegalArgumentException("Input array cannot be null");
        }
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(file);
        deflater.finish();

        ByteArrayOutputStream stream = new ByteArrayOutputStream(file.length);
        byte[] dataHolder = new byte[4 * 1024];

        while (!deflater.finished()) {
            int size = deflater.deflate(dataHolder);
            stream.write(dataHolder, 0, size);

        }
        try {
            stream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stream.toByteArray();
    }

    // Called when a compressed file from the database is called to be downloaded
    public static byte[] deCompressFile(byte[] file) {
        Inflater inflater = new Inflater();
        inflater.setInput(file);
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream(file.length);
            byte[] dataHolder = new byte[4 * 1024];
            while (!inflater.finished()) {
                int size = inflater.inflate(dataHolder);
                stream.write(dataHolder, 0, size);
                stream.close();
                return stream.toByteArray();
            }

        } catch (DataFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }


}
