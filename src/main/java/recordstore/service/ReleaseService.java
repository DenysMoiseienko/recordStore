package recordstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import recordstore.entity.Genre;
import recordstore.entity.Release;

import java.io.IOException;
import java.util.List;

public interface ReleaseService {

    Release getRelease(long id);
    void saveRelease(Release release) throws IOException;
    void deleteRelease(long id) throws IOException;

    Page<Release> getAllReleases(Pageable pageable);
    Page<Release> getReleasesByGenre(long id, Pageable pageable);
    Page<Release> getReleasesByArtist(long id, Pageable pageable);
    Page<Release> getReleasesByLabel(long id, Pageable pageable);
    Page<Release> getCollectionByAccount(long id, Pageable pageable);
    Page<Release> getWantListByAccount(long id, Pageable pageable);

    List<String> search(String keyword);
    Release getReleaseByTitle(String title);
}