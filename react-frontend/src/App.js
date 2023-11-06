import './App.css';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import {
    HomePage, WalkInLogin, StaffLogin, AppointmentLogin, AppointmentHome,
    WalkInHome, StaffHome, ListPeopleComponent, CreatePeopleComponent,
    UpdatePeopleComponent, ViewPeopleComponent, Questionnaire, Payment, 
    Registration, PaymentQRPage, Qticketpage, HeaderTv, OnlineWebsite, OnlineWebsiteLogin
} from './components';
import ApptDB from './components/ApptDB/ApptDB';

function App() {
    localStorage.setItem('url', 'http://healthcareapismu.ghcba8bbfgfyg2bd.southeastasia.azurecontainer.io:8080/api/v1');
    return (
        <Router>
            <div>
                <div className='user-login-page'>
                    {/* <h1><center> SINGHEALTH POLYCLINIC TICKETING & BOOKING SYSTEM</center></h1>
                    <HeaderTv/> */}
                    <div className="header-container">
                        
                        
                        <h1 style={{flex: 5 }} ><center> SINGHEALTH POLYCLINIC TICKETING & BOOKING SYSTEM</center></h1>
                        <div style={{flex: 1 }} >
                            <HeaderTv />
                        </div>
                        
                    </div>
                </div>

                <Routes>
                
                /* Buttons */
                    <Route path="/" element={<HomePage />} />

                /* Login Pages */
                    <Route path="/staffLogin" element={<StaffLogin />} />
                    <Route path="/walkinLogin" element={<WalkInLogin />} />
                    <Route path="/OnlineWebsite" element={<OnlineWebsite />} />
                    <Route path="/OnlineWebsiteLogin" element={<OnlineWebsiteLogin />} />


                /* Home Pages */
                    <Route path="/appointmentHome" element={<AppointmentHome />} />
                    <Route path="/StaffHome" element={<StaffHome />} />
                    <Route path="/walkinHome" element={<WalkInHome />} />

                /* Patient Management Page */
                    <Route path="/patient" element={<ListPeopleComponent />} />
                    <Route path="/add-patient" element={<CreatePeopleComponent />} />
                    <Route path="/update-patient/:id" element={<UpdatePeopleComponent />} />
                    <Route path="/view-patient/:id" element={<ViewPeopleComponent />} />

                /* Appointment Management Page */
                    <Route path="/Appointments" element={<ApptDB/>} />
                    <Route path="/add-appointment" element={<CreatePeopleComponent />} />
                    <Route path="/update-appointment/:id" element={<UpdatePeopleComponent />} />
                    <Route path="/view-appointment/:id" element={<ViewPeopleComponent />} />              
                    

                    <Route path="/walkinHome/questionnaire" element={<Questionnaire/>} />
                    <Route path="/walkinHome/qticketpage" element={<Qticketpage/>} />

                    <Route path="/walkinHome/payment" element={<Payment/>} />
                    <Route path="/walkinHome/payment/paymentQR" element={<PaymentQRPage/>} />
                    <Route path="/registration" element={<Registration/>} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
