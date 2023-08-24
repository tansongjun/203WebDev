import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginComponent from './components/LoginComponent';
import ListPeopleComponent from './components/ListPeopleComponent';
import HeaderComponent from './components/HeaderComponent';
import FooterComponent from './components/FooterComponent';
import CreatePeopleComponent from './components/CreatePeopleComponent';
import UpdatePeopleComponent from './components/UpdatePeopleComponent';

function App() {
    return (
        <div>
            <Router>
                <div className="container">
                    <HeaderComponent />
                    <div className="container">
                        <Routes>
                            <Route path="/" element={<LoginComponent />} />
                            <Route path="/patient" element={<ListPeopleComponent />} />
                            <Route path="/add-patient" element={<CreatePeopleComponent />} />
                            <Route path="/update-patient/:id" element={<UpdatePeopleComponent />} />
                        </Routes>
                    </div>
                    <FooterComponent />
                </div>
            </Router>
        </div>
    );
}

export default App;
