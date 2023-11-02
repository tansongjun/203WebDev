import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Axios from "axios";

function QTicketPage() {
  const [auth, setAuth] = useState({
    user: sessionStorage.getItem("user"),
    pwd: sessionStorage.getItem("pwd"),
    personId: sessionStorage.getItem("person_id"),
  });

  const [queueNumber, setQueueNumber] = useState(null);

  const getQueueNumber = async () => {
    try {
      const response = await Axios.get(
        `http://localhost:8080/api/v1/patients/${auth.personId}/getQtoday`,
        {
          headers: {
            "Content-Type": "application/json",
          },
          auth: {
            username: `${auth.user}`,
            password: `${auth.pwd}`,
          },
        }
      );

      if (
        response.data &&
        response.data.dateTimeSlot &&
        response.data.dateTimeSlot.qticket
      ) {
        setQueueNumber(response.data.dateTimeSlot.qticket.ticketno);
      } else {
        // If not, create a new ticket
        createNewTicket();
      }

      console.log("Queue Number:", response.data);
    } catch (error) {
      // Handle errors here
      createNewTicket();
      console.error("Error getting queue number:", error);
    }
  };

  const createNewTicket = async () => {
    try {
      const response = await Axios.post(
        `http://localhost:8080/api/v1/patients/${auth.personId}/getnewQ`,
        null, // You can pass data as the second argument if needed
        {
          headers: {
            "Content-Type": "application/json",
          },
          auth: {
            username: `${auth.user}`,
            password: `${auth.pwd}`,
          },
        }
      ).then((response) => {
        // Set the queue number in the state
        if (
          response.data &&
          response.data.qticket &&
          response.data.qticket.ticketno
        ) {
          setQueueNumber(response.data.qticket.ticketno);
          window.location.reload(true);
        }

        console.log("New Ticket Created:", response.data);
      });
    } catch (error) {
      // Handle errors here
      const errorMessage = "Patient has already got a queue ticket for today";
      // if (error.response.data.message === errorMessage) {
      //   getQueueNumber();
      // }
      console.error(
        "Error creating a new ticket:",
        error.response.data.message
      );
    }
  };

  useEffect(() => {
    getQueueNumber(); // Check if the user has a ticket number
  }, []);

  return (
    <div>
      <div style={{ textAlign: "center", fontSize: "50px", marginTop: "20px" }}>
        <h1>Queue ticket number is {queueNumber}</h1>
      </div>
      <Link to="/walkinHome">
        <button className="queue-button">Back</button>
      </Link>
    </div>
  );
}

export default QTicketPage;