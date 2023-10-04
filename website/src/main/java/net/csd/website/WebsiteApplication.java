package net.csd.website;

import java.time.LocalDate;

import org.apache.catalina.valves.rewrite.RewriteCond.Condition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import net.csd.website.repository.*;
import net.csd.website.service.*;
import net.csd.website.model.*;

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
            "eml@my.eml", 60, net.csd.website.model.Person.Condition.NONE, "admin", 
            encoder.encode("goodpass"), "ROLE_ADMIN")).getUsername()
        );

        /* create Room and timeslot 
         * 
         */
        RoomService roomService = ctx.getBean(RoomService.class);
        LocalDate currentDate = LocalDate.now();
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        // Create rooms from today until the end of the month
        while (currentDate.isBefore(lastDayOfMonth) || currentDate.isEqual(lastDayOfMonth)) {
            // Create room with the room number 1
            Room room = new Room();
            room.setRoomNumber(1);
            roomService.createRoom(room, currentDate);

            // Create room with the room number 2
            Room room2 = new Room();
            room2.setRoomNumber(2);
            roomService.createRoom(room2, currentDate);

            // Create room with the room number 3
            Room room3 = new Room();
            room3.setRoomNumber(3);
            roomService.createRoom(room3, currentDate);

            // Move to the next day
            currentDate = currentDate.plusDays(1);
        }

        System.out.println("[Rooms created from today until the end of the month]");
        
	}
}
