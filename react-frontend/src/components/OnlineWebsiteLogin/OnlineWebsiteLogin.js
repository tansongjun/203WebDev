import LoginComponent from "../LoginComponent/LoginComponent";

function OnlineWebsiteLogin() {
    return (
        <div>
           <h1>ONLINE WEBSITE LOGIN</h1>
           <LoginComponent handleNavigation={"/OnlineWebsite"} userType={"ROLE_PATIENT"}/>
        </div>
    );
}

export default OnlineWebsiteLogin;
