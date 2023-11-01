import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

function QTicketPage() {
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
          <div style={{ textAlign: "center", fontSize: "50px", marginTop: "20px" }}>
            <h1>Queue ticket number is </h1>
          </div>
          <Link to="/walkinHome">
        <button className="queue-button">Back</button>
      </Link>
    
        </div>
    );
}

export default QTicketPage;
