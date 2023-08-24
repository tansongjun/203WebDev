import React, { Component } from 'react';
import PeopleService from '../services/PeopleService';
import { useNavigate } from 'react-router-dom';

class ListPeopleComponentInner extends Component {
    constructor(props) {
        super(props)

        this.state = {
            people: []
        }
        this.addPeople = this.addPeople.bind(this);
    }

    componentDidMount() {
        PeopleService.getPeople().then(res => {
            this.setState({ people: res.data });
        });
    }
    
    addPeople() {
        this.props.navigate('/add-patient');
    }

    render() {
        return (
            <div>
                <h2 className="text-center">Patient List</h2>
                <div className = "row">
                    <button className = "btn btn-primary" onClick={this.addPeople}>Add Patient</button>
                </div>
                <div className="row">
                    <table className="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>Patient First Name</th>
                                <th>Patient Last Name</th>
                                <th>Patient Email id</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.people.map(
                                    people => 
                                    <tr key={people.id}>
                                        <td>{people.firstName}</td>
                                        <td>{people.lastName}</td>
                                        <td>{people.emailId}</td>
                                    </tr> 
                                )
                            }
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

function ListPeopleComponent(props) {
    const navigate = useNavigate();
    return <ListPeopleComponentInner {...props} navigate={navigate} />;
}

export default ListPeopleComponent;
