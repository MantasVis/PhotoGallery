package lt.insoft.gallery.gallerymodel.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long tag_id;

    @NotNull
    @Getter @Setter
    private String name;

    @ManyToMany(mappedBy = "tags")
    @Getter @Setter
    private Set<Picture> pictures = new HashSet<>();

    public Tag(@NotNull String name) {
        this.name = name;
    }
}
