package lt.insoft.gallery.gallerymodel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lt.insoft.gallery.gallerymodel.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> getAllBy();

    @Query("select name from Tag where name = :name")
    String getTagNameByName(@Param("name") String name);

    @Query("select t from Tag t where t.name = :name")
    Tag getTagByName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("delete from Tag where name = :name")
    void deleteByTagName(@Param("name") String name);
}
