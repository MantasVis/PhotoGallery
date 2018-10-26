package lt.insoft.gallery.galleryui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lt.insoft.gallery.gallerybl.UserDetailsServiceImp;
import lt.insoft.gallery.gallerymodel.repository.UserRepository;
import lt.insoft.gallery.gallerymodel.repository.UserRoleRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public WebSecurityConfig(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImp(userRepository, userRoleRepository);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers( "/js/**", "/css/**", "/", "/tags", "/error**", "/image", "/webjars/**", "/register/**").permitAll()
                .antMatchers("/h2/**" , "/delete/**", "/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                //.anyRequest().permitAll()
                .and().formLogin().loginPage("/login").permitAll()
                .and().headers().frameOptions().sameOrigin()
                .and().logout().permitAll()
                .and().csrf().disable();

    }
}
