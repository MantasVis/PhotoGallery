package lt.insoft.gallery.gallerymodel.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lt.insoft.gallery.gallerymodel.model.Picture;
import lt.insoft.gallery.gallerymodel.model.Tag;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

    List<Picture> getAllBy();

    @Query("select p.pictureBytes from Picture p join p.tags t where t.tag_id = :tag")
    List<byte[]> getAllByTagsMatches(@Param("tag") Long tag);

    @Query("select pictureBytes from Picture where upper(description) like upper(concat('%', :search, '%')) or upper(date) like upper(concat('%', :search, '%'))")
    List<byte[]> getAllByDescriptionContaining(@Param("search") String search);

    @Query("select p.picture_id from Picture p join p.tags t where t.tag_id = :tag")
    List<Long> getPictureIdsByTagsMatches(@Param("tag") Long tag);

    @Query("select picture_id from Picture where upper(description) like upper(concat('%', :search, '%')) or upper(date) like upper(concat('%', :search, '%'))")
    List<Long> getPictureIdsByDescriptionContaining(@Param("search") String search);

    @Query("select p from Picture p where picture_id = :id")
    Picture getPictureById(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("delete from Picture where picture_id = :id")
    //Query("select 1 from dual")
    void deleteByPicture_id(@Param("id") Long id);

    @Query("select p.tags from Picture p where p.picture_id = :id")
    Set<Tag> pictureTagsByPictureId(@Param("id") Long id);
}