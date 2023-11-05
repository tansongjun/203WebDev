import React, { Component, useEffect } from 'react';
import PeopleService from '../../services/PeopleService';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
const ListPeopleComponentInner = () => {
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

    const verifyPeople = (id) => {
        // Send a POST request to the verification endpoint
        axios.post(`http://localhost:8080/api/v1/admin/person/verify/${id}`, null, {
            auth: {
                username: auth.user,
                password: auth.pwd,
            },
        }).then(res => {
            if (res.status === 200) {
                alert('Patient verified successfully.');
                window.location.reload();
            }
        }).catch(err => {
            console.log(err);
        });
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

    const addPeople = () => {
        navigate('/add-patient');
    }

    return (
        <div>
            <h2 className="text-center">User List</h2>
            <div className="row">
                <button className="btn btn-primary" onClick={addPeople} style={{ marginTop: "50px", marginLeft: "75px" }}>Add Patient</button>
            </div>
            <div className="row">
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
                            people.map(
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
                </table>
            </div>
        </div>
    );

}

function ListPeopleComponent(props) {
    return <ListPeopleComponentInner {...props} />;
}

export default ListPeopleComponent;
