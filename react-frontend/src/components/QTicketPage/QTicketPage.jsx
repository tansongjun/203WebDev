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
  const [roomNo, setRoomNo] = useState(0);
  const [isWaiting, setIsWaiting] = useState(false);
  const [startTime, setStartTime] = useState(null);
  const LOGIN_URL = localStorage.getItem('url');

  const getQueueNumber = async () => {
    try {
      const response = await Axios.get(
        `${LOGIN_URL}/patients/${auth.personId}/getQtoday`,
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
      // console.log("Response:", response.data);
      const dateTimeSlot = response.data.dateTimeSlot;
      const qTicket = response.data.qTicket;

      if (
        response.data &&
        dateTimeSlot
      ) {
        setQueueNumber(dateTimeSlot.id);
        setRoomNo(dateTimeSlot.room.roomNumber);
        setStartTime(dateTimeSlot.startDateTime.substring(11, 16));
        setIsWaiting(false);
      } else if (
        response.data &&
        qTicket.waitingNo !== null
      ){
        setQueueNumber(qTicket.waitingNo);
        setIsWaiting(true);
      }

      console.log("Queue Number:", response.data);
    } catch (error) {
      // Handle errors here
      // createNewTicket();
      console.error("Error getting queue number:", error);
    }
  };

  // const createNewTicket = async () => {
  //   try {
  //     const response = await Axios.post(
  //       `http://localhost:8080/api/v1/patients/${auth.personId}/getnewQ`,
  //       null, // You can pass data as the second argument if needed
  //       {
  //         headers: {
  //           "Content-Type": "application/json",
  //         },
  //         auth: {
  //           username: `${auth.user}`,
  //           password: `${auth.pwd}`,
  //         },
  //       }
  //     ).then((response) => {
  //       // Set the queue number in the state
  //       if (
  //         response.data &&
  //         response.data.qticket &&
  //         response.data.qticket.ticketno
  //       ) {
  //         setQueueNumber(response.data.qticket.ticketno);
  //         window.location.reload(true);
  //       }

  //       console.log("New Ticket Created:", response.data);
  //       // setQueueNumber(response.data.qticket.ticketno);

  //     });
  //   } catch (error) {
  //     // Handle errors here
  //     const errorMessage = "Patient has already got a queue ticket for today";
  //     // if (error.response.data.message === errorMessage) {
  //     //   getQueueNumber();
  //     // }
  //     console.error(
  //       "Error creating a new ticket:",
  //       error.response.data.message
  //     );
  //   }
  // };

  // useEffect(() => {
  //   getQueueNumber(); // Check if the user has a ticket number
  // }, []);
  useEffect(() => {
    getQueueNumber(); // Initial fetch of the queue number

    // Set up an interval to fetch the queue number at regular intervals
    const intervalId = setInterval(() => {
      getQueueNumber();
    }, 500); // Adjust the interval duration as needed (e.g., fetch every 5 seconds)

    // Clean up the interval when the component unmounts
    return () => {
    clearInterval(intervalId);
    };
    }, [auth.personId, auth.user, auth.pwd]);
  // }, [queueNumber]);

  return (
    <div>
      <div style={{ textAlign: "center", fontSize: "50px", marginTop: "20px" }}>
        {/* <h1>Queue ticket number is {queueNumber}</h1> */}
        {/* <h1>Queue ticket number is </h1> */}
        {queueNumber !== null && !isWaiting ? (<div>
          <h1>Queue ticket number is {queueNumber}</h1>
          <h2>Appointment timing: {startTime}</h2>
          <br></br>
          <h2>Please proceed to room {roomNo}.</h2>
        </div>) : queueNumber !== null && isWaiting ? 
        (<div>
          <h1>Waiting ticket number is {queueNumber}</h1>
          <h2>Please wait at the reception area and wait for waitingNo to be processed.</h2>
        </div>) : (
          <h1>Loading...</h1>
        )}
      </div>
      <Link to="/walkinHome">
        <button className="queue-button">Back</button>
      </Link>
    </div>
  );
}

export default QTicketPage;
