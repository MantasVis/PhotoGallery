package lt.insoft.gallery.gallerymodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import lt.insoft.gallery.gallerymodel.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where username = :username")
    User findByUsername(@Param("username") String username);
}