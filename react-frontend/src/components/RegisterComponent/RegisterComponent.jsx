import React, { useRef, useState, useEffect, useContext } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import Axios from "axios";

function RegisterComponent({ handleNavigation, userType }) {
  const url = "http://localhost:8080/api/v1/patient/registration";
  // user reference set focus for user
  const userRef = useRef();
  const errRef = useRef();
  const navigate = useNavigate();
  const LOGIN_URL = localStorage.getItem('url');

  // state: render username input
  const [user, setUser] = useState("");
  // state: render password input
  const [pwd, setPwd] = useState("");
  // state: render Error Messages
  const [errorMessages, setErrorMessages] = useState("");
  // state: render success to respective uri
  const [success, setSuccess] = useState("");
  const [username, setUsername] = useState(""); // Store the username
  const [password, setPassword] = useState(""); // Store the password
  const [emailid, setEmailid] = useState(""); // Store the email id
  const [birthdate, setBirthdate] = useState(""); // Store the birthday
  const [condition, setCondition] = useState(""); // Store the condition
  const [firstname, setFirstname] = useState(""); // Store the firstname
  const [lastname, setLastname] = useState(""); // Store the lastname
  const [nric, setNric] = useState(""); // Store the nric

  const handleSubmit = (e) => {
    e.preventDefault();
    createNewPerson();
  };

  const createNewPerson = async () => {
    const requestData = {
      firstName: firstname,
      lastName: lastname,
      emailId: emailid,
      birthDate: birthdate,
      condition: condition,
      nric: nric,
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
      navigate('/');
    } catch (error) {
      // Handle errors here
      console.error("Error creating a new person:");
      if (error.response) {
        console.log("username:" + username);
        console.log("pw:" + password);
        console.error("Status Code:", error.response.status);
        console.error("Response Data:", error.response.data);
      } else {
        console.error("Error Details:", error.message);
      }
    }
  };

  const conditionOptions = ["NONE", "MODERATE", "MILD", "SEVERE"];

  return (
    <>
      {" "}
      {success ? (
        <p>success</p>
      ) : (
        // window.location.replace('/walkinHome')
        // (authy === 'ROLE_PATIENT') ? (window.location.replace('/walkinHome')) : (window.location.replace(''))
        <div className="app">
          <div className="login-form">
            <section>
              {/* aria-live: assertive to show error message immediately */}
              <p
                ref={errRef}
                className={errorMessages ? "errormessages" : "offscreen"}
                aria-live="assertive"
              >
                {errorMessages}
              </p>
              <h1>Register here</h1>
              <form onSubmit={handleSubmit}>
                <label htmlFor="firstname">First Name:</label>
                <br />
                <input
                  type="text"
                  id="firstname"
                  ref={userRef}
                  autoComplete="off"
                  onChange={(e) => setFirstname(e.target.value)}
                  value={firstname}
                  // placeholder=''
                  required
                />
                <br />

                <label htmlFor="lastname">Last Name:</label>
                <br />
                <input
                  type="text"
                  id="lastname"
                  ref={userRef}
                  autoComplete="off"
                  onChange={(e) => setLastname(e.target.value)}
                  value={lastname}
                  // placeholder='enter password'
                  required
                />
                <br />

                <label htmlFor="emailid">Email id:</label>
                <br />
                <input
                  type="text"
                  id="emailid"
                  ref={userRef}
                  autoComplete="off"
                  onChange={(e) => setEmailid(e.target.value)}
                  value={emailid}
                  // placeholder='enter emailid'
                  required
                />
                <br />

                <label htmlFor="birthdate">Birthdate:</label>
                <br />
                <input
                  type="text"
                  id="birthdate"
                  ref={userRef}
                  autoComplete="off"
                  onChange={(e) => setBirthdate(e.target.value)}
                  value={birthdate}
                  placeholder="YYYY-MM-DD"
                  required
                />
                <br />

                <label htmlFor="nric">NRIC:</label>
                <br />
                <input
                  type="text"
                  id="nric"
                  ref={userRef}
                  autoComplete="off"
                  onChange={(e) => setNric(e.target.value)}
                  value={nric}
                  placeholder='SXXXXXXXA'
                  required
                />
                <br />

                <label htmlFor="username">Username:</label>
                <br />
                <input
                  type="text"
                  id="username"
                  ref={userRef}
                  autoComplete="off"
                  onChange={(e) => setUsername(e.target.value)}
                  value={username}
                  //  placeholder='YYYY-MM-DD'
                  required
                />
                <br />

                <label htmlFor="password">Password:</label>
                <br />
                <input
                  type="password"
                  id="password"
                  ref={userRef}
                  autoComplete="off"
                  onChange={(e) => setPassword(e.target.value)}
                  value={password}
                  //  placeholder='YYYY-MM-DD'
                  required
                />
                <br />


                <label htmlFor="condition">Condition:</label>
                <br />
                {/* <input
                  type="text"
                  id="condition"
                  ref={userRef}
                  autoComplete="off"
                  onChange={(e) => setCondition(e.target.value)}
                  value={condition}
                  //  placeholder='YYYY-MM-DD'
                  required
                /> */}
                 <select
                  id="condition"
                  ref={userRef}
                  onChange={(e) => setCondition(e.target.value)}
                  value={condition}
                  required
                >
                  {conditionOptions.map((option) => (
                    <option key={option} value={option}>
                      {option}
                    </option>
                  ))}
                </select>
                <br />
                <br />
                <br />
                <button style={{ border: '2px solid #000' }}
                 className="cetn_btn">Sign Up</button>
                <br />
                <br />
              </form>
            </section>
          </div>
        </div>
      )}
    </>
  );
}

export default RegisterComponent;
