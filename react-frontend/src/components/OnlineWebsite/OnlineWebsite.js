import LoginComponent from "../LoginComponent/LoginComponent";
import { Link, useNavigate } from 'react-router-dom';

function OnlineWebsite() {
  return (
    <div>
      <h1>ONLINE WEBSITE</h1>
      {/* <LoginComponent handleNavigation={"/appointmentHome"} userType={"ROLE_PATIENT"}/> */}
      
      <br></br>
      <Link to="/appointmentHome">
        <button className="appointment-button">Book an Appointment</button>
      </Link>

      <br></br>
      <Link to="/walkinHome/payment">
        <button className="payment-button">Payment</button>
      </Link>
    </div>
  );
}

export default OnlineWebsite;
