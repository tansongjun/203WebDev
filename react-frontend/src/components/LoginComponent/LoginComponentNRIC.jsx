import React, { useRef, useState, useEffect, useContext } from "react";
import { useNavigate, useLocation } from "react-router-dom";

import "./LoginComponent.css";
import axios from 'axios';

function LoginComponentNRIC({ handleNavigation, userType }) {
  const LOGIN_URL = localStorage.getItem('url');
  // user reference set focus for user
  const userRef = useRef();
  const errRef = useRef();
  const navigate = useNavigate();

  // state: render username input
  const [user, setUser] = useState('');
  const [nric, setNRIC] = useState('');

  // state: render password input
  const [pwd, setPwd] = useState('');
  // state: render authorities input
  const [authy, setAuthi] = useState('');
  // state: render Error Messages
  const [errorMessages, setErrorMessages] = useState('');
  // state: render success to respective uri
  const [success, setSuccess] = useState('');

  // effect: set focus on user input
  useEffect(() => {
    userRef.current.focus();
  }, [])

  // effect: empty out error message, if user changes username or password input
  useEffect(() => {
    setErrorMessages('');
  }, [user, pwd])

  // function: handle submit details
  const handleSubmit = async (e) => {
    e.preventDefault();
    const link = LOGIN_URL + '/loginNRIC/' + nric;
    console.log(link);
    axios.get(
      link
    ).then(function (response) {
      const result = response?.data.person;
      const password = response?.data.password;
      if (response.status === 200) {
        const authi = result.authorities[0]?.authority;

        sessionStorage.setItem('user', result.username);
        sessionStorage.setItem('pwd', password);
        sessionStorage.setItem('person_id', result.id);

        console.log(authi);
        console.log(response.data);

        alert('Login successful!');
        // setUser(user);
        // setPwd(pwd);
        window.location.replace('/walkinHome')
      } else {
        console.log(response.status);
      }
    }).catch(async function (error) {
      if (error.response) {
        // The request was made and the server responded with a status code that falls out of the range of 2xx
        console.log('error.data', error.response.data);
        // console.log('error.headers', error.response.headers);
        console.log('error.status:', error.response.status, error.response.data.message);
        let errorMessages = {
          401: 'Error: Wrong username or password!',
          403: 'Error: You are not verified!. Please approach the staff for assitance.'
        };
        setErrorMessages(errorMessages[error.response.status])

        errRef.current.focus();
        await window.alert(errorMessages[error.response.status]);

      } else if (error.request) {
        // The request was made but no response was received
        // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
        // http.ClientRequest in node.js
        console.log('error.request', error.request);
      } else {
        // Something happened in setting up the request that triggered an Error
        console.log('error.message', error);
      }
    });


  }

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
              <h1>Sign In</h1>
              <form onSubmit={handleSubmit}>
                <label htmlFor='nric'>NRIC:</label><br />
                <input
                  type='text'
                  id='nric'
                  ref={userRef}
                  autoComplete="off"
                  onChange={(e) => setNRIC(e.target.value)}
                  value={nric}
                  placeholder='enter nric'
                  required
                />
                <br /><br /><br />

                <button className="cetn_btn">Enter</button><br /><br />
                <p className="cetn_sgn">
                  {/* <span className="line">
                    {/* {navigate('/registration')} 
                    <a href="/registration">Register an Account</a>
                  </span> */}
                </p>
              </form>
            </section>
          </div>
        </div>
      )}
    </>
  )
}

export default LoginComponentNRIC;
