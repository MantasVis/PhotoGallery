package lt.insoft.gallery.gallerymodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import lt.insoft.gallery.gallerymodel.model.User;
import lt.insoft.gallery.gallerymodel.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("select role from UserRole where user = :user")
    String findRoleByUsername(@Param("user") User user);
}
