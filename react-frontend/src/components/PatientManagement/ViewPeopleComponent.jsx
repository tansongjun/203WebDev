import React, { useState, useEffect } from 'react';
import PeopleService from '../../services/PeopleService';
import { useParams, Link } from 'react-router-dom';

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
    const [todayAppts, setTodayAppts] = useState();

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
            fetch(LOGIN_URL + `/patients/${id}/getFutureappt`, {
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

        const gettodaytic = () => {
            fetch(LOGIN_URL + `/patients/${id}/getQtoday`, {
                method: 'GET',
                headers: {
                    authorization: 'Basic ' + btoa(auth.user + ':' + auth.pwd)
                }
            })
                .then(res => res.json())
                .then(data => {
                    console.log(data);
                    setTodayAppts(data);
                })
                .catch(console.log);
        }



        PeopleService.getPeopleById(id, auth).then(res => {
            // console.log(res.data)
            let data = res.data;
            data.authorities = data.authorities[0].authority;
            setPeople(data);
            futureAppts();
            gettodaytic();
        });

    }, [id]);


    return (
        <div>
            <br />
            <div className="card col-md-6 offset-md-3">
                <h3 className="text-center"> Patient Details</h3>
                <div className="card-body">
                    <div className="row">
                        <label>NRIC: </label>
                        <div> {people.nric}</div>
                    </div>
                    <div className="row">
                        <label>First Name: </label>
                        <div> {people.firstName}</div>
                    </div>
                    <div className="row">
                        <label>Last Name: </label>
                        <div> {people.lastName}</div>
                    </div>
                    <div className="row">
                        <label>Email: </label>
                        <div> {people.emailId}</div>
                    </div>
                    <div className="row">
                        <label>Age: </label>
                        <div> {people.age}</div>
                    </div>
                    <div className="row">
                        <label>Birthday: </label>
                        <div> {people.birthDate}</div>
                    </div>
                    <div className="row">
                        <label>Condition: </label>
                        <div> {people.condition}</div>
                    </div>
                    <div className="row">
                        <label>Risk Level: </label>
                        <div> {people.riskLevel}</div>
                    </div>
                    <div className="row">
                        <label>Username: </label>
                        <div> {people.username}</div>
                    </div>
                    <div className="row">
                        <label>Authorities: </label>
                        <div> {people.authorities}</div>
                    </div>
                    {
                        todayAppts && todayAppts.dateTimeSlot == null && todayAppts.qTicket != null ?
                        <div>
                            <br />
                            <h3 className="text-center">Today's Appointment</h3>
                            <table className="table table-striped table-bordered" style={{ marginTop: "10px", marginLeft: "75px", width: "90%" }}>
                                <thead>
                                    <tr>
                                        <th style={{ width: "20%" }}>Waiting number</th>
                                        <th style={{ width: "25%" }}>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>{todayAppts.qTicket.waitingNo}</td>
                                        <td>{todayAppts.qTicket.qstatus}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        : todayAppts && todayAppts.dateTimeSlot != null ?
                        <div>
                            <br />
                            <h3 className="text-center">Today's Appointment</h3>
                            <table className="table table-striped table-bordered" style={{ marginTop: "10px", marginLeft: "75px", width: "90%" }}>
                                <thead>
                                    <tr>
                                        <th style={{ width: "20%" }}>Start time</th>
                                        <th style={{ width: "25%" }}>End time</th>
                                        <th style={{ width: "25%" }}>Room Number</th>
                                        <th style={{ width: "25%" }}>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>{todayAppts.dateTimeSlot.startDateTime.substring(11)}</td>
                                        <td>{todayAppts.dateTimeSlot.endDateTime.substring(11)}</td>
                                        <td>{todayAppts.dateTimeSlot.room.roomNumber}</td>

                                        <td>
                                            {todayAppts.qTicket.qstatus}
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        : null
                    }

                </div>
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

            <div >
                <Link to="/StaffHome">
                    <button className="payment-button">Back</button>
                </Link>
            </div>
        </div>
    );
}

export default ViewPeopleComponent;
