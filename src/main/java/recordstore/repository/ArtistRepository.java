package recordstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import recordstore.entity.Artist;
import recordstore.projections.ArtistProjection;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    @Query(value = "SELECT id, name FROM artists WHERE name LIKE :keyword% LIMIT 5", nativeQuery = true)
    List<ArtistProjection> findAllBy(@Param("keyword") String keyword);

    @Query(value = "SELECT name FROM artists WHERE name LIKE :keyword%", nativeQuery = true)
    List<String> search(@Param("keyword") String keyword);
    Artist findArtistByName(String name);
}