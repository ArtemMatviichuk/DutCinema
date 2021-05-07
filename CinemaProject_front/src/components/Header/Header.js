import React, { Component } from "react";
import './Header.css'
import {
    Navbar,
    Nav,
    Container,
    Form,
    Button
} from "react-bootstrap";
import avatar from "./user128.png";
import { Link } from "react-router-dom";
import AccountService from "../../services/AccountService";

export default class Header extends Component {
    constructor(props){
        super(props)
        this.state = {
            isAdmin: false
        }
    }

    componentDidMount(){
        AccountService.isAdmin()
            .then(()=>{ 
                this.setState({
                    isAdmin: true
                })
            })
            .catch()
    }

    render (){      
        return (
            <>
            <Navbar className="navbar-custom" collapseOnSelect /*fixed="top"*/ expand="md" variant="dark">
                <Container>
                    <Nav.Link href="/">
                        <Navbar.Brand href="/">
                            
                            <img
                            src="https://icons.iconarchive.com/icons/seanau/fresh-web/256/Popcorn-icon.png"
                            height="70"
                            className="d-inline-block align-top"
                            alt="Logo"
                            />

                        </Navbar.Brand>
                    </Nav.Link>
                    <Nav.Link href="/"><Navbar className="cinema_name__item">DutCinema</Navbar></Nav.Link>
                    {
                        this.state.isAdmin && 
                        <Link to={'/admin'} id="button_adminpage__item">Admin Page</Link>
                    }
                    <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
                    <Navbar.Collapse id="responsive-navbar-nav">
                        <Nav className="mr-auto">
                            <div className="custom-nav"> </div>
                        </Nav>
                        {!AccountService.isLogged() && (
                                <Form inline >
                                    <Button href="/registration" id="SignUp__item" variant="secondary">Sign up</Button>
                                    <Button href="/login" id="SignIn__item" variant="secondary">Sign in</Button>
                                </Form>
                        )}
                        {AccountService.isLogged() &&
                            <Navbar>
                                <Button variant="secondary" id="SignOut__item" onClick={()=>{AccountService.logout();}}>Sign Out</Button>
                                <Nav.Link href = "/account">
                                    <img
                                        src={avatar}
                                        height="50"
                                        className="d-inline-block align-top"
                                        alt="Avatar"
                                        id="user_ava_item"
                                    />
                                </Nav.Link>
                         </Navbar>}
                    </Navbar.Collapse>
                </Container>
            </Navbar>
            </>
        )
    }
}