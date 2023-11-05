import LoginComponent from "../LoginComponent/LoginComponent";
import { Link, useNavigate } from 'react-router-dom';

function StaffLogin() {
    const buttonStyle = {
        marginTop: '50px'
      };
  return (
    <div>
      <h1>STAFF LOGIN</h1>
      <LoginComponent handleNavigation={"/StaffHome"} userType={"ROLE_ADMIN"} />
      <Link to="/">
        <button style={buttonStyle}  className="payment-button">Log out</button>
      </Link>

    </div>
  );
}

export default StaffLogin;
