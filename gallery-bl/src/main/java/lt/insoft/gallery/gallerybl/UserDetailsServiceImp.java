package lt.insoft.gallery.gallerybl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lt.insoft.gallery.gallerymodel.model.User;
import lt.insoft.gallery.gallerymodel.repository.UserRepository;
import lt.insoft.gallery.gallerymodel.repository.UserRoleRepository;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserDetailsServiceImp(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByUsername(username);

        UserBuilder builder;
        if (user != null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(new BCryptPasswordEncoder().encode(user.getPassword()));
            builder.roles(userRoleRepository.findRoleByUsername(user));
        } else {
            throw new UsernameNotFoundException("User not found");
        }
        return builder.build();
    }
}
