package lt.insoft.gallery.galleryui;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lt.insoft.gallery.gallerybl.GenerateHtml;
import lt.insoft.gallery.gallerybl.InsertToDb;
import lt.insoft.gallery.gallerymodel.model.Picture;
import lt.insoft.gallery.gallerymodel.model.Tag;

@Controller
public class MainController {

    private final GenerateHtml generateHtml;
    private final InsertToDb insertToDb;

    private final String tags = "tags";
    private final String pictures = "pictures";
    private final String image = "imageSource";
    private final String tagValue = "tagValue";
    private final String imageId = "imageId";
    private final String table = "table";

    private final String htmlGallery = "gallery";
    private final String htmlUpload = "upload";
    private final String htmlLogin = "login";
    private final String htmlImageView = "imageView";
    private final String htmlEdit = "editImage";
    private final String htmlDelete = "deleteImage";
    private final String htmlAdmin = "adminMenu";
    private final String htmlAdminUsers = "adminUsers";
    private final String htmlRegister = "register";
    private final String htmlEditUsers = "editUser";

    private final String redirectToMain = "redirect:/";
    private final String redirectUpload = "redirect:/upload";

    private String redirect;

    public MainController(GenerateHtml generateHtml, InsertToDb insertToDb) {

        this.generateHtml = generateHtml;
        this.insertToDb = insertToDb;
    }

    @RequestMapping(value = "/")
    public String gallery(Map<String, Object> model) {

        String pictures = this.allPictures();
        if(pictures.equals(""))
        {
            model.put("message", "There are no pictures in the gallery");
        } else {
            model.put(this.pictures, this.allPictures());
        }
        model.put(tags, this.allTags());
        return htmlGallery;
    }

    @RequestMapping(value = {"/", "/tags"}, params = "search")
    public String gallery(Map<String, Object> model, @RequestParam("search") String search, RedirectAttributes redirectAttributes){

        String pictures = this.picturesWithSearch(search);
        if (pictures.equals(""))
        {
            model.put("message", "No pictures found");
        } else {
            model.put(this.pictures, this.picturesWithSearch(search));
        }
        model.put(tags, this.allTags());
        return htmlGallery;
    }

    @RequestMapping(value = "/tags")
    public String gallery(Map<String, Object> model, @RequestParam("tag") Long tag){

        model.put(pictures, this.picturesWithTag(tag));
        model.put(tags, this.allTags());
        return htmlGallery;
    }

    @RequestMapping(value = "/login")
    public String login(Map<String, Object> model){
        model.put(tags, this.allTags());
        return htmlLogin;
    }

    @RequestMapping(value = "/login", params = "logout")
    public String logout(HttpServletRequest request)
    {
        return redirectToCurrent(request);
    }

    @RequestMapping(value = "/login", params = "status")
    public String loginFromMain(HttpServletRequest request, @RequestParam("status") String status) {
        if (status.equals("main"))
        {
            return redirectToCurrent(request);
        }
        return htmlLogin;
    }

    @RequestMapping(value = "/upload")
    public String upload(Map<String, Object> model) {
        model.put(tags, this.allTags());
        return htmlUpload;
    }

    @RequestMapping(value = "/upload", consumes = {"multipart/form-data"}, method = { RequestMethod.POST, RequestMethod.GET})
    public String upload(RedirectAttributes redirectAttributes,
            @RequestParam("photo-url") MultipartFile file, @RequestParam("photo-desc") String description, @RequestParam("photo-tags") String tags) {

        String[] acceptableTypes = {"png", "jpg", "jpeg", "jpe", "jfif", "bmp", "gif"};
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return redirectUpload;
        } else {

            for (String s : acceptableTypes) {
                if (extension.equals(s)) {
                    try {
                        byte[] bytes = file.getBytes();
                        Object[] photoData = {bytes, description, tags};
                        insertToDb.newPicture(photoData);
                        redirectAttributes.addFlashAttribute("message", "Succesfully uploaded '" + file.getOriginalFilename() + "'");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return redirectUpload;
                }
            }
            redirectAttributes.addFlashAttribute("message", "Unsupported file type '" + extension + "'. Please select another image file");
            return redirectUpload;
        }
    }

    @RequestMapping(value = "/image", params = "image")
    public String imageView(Map<String, Object> model, @RequestParam("image") Long imageId) {
        model.put(this.imageId, imageId);
        model.put(image, this.imageSrc(imageId));
        model.put(tags, this.allTags());
        model.put("imageDescription", this.imageDescription(imageId));
        model.put("imageDate", this.imageDate(imageId));
        return htmlImageView;
    }

    @RequestMapping(value = "/edit", method = { RequestMethod.POST, RequestMethod.GET})
    public String editImage(HttpServletRequest request, Map<String, Object> model, @RequestParam Map<String, String> paramValues) {

        String editParams = String.valueOf(paramValues);

        if (paramValues.size() >= 1) {
            String referer = request.getHeader("Referer");
            Object[] params = paramValues.values().toArray();

            if (editParams.contains("photo-desc"))
            {
                String imageIdString = String.valueOf(params[2]);
                Long imageId = Long.valueOf(imageIdString);
                editPhotoInfo(imageId, String.valueOf(params[0]), String.valueOf(params[1]));
                return redirect;
            } else {
                String[] link = referer.split("/");
                redirect = "redirect:/" + link[3];
                String imageIdString = String.valueOf(params[0]);
                Long imageId = Long.valueOf(imageIdString);

                Set<Tag> pictureTags = this.pictureTagsByPictureId(imageId);
                if(pictureTags.size() > 0) {
                    StringBuilder tagListBuilder = new StringBuilder();
                    for (Tag t : pictureTags) {
                        tagListBuilder.append(t.getName()).append(", ");
                    }
                    String tagList = String.valueOf(tagListBuilder);
                    tagList = tagList.substring(0, tagList.length() - 2);
                    model.put(tagValue, tagList);
                } else {
                    model.put(tagValue, "");
                }
                model.put(this.imageId, imageId);
                model.put(tags, this.allTags());
                model.put("descriptionValue", this.imageDescription(imageId));
                return htmlEdit;
            }
        }
        else {
            return redirectToMain;
        }
    }

