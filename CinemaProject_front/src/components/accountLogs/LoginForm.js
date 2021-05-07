import React from "react";
import AccountService from "../../services/AccountService";
import './RegistrationForm.css'

class LoginForm extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            email: '',
            password: '',
            emailDirty: false,
            passwordDirty: false,
            emailError: 'This field is required',
            passwordError: 'This field is required',
            loginFailed: false,
            borderColorEmail:"",
            borderColorPassword:"",
            borderColorRed: "2px solid red",
            borderColorGreen: "2px solid green"
        }

        if (AccountService.isLogged())
            this.props.history.push("/")
    }

    loginCheck = async (event) => {
        event.preventDefault();
        if(!this.state.passwordDirty || !this.state.emailDirty){
            this.setState({emailDirty: true, passwordDirty: true})
            this.correctEmail();
            this.correctPassword();
        }
        else if(this.state.emailError==='' && this.state.passwordError===''){
            const login ={
                email: this.state.email,
                password: this.state.password
            }
            await AccountService.login(login)
                .then(()=>{
                    window.location.replace("/")
                })
                .catch(()=>{
                    this.setState({loginFailed: true});
                })
        }

    }
    closeButton = () =>{
        this.setState({loginFailed: false});
    }
    blurHandler = (e) =>{
        // eslint-disable-next-line default-case
        switch (e.target.name){
            case 'email':
                this.setState({emailDirty:true})
                if(this.state.borderColorEmail==="")
                    this.setState({borderColorEmail:this.state.borderColorRed})
                break
            case 'password':
                this.setState({passwordDirty:true})
                if(this.state.borderColorPassword==="")
                    this.setState({borderColorPassword:this.state.borderColorRed})
                break
        }
    };
    changeHandler = (e) =>{
        // eslint-disable-next-line default-case
        switch (e.target.name){
            case 'email':
                // eslint-disable-next-line react/no-direct-mutation-state
                this.state.email=e.target.value;
                this.correctEmail()
                break
            case 'password':
                // eslint-disable-next-line react/no-direct-mutation-state
                this.state.password=e.target.value;
                this.correctPassword()
                break
        }
    };
    correctPassword = () => {
        this.setState({passwordDirty:true})
        if ((this.state.password) === '') {
            this.setState({passwordError: 'This field is required'})
            this.setState({borderColorPassword:this.state.borderColorRed})
        }
        else {
            this.setState({passwordError: ''})
            this.setState({borderColorPassword:this.state.borderColorGreen})
        }
    };
    correctEmail=()=>{
        this.setState({emailDirty:true})
        const re = /^[^@\s]+@[^@\s]+\.[^@\s]+$/;
        if(!this.state.email) {
            this.setState({emailError: 'This field is required'})
            this.setState({borderColorEmail:this.state.borderColorRed})
        }
        else if(!re.test(String(this.state.email).toLowerCase())) {
            this.setState({emailError: 'Incorrect email'})
            this.setState({borderColorEmail:this.state.borderColorRed})
        }
        else {
            this.setState({emailError: ''})
            this.setState({borderColorEmail:this.state.borderColorGreen})
        }
    };

    render() {
        return(
            <div className="login_block">
                <form className="signin_form__item" onSubmit={this.loginCheck} >
                    {(this.state.loginFailed) && <div style={{color: 'red'}}> Incorrect username or password
                        <button onClick={this.closeButton} className="btn-close" aria-label="Close">x</button>
                    </div>}
                    <h1  id="signin_text__item" className="h3 mb-3 fw-normal">Please sign in</h1>
                    <div>
                        <input onChange={e => this.changeHandler(e)} value={this.email} onBlur={e => this.blurHandler(e)} type="text" name="email" className="form-control" placeholder="Email address"
                               required="" autoFocus="" style={{border: this.state.borderColorEmail}}/>
                        {(this.state.emailDirty && this.state.emailError) && <div className="error__item" style={{color: 'red'}}>{this.state.emailError}</div>}
                    </div>
                    <div>
                        <input onChange={e => this.changeHandler(e)} value={this.password} onBlur={e => this.blurHandler(e)}  type="password" name="password" className="form-control"
                               placeholder="Password" required="" style={{border: this.state.borderColorPassword}}/>
                        {(this.state.passwordDirty && this.state.passwordError) && <div className="error__item" style={{color: 'red'}}>{this.state.passwordError}</div>}
                    </div>
                    <div>
                        <button id="btn_singin__item" className="btn btn-lg btn-primary" type="submit">Sign in</button>
                    </div>
                </form>
            </div>
        );
    }
}
export default LoginForm;