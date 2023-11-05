import ApptDB from "../ApptDB/ApptDB";
import ListPeopleComponent from "../PatientManagement/ListPeopleComponent";
import { Link, useNavigate } from 'react-router-dom';

function StaffHome() {
  return (
    <div>
      <h1>STAFF HOME</h1>
      <ListPeopleComponent />
      <ApptDB></ApptDB>

      <Link to="/">
        <button className="payment-button">Log out</button>
      </Link>
      <br></br>
    </div>
  );
}

export default StaffHome;
