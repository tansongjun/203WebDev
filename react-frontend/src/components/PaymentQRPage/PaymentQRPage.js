import React from "react";
import { useLocation } from "react-router-dom";
import image from "./payment.jpg";
import { Link } from "react-router-dom";
import axios from "axios";

function PaymentQRPage() {
  const location = useLocation();
  const { query } = location.state;
  console.log("Data received in PaymentQRPage:", location.state);
  const [url, setUrl] = React.useState(
    "https://sgqrcode.com/paynow?mobile=&uen=198702955E43F&editable=1&amount=" +
      location.state.selectedBill.amountDue +
      "&expiry=2024%2F02%2F01%2018%3A27&ref_id=NA&company=HealthSing"
  );

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
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        if (response.status === 200) {
          // Payment confirmation was successful
          alert("Payment confirmed successfully");
        } else {
          // Handle other response statuses as needed
          alert("Failed to confirm payment");
        }
      })
      .catch((error) => {
        // Handle the error and display an error message
        console.error("An error occurred while confirming payment:", error);
        alert("Error confirming payment");
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
      <img src={url} alt="Image Description" />
      <button className="paid-button" onClick={confirmPayment}>
        Paid
      </button>
      <Link to="/walkinHome/payment" state={{ query }}>
        <button className="queue-button">Back</button>
      </Link>
    </div>
  );
}

export default PaymentQRPage;
