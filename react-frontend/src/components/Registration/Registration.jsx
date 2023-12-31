import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import RegisterComponent from '../RegisterComponent/RegisterComponent';

function Registration() {
    const [selectedButton1, setSelectedButton1] = useState(null);
    const [selectedButton2, setSelectedButton2] = useState(null);
    const [selectedButton3, setSelectedButton3] = useState(null);
    const navigate = useNavigate();

    const buttonStyle = {
        marginTop: '10px'
      };

    
    const handleSubmit = () => {
        // if (selectedButton1 === 'Yes' || selectedButton2 === 'Yes' || selectedButton3 === 'Yes') {
        //     alert('Please head to the front desk immediately');
        //     navigate('/walkinHome');
        // }else{
            navigate('/');
        // }
    };

    return (
        <div>
           <h1>REGISTRATION</h1>
           <RegisterComponent handleNavigation={"/walkinHome"} userType={"ROLE_PATIENT"}/>
           <button style={buttonStyle} className="back-button" onClick={handleSubmit}>
                Back
            </button>
        </div>
    );
}

export default Registration;
