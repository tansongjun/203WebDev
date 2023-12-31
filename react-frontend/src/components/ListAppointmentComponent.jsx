import React, { Component, useEffect } from 'react';
import PeopleService from '../services/PeopleService';
import { useNavigate } from 'react-router-dom';

const ListAppointmentComponentInner = () => {
    const navigate = useNavigate();

    const [people, setPeople] = React.useState([]);
    const [auth, setAuth] = React.useState({
        user: sessionStorage.getItem('user'),
        pwd: sessionStorage.getItem('pwd'),
        personId: sessionStorage.getItem('person_id')
    })

    const deletePeople = (id) => {
        PeopleService.deletePeople(id,auth).then(res => {
            if (res.status === 200){
                alert('Patient deleted successfully.');
                window.location.reload();
            }
        });
    }

    const viewPeople = (id) => {
        navigate(`/view-patient/${id}`);
    }

    const editPeople = (id) => {
        navigate(`/update-patient/${id}`);
    }

    useEffect(() => {
        PeopleService.getPeople(auth)
        .then(res => {

            console.log(res.data);
            setPeople(res.data);
        }).catch(err => {
            console.log(err);
        });

    }, [])

    const addAppointment = () => {
        navigate('/add-appointment');
    }

    return (
        <div>
            <h2 className="text-center">Appointments List</h2>
            <div className="row">
                <button className="btn btn-primary" onClick={addAppointment} style={{ marginTop: "50px", marginLeft: "75px" }}>Add Appointment</button>
            </div>
            <div className="row">
                <table className="table table-striped table-bordered" style={{ marginTop: "10px", marginLeft: "75px", width: "90%" }}>
                    <thead>
                        <tr>
                            <th style={{ width: "10%" }}>Appointment ID</th>
                            <th style={{ width: "20%" }}>Patient First Name</th>
                            <th style={{ width: "20%" }}>Patient Last Name</th>
                            <th style={{ width: "10%" }}>Appointment Date</th>
                            <th style={{ width: "10%" }}>Appointment Time</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            people.map(
                                people =>
                                    <tr key={people.id}>
                                        <td>{people.firstName}</td>
                                        <td>{people.lastName}</td>
                                        <td>{people.emailId}</td>
                                        <td>
                                            <button onClick={() => editPeople(people.id)} className="btn btn-info">Update </button>
                                            <button style={{ marginLeft: "10px" }} onClick={() => deletePeople(people.id)} className="btn btn-danger">Delete </button>
                                            <button style={{ marginLeft: "10px" }} onClick={() => viewPeople(people.id)} className="btn btn-info">View </button>
                                        </td>
                                    </tr>
                            )
                        }
                    </tbody>
                </table>
            </div>
        </div>
    );

}

function ListAppointmentComponent(props) {
    return <ListAppointmentComponentInner {...props} />;
}

export default ListAppointmentComponent;
