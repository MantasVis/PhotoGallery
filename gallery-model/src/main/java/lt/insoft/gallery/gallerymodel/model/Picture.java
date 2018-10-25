package lt.insoft.gallery.gallerymodel.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pictures")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public @Data class  Picture implements Serializable {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long picture_id;

    @NotNull
    @Lob
    private byte[] pictureBytes;

    @NotNull
    private String date;


    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "picture_tag", joinColumns = @JoinColumn(name = "picture_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @Getter @Setter
    private Set<Tag> tags = new HashSet<>();

    public Picture(@NotNull byte[] pictureBytes, @NotNull String date, String description) {
        this.pictureBytes = pictureBytes;
        this.date = date;
        this.description = description;
    }
}