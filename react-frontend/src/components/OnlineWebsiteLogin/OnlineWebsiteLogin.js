import LoginComponent from "../LoginComponent/LoginComponent";
import { Link, useNavigate } from "react-router-dom";

function OnlineWebsiteLogin() {
    const buttonStyle = {
        marginTop: '50px'
      };

  return (
    <div>
      <h1>ONLINE WEBSITE LOGIN</h1>
      <LoginComponent
        handleNavigation={"/OnlineWebsite"}
        userType={"ROLE_PATIENT"}
      />
      <Link to="/">
        <button style={buttonStyle} className="payment-button">Log out</button>
      </Link>
    </div>
  );
}

export default OnlineWebsiteLogin;
