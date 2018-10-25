package lt.insoft.gallery.galleryui.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public String handleError1(MultipartException e, RedirectAttributes redirectAttributes) {

        System.out.println("ERROR1");

        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        return "redirect:/upload";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleError2(MaxUploadSizeExceededException e, RedirectAttributes redirectAttributes) {

        System.out.println("ERROR2");
        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        return "redirect:/upload";
    }


}
