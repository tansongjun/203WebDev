import LoginComponent from "../LoginComponent/LoginComponent";
import { Link, useNavigate } from 'react-router-dom';

function OnlineWebsite() {
  const backURL = "/OnlineWebsite";
  return (
    <div>
      <h1>ONLINE WEBSITE</h1>
      {/* <LoginComponent handleNavigation={"/appointmentHome"} userType={"ROLE_PATIENT"}/> */}
      
      <br></br>
      
      <Link to={{pathname: `/appointmentHome`}} state={{ query:backURL }} >
        <button className="appointment-button">Book an Appointment</button>
      </Link>

      <br></br>
      <Link to={{pathname: `/walkinHome/payment`}}  state={{ query:backURL }}>
        <button className="payment-button">Payment</button>
      </Link>

      <br></br>
      <Link to="/">
        <button className="payment-button">Log out</button>
      </Link>
    </div>
  );
}

export default OnlineWebsite;
