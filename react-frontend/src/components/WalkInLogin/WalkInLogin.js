import LoginComponent from "../LoginComponent/LoginComponent";

function WalkInLogin() {
    return (
        <div>
           <h1>WALK IN LOGIN</h1>
           <LoginComponent handleNavigation={"/walkinHome"} userType={"ROLE_PATIENT"}/>
        </div>
    );
}

export default WalkInLogin;
