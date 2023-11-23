package dropbox.services;
import org.springframework.scheduling.annotation.Async;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class FileUtil {

    @Async
    public static byte[] compressFile(byte[] file){
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(file);
        deflater.finish();

        ByteArrayOutputStream stream = new ByteArrayOutputStream(file.length);
        byte[] dataHolder = new byte[4 * 1024 ];

        while ( !deflater.finished()){
            int size = deflater.deflate(dataHolder);
            stream.write(dataHolder,0,size);

        }
        try{
        stream.close();

    } catch (Exception e) {
            e.printStackTrace();
        }

        return stream.toByteArray();
    }

    @Async
    public static byte[] deCompressFile(byte[] file) {
        Inflater inflater = new Inflater();
        inflater.setInput(file);
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream(file.length);
            byte[] dataHolder = new byte[4* 1024];
            while(!inflater.finished()){
                int size = inflater.inflate(dataHolder);
                stream.write(dataHolder,0,size);
                stream.close();
                return stream.toByteArray();
            }

        }
        catch (DataFormatException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }


}
