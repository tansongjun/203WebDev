import React, { useState } from 'react';
import PeopleService from '../services/PeopleService';
import { useNavigate } from 'react-router-dom';

function CreatePeopleComponent() {
    const navigate = useNavigate();

    const [state, setState] = useState({
        firstName: '',
        lastName: '',
        emailId: '',
        age: '',
        condition: '',
        username: '',
        password: '',
        authorities: 'ROLE_PATIENT'
    });
    const [auth, setAuth] = React.useState({
        user: sessionStorage.getItem('user'),
        pwd: sessionStorage.getItem('pwd'),
        personId: sessionStorage.getItem('person_id')
    })

    const savePeople = (e) => {
        e.preventDefault();
        let people = {
            firstName: state.firstName,
            lastName: state.lastName,
            emailId: state.emailId,
            age: state.age,
            condition: state.condition,
            username: state.username,
            password: state.password,
            authorities: state.authorities
        };

        PeopleService.createPeople(people,auth).then((res) => {
            if (res.status === 200) {
                alert('Patient added successfully.');
                navigate('/patient');

            }
        }).catch(err => {
            console.log(err.response.data);
            alert(err.response.data.message);
            console.log(err.response.data.errors);
        });
    };

    return (
        <div>
            <br />
            <div className="container">
                <div className="row">
                    <div className="card col-md-6 offset-md-3 offset-md-3">
                        <h3 className="text-center">Add Patient</h3>
                        <div className="card-body">
                            <form>
                                <div className="form-group">
                                    <label> First Name: </label>
                                    <input
                                        placeholder="First Name"
                                        name="firstName"
                                        className="form-control"
                                        value={state.firstName}
                                        onChange={(e) =>
                                            setState({
                                                ...state,
                                                firstName: e.target.value
                                            })
                                        }
                                    />
                                </div>
                                <div className="form-group">
                                    <label> Last Name: </label>
                                    <input
                                        placeholder="Last Name"
                                        name="lastName"
                                        className="form-control"
                                        value={state.lastName}
                                        onChange={(e) =>
                                            setState({
                                                ...state,
                                                lastName: e.target.value
                                            })
                                        }
                                    />
                                </div>
                                <div className="form-group">
                                    <label> Email Id: </label>
                                    <input
                                        placeholder="Email Address"
                                        name="emailId"
                                        className="form-control"
                                        value={state.emailId}
                                        onChange={(e) =>
                                            setState({
                                                ...state,
                                                emailId: e.target.value
                                            })
                                        }
                                    />
                                </div>
                                <div className="form-group">
                                    <label> Age: </label>
                                    <input
                                        placeholder="Age"
                                        name="age"
                                        className="form-control"
                                        value={state.age}
                                        onChange={(e) =>
                                            setState({
                                                ...state,
                                                age: e.target.value
                                            })
                                        }
                                    />
                                </div>
                                <div className="form-group">
                                    <label> Username: </label>
                                    <input
                                        placeholder="Username"
                                        name="username"
                                        className="form-control"
                                        value={state.username}
                                        onChange={(e) =>
                                            setState({
                                                ...state,
                                                username: e.target.value
                                            })
                                        }
                                    />
                                </div>
                                <div className="form-group">
                                    <label> Password: </label>
                                    <input
                                        type='password'
                                        placeholder="Password"
                                        name="password"
                                        className="form-control"
                                        value={state.password}
                                        onChange={(e) =>
                                            setState({
                                                ...state,
                                                password: e.target.value
                                            })
                                        }
                                    />
                                </div>
                                <div className="form-group">
                                    <label> Condition: </label>
                                    <input
                                        placeholder="Condition"
                                        name="condition"
                                        className="form-control"
                                        value={state.condition}
                                        onChange={(e) =>
                                            setState({
                                                ...state,
                                                condition: e.target.value
                                            })
                                        }
                                    />
                                </div>

                                <button
                                    className="btn btn-success"
                                    onClick={savePeople}
                                >
                                    Save
                                </button>
                                <button
                                    className="btn btn-danger"
                                    onClick={() => navigate('/patient')}
                                    style={{ marginLeft: '10px' }}
                                >
                                    Cancel
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default CreatePeopleComponent;
