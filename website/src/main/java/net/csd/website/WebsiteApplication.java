package net.csd.website;

import java.time.LocalDate;
import java.time.Month;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import net.csd.website.repository.*;
import net.csd.website.service.*;
import net.csd.website.model.*;
import net.csd.website.model.Person.Authority;

@SpringBootApplication
@EnableScheduling
public class WebsiteApplication {

	public static void main(String[] args) {
		// SpringApplication.run(WebsiteApplication.class, args);
        // JPA user repository init
		ApplicationContext ctx = SpringApplication.run(WebsiteApplication.class, args);
        PersonRepository users = ctx.getBean(PersonRepository.class);
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        RoomService roomService = ctx.getBean(RoomService.class);
        intialSetUp(users, encoder, roomService);
	}

    public static void intialSetUp(PersonRepository users, BCryptPasswordEncoder encoder, RoomService roomService) {

        System.out.println("[Add user]: " + users.save(
            new Person("myuser", "mypass", 
            "eml@my.eml",  LocalDate.of(1992,Month.APRIL,1), net.csd.website.model.Person.Condition.NONE, "admin", 
            encoder.encode("goodpass"), "goodpass","T0012112F", Authority.ROLE_ADMIN
            )).getUsername()
        );
        System.out.println("[Add user]: " + users.save(
            new Person("Johnny", "Tan", 
            "johnny@smu.sg", LocalDate.of(1992,Month.APRIL,1), net.csd.website.model.Person.Condition.SEVERE, "johnt", 
            encoder.encode("goodpass"),"goodpass", "T2012112F", Authority.ROLE_PATIENT)).getUsername()
        );

        /* create 3 Room for 2 months
         * 
         */
        
        int noOfmonths = 2;
        int noOfRooms = 3;
        
        LocalDate currentDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);

        for (int i = 0; i < noOfmonths; i++) {
            roomService.createNewRoomforMonth(noOfRooms, currentDate.plusMonths(i));
        }

        System.out.println("Set up complete!");
    }

    
}
