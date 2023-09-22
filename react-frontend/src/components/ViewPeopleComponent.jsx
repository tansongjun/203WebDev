import React, { useState, useEffect } from 'react';
import PeopleService from '../services/PeopleService';
import { useParams } from 'react-router-dom';

function ViewPeopleComponent() {
    const { id } = useParams();

    const [people, setPeople] = useState({});

    useEffect(() => {
        PeopleService.getPeopleById(id).then(res => {
            setPeople(res.data);
        });
    }, [id]);


    return (
        <div>
            <br />
            <div className="card col-md-6 offset-md-3">
                <h3 className="text-center"> View Patient Details</h3>
                <div className="card-body">
                    <div className="row">
                        <label> Patient First Name: </label>
                        <div> {people.firstName}</div>
                    </div>
                    <div className="row">
                        <label> Patient Last Name: </label>
                        <div> {people.lastName}</div>
                    </div>
                    <div className="row">
                        <label> Patient Email ID: </label>
                        <div> {people.emailId}</div>
                    </div>
                    <div className="row">
                        <label> Patient Age: </label>
                        <div> {people.age}</div>
                    </div>
                    <div className="row">
                        <label> Patient Condition: </label>
                        <div> {people.condition}</div>
                    </div>
                    <div className="row">
                        <label> Username: </label>
                        <div> {people.username}</div>
                    </div>
                    <div className="row">
                        <label> Password: </label>
                        <div> {people.password}</div>
                    </div>
                    <div className="row">
                        <label> Authorities: </label>
                        <div> {people.authorities}</div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ViewPeopleComponent;
