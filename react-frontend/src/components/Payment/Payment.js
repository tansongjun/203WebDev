import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
// import QRCode from "qrcode.react";

function Payment() {
  // Function to generate a random amount between 0 and 100
  const getRandomAmount = () => {
    return (Math.random() * 100).toFixed(2); // Generates a random number with 2 decimal places
  };

  // State to store the random amount
  const [amountPayable, setAmountPayable] = useState(getRandomAmount());

  // Function to generate a new random amount when needed
  const regenerateRandomAmount = () => {
    setAmountPayable(getRandomAmount());
  };

  return (
    <div className="payment-container">
      <h1>PAYMENT</h1>
      <h2>Amount payable($): {amountPayable}</h2>
      {/* <button onClick={regenerateRandomAmount}>Generate New Amount</button> */}
      <div className="centered-qr-code">
        {/* <QRCode className="qr-code" value={`Payment: $${amountPayable}`} size={256} /> */}
      </div>
      <Link to="/walkinHome">
        <button className="queue-button">Back</button>
      </Link>
    </div>
  );
}

export default Payment;
