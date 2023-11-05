import LoginComponentNRIC from "../LoginComponent/LoginComponentNRIC";
import { Link, useNavigate } from "react-router-dom";

function WalkInLogin() {
  return (
    <div>
      <h1>WALK IN LOGIN</h1>
      <LoginComponentNRIC
        handleNavigation={"/walkinHome"}
        userType={"ROLE_PATIENT"}
      />
      <Link to="/">
        <button className="payment-button">Log out</button>
      </Link>
    </div>
  );
}

export default WalkInLogin;
