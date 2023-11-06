import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Axios from "axios";

function Registration() {
    const [selectedButton1, setSelectedButton1] = useState(null);
    const [selectedButton2, setSelectedButton2] = useState(null);
    const [selectedButton3, setSelectedButton3] = useState(null);
    const navigate = useNavigate();
    const LOGIN_URL = localStorage.getItem('url');

    const [auth, setAuth] = useState({
        user: sessionStorage.getItem("user"),
        pwd: sessionStorage.getItem("pwd"),
        personId: sessionStorage.getItem("person_id"),
      });
    

    const toggleBackgroundColor = (button, setButton) => {
        setButton((prevButton) => (prevButton === button ? null : button));
    };

    
    const handleSubmit = () => {
        if (selectedButton1 === 'Yes' || selectedButton2 === 'Yes' || selectedButton3 === 'Yes') {
            alert('Please head to the front desk immediately');
            navigate('/walkinHome');
        }else{
            getQueueNumber();
            console.log("qNumber:" +queueNumber);
            if (queueNumber === null) {
                console.log("NULL - create new ticket now");
                createNewTicket();
            }else{
                console.log("NOT NULL");
            }

            navigate('/walkinHome/qticketpage');
        }
    };

    const [queueNumber, setQueueNumber] = useState(null);
    const [responseData, setResponseData] = useState(null);


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
  
        if (response.data && response.data.dateTimeSlot) {
          const qticket = response.data.dateTimeSlot.qticket;
          if (qticket) {
            setQueueNumber(qticket.ticketno);
            console.log("success set qNumber:" + queueNumber);
          }
        }
  
      } catch (error) {
        // Handle errors here
        console.error("Error getting queue number:", error.response.data);
      }
  };
  

  useEffect(() => {
    // Call fetchQueueNumber initially when the component mounts
    getQueueNumber();

    // Set up an interval to call fetchQueueNumber every, for example, 5 seconds (you can adjust the interval)
    const intervalId = setInterval(getQueueNumber, 5000);
    console.log(queueNumber);
    // Clean up the interval when the component unmounts
    return () => {
        clearInterval(intervalId);
    };
}, []); // The empty dependency array ensures that this effect only runs once

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
      )
      // ).then((response) => {
        // Set the queue number in the state
        // if (
        //   response.data &&
        //   response.data.qticket &&
        //   response.data.qticket.ticketno
        // ) {
        //   setQueueNumber(response.data.qticket.ticketno);
        // }
        console.log(response.data);
        const newQueueNumber = response.data.qTicket.ticketno;
        sessionStorage.setItem("queueNumber", newQueueNumber);
        sessionStorage.setItem("queueNumber", newQueueNumber);
        setQueueNumber(newQueueNumber);
        
        console.log("New Ticket Number:", newQueueNumber);
        console.log("current ticket number after creation: " + queueNumber);
    } catch (error) {
      // Handle errors here
      const errorMessage = "Patient has already got a queue ticket for today";
      console.log(error);
      // if (error.response.data.message === errorMessage) {
      //   getQueueNumber();
      // }
    //   console.error(
    //     "Error creating a new ticket:",
    //     error.response.data.message
    //   );
    }
  };


    return (
        <div>
            <h2>Do you have a fever above 37.5 degrees?</h2>
            <button
                className="questionnaire-button"
                style={{
                    backgroundColor: selectedButton1 === 'Yes' ? 'rgb(10, 30, 181)' : 'aqua',
                }}
                onClick={() => toggleBackgroundColor('Yes', setSelectedButton1)}
            >
                Yes
            </button>
            <button
                className="questionnaire-button"
                style={{
                    backgroundColor: selectedButton1 === 'No' ? 'rgb(10, 30, 181)' : 'aqua',
                }}
                onClick={() => toggleBackgroundColor('No', setSelectedButton1)}
            >
                No
            </button>

            <h2>Have you been to the Middle East recently?</h2>
            <button
                className="questionnaire-button"
                style={{
                    backgroundColor: selectedButton2 === 'Yes' ? 'rgb(10, 30, 181)' : 'aqua',
                }}
                onClick={() => toggleBackgroundColor('Yes', setSelectedButton2)}
            >
                Yes
            </button>
            <button
                className="questionnaire-button"
                style={{
                    backgroundColor: selectedButton2 === 'No' ? 'rgb(10, 30, 181)' : 'aqua',
                }}
                onClick={() => toggleBackgroundColor('No', setSelectedButton2)}
            >
                No
            </button>

            <h2>Do you have a cough or sore throat?</h2>
            <button
                className="questionnaire-button"
                style={{
                    backgroundColor: selectedButton3 === 'Yes' ? 'rgb(10, 30, 181)' : 'aqua',
                }}
                onClick={() => toggleBackgroundColor('Yes', setSelectedButton3)}
            >
                Yes
            </button>
            <button
                className="questionnaire-button"
                style={{
                    backgroundColor: selectedButton3 === 'No' ? 'rgb(10, 30, 181)' : 'aqua',
                }}
                onClick={() => toggleBackgroundColor('No', setSelectedButton3)}
            >
                No
            </button>
            <><h2>Confirm:</h2></>
            <button className="submit-button" onClick={handleSubmit}>
                Submit
            </button>
        </div>
    );
}

export default Registration;
