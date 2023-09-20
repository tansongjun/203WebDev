// import './App.css';
// import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
// import HeaderComponent from './components/HeaderComponent';
// import FooterComponent from './components/FooterComponent';
// import LoginComponent from './components/LoginComponent';
// import ListPeopleComponent from './components/ListPeopleComponent';
// import CreatePeopleComponent from './components/CreatePeopleComponent';
// import UpdatePeopleComponent from './components/UpdatePeopleComponent';
// import ViewPeopleComponent from './components/ViewPeopleComponent';

// function App() {
//     return (
//         <div>
//             <Router>
//                 <div className="container">
//                     <HeaderComponent />
//                     <div className="container">
//                         <Routes>
//                             <Route path="/" element={<LoginComponent />} />
//                             <Route path="/patient" element={<ListPeopleComponent />} />
//                             <Route path="/add-patient" element={<CreatePeopleComponent />} />
//                             <Route path="/update-patient/:id" element={<UpdatePeopleComponent />} />
//                             <Route path="/view-patient/:id" element={<ViewPeopleComponent />} />
//                         </Routes>
//                     </div>
//                     <FooterComponent />
//                 </div>
//             </Router>
//         </div>
//     );
// }

// export default App;

import './App.css';
import { Route, BrowserRouter as Router , Routes } from 'react-router-dom';
import { UserLogin,  WalkInLogin, StaffLogin, AppointmentLogin, AppointmentHome, 
    WalkInHome, StaffHome, ListPeopleComponent, CreatePeopleComponent, 
    UpdatePeopleComponent, ViewPeopleComponent} from './components';

function App() {
    return (
        <Router>
            <div>
            <div className='user-login-page'>
                <h1>SINGHEALTH POLYCLINIC TICKETING & BOOKING SYSTEM</h1>
            </div>
            
            <Routes>
                <Route path="/" element={<UserLogin />} />
                <Route path="/staffLogin" element={<StaffLogin />} />
                <Route path="/walkinLogin" element={<WalkInLogin />} />
                <Route path="/appointmentLogin" element={<AppointmentLogin />} />

                <Route path="/appointmentHome" element={<AppointmentHome />} />
                <Route path="/StaffHome" element={<StaffHome />} />
                <Route path="/walkinHome" element={<WalkInHome />} />

              <Route path="/patient" element={<ListPeopleComponent />} />
              <Route path="/add-patient" element={<CreatePeopleComponent />} />
              <Route path="/update-patient/:id" element={<UpdatePeopleComponent />} />
//            <Route path="/view-patient/:id" element={<ViewPeopleComponent />} />
            </Routes>
        </div>
        </Router>
    );
}

export default App;
