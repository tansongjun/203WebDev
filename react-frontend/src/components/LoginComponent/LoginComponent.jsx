import React, { useRef, useState, useEffect, useContext } from "react";
import { useNavigate, useLocation } from "react-router-dom";

import "./LoginComponent.css";
import axios from 'axios';

function LoginComponent({handleNavigation, userType}) {
  const LOGIN_URL = 'http://localhost:8080/api/v1';
  // user reference set focus for user
  const userRef = useRef();
  const errRef = useRef();
  const navigate = useNavigate();

  // state: render username input
  const [user, setUser] = useState('');
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
    console.log(user, pwd);

    // if role is patient, go to '/login/username'
    if (userType == 'ROLE_PATIENT') {
      axios.get(
        LOGIN_URL + '/login/'+user,
        {
          auth: {
            username: `${user}`,
            password: `${pwd}`
          }
        }).then(function (response) {
          if (response.status === 200) {
            const authi = response?.data?.authorities[0]?.authority;
            
            sessionStorage.setItem('user', user);
            sessionStorage.setItem('pwd', pwd);
            sessionStorage.setItem('person_id', response?.data?.id);

            console.log(authi);
            console.log(response.data);


            // setUser(user);
            // setPwd(pwd);
            // window.location.replace('/walkinHome')
            window.location.replace(handleNavigation)
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
              403: 'Patient is not verified please check with staff'
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
            console.log('error.message', error.message);
          }
        });
    } else if (userType == 'ROLE_ADMIN') {
      axios.get(
        LOGIN_URL + '/admin/login/admin',
        {
          auth: {
            username: `${user}`,
            password: `${pwd}`
          }
        }).then(function (response) {
          if (response.status === 200) {
            const authi = response?.data?.authorities[0]?.authority;
            sessionStorage.setItem('user', user);
            sessionStorage.setItem('pwd', pwd);
            sessionStorage.setItem('person_id', response?.data?.id);
            // console.log(authi);
            // setUser(user);
            // setPwd(pwd);
            window.location.replace('/StaffHome')
          } else {
            console.log(response.status);
          }
        }).catch(function (error) {
          if (error.response) {
            // The request was made and the server responded with a status code that falls out of the range of 2xx
            console.log('error.data', error.response.data);
            // console.log('error.headers', error.response.headers);
            console.log('error.status:', error.response.status, error.response.data.message);
            let errorMessages = {
              401: 'Error: Wrong username or password!',
              403: 'Error: You are not allowed to access!'
            };
            setErrorMessages(errorMessages[error.response.status])

            errRef.current.focus();
          } else if (error.request) {
            // The request was made but no response was received
            // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
            // http.ClientRequest in node.js
            console.log('error.request', error.request);
          } else {
            // Something happened in setting up the request that triggered an Error
            console.log('error.message', error.message);
          }
        });
    }

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
                <label htmlFor='username'>Username:</label><br />
                <input
                  type='text'
                  id='username'
                  ref={userRef}
                  autoComplete="off"
                  onChange={(e) => setUser(e.target.value)}
                  value={user}
                  // placeholder='enter username'
                  required
                /><br /><br />

                <label htmlFor='password'>Password:</label><br />
                <input
                  type='password'
                  id='password'
                  ref={userRef}
                  autoComplete="off"
                  onChange={(e) => setPwd(e.target.value)}
                  value={pwd}
                  // placeholder='enter password'
                  required
                /><br /><br /><br />
                <button style={{ border: '2px solid #000' }} 
                className="cetn_btn">Sign In</button><br /><br />
                <p className="cetn_sgn">
                  <span className="line">
                    {/* {navigate('/registration')} */}
                    <a href="/registration">Register an Account</a>
                  </span>
                </p>
              </form>
            </section>
          </div>
        </div>
      )}
    </>
  )



}

export default LoginComponent;
