package recordstore.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileService {
    void saveFile(String filename, MultipartFile multipartFile) throws IOException;
    void deleteFile(String filename) throws IOException;
}
