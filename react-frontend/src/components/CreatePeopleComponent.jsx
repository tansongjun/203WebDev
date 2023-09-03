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
        condition: ''
    });

    const savePeople = (e) => {
        e.preventDefault();
        let people = {
            firstName: state.firstName,
            lastName: state.lastName,
            emailId: state.emailId,
            age: state.age,
            condition: state.condition
        };

        PeopleService.createPeople(people).then(() => {
            navigate('/patient');
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
