import React from "react";
import AccountService from "../../services/AccountService";
import './RegistrationForm.css'

class RegistrationForm extends React.Component{

    constructor(props) {
        super(props);
        this.state = {
            firstName:'',
            lastName:'',
            email: '',
            password: '',
            firstNameDirty: false,
            lastNameDirty: false,
            emailDirty: false,
            passwordDirty: false,
            firstNameError: 'This field is required',
            lastNameError: 'This field is required',
            emailError: 'This field is required',
            passwordError: 'This field is required',
            registrationFailed: false,
            borderColorFirstName:"",
            borderColorLastName:"",
            borderColorEmail:"",
            borderColorPassword:"",
            borderColorRed: "2px solid red",
            borderColorGreen: "2px solid green"
        }

        if (AccountService.isLogged())
            this.props.history.push("/")
    }
    addNewUser = async (event) => {
        event.preventDefault();
        if(!this.state.passwordDirty || !this.state.emailDirty || !this.state.lastNameDirty || !this.state.firstNameDirty){
            this.setState({emailDirty: true, passwordDirty: true, firstNameDirty: true, lastNameDirty: true})
            this.correctEmail();
            this.correctPassword();
            this.correctLastName();
            this.correctFirstName();
        }
        else if(this.state.emailError==='' && this.state.passwordError==='' && this.state.firstNameError==='' && this.state.lastNameError===''){
            const newUser ={
                firstName: this.state.firstName,
                lastName: this.state.lastName,
                email: this.state.email,
                password: this.state.password
            };
            
            await AccountService.register(newUser)
                .then((response)=>{
                    if(response.ok)
                        window.location.replace("/login")
                    else
                        this.setState({registrationFailed: true});
                })
                .catch(()=>{
                    this.setState({registrationFailed: true});
                })
        }
    }
    blurHandler = (e) =>{
        // eslint-disable-next-line default-case
        switch (e.target.name){
            case 'firstName':
                this.setState({firstNameDirty:true})
                if(this.state.borderColorFirstName==="")
                    this.setState({borderColorFirstName:this.state.borderColorRed})
                break
            case 'lastName':
                this.setState({lastNameDirty:true})
                if(this.state.borderColorLastName==="")
                    this.setState({borderColorLastName:this.state.borderColorRed})
                break
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
            case 'firstName':
                // eslint-disable-next-line react/no-direct-mutation-state
                this.state.firstName=e.target.value;
                this.correctFirstName()
                break
            case 'lastName':
                // eslint-disable-next-line react/no-direct-mutation-state
                this.state.lastName=e.target.value;
                this.correctLastName()
                break
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
    correctFirstName = () => {
        const re =/^([A-zА-яЁё]{3,})$/
        this.setState({firstNameDirty:true})
        if ((this.state.firstName) === '')
            this.setState({firstNameError: 'This field is required', borderColorFirstName:this.state.borderColorRed})
        else if(this.state.firstName.length<3)
            this.setState({firstNameError: 'The first name cannot be shorter than 3 characters', borderColorFirstName:this.state.borderColorRed})
        else if(!re.test(String(this.state.firstName)))
            this.setState({firstNameError: 'The first name should include only letters', borderColorFirstName:this.state.borderColorRed})
        else
            this.setState({firstNameError:'', borderColorFirstName:this.state.borderColorGreen})
    };
    correctLastName = () => {
        this.setState({lastNameDirty:true})
        const re =/^([A-zА-яЁё]{3,})$/
        if ((this.state.lastName) === '') {
            this.setState({lastNameError: 'This field is required'})
            this.setState({borderColorLastName:this.state.borderColorRed})
        }
        else if(this.state.lastName.length<3) {
            this.setState({lastNameError: 'The last name cannot be shorter than 3 characters'})
            this.setState({borderColorLastName:this.state.borderColorRed})
        }
        else if(!re.test(String(this.state.lastName))) {
            this.setState({lastNameError: 'The last name should include only letters'})
            this.setState({borderColorLastName:this.state.borderColorRed})
        }
        else {
            this.setState({lastNameError: ''})
            this.setState({borderColorLastName:this.state.borderColorGreen})
        }
    };
    correctPassword = () => {
        this.setState({passwordDirty:true})
        const re =/^([a-zA-Z0-9]{8,})$/
        if ((this.state.password) === '') {
            this.setState({passwordError: 'This field is required'})
            this.setState({borderColorPassword:this.state.borderColorRed})
        }
        else if(this.state.password.length<8) {
            this.setState({passwordError: 'The password cannot be shorter than 8 characters'})
            this.setState({borderColorPassword:this.state.borderColorRed})
        }
        else if(!re.test(String(this.state.password))) {
            this.setState({passwordError: 'The password should include only letters or numbers'})
            this.setState({borderColorPassword:this.state.borderColorRed})
        }
        else {
            this.setState({passwordError: ''})
            this.setState({borderColorPassword:this.state.borderColorGreen})
        }
    };
    correctEmail=async()=>{
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
            const response = await AccountService.isEmailFree(this.state.email);
            console.log("d")
            if(response.status===200){
                this.setState({emailError: ''})
                this.setState({borderColorEmail:this.state.borderColorGreen})
            }
            else{
                this.setState({emailError: 'This email is already taken'})
                this.setState({borderColorEmail:this.state.borderColorRed})
            }
        }
    };
    closeButton = () =>{
        this.setState({registrationFailed: false});
    }

    render() {
        return(
            <div className="login_block">
                <form className="registration_form__item"  onSubmit={this.addNewUser} >
                    {(this.state.registrationFailed) && <div style={{color: 'red'}}>An unknown error occurred
                        <button onClick={this.closeButton} className="btn-close" aria-label="Close">x</button>
                    </div>}
                    <h1  id="registr_text__item" className="h3 mb-3 fw-normal">Registration</h1>
                    <div>
                        <input type="text" name="firstName" value={this.firstName} onChange={e =>this.changeHandler(e)} onBlur={e => this.blurHandler(e)} className="form-control" placeholder="First name"
                               required="" style={{border: this.state.borderColorFirstName}}/>
                        {(this.state.firstNameDirty && this.state.firstNameError) && <div className="error__item" style={{color: 'red'}}>{this.state.firstNameError}</div>}
                    </div>
                    <div>
                        <input type="text" name="lastName" value={this.lastName} onChange={e =>this.changeHandler(e)} onBlur={e => this.blurHandler(e)} className="form-control" placeholder="Last name"
                               required="" style={{border: this.state.borderColorLastName}} />
                        {(this.state.lastNameDirty && this.state.lastNameError) && <div className="error__item" style={{color: 'red'}}>{this.state.lastNameError}</div>}
                    </div>
                    <div>
                        <input onChange={e => this.changeHandler(e)} value={this.email} onBlur={e => this.blurHandler(e)} type="text" name="email" className="form-control" placeholder="Email address"
                               required="" style={{border: this.state.borderColorEmail}} />
                        {(this.state.emailDirty && this.state.emailError) && <div className="error__item" style={{color: 'red'}}>{this.state.emailError}</div>}
                    </div>
                    <div>
                        <input onChange={e => this.changeHandler(e)} value={this.password} onBlur={e => this.blurHandler(e)}  type="password" name="password" className="form-control"
                               placeholder="Password" required="" style={{border: this.state.borderColorPassword}} />
                        {(this.state.passwordDirty && this.state.passwordError) && <div className="error__item" style={{color: 'red'}}>{this.state.passwordError}</div>}
                    </div>
                    <div>
                        <button id="btn_singup__item" className="btn btn-lg btn-primary" type="submit">Sign up</button>
                    </div>
                </form>
            </div>
        );
    }
}


export default RegistrationForm;