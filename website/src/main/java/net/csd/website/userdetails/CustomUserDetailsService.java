package net.csd.website.userdetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.csd.website.repository.PersonRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private PersonRepository users;
    
    public CustomUserDetailsService(PersonRepository users) {
        this.users = users;
    }
    @Override
    /** To return a UserDetails for Spring Security 
     *  Note that the method takes only a username.
        The UserDetails interface has methods to get the password.
    */
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException {
        return users.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
    }
    
}