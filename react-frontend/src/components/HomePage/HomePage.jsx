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

    const handleAppointmentButtonClick = () => {
      navigate(1);
    };

    return (
        <div>
            <br/>
            <Link to="/walkinLogin">
                <button className="queue-button" onClick={handleQueueButtonClick}>
                    Get Queue number
                </button>
            </Link>
            <br/>
            <Link to="/staffLogin">
                <button className="staff-button" onClick={handleStaffButtonClick}>Staff</button>
            </Link>
            <br/>
            <Link to="/appointmentLogin">
                <button className="appointment-button" onClick={handleAppointmentButtonClick}>
                  Make appointments
                </button>
            </Link>
        </div>
    );
}

export default HomePage;
