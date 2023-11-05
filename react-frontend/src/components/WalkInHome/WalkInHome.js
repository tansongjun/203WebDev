import axios from "axios";
import { useState } from "react";
import { Link, useNavigate } from 'react-router-dom';

function WalkInHome() {
  const backURL = "/walkinHome";
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

  const navigate = useNavigate();
  const handleAppointmentButtonClick = () => {
    navigate(1);
  };
  const getQueueButtonClick = () => {
    navigate(1);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
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
        } else {
          console.log(response.status);
        }
      }).catch(function (error) {
        console.log(error);
      });
  }

  return (
    <div>
      <h1>WALK IN HOME</h1>
      <div style={{ textAlign: "center", fontSize: "50px", marginTop: "20px" }}>
        {randomNumber !== null
          ? "Your Queue Number: " + randomNumber
          : "Welcome"}
      </div>
      <Link to="/walkinHome/questionnaire">
        <button
          className="queue-button" onClick={getQueueButtonClick}>
          Get Queue Number
        </button>
      </Link>
      
      <br></br>
      <Link to="/appointmentHome" state={{ query:backURL }}>
        <button className="appointment-button">
          Book an Appointment 
        </button>
        </Link>

      <br></br>
      <Link to="/walkinHome/payment" state={{ query:backURL }}>
        <button className="payment-button">
          Payment
        </button>
      </Link>
      <br></br>

      <Link to="/">
        <button className="payment-button">
          Log out
        </button>
      </Link>
      <br></br>

    </div>
  );
}

export default WalkInHome;