package lt.insoft.gallery.gallerymodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import lt.insoft.gallery.gallerymodel.model.Picture;
import lt.insoft.gallery.gallerymodel.model.Tag;
import lt.insoft.gallery.gallerymodel.repository.PictureRepository;
import lt.insoft.gallery.gallerymodel.repository.TagRepository;

import static org.fest.assertions.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DatabaseTest {

    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testSavePicture() {
        Picture picture = getPicture();
        Picture savedInDb = entityManager.persist(picture);
        Picture getFromDb = pictureRepository.getPictureById(savedInDb.getPicture_id());

        assertThat(getFromDb).isEqualTo(savedInDb);
    }

    @Test
    public void whenFindByName_thenReturnTag() {
        // given
        Tag testTag = new Tag("Test");
        entityManager.persist(testTag);
        entityManager.flush();

        // when
        Tag found = tagRepository.getTagByName(testTag.getName());

        // then
        assertThat(found.getName()).isEqualTo(testTag.getName());
    }

    private Picture getPicture() {
        byte[] b = new byte[10000];
        new Random().nextBytes(b);

        String desc = "Test description";

        DateFormat format  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
        Date now = new Date();
        String date = format.format(now);

        return new Picture(b, desc, date);
    }
}
