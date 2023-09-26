import axios from "axios";
import { useState } from "react";

function WalkInHome() {

  const [randomNumber, setRandomNumber] = useState(null);
  const [generatedNumbers, setGeneratedNumbers] = useState([]);
  const [buttonDisabled, setButtonDisabled] = useState(false);
  const [auth, setAuth] = useState({
    user: sessionStorage.getItem('user'),
    pwd: sessionStorage.getItem('pwd'),
    personId: sessionStorage.getItem('person_id')

  });
  const [errorMessages, setErrorMessages] = useState('');

  const LOGIN_URL = 'http://localhost:8080/api/v1';

  const handleSubmit = async (e) => {
    e.preventDefault();
    // console.log(user, pwd);
    let queryURL = LOGIN_URL + '/patients/'+ auth.personId +'/getnewQ';
    console.log(queryURL);
    console.log(auth.user, auth.pwd);

    
    axios.post(
      queryURL,{},
      {
        auth: {
          username: `${auth.user}`,
          password: `${auth.pwd}`
        }
      }).then(function (response) {
        if (response.status === 200) {
          let resultjson = response.data;


          setRandomNumber(resultjson.ticketno);
          setButtonDisabled(true);

          // console.log(authi);
          // setUser(user);
          // setPwd(pwd);
          // window.location.replace('/walkinHome')
        } else {
          console.log(response.status);
        }
      }).catch(function (error) {
        console.log(error);
        // setErrorMessages(error.response.data.message);
      });
      

  }

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
      {!buttonDisabled ?<button
        className="queue-button"
        onClick={handleSubmit}
        disabled={buttonDisabled} // Set the disabled attribute based on buttonDisabled state
      >
        Get Queue Number
      </button>:null}
    </div>
  );
}

export default WalkInHome;
