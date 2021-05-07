import React, {Component} from 'react'
import Ticket from '../Ticket/Ticket'
import AccountService from '../../services/AccountService'
import TicketService from '../../services/TicketService'
import './Account.css'
import moment from 'moment';
import avatar from "../Header/user128.png";

class UserPage extends Component{
    constructor(props){
        super(props)
        this.state = {
            isLoaded: false,
            error: null,
            tickets: [],
            user: {},
            sort: 0
        }
    }


    async errorHandler(response){
       if (!response.ok){
           await response.json()
                   .then(res => {
                       throw Error(res.message)
                   })
       }

       return response.json()
   }
 
   componentDidMount() {
    AccountService.getUser()
        .then(res => {
            this.setState({
                user: res
            })
            TicketService.getUsersTickets()
                .then(res=>{
                    this.setState({
                        isLoaded: true,
                        tickets: res
                    })
                })
                .catch(error => {
                    this.setState({
                        ticketsError: error
                    })
                
            })
        })
        .catch(err => {
            this.setState({
                isLoaded: true,
                error: err
            })
        })
    
    
    }
    
    render() {
        const {isLoaded, error, user} = this.state
        const bookedTickets = this.state.tickets.filter(e =>(moment(e.date) > moment()))
        const historyTickets = this.state.tickets.filter(e => moment(e.date) < moment())
        
        const groupBy = (arr, fn) =>
            arr
                .map(typeof fn === 'function' ? fn : val => val[fn])
                .reduce((acc, val, i) => {
                acc[val] = (acc[val] || []).concat(arr[i]); 
                return acc
        }, {});
        
        return(
            <div className="wrapper">
                <div className="user_info__item">
                    <img className="user_img__item" src={avatar} alt='img'/>
                    <div className="user_name__item">{user.firstName} {user.lastName}</div>
                </div>
                <div className="user_list__item"> 
                                <fieldset className="lists_wrapper__item">
                                    
                                    <input  type="radio" name="sizeBy" value="weight" id="sizeWeight" defaultChecked/>
                                    <label id="bookedlist_controller__item" className="radio_button__item" htmlFor="sizeWeight">Booked tickets</label>
                                   
                                    <input type="radio" name="sizeBy" value="dimensions" id="sizeDimensions" />
                                    <label id="historylist_controller__item"className="radio_button__item" htmlFor="sizeDimensions">History</label>
                               
                                    <div id="booked_list__item" className="container"><div className="row"><Ticket tickets={groupBy(bookedTickets,'sessionId')} /></div></div>
                                    <div id="history_list__item"className="container"><div className="row"><Ticket tickets={groupBy(historyTickets,'sessionId')} /></div></div>
                                </fieldset>
                </div>
                
            </div>
        )
    }
}

export default UserPage