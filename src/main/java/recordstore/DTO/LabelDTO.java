package recordstore.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import recordstore.entity.Release;
import recordstore.validation.MultipartFileSize;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class LabelDTO {

    private long id;

    @NotBlank(message = "Field is mandatory")
    private String title;

    private String country;

    @Size(max = 2000, message = "Description too long, max size 2000")
    private String description;

    private String img = "noImageAvailable.png";

    @MultipartFileSize
    private MultipartFile data;

    private List<Release> releases = new ArrayList<>();
}
