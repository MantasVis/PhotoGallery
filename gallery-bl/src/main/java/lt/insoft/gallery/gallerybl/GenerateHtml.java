package lt.insoft.gallery.gallerybl;

import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import lt.insoft.gallery.gallerymodel.model.Picture;
import lt.insoft.gallery.gallerymodel.model.Tag;
import lt.insoft.gallery.gallerymodel.repository.PictureRepository;
import lt.insoft.gallery.gallerymodel.repository.TagRepository;

@Service
public class GenerateHtml {

    private final String insertPhotoDiv = "<div class=\"col-lg-3 col-md-4 col-xs-6 thumb\"> <a href=\"/image?image=";
    private final String insertPhoto = "\"><img class=\"img-thumbnail\" src=\"data:image/jpg;base64, ";

    private final PictureRepository pictureRepository;
    private final TagRepository tagRepository;

    public GenerateHtml(PictureRepository pictureRepository, TagRepository tagRepository)
    {
        this.pictureRepository = pictureRepository;
        this.tagRepository = tagRepository;
    }

    //--------------------------------PICTURE REPOSITORY--------------------------------------
    /**
     *  Returns all pictures
     */
    private List<Picture> getAllPictures() {
        return pictureRepository.getAllBy();
    }

    /**
     *  Returns pictures that match specific tag
     */
    private List<byte[]> getPicturesByTag(Long tag) {
        return pictureRepository.getAllByTagsMatches(tag);
    }

    /**
     *  Returns pictures that match the search criteria
     */
    private List<byte[]> getPicturesBySearch(String search) {
        return pictureRepository.getAllByDescriptionContaining(search);
    }

    /**
     *  Returns picture IDs by tag ID
     */
    private List<Long> getPictureIdsByTagId(Long tag) {
        return pictureRepository.getPictureIdsByTagsMatches(tag);
    }
    /**
     *  Returns picture IDs that match the search criteria
     */
    private List<Long> getPictureIdsBySearch(String search) {
        return pictureRepository.getPictureIdsByDescriptionContaining(search);
    }

    /**
     *  Returns picture by ID
     */
    private Picture getPictureById(Long id) {
        return pictureRepository.getPictureById(id);
    }
    //---------------------------------TAG REPOSITORY--------------------------------------
    /**
     *  Returns all tags
     */
    private List<Tag> getAllTags() {
        return tagRepository.getAllBy();
    }
    //--------------------------------------------------------------------------------------


    public String createDivs() {
        List<Picture> pictures = getAllPictures();
        StringBuilder html = new StringBuilder();

        for (Picture picture : pictures) {
            byte[] photo = picture.getPictureBytes();
            String img = Base64.getEncoder().encodeToString(photo);
            html.append(insertPhotoDiv).append(picture.getPicture_id()).append(insertPhoto).append(img).append("\"></a></div>");
        }
        return html.toString();
    }

    public String createDivs(Long tag) {
        List<byte[]> pictures = getPicturesByTag(tag);
        List<Long> id = getPictureIdsByTagId(tag);
        return buildHtmlString(pictures, id);
    }

    public String createDivsSearch(String search) {
        List<byte[]> pictures = getPicturesBySearch(search);
        List<Long> id = getPictureIdsBySearch(search);
        return buildHtmlString(pictures, id);
    }

    private String buildHtmlString(List<byte[]> pictures, List<Long> id) {
        StringBuilder html = new StringBuilder();

        for (int i = 0; i < pictures.size(); i++) {
            byte[] photo = pictures.get(i);
            String img = Base64.getEncoder().encodeToString(photo);
            html.append(insertPhotoDiv).append(id.get(i)).append(insertPhoto).append(img).append("\"></a></div>");
        }
        return html.toString();
    }

    public String createTags() {
        List<Tag> tags = getAllTags();
        StringBuilder html = new StringBuilder();

        for (Tag t : tags) {
            String tag = t.getName();
            html.append("<a href=\"/tags?tag=").append(t.getTag_id()).append("\">").append(tag).append("</a>");
        }
        return html.toString();
    }

    public String createImageSrc(Long id) {
        Picture picture = getPictureById(id);
        byte[] image = picture.getPictureBytes();
        String img = Base64.getEncoder().encodeToString(image);
        return "data:image/jpg;base64, " + img;
    }

    public String getImageDescription(Long id) {
        Picture picture = getPictureById(id);
        return picture.getDescription();
    }

    public String getImageDate(Long id) {
        Picture picture = getPictureById(id);
        return picture.getDate();
    }
}