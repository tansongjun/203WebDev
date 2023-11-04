import LoginComponent from "../LoginComponent/LoginComponent";

function AppointmentLogin() {
    return (
        <div>
            <h1>APPOINTMENT LOGIN</h1>
            <LoginComponent handleNavigation={"/appointmentHome"} userType={"ROLE_PATIENT"}/>
        </div>
    );
}

export default AppointmentLogin;