    @RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET})
    public String deleteImage(HttpServletRequest request, Map<String, Object> model, @RequestParam Map<String, String> paramValues) {
        String editParams = String.valueOf(paramValues);
        Object[] params = paramValues.values().toArray();
        String referer = request.getHeader("Referer");
        if (paramValues.size() >= 1) {
            if (editParams.contains("action=cancel")) {
                return redirect;
            } else if (editParams.contains("action=delete")) {
                String imageIdString = String.valueOf(params[0]);
                Long imageId = Long.valueOf(imageIdString);
                deletePhoto(imageId);
                return redirectToMain;
            } else {
                String[] link = referer.split("/");
                redirect = "redirect:/" + link[3];
                String imageIdString = String.valueOf(params[0]);
                Long imageId = Long.valueOf(imageIdString);

                model.put(tags, this.allTags());
                model.put("imageId", imageId);
                return htmlDelete;
            }
        }
        else {
            return redirectToMain;
        }
    }

    @RequestMapping(value = "/register")
    public String register(Map<String, String> model) {
        model.put(tags, this.allTags());
        return htmlRegister;
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST, RequestMethod.GET}, params = ("username"))
    public String register(Map<String, String> model, @RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("passwordRepeat") String passwordRepeat,
            RedirectAttributes redirectAttributes) {
        String message = registerUser(username, password, passwordRepeat);
        model.put(tags, this.allTags());
        if (message == null) {
            return htmlLogin;
        } else {
            redirectAttributes.addFlashAttribute("message", message);
            if (message.contains("complete")) {
                return "redirect:/login";
            } else {
                return "redirect:/register";
            }
        }
    }

    @RequestMapping(value = "/admin")
    public String adminMenu(Map<String, String> model) {
        model.put(tags, this.allTags());
        return htmlAdmin;
    }

    @RequestMapping(value = "/admin/users")
    public String adminUsers(Map<String, String> model) {
        model.put(table, this.allUsers());
        model.put(tags, this.allTags());
        return htmlAdminUsers;
    }

    @RequestMapping(value = "/admin/users", params = ("userid"))
    public String editUser(Map<String, String> model, @RequestParam("userid") Long userid) {
        String username = this.usernameByRoleId(userid);
        model.put("username", username);
        model.put("date", this.dateByUsername(username));

        String role = this.roleById(userid);
        String otherRole;
        if (role.equals("ADMIN")) {
            otherRole = "USER";
        } else {
            otherRole = "ADMIN";
        }

        model.put("role", role);
        model.put("otherRole", otherRole);
        model.put("userId", String.valueOf(userid));
        model.put(table, this.allUsers());
        model.put(tags, this.allTags());
        return htmlEditUsers;
    }

    @RequestMapping(value = "/admin/users", method = { RequestMethod.POST, RequestMethod.GET}, params = "role")
    public String editUsers(Map<String, String> model, @RequestParam("role") String role, @RequestParam("userId") Long userid) {
        updateRoles(role, userid);
        model.put(table, this.allUsers());
        model.put(tags, this.allTags());
        return htmlAdminUsers;
    }

    private String allPictures() {
        return generateHtml.createDivs();
    }

    private String allTags() {
        return generateHtml.createTags();
    }

    private String picturesWithTag(Long tag) {
        return generateHtml.createDivs(tag);
    }

    private String picturesWithSearch(String search) {
        return generateHtml.createDivsSearch(search);
    }

    private String imageSrc(Long imageId) {
        return generateHtml.createImageSrc(imageId);
    }

    private String imageDescription(Long imageId) {
        return generateHtml.getImageDescription(imageId);
    }

    private String imageDate(Long imageId) {
        return generateHtml.getImageDate(imageId);
    }

    private void editPhotoInfo(Long imageId, String description, String tags) {
        insertToDb.editPhotoInfo(imageId, description, tags);
    }

    private void deletePhoto(Long imageId) {
        insertToDb.deletePhoto(imageId);
    }

    private String allUsers() {
        return generateHtml.createTableOfUsers();
    }

    private String redirectToCurrent(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        String[] link = referer.split("/");
        String redirect;
        if(link.length > 3) {
            redirect = "redirect:/" + link[3];
        }
        else {
            redirect = "redirect:/";
        }
        return redirect;
    }

    private Set<Tag> pictureTagsByPictureId(Long id) {
        return insertToDb.pictureTagsByPictureId(id);
    }

    private String registerUser(String username, String password, String repeatPassword) {
        return insertToDb.registerNewAccount(username, password, repeatPassword);
    }

    private String usernameByRoleId(Long id) {
        return insertToDb.usernameByRoleId(id);
    }

    private String dateByUsername(String username) {
        return insertToDb.dateByUsername(username);
    }

    private String roleById(Long id) {
        return insertToDb.roleById(id);
    }

    private void updateRoles(String roles, Long id) {
        insertToDb.updateRoles(roles, id);
    }
}