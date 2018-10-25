package lt.insoft.gallery.gallerymodel.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements Serializable {

    @Id
    @NotNull
    @Size(max = 45)
    @Column(name = "username")
    @Getter @Setter
    private String username;

    @NotNull
    @Size(max = 45)
    @Column(name = "password")
    @Getter @Setter
    private String password;

    public User(@NotNull String username, @NotNull String password)
    {
        this.username = username;
        this.password = password;
    }
}