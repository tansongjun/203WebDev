import LoginComponentNRIC from "../LoginComponent/LoginComponentNRIC";

function WalkInLogin() {
    return (
        <div>
           <h1>WALK IN LOGIN</h1>
           <LoginComponentNRIC handleNavigation={"/walkinHome"} userType={"ROLE_PATIENT"}/>
        </div>
    );
}

export default WalkInLogin;
