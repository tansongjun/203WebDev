import React, { Component } from 'react';
import { Link } from 'react-router-dom';

class FooterComponent extends Component {
    render() {
        return (
            <div className="footer">
                <div className="container">
                    <div className="row">
                        <div className="col-md-6">
                            CS203 G4-T5
                        </div>
                        <div className="col-md-6 text-right">
                            © 2023 Patient Queuing App
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default FooterComponent;