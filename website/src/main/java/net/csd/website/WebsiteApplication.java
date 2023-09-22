package net.csd.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import net.csd.website.repository.PatientRepository;
import net.csd.website.repository.PersonRepository;
import net.csd.website.model.Patient;
import net.csd.website.model.Person;

@SpringBootApplication
public class WebsiteApplication {

	public static void main(String[] args) {
		// SpringApplication.run(WebsiteApplication.class, args);

        // JPA user repository init
		ApplicationContext ctx = SpringApplication.run(WebsiteApplication.class, args);
        PersonRepository users = ctx.getBean(PersonRepository.class);
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        System.out.println("[Add user]: " + users.save(
            new Person("myuser", "mypass", 
            "eml@my.eml", 60, "extreme", "admin", 
            encoder.encode("goodpass"), "ROLE_ADMIN")).getUsername()
        );
	}
}
