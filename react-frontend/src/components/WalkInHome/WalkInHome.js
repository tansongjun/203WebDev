import React, { useState, useEffect } from "react";

function WalkInHome() {
  const [randomNumber, setRandomNumber] = useState(null);
  const [generatedNumbers, setGeneratedNumbers] = useState([]);

  const generateRandomNumber = () => {
    let newNumber;
    do {
      newNumber = Math.floor(Math.random() * (4000 - 2000 + 1)) + 2000;
    } while (generatedNumbers.includes(newNumber));

    setRandomNumber(newNumber);
    setGeneratedNumbers([...generatedNumbers, newNumber]);
  };

  // Automatically generate a random number when the component mounts
  useEffect(() => {
    generateRandomNumber();
  }, []); // The empty dependency array ensures this runs only once

  return (
    <div>
      <h1>WALK IN HOME</h1>
      <div style={{ textAlign: "center", fontSize: "50px", marginTop: "20px" }}>
        {randomNumber !== null ? randomNumber : "Generating..."}
      </div>
    </div>
  );
}

export default WalkInHome;
