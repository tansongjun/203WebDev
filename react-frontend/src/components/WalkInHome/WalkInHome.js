import { useState } from "react";

function WalkInHome() {
  const [randomNumber, setRandomNumber] = useState(null);
  const [generatedNumbers, setGeneratedNumbers] = useState([]);
  const [buttonDisabled, setButtonDisabled] = useState(false);

  const generateRandomNumber = () => {
    let newNumber;
    do {
      newNumber = Math.floor(Math.random() * (4000 - 2000 + 1)) + 2000;
    } while (generatedNumbers.includes(newNumber));

    setRandomNumber(newNumber);
    setGeneratedNumbers((previouslyGeneratedNumber) => [
      ...previouslyGeneratedNumber,
      newNumber,
    ]);

    setButtonDisabled(true);
  };

  return (
    <div>
      <h1>WALK IN HOME</h1>
      <div style={{ textAlign: "center", fontSize: "50px", marginTop: "20px" }}>
        {randomNumber !== null
          ? "Your Queue Number: " + randomNumber
          : "Register here"}
      </div>
      <button
        className="queue-button"
        onClick={generateRandomNumber}
        disabled={buttonDisabled} // Set the disabled attribute based on buttonDisabled state
      >
        Get Queue Number
      </button>
    </div>
  );
}

export default WalkInHome;
