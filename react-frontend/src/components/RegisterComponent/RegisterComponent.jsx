import React, { useRef, useState, useEffect, useContext } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import Axios from 'axios';

function RegisterComponent({handleNavigation, userType}) {
  const url = 'http://localhost:8080/api/v1/patient/registration';
  // user reference set focus for user
  const userRef = useRef();
  const errRef = useRef();

  // state: render username input
  const [user, setUser] = useState('');
  // state: render password input
  const [pwd, setPwd] = useState('');
  // state: render Error Messages
  const [errorMessages, setErrorMessages] = useState('');
  // state: render success to respective uri
  const [success, setSuccess] = useState('');
  const [username, setUsername] = useState(''); // Store the username
  const [password, setPassword] = useState(''); // Store the password

  
  const handleSubmit = (e) => {
    e.preventDefault();
    createNewPerson();
  }

  const createNewPerson = async () => {
    const requestData = {
      firstName: "Jacky",
      lastName: "SureCan",
      emailId: "emailId@asd.com",
      birthDate: "1999-02-15",
      condition: "MODERATE",
      username: username,
      password: password,
    };
  
    try {
      const response = await Axios.post(
        `http://localhost:8080/api/v1/patient/registration`,
        requestData,
        {
          headers: {
            "Content-Type": "application/json",
          },
          // auth: {
          //   username: `${auth.user}`,
          //   password: `${auth.pwd}`,
          // },
        }
      );
        
      window.alert("Registration Successful!"); // Display a success message
      console.log("Username:", username); // Log the username
      console.log("Password:", password); // Log the password
      console.log("New Person Created:", response.data);
    } catch (error) {
      // Handle errors here
      console.error("Error creating a new person:");
      if (error.response) {
        console.log("username:"+ username);
        console.log("pw:"+ password);
        console.error("Status Code:", error.response.status);
        console.error("Response Data:", error.response.data);
      } else {
        console.error("Error Details:", error.message);
      }
    }
  };
  
  
  return (
    <> {
      success ? (
        <p>success</p>
        // window.location.replace('/walkinHome')
        // (authy === 'ROLE_PATIENT') ? (window.location.replace('/walkinHome')) : (window.location.replace(''))
      ) : (
        <div className="app">
          <div className="login-form">
            <section>
              {/* aria-live: assertive to show error message immediately */}
              <p ref={errRef} className={errorMessages ? "errormessages" : "offscreen"} aria-live="assertive">{errorMessages}</p>
              <h1>Register here</h1>
              <form onSubmit={handleSubmit}>
                <label htmlFor='username'>Username:</label><br />
                <input
                  type='text'
                  id='username'
                  ref={userRef}
                  autoComplete="off"
                  onChange={(e) => setUsername(e.target.value)
                  } 
                  value={username}
                  // placeholder='enter username'
                  required
                /><br /><br />

                <label htmlFor='password'>Password:</label><br />
                <input
                  type='password'
                  id='password'
                  ref={userRef}
                  autoComplete="off"
                  onChange={(e) => setPassword(e.target.value)}
                  value={password}
                  // placeholder='enter password'
                  required
                /><br /><br /><br />
                <button className="cetn_btn">Sign Up</button><br /><br />
              </form>
            </section>
          </div>
        </div>
      )}
    </>
  )
}

export default RegisterComponent;
