import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import image from "./payment.jpg";
import axios from "axios";

function Payment() {
  const [auth, setAuth] = useState({
    user: sessionStorage.getItem("user"),
    pwd: sessionStorage.getItem("pwd"),
    personId: sessionStorage.getItem("person_id"),
  });

  const [billings, setBillings] = useState([
    {
      id: 1,
      date: "2023-10-15",
      amount: 50.0,
      description: "Dummy Bill 1",
    },
    {
      id: 2,
      date: "2023-10-16",
      amount: 75.5,
      description: "Dummy Bill 2",
    },
    {
      id: 3,
      date: "2023-10-17",
      amount: 30.25,
      description: "Dummy Bill 3",
    },
  ]);

  const fetchBills = (date) => {
    console.log("intial:", date);
    // console.log("date instanceof Date" + date instanceof Date);
    const querydate = new Date(date).toLocaleDateString("en-CA");
    axios({
      method: "get",
      url: `http://localhost:8080/api/v1/patient/${auth.personId}/getAwaitingPayment`,
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
          console.log("returned data: ", response.data);
          setBillings(response.data);
        } else if (response.status === 404) {
        }
    })
      .catch((error) => {
        console.error("Failed to fetch billings from the API");
        setBillings([]);

        console.error("An error occurred while fetching billings:", error);
        alert(error.response.data.message);
      });
  };

  const handleBillClick = (timeSlot) => {
    // if (isConfirmed) {
    //   console.log(timeSlot.id);
    //   // POST http://localhost:8080/api/v1/appointment/bookNewAppointment/{auth.personId}/{timeslot.id}
    //   const bookingUrl = `http://localhost:8080/api/v1/appointment/bookNewAppointment/${auth.personId}/${timeSlot.id}`;
    // axios
    //   .post(bookingUrl, {}, { auth: { username: auth.user, password: auth.pwd } })
    //   .then((response) => {
    //     if (response.status === 200) {
    //       console.log(`Appointment booked for: ${formattedTime}`);
    //       // You can display a success message to the user
    //       alert(`Appointment booked for: ${formattedTime}`);
    //     } else {
    //       // Handle other response statuses as needed
    //       console.error('Failed to book appointment.');
    //     }
    //   })
    //   .catch((error) => {
    //     console.error('An error occurred while booking the appointment:', error);
    //     // You can display an error message to the user
    //     alert('An error occurred while booking the appointment.');
    //   });
    //   // console.log(`Appointment booked for: ${formattedTime}`);
    // } else {
    //   // User canceled the selection, you can handle this case as needed
    //   console.log("Appointment selection canceled");
    // }
  };

  // Function to generate a random amount between 0 and 100
  // const getRandomAmount = () => {
  //   console.log('personId: ' + auth.personId);
  //   return (Math.random() * 100).toFixed(2); // Generates a random number with 2 decimal places
  // };
  

  // State to store the random amount
  // const [amountPayable, setAmountPayable] = useState(getRandomAmount());

  // Function to generate a new random amount when needed
  // const regenerateRandomAmount = () => {
  //   setAmountPayable(getRandomAmount());
  // };

  useEffect(() => {
    fetchBills();
  })

  return (
    <div className="payment-container">
      <h1>Bills due:</h1>

      {/* <img src={image} alt="Example" /> */}

      {/* <button onClick={regenerateRandomAmount}>Generate New Amount</button> */}
      <div className="centered-qr-code">
        {/* <QRCode className="qr-code" value={`Payment: $${amountPayable}`} size={256} /> */}
      </div>
      <ul>
        {billings &&
          billings.map((bill) => (
            <li key={bill.id}>
              <h2>
                Date: {bill.date}, Description: {bill.description},
                <strong>Amount ($): {bill.amount}</strong>
              </h2>

              <button onClick={() => handleBillClick(bill)}>
                Pay this bill
              </button>
            </li>
          ))}
      </ul>
      <button className="queue-button">Pay All Bills</button>
      <br></br>
      <Link to="/walkinHome">
        <button className="queue-button">Back</button>
      </Link>
    </div>
  );
}

export default Payment;