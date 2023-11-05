import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
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
      ticketno: 2,
      amountDue: 75.83973365214874,
      createdAt: "2023-10-31T20:08:14.875616",
      person: {
        id: 2,
        firstName: "user2",
        lastName: "mypass",
        emailId: "eml@my.eml",
        birthDate: "1992-04-01",
        condition: "SEVERE",
        username: "user2",
        password:
          "$2a$10$1VVm.Q50swe6T9Io89DhseX3C6yGZosVYBndvh4sC1bkVIc47Zzkq",
        authorities: [
          {
            authority: "ROLE_PATIENT",
          },
        ],
        enabled: true,
        riskLevel: 3,
        accountNonExpired: true,
        credentialsNonExpired: true,
        age: 31,
        accountNonLocked: true,
      },
      qstatus: "AWAITINGPAYMENT",
    },
    {
      id: 2,
      date: "2023-10-16",
      amountDue: 75.5,
      description: "Dummy Bill 2",
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
          console.log("Amount due: ", response.data[0].amountDue);

          // if (response.data) {
            // parseFloat(response.data[0].amountDue).toFixed(2)
          // }
          setBillings(response.data);
          // setBillings(parseFloat(response.data[0].amountDue).toFixed(2));
        } else if (response.status === 404) {
        }
      })
      .catch((error) => {
        console.error("Failed to fetch billings from the API");
        setBillings([]);

        console.error("An error occurred while fetching billings:", error);
        // alert(error.response.data.message);
      });
  };

  const handleBillClick = (timeSlot) => {};

  useEffect(() => {
    fetchBills();
  });

  const navigate = useNavigate();
  const sendBill = (selectedBill) => {
    navigate("/walkinHome/payment/paymentQR", {
      state: {
        selectedBill: selectedBill,
      },
    });
  };

  return (
    <div className="payment-container">
      <h1>Bills due:</h1>

      <div className="centered-qr-code"></div>
      <ul>
        {billings &&
          billings.map((bill) => (
            <li key={bill.id}>
              <h2>
                Date: {bill.date}, Description: {bill.description},
                <strong>Amount ($): {bill.amountDue}</strong>
              </h2>
              
              <Link
                to={{
                  pathname: "/walkinHome/payment/paymentQR",
                  state: {
                    selectedBill: bill,
                  },
                }}
              ></Link>
              <button
                className="indv-bill-button"
                onClick={() => sendBill(bill)}
              >
                Pay this bill
              </button>
            </li>
          ))}
      </ul>
      {/* <button className="queue-button">Pay All Bills</button> */}
      <br></br>
      <Link to="/walkinHome">
        <button className="queue-button">Back</button>
      </Link>
    </div>
  );
}

export default Payment;
