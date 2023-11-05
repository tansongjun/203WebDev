import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

function HomePage() {
    const navigate = useNavigate();

    const handleQueueButtonClick = () => {
        navigate(1);
    };
    const handleStaffButtonClick = () => {
      navigate(1); 
    };

    // const handleAppointmentButtonClick = () => {
    //   navigate(1);
    // };

    return (
        <div>
            <br/>
            <Link to="/walkinLogin">
                <button className="queue-button" onClick={handleQueueButtonClick}>
                    Walk In Kiosk 
                </button>
            </Link>
            <br/>
            <Link to="/staffLogin">
                <button className="staff-button" onClick={handleStaffButtonClick}>
                    Staff
                </button>
            </Link>
            <br/>
            <Link to="/OnlineWebsiteLogin">
                <button className="appointment-button" >
                Online Website
                </button>
            </Link>
        </div>
    );
}

export default HomePage;
