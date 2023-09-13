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
        this.editPeople = this.editPeople.bind(this);
        this.deletePeople = this.deletePeople.bind(this);
    }

    deletePeople(id) {
        PeopleService.deletePeople(id).then( res => {
            this.setState({people: this.state.people.filter(people => people.id !== id)});
        });
    }

    viewPeople(id) {
        this.props.navigate(`/view-patient/${id}`);
    }

    editPeople(id) {
        this.props.navigate(`/update-patient/${id}`);
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
                    <button className = "btn btn-primary" onClick={this.addPeople} style={{marginTop:"50px", marginLeft:"75px"}}>Add Patient</button>
                </div>
                <div className="row">
                    <table className="table table-striped table-bordered" style={{marginTop:"10px", marginLeft:"75px", width:"90%"}}>
                        <thead>
                            <tr>
                                <th style={{width:"20%"}}>Patient First Name</th>
                                <th style={{width:"20%"}}>Patient Last Name</th>
                                <th style={{width:"25%"}}>Patient Email id</th>
                                <th style={{width:"25%"}}>Actions</th>
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
                                        <td>
                                                 <button onClick={ () => this.editPeople(people.id)} className="btn btn-info">Update </button>
                                                 <button style = {{marginLeft:"10px"}} onClick={ () => this.deletePeople(people.id)} className="btn btn-danger">Delete </button>
                                                 <button style = {{marginLeft:"10px"}} onClick={ () => this.viewPeople(people.id)} className="btn btn-info">View </button>
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
}

function ListPeopleComponent(props) {
    const navigate = useNavigate();
    return <ListPeopleComponentInner {...props} navigate={navigate} />;
}

export default ListPeopleComponent;
