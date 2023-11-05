import React, { useState, useEffect } from 'react';
import PeopleService from '../../services/PeopleService';
import { useParams } from 'react-router-dom';

function ViewPeopleComponent() {
    const { id } = useParams();
    const [auth, setAuth] = React.useState({
        user: sessionStorage.getItem('user'),
        pwd: sessionStorage.getItem('pwd'),
        personId: sessionStorage.getItem('person_id')
    })
    const LOGIN_URL = localStorage.getItem('url');

    const [futureAppts, setFutureAppts] = useState([
        // {
        //     "qTicket": {
        //       "ticketno": 2,
        //       "qstatus": "WAITING"
        //     },
        //     "dateTimeSlot": {
        //       "id": 235,
        //       "startDateTime": "2023-11-10T08:00:00",
        //       "endDateTime": "2023-11-10T08:20:00",
        //       "room": {
        //         "id": 10,
        //         "roomNumber": 1,
        //         "creationDate": "2023-11-10"
        //       }
        //     }
        //   }
    ]);
    const [pastAppts, setPastappts] = useState([]);

    const [people, setPeople] = useState({
        firstName: '',
        lastName: '',
        emailId: '',
        age: '',
        condition: '',
        username: '',
        password: '',
        authorities: ''
    });





    useEffect(() => {
        const futureAppts = () => {
            fetch( LOGIN_URL+ '/patients/'+id+'/getFutureappt', {
                method: 'GET',
                headers: {
                    authorization: 'Basic ' + btoa(auth.user + ':' + auth.pwd)
                }
            })
                .then(res => res.json())
                .then(data => {
                    console.log(data);
                    setFutureAppts(data);
                })
                .catch(console.log);
        }
        
        const pastAppts = () => {
            fetch(LOGIN_URL+ 'patients/{id}/getPastappt', {
                method: 'GET',
                headers: {
                    authorization: 'Basic ' + btoa(auth.user + ':' + auth.pwd)
                }
            })
                .then(res => res.json())
                .then(data => {
                    console.log(data);
                    // setPastappts(data);
                })
                .catch(console.log);
        }

        

        PeopleService.getPeopleById(id, auth).then(res => {
            // console.log(res.data)
            let data = res.data;
            data.authorities = data.authorities[0].authority;
            setPeople(data);
            futureAppts();
        });

    }, [id]);


    return (
        <div>
            <br />
            <div className="card col-md-6 offset-md-3">
                <h3 className="text-center"> Patient Details</h3>
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
                        <label> Authorities: </label>
                        <div> {people.authorities}</div>
                    </div>
                    <br />
                    <h3 className="text-center">Upcoming Appointments</h3>
                    

                    <table className="table table-striped table-bordered" style={{ marginTop: "10px", marginLeft: "75px", width: "90%" }}>
                        <thead>
                            <tr>
                                <th style={{ width: "20%" }}>Date</th>
                                <th style={{ width: "20%" }}>Start time</th>
                                <th style={{ width: "25%" }}>End time</th>
                                <th style={{ width: "25%" }}>Room Number</th>
                                <th style={{ width: "25%" }}>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                futureAppts.map(
                                    appt =>
                                        <tr key={appt.qTicket.ticketno}>
                                            <td>{appt.dateTimeSlot.startDateTime.substring(0, 10)}</td>
                                            <td>{appt.dateTimeSlot.startDateTime.substring(11)}</td>
                                            <td>{appt.dateTimeSlot.endDateTime.substring(11)}</td>
                                            <td>{appt.dateTimeSlot.room.roomNumber}</td>
                                            
                                            <td>
                                                {appt.qTicket.qstatus}
                                            </td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>

                    {/* 
                    <br />
                    <h4 className="text-center"> Past Appointments</h4>
                    <table className="table table-striped table-bordered" style={{ marginTop: "10px", marginLeft: "75px", width: "90%" }}>
                        <thead>
                            <tr>
                                <th style={{ width: "20%" }}>Person First Name</th>
                                <th style={{ width: "20%" }}>Person Last Name</th>
                                <th style={{ width: "25%" }}>Person Email id</th>
                                <th style={{ width: "25%" }}>Person role</th>
                                <th style={{ width: "25%" }}>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                pastAppts.map(
                                    people =>
                                        <tr key={people.id}>
                                            <td>{people.firstName}</td>
                                            <td>{people.lastName}</td>
                                            <td>{people.emailId}</td>
                                            <td>{people.authorities[0].authority}</td>
                                            <td>
                                                <button onClick={() => editPeople(people.id)} className="btn btn-info">Update </button>
                                                <button style={{ marginLeft: "10px" }} onClick={() => deletePeople(people.id)} className="btn btn-danger">Delete </button>
                                                <button style={{ marginLeft: "10px" }} onClick={() => viewPeople(people.id)} className="btn btn-info">View </button>
                                                {people.authorities[0].authority == "ROLE_PATIENT_UNVERIFIED" &&
                                                    <button style={{ marginLeft: "10px" }} onClick={() => verifyPeople(people.id)} className="btn btn-verify">Verify </button>
                                                }
                                            </td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table> */}
                </div>
            </div>
        </div>
    );
}

export default ViewPeopleComponent;
