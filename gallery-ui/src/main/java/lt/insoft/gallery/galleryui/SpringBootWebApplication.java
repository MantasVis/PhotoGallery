package lt.insoft.gallery.galleryui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"lt.insoft.gallery"})
@ComponentScan(basePackages = {"lt.insoft.gallery"})
@EnableJpaRepositories("lt.insoft.gallery.gallerymodel.repository")
@SpringBootApplication
public class SpringBootWebApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootWebApplication.class, args);
    }
}

//---------------------------BUGS--------------------------
//todo: ExceptionHandler not redirecting, instead just looping