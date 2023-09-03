import React, { useState, useEffect } from 'react';
import PeopleService from '../services/PeopleService';
import { useNavigate, useParams } from 'react-router-dom';

function UpdatePeopleComponent() {
    const navigate = useNavigate();
    const { id } = useParams();

    const [state, setState] = useState({
        firstName: '',
        lastName: '',
        emailId: '',
        age: '',
        condition: ''
    });

    useEffect(() => {
        // Fetch the data for the patient using the given ID
        PeopleService.getPeopleById(id).then(res => {
            const data = res.data;
            setState({
                firstName: data.firstName,
                lastName: data.lastName,
                emailId: data.emailId,
                age: data.age,
                condition: data.condition
            });
        });
    }, [id]);

    const updatePeople = (e) => {
        e.preventDefault();
        let people = {
            firstName: state.firstName,
            lastName: state.lastName,
            emailId: state.emailId,
            age: state.age,
            condition: state.condition
        };

        PeopleService.updatePeople(people, id).then(() => {
            navigate('/patient');
        });
    };

    return (
        <div>
            <br />
            <div className="container">
                <div className="row">
                    <div className="card col-md-6 offset-md-3 offset-md-3">
                        <h3 className="text-center">Update Patient</h3>
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
                                    onClick={updatePeople}
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

export default UpdatePeopleComponent;
