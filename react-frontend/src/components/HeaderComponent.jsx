import React, { Component } from 'react';
import { Link } from 'react-router-dom';

class HeaderComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {

        }
    }

    render() {
        return (
            <div>
                <header>
                    <nav className="navbar navbar-expand-md navbar-dark bg-dark">
                        <div>
                            <h1>HELLLLLLOOOOOO</h1>
                            {/*SONG JUN PLS CHANGE LINK TO USER SELECTION PAGE*/}
                            <Link to="/patient" className="navbar-brand">Patient Queuing App</Link>
                        </div>
                    </nav>
                </header>
            </div>
        );
    }
}

export default HeaderComponent;
