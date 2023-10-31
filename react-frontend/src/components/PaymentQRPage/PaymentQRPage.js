import React from "react";
import { useLocation } from "react-router-dom";
import image from "./payment.jpg";
import { Link } from "react-router-dom";
import axios from "axios";

function PaymentQRPage() {
  const location = useLocation();
  console.log("Data received in PaymentQRPage:", location.state);

  // Function to send the PUT request when "Paid" button is clicked
  const confirmPayment = () => {
    // Construct the URL and data for the request
    const selectedBill = location.state.selectedBill;
    const personId = selectedBill.person.id;
    const ticketNo = selectedBill.ticketno;

    const apiUrl = `http://localhost:8080/api/v1/patient/${personId}/confirmPayment/${ticketNo}`;

    const auth = {
      username: "admin",
      password: "goodpass",
    };
    const data = {}; // You can include data if required

    // Send the PUT request
    axios
      .put(apiUrl, data, {
        auth: auth,
        headers: {
          'Content-Type': 'application/json',
        },
      })
      .then((response) => {
        if (response.status === 200) {
          // Payment confirmation was successful
          alert('Payment confirmed successfully');
        } else {
          // Handle other response statuses as needed
          alert('Failed to confirm payment');
        }
      })
      .catch((error) => {
        // Handle the error and display an error message
        console.error('An error occurred while confirming payment:', error);
        alert('Error confirming payment');
      });
  };

  return (
    <div className="paymentQR-container">
      <h1>Payment Details:</h1>
      {location.state && (
        <div>
          <h2>
            Date: {location.state.selectedBill.date}, Description:{" "}
            {location.state.selectedBill.description},
            <strong>Amount ($): {location.state.selectedBill.amountDue}</strong>
          </h2>
        </div>
      )}
      <img src={image} alt="Image Description" />
      <button className="paid-button" onClick={confirmPayment}>
        Paid
      </button>
      <Link to="/walkinHome/payment">
        <button className="queue-button">Back</button>
      </Link>
    </div>
  );
}

export default PaymentQRPage;
