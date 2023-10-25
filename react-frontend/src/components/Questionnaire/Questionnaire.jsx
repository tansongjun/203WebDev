import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

function Registration() {
    const [selectedButton1, setSelectedButton1] = useState(null);
    const [selectedButton2, setSelectedButton2] = useState(null);
    const [selectedButton3, setSelectedButton3] = useState(null);
    const navigate = useNavigate();


    const toggleBackgroundColor = (button, setButton) => {
        setButton((prevButton) => (prevButton === button ? null : button));
    };

    const handleSubmit = () => {
        if (selectedButton1 === 'Yes' || selectedButton2 === 'Yes' || selectedButton3 === 'Yes') {
            alert('Please head to the front desk immediately');
            navigate('/walkinHome');
        }else{
            navigate('/walkinHome');
        }
    };

    return (
        <div>
            <h2>Do you have a fever above 37.5 degrees?</h2>
            <button
                className="questionnaire-button"
                style={{
                    backgroundColor: selectedButton1 === 'Yes' ? 'rgb(10, 30, 181)' : 'aqua',
                }}
                onClick={() => toggleBackgroundColor('Yes', setSelectedButton1)}
            >
                Yes
            </button>
            <button
                className="questionnaire-button"
                style={{
                    backgroundColor: selectedButton1 === 'No' ? 'rgb(10, 30, 181)' : 'aqua',
                }}
                onClick={() => toggleBackgroundColor('No', setSelectedButton1)}
            >
                No
            </button>

            <h2>Have you been to the Middle East recently?</h2>
            <button
                className="questionnaire-button"
                style={{
                    backgroundColor: selectedButton2 === 'Yes' ? 'rgb(10, 30, 181)' : 'aqua',
                }}
                onClick={() => toggleBackgroundColor('Yes', setSelectedButton2)}
            >
                Yes
            </button>
            <button
                className="questionnaire-button"
                style={{
                    backgroundColor: selectedButton2 === 'No' ? 'rgb(10, 30, 181)' : 'aqua',
                }}
                onClick={() => toggleBackgroundColor('No', setSelectedButton2)}
            >
                No
            </button>

            <h2>Do you have a cough or sore throat?</h2>
            <button
                className="questionnaire-button"
                style={{
                    backgroundColor: selectedButton3 === 'Yes' ? 'rgb(10, 30, 181)' : 'aqua',
                }}
                onClick={() => toggleBackgroundColor('Yes', setSelectedButton3)}
            >
                Yes
            </button>
            <button
                className="questionnaire-button"
                style={{
                    backgroundColor: selectedButton3 === 'No' ? 'rgb(10, 30, 181)' : 'aqua',
                }}
                onClick={() => toggleBackgroundColor('No', setSelectedButton3)}
            >
                No
            </button>
            <><h2>Confirm:</h2></>
            <button className="submit-button" onClick={handleSubmit}>
                Submit
            </button>
        </div>
    );
}

export default Registration;
