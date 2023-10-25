import React, { useRef, useState, useEffect, useContext } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import axios from 'axios';

function RegisterComponent({handleNavigation, userType}) {
  const LOGIN_URL = 'http://localhost:8080/api/v1';
  // user reference set focus for user
  const userRef = useRef();
  const errRef = useRef();

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
        LOGIN_URL + '/login/username',
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
              403: 'Error: You are not allowed to access patient records!'
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
              <h1>Register here</h1>
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

  // // Initialize useNavigate & location
  // const navigate = useNavigate();
  // const location = useLocation();

  // // state: render Error Messages
  // const [errorMessages, setErrorMessages] = useState({ });


  // // User Login info
  // const database = [
  //   {
  //     username: "user1",
  //     password: "pass1"
  //   },
  //   {
  //     username: "user2",
  //     password: "pass2"
  //   }
  // ];

  // const errors = {
  //   uname: "invalid username",
  //   pass: "invalid password"
  // };

  // const handleSubmit = (event) => {
  //   // Prevent page reload
  //   event.preventDefault();

  //   var { uname, pass } = event.target;

  //   // Find user login info
  //   const userData = database.find((user) => user.username === uname.value); 

  //   // do axios 
  //   // 1. do axios connection to api
  //   // axios.get('http://localhost:8080/api/v1/login')
  //   //   .then(res => { console.log("Retrieved all messages successfully", res); })
  //   //   .catch(err => { console.log("Could not retrieve messages", err) });
  //   // 2. after calling api, if 404 error, return message as windows.alert
  //   console.log(uname.value, pass.value);



  //   // const requestOptions = {
  //   //   method: 'GET',
  //   //   headers: { 'Content-Type': 'application/json' },
  //   //   body: JSON.stringify({ username: uname.value, password: pass.value })
  //   // };

  //   // console.log(requestOptions);
  //   // fetch('http://localhost:8080/api/v1/login', requestOptions)
  //   //   .then(async response => {
  //   //     const isJson = response.headers.get('content-type')?.includes('application/json');
  //   //     console.log(isJson);
  //   //     const data = isJson && await response.json();
  //   //     console.log(data);

  //   //     // check for error response
  //   //     if (!response.ok) {
  //   //       // get error message from body or default to response status
  //   //       const error = (data && data.message) || response.status;
  //   //       console.log(Promise.reject(error));
  //   //     }
  //   //   })
  //   //   .catch(error => {
  //   //     // this.setState({ errorMessage: error.toString() });
  //   //     console.error('There was an error!', error);
  //   //   });

  //   // } else if (userType == "ROLE_ADMIN") {
  //   //   const requestOptions = {
  //   //     method: 'GET',
  //   //     headers: { 'Content-Type': 'application/json' },
  //   //     body: JSON.stringify({ username: uname, password: pass })
  //   //   };
  //   //   fetch('http://localhost:8080/api/v1/admin/login', requestOptions)
  //   //     .then(response => response.json())
  //   //     .then(data => this.setState({ postId: data.id }));
  //   // }

  //   // before navigate
  //   // navigate(handleNavigation);

  //   // do cookie


  //   // Compare user info
  //   if (userData) {
  //     if (userData.password !== pass.value) {
  //       // Invalid password
  //       setErrorMessages({ name: "pass", message: errors.pass });
  //     } else {
  //       const currentPath = location.pathname;
  //       let redirectTo = "/StaffHome"; // Default redirection URL

  //      if (currentPath === '/walkinLogin') {
  //       redirectTo = "/walkinHome";
  //      }else if(currentPath ==='/appointmentLogin'){
  //       redirectTo = "/appointmentHome";
  //      }

  //       navigate(redirectTo);
  //     }
  //   } else {
  //     // Username not found
  //     setErrorMessages({ name: "uname", message: errors.uname });
  //   }
  // };

  // // JSX code: Error Message
  // const renderErrorMessage = (name) =>
  //   name === errorMessages.name && (
  //     <div className="error">{errorMessages.message}</div>
  //   );

  // // JSX code: Login Form
  // const renderForm = (
  //   <div className="form">
  //     <form onSubmit={handleSubmit}>
  //       <div className="input-container">
  //         <label>Username </label>
  //         <input type="text" name="uname" required />
  //         {renderErrorMessage("uname")}
  //       </div>
  //       <div className="input-container">
  //         <label>Password </label>
  //         <input type="password" name="pass" required />
  //         {renderErrorMessage("pass")}
  //       </div>
  //       <div className="button-container">
  //         <input type="submit" />
  //       </div>
  //     </form>
  //   </div>
  // );

  // return (
  //   <div className="app">
  //     <div className="login-form">
  //       <div className="title">Sign In</div>
  //       {renderForm}
  //     </div>
  //   </div>
  // );