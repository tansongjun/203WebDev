import React, { useState, useEffect } from "react";
import PeopleService from "../../services/PeopleService";
import { useNavigate, useParams } from "react-router-dom";

function UpdatePeopleComponent() {
  const navigate = useNavigate();
  const conditionOptions = ["NONE", "MODERATE", "MILD", "SEVERE"];
  const { id } = useParams();

  const [state, setState] = useState({
    firstName: "",
    lastName: "",
    emailId: "",
    birthDate: "",
    condition: "",
    nric: "",
    username: "",
    password: "",
    authorities: "",
  });
  const [auth, setAuth] = React.useState({
    user: sessionStorage.getItem("user"),
    pwd: sessionStorage.getItem("pwd"),
    personId: sessionStorage.getItem("person_id"),
  });

  useEffect(() => {
    // Fetch the data for the patient using the given ID
    PeopleService.getPeopleById(id, auth).then((res) => {
      const data = res.data;
      setState({
        firstName: data.firstName,
        lastName: data.lastName,
        emailId: data.emailId,
        birthDate: data.birthDate,
        condition: data.condition,
        nric: data.nric,
      });
    });
  }, [id]);

  const updatePeople = (e) => {
    e.preventDefault();
    let people = {
      firstName: state.firstName,
      lastName: state.lastName,
      emailId: state.emailId,
      birthDate: state.birthDate,
      condition: state.condition,
      nric: state.nric,
      username: "Somefield",
      password: "SomeField",
      authorities: "ROLE_PATIENT",
    };

    PeopleService.updatePeople(people, id, auth)
      .then((res) => {
        if (res.status === 200) {
          navigate("/patient");
        }
      })
      .catch((err) => {
        console.log(err.response.data);
        alert(err.response.data.message);
        console.log(err.response.data.errors);
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
                        firstName: e.target.value,
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
                        lastName: e.target.value,
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
                        emailId: e.target.value,
                      })
                    }
                  />
                </div>
                <div className="form-group">
                  <label> Birthday: </label>
                  <input
                    placeholder="yyyy-mm-dd"
                    name="birthDate"
                    className="form-control"
                    value={state.birthDate}
                    onChange={(e) =>
                      setState({
                        ...state,
                        birthDate: e.target.value,
                      })
                    }
                  />
                </div>
                <div className="form-group">
                  <label> Condition: </label>
                  <select
                    name="condition"
                    className="form-control"
                    value={state.condition}
                    onChange={(e) =>
                      setState({
                        ...state,
                        condition: e.target.value,
                      })
                    }
                  >
                    <option value="">Select a condition</option>
                    {conditionOptions.map((option) => (
                      <option key={option} value={option}>
                        {option}
                      </option>
                    ))}
                  </select>
                </div>

                <button className="btn btn-success" onClick={updatePeople}>
                  Save
                </button>
                <button
                  className="btn btn-danger"
                  onClick={() => navigate("/patient")}
                  style={{ marginLeft: "10px" }}
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
