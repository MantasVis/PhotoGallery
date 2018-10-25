package lt.insoft.gallery.gallerymodel.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_roles", uniqueConstraints = {@UniqueConstraint(columnNames = { "role", "username"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRole implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_role_id")
    @NotNull
    @Getter @Setter
    private int userRoleId;

    @NotNull
    @Column(name = "role")
    @Getter @Setter
    private String role;

    @ManyToOne
    @JoinColumn(name = "username")
    @Getter @Setter
    private User user;

    public UserRole(int userRoleId, @NotNull String role) {
        this.userRoleId = userRoleId;
        this.role = role;
    }
}