import axios from "axios";
import React, { useState, useEffect } from "react";
function HeaderTv() {
  const [auth, setAuth] = useState({
    user: sessionStorage.getItem("user"),
    pwd: sessionStorage.getItem("pwd"),
    personId: sessionStorage.getItem("person_id"),
  });
  const LOGIN_URL = localStorage.getItem('url');


  const [waitingData, setWaitingData] = useState({
    processedWaitingNo: 0,
    currentWaitingNo: 1,
    waitingListSize: 0,
  });

  const fetchWaitingQStatus = (date) => {
    // console.log("intial:", date);
    // console.log("date instanceof Date" + date instanceof Date);
    const querydate = new Date(date).toLocaleDateString("en-CA");
    axios({
      method: "get",
      url: `${LOGIN_URL}/getWaitingQStatus`,
      auth: {
        // username: auth.user,
        // password: auth.password,
        username: "admin",
        password: "goodpass",
      },
    })
      //GET http://localhost:8080/api/v1/patient/2/getAwaitingPayment
      .then((response) => {
        if (response.status === 200) {
          // console.log("Fetched JSON: ", response.data);
          //   console.log("Amount due: ", response.data[0].amountDue);
          const { processedWaitingNo, currentWaitingNo, waitingListSize } =
            response.data;
          setWaitingData({
            processedWaitingNo,
            currentWaitingNo,
            waitingListSize,
          });
          // if (response.data) {
          // parseFloat(response.data[0].amountDue).toFixed(2)
          // }
          //   setBillings(response.data);
          // setBillings(parseFloat(response.data[0].amountDue).toFixed(2));
        } else if (response.status === 404) {
        }
      })
      .catch((error) => {
        console.error("Failed to fetch billings from the API");
        // setBillings([]);

        console.error("An error occurred while fetching billings:", error);
        alert(error.response.data.message);
      });
  };

  useEffect(() => {
    // Initial call when the component mounts
    fetchWaitingQStatus();

    // Schedule subsequent calls every N milliseconds (e.g., every 10 seconds)
    const intervalId = setInterval(() => {
      fetchWaitingQStatus();
    }, 1000); // Adjust the interval as needed (10 seconds in this example)

    // Clear the interval when the component unmounts
    return () => {
      clearInterval(intervalId);
    };
  }, []);

  const blueBoxStyle = {
    backgroundColor: "transparent", // Set background color to transparent
    border: "10px solid #FFFFFF",  // Set border to create an outline
    color: "white", // Set text color
    padding: "10px", // Add padding for spacing inside the box
  };

  return (
    <div style={blueBoxStyle}>
      <p>Waiting No: {waitingData.processedWaitingNo}</p>
      <p>
      If your number has been shown or skipped, 
      please proceed to the kiosk for your new queue number and room assignment.</p> 
      {/* <p>Current Waiting No: {waitingData.currentWaitingNo}</p> */}
      <p>Number of people waiting: {waitingData.waitingListSize}</p>
    </div>
  );
}

export default HeaderTv;
