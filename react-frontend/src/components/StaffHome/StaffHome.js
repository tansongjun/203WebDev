import ApptDB from "../ApptDB/ApptDB";
import ListPeopleComponent from "../PatientManagement/ListPeopleComponent";

function StaffHome() {
    return (
        <div>
            <h1>STAFF HOME</h1>
            <ListPeopleComponent/>
            <ApptDB></ApptDB>
        </div>
        
    );
}

export default StaffHome;
