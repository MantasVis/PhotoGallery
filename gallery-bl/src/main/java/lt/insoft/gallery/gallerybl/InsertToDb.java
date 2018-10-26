package lt.insoft.gallery.gallerybl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lt.insoft.gallery.gallerymodel.model.Picture;
import lt.insoft.gallery.gallerymodel.model.Tag;
import lt.insoft.gallery.gallerymodel.model.User;
import lt.insoft.gallery.gallerymodel.model.UserRole;
import lt.insoft.gallery.gallerymodel.repository.PictureRepository;
import lt.insoft.gallery.gallerymodel.repository.TagRepository;
import lt.insoft.gallery.gallerymodel.repository.UserRepository;
import lt.insoft.gallery.gallerymodel.repository.UserRoleRepository;

@Component
public class InsertToDb {

    private final PictureRepository pictureRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public InsertToDb(PictureRepository pictureRepository, TagRepository tagRepository, UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.pictureRepository = pictureRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    //---------------------------------TAG REPOSITORY--------------------------------------
    /**
     *  Returns name of a tag by its name
     */
    private String getTagNameByName(String name) {
        return tagRepository.getTagNameByName(name);
    }

    /**
     *  Returns tag entity by its name
     */
    private Tag getTagByName(String name) {
        return tagRepository.getTagByName(name);
    }

    /**
     *  Returns a picture by its id
     */
    private Picture getPictureById(Long id) {
        return pictureRepository.getPictureById(id);
    }

    /**
     *  Returns tags by picture id
     */
    public Set<Tag> pictureTagsByPictureId(Long id) {
        return pictureRepository.pictureTagsByPictureId(id);
    }

    //----------------------------------USER REPOSITORY----------------------------------------
    /**
     *  Returns user by username
     */
    private User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     *  Returns user count
     */
    private int getUserCount() {
        return userRepository.getUserCount();
    }

    /**
     *  Returns date by username
     */
    public String dateByUsername(String username) {
        return userRepository.findDateByUsername(username);
    }
    //---------------------------------USER ROLE REPOSITORY-------------------------------------
    /**
     *  Returns role by name
     */
    private UserRole getUserRoleByRole(String role) {
        return userRoleRepository.getUserRoleByRole(role);
    }

    /**
     *  Returns username by role id
     */
    public String usernameByRoleId(Long id) {
        User user = userRoleRepository.findUsernameByRoleId(id);
        return user.getUsername();
    }

    /**
     *  Returns role name by role id
     */
    public String roleById(Long id) {
        return userRoleRepository.findRoleNameById(id);
    }

    /**
     *  Returns role by id
     */
    public UserRole findRoleById(Long id) {
        return userRoleRepository.findRoleById(id);
    }
    //------------------------------------------------------------------------------------------

    public void newPicture(Object[] photoData) {

        if(photoData.length > 0) {
            DateFormat format  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
            Date now = new Date();

            byte[] photo = (byte[]) photoData[0];
            String description = (String) photoData[1];
            if (description.equals(""))
            {
                description = null;
            }
            String date = format.format(now);
            Picture picture = new Picture(photo, date, description);
            Set<Tag> tagSet = new HashSet<>();

            String tags = (String) photoData[2];
            ArrayList<String> tagList = new ArrayList<>(Arrays.asList(tags.split(", ")));
            if (tags.equals(""))
            {
                tagSet = null;
            } else {
                generateTags(picture, tagSet, tagList);
            }

            picture.setTags(tagSet);
            pictureRepository.save(picture);
        }
    }

    public void editPhotoInfo(Long id, String description, String tags) {
        Picture picture = getPictureById(id);
        Set<Tag> newTagSet = new HashSet<>();
        Set<Tag> originalTagSet = picture.getTags();

        if (description.equals("")) {
            description = null;
        }
        if (!tags.equals("")) {
            ArrayList<String> tagList = new ArrayList<>(Arrays.asList(tags.split(", ")));
            generateTags(picture, newTagSet, tagList);
        } else {
            newTagSet = null;
        }

        picture.setDescription(description);
        picture.setTags(newTagSet);
        pictureRepository.save(picture);
        checkForEmptyTags(originalTagSet);
    }

    private void generateTags(Picture picture, Set<Tag> newTagSet, ArrayList<String> tagList) {
        for (String s : tagList) {
            if (getTagNameByName(s) == null) {
                Tag tag = new Tag(s);
                Set<Picture> pictureSet = new HashSet<>();
                pictureSet.add(picture);
                tag.setPictures(pictureSet);
                newTagSet.add(tag);
                tagRepository.save(tag);
            } else {
                newTagSet.add(getTagByName(s));
            }
        }
    }

    public void deletePhoto(Long id) {
        Picture picture = getPictureById(id);
        removeTags(picture);
        pictureRepository.delete(picture);
    }

    private void removeTags(Picture p) {
        Set<Tag> tags = p.getTags();
        for(Tag t : tags) {
            t.getPictures().remove(p);

            if (t.getPictures().size() == 0) {
                tagRepository.deleteByTagName(t.getName());
            }
        }
    }

    private void checkForEmptyTags(Set<Tag> tags) {
        for(Tag t : tags) {
            if (t.getPictures().size() == 0) {
                tagRepository.deleteByTagName(t.getName());
            }
        }
    }

    public String registerNewAccount(String username, String password, String repeatPassword) {
        if (!password.equals(repeatPassword))
        {
            return "The passwords did not match";
        }

        String encryptedPassword = bCryptPasswordEncoder.encode(password);

        User exists = findByUsername(username);
        if (exists != null) {
            return "Username already exists";
        }

        DateFormat format  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
        Date now = new Date();
        String date = format.format(now);

        User newUser = new User(username, encryptedPassword, date);
        UserRole role;

        if (getUserCount() == 0) {
            role = new UserRole("ADMIN");
        } else {
            role = new UserRole("USER");
        }
        role.setUser(newUser);
        userRepository.save(newUser);
        userRoleRepository.save(role);

        return "Registration complete";
    }

    public void updateRoles(String roleName, Long id) {
        UserRole role = findRoleById(id);
        role.setRole(roleName);
        userRoleRepository.save(role);
    }
}
