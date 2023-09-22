import LoginComponent from "../LoginComponent/LoginComponent";

function StaffLogin() {
    return (
        <div>
            <h1>STAFF LOGIN</h1>
            <LoginComponent handleNavigation={"/StaffHome"} userType={"ROLE_ADMIN"}/>
        </div>
    );
}

export default StaffLogin;
