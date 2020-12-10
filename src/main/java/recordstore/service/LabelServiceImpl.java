package recordstore.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import recordstore.entity.Label;
import recordstore.projections.LabelProjection;
import recordstore.repository.LabelRepository;
import recordstore.utils.FileUploadUtil;

import java.io.IOException;
import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {

    @Value("${upload.path}")
    String path;

    private final LabelRepository repository;

    public LabelServiceImpl(LabelRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveLabel(Label label) throws IOException {
        String filename = StringUtils.cleanPath(label.getData().getOriginalFilename());
        String removePicture = label.getImg();

        if(!label.getData().isEmpty()) {
            label.setImg(filename);
        }
        repository.save(label);
        if(!label.getData().isEmpty()) {
            FileUploadUtil.saveFile(filename, label.getData(), path);
            FileUploadUtil.deleteFile(removePicture, path);
        }
    }

    @Override
    public void deleteLabel(long id) throws IOException {
        Label label = repository.getOne(id);
        if (label.getReleases().size() == 0) {
            repository.deleteById(id);
            FileUploadUtil.deleteFile(label.getImg(), path);
        }
    }

    @Override
    public Label getLabel(long id) {
        return repository.getOne(id);
    }

    @Override
    public List<LabelProjection> getAllLabelsTitles() {
        return repository.findAllBy();
    }

    @Override
    public Page<Label> getAllLabels(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<String> search(String keyword) {
        return repository.search(keyword);
    }

    @Override
    public Label getLabelByTitle(String title) {
        return repository.findLabelByTitle(title);
    }
}