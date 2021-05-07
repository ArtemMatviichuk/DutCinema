import React from 'react'
import { Link } from 'react-router-dom';
import AccountService from '../../services/AccountService';
import './style.css'

class ErrorComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            error: props.error
        };
    }

    componentDidMount(){
        if(this.state.error?.status == 403)
            if (!AccountService.isLogged())
                window.location.replace("/login")
            else
                AccountService.logout()
    }

    render() {
        const { error } = this.state;

        if(error?.status == 403)
            return <div></div>

        let body
        if (error === undefined)
            body = <div className="error-body">
                        <div className="col-md-12 error-status">404</div>
                        <img className="error-img col-md-6" src="https://t4.ftcdn.net/jpg/02/31/63/01/360_F_231630108_LAeypi4WO6qDkgmyeSLuxRXF79pNmyky.jpg"></img>
                        <p className="error-p">It seems you are trying to reach something that does not exist.</p>
                        <p className="error-p">Please, check address</p>
                    </div>
        else
            body = <div className="error">
                        <div className="col-md-12 error-status">{error.status}</div>
                        <img className="error-img col-md-6" src="https://t4.ftcdn.net/jpg/02/31/63/01/360_F_231630108_LAeypi4WO6qDkgmyeSLuxRXF79pNmyky.jpg"></img>
                        <p className="error-message">{error.message}</p>
                    </div>
        
        return(
            <div className="error">
                {body}
                <Link to={"/"}>
                    <div className="toMain col-md-3">
                        Go to main page
                    </div>
                </Link>
            </div>
        )
    }
}

export default ErrorComponent;