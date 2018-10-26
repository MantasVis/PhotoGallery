package lt.insoft.gallery.gallerymodel.repository;

import java.util.List;

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

    @Query("select r from UserRole r where r.role =:role")
    UserRole getUserRoleByRole(@Param("role") String role);

    List<UserRole> getAllBy();

    @Query("select u.user from UserRole u where u.userRoleId = :id")
    User findUsernameByRoleId(@Param("id") Long id);

    @Query("select role from UserRole where userRoleId = :id")
    String findRoleNameById(@Param("id") Long id);

    @Query("select r from UserRole r where r.userRoleId = :id")
    UserRole findRoleById(@Param("id") Long id);
}
