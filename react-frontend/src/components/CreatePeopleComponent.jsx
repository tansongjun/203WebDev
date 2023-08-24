import React, { useState, useEffect } from 'react';
import PeopleService from '../services/PeopleService';
import { useParams, useNavigate } from 'react-router-dom';

function CreatePeopleComponent() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [state, setState] = useState({
        id: id === '_add' ? null : parseInt(id), // Convert to number for updating
        firstName: '',
        lastName: '',
        emailId: ''
    });

    useEffect(() => {
        if (id === '_add') {
            return;
        } else {
            PeopleService.getPeopleById(id).then((res) => {
                let people = res.data;
                setState({
                    ...state,
                    firstName: people.firstName,
                    lastName: people.lastName,
                    emailId: people.emailId
                });
            });
        }
    }, [id]);

    const saveOrUpdatePeople = (e) => {
        e.preventDefault();
        let people = {
            firstName: state.firstName,
            lastName: state.lastName,
            emailId: state.emailId
        };

        if (state.id === null) { // Creating new person
            PeopleService.createPeople(people).then(() => {
                navigate('/patient');
            });
        } else { // Updating existing person
            PeopleService.updatePeople(people, state.id).then(() => {
                navigate('/patient');
            });
        }
    };

    const getTitle = () => {
        return state.id === null ? (
            <h3 className="text-center">Add Patient</h3>
        ) : (
            <h3 className="text-center">Add Patient</h3>
        );
    };

    return (
        <div>
            <br />
            <div className="container">
                <div className="row">
                    <div className="card col-md-6 offset-md-3 offset-md-3">
                        {getTitle()}
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

                                <button
                                    className="btn btn-success"
                                    onClick={saveOrUpdatePeople}
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
