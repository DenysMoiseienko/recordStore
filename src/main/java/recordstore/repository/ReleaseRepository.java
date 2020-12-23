package recordstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import recordstore.entity.Release;

import java.util.List;

public interface ReleaseRepository extends JpaRepository<Release, Long> {

    @Query(value = "SELECT title FROM releases WHERE title LIKE %:keyword%", nativeQuery = true)
    List<String> search(@Param("keyword") String keyword);
    Release findReleaseByTitle(String title);
}