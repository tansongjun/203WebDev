import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

function UserLogin() {
    const navigate = useNavigate();

    const handleQueueButtonClick = () => {
        navigate('/walkin');
    };
    const handleStaffButtonClick = () => {
      navigate('/staffLogin'); 
    };

    const handleAppointmentButtonClick = () => {
      navigate('/appointmentLogin');
    };

    return (
        <div>
            <Link to="/walkinLogin">
                <button className="queue-button" onClick={handleQueueButtonClick}>
                    Get Queue number
                </button>
            </Link>
            <Link to="/staffLogin">
                <button className="staff-button" onClick={handleStaffButtonClick}>Staff</button>
            </Link>
            <Link to="/appointmentLogin">
                <button className="appointment-button" onClick={handleAppointmentButtonClick}>
                  Make appointments
                </button>
            </Link>
        </div>
    );
}

export default UserLogin;
