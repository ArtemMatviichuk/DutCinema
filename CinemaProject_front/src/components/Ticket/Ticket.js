import React from "react";
import './Ticket.css'
import moment from 'moment';
import { Link } from 'react-router-dom';
import Loading from "../Loading/Loading";
import reverse from "./reverse.png";
import TicketService from '../../services/TicketService'
import ErrorComponent from '../error/ErrorComponent'
import { BsArrowUpDown } from 'react-icons/bs';
import { AiFillDelete } from "react-icons/ai";



class Ticket extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoaded: false,
            tickets: [],
            reversed: false
        };

        this.changeSort = this.changeSort.bind(this)
        this.reverse = this.reverse.bind(this)
        this.deleteMode = this.deleteMode.bind(this)
        this.removeTicket = this.removeTicket.bind(this)
    }

    componentWillReceiveProps(nextProps){
        this.setState({
            isLoaded: true,
            tickets: Object.entries(nextProps.tickets).sort((a, b) =>
                moment(a[1][0].date) >= moment(b[1][0].date) ? 1 : -1)
        })
    }

    
    changeSort(event){
        this.setState({
            isLoaded: false
        })

        let newList = {}
        switch (event.target.value){
            case "0":
                newList = this.state.tickets.sort((a, b) => 
                    moment(a[1][0].date) >= moment(b[1][0].date) ? 1 : -1
                )
                break;
            case "1":
                newList = this.state.tickets.sort((a, b) => 
                    a[1][0].movieTitle.toLowerCase() >= b[1][0].movieTitle.toLowerCase() ? 1 : -1
                )
                break;
            case "2":
                newList = this.state.tickets.sort((a, b) => 
                    a[1][0].hallId >= b[1][0].hallId ? 1 : -1
                )
            case "3":
                newList = this.state.tickets.sort((a, b) => 
                    a[1].length >= b[1].length ? 1 : -1
                )
                break;
            default: break;
        }

        if (this.state.reversed)
            newList = newList.reverse()

        this.setState({
            isLoaded: true,
            tickets: newList
        })
    }

    reverse(event){
        this.setState({
            isLoaded: true,
            tickets: this.state.tickets.reverse(),
            reversed: !this.state.reversed
        })
    }
    
    deleteMode(event){
        this.setState({
            deleteMode: !this.state.deleteMode
        })
    }

    removeTicket(event){
        TicketService.cancelBooking(event.target.value)
            .then(() => {
                let shouldFilter = false

                this.state.tickets.map(el => {
                    el[1] = el[1].filter(e => e.id != event.target.value)
                    if (el[1].length==0)
                        shouldFilter = true
                })
                if (shouldFilter)
                    this.state.tickets = this.state.tickets.filter(el => 
                        el[1].length > 0
                    )

                this.setState({})
            })
            .catch(err => {
                this.setState({
                    error: err
                })
            })
    }

    render() {
        const { tickets, deleteMode, error } = this.state
        if(!this.state.isLoaded)
            return <Loading />
        else if (error)
            return <ErrorComponent error={error} />
        else if (tickets==null || tickets.length==0 || tickets[0].length==0)
            return <div id="notickets__item">No tickets!</div>
        else

        return (
           <div className="tickets_wrap__item col-md-12">
                <div className="filters__item">
                    
                

                    <span>Sort by: </span>
                    <select className="select__item selectpicker" data-style="btn-primary" onChange={this.changeSort}>
                        <option value="0">Date</option>
                        <option value="1">Movie</option>
                        <option value="2">Hall</option>
                        <option value="3">Amount</option>
                    </select>
                    <label>
                        <input type="checkbox" className="input_reverse__item" onChange={this.reverse}/>
                        <span class="icon" id="reverse_icon__item"><BsArrowUpDown/></span>
                    </label>
                    <label>
                            <input type="checkbox" onChange={this.deleteMode}/>
                            <span class="icon" id="delete_icon__item"><AiFillDelete/></span>
                        </label>
                </div>
               {
                   tickets.map(el => {

                       return <div>
                           <div className={el[1][0].isCanceled ? "main canceled__session" : "main"}>                        
                                <img className="img_ticket__item" src={el[1][0].posterPath} alt=""/> 
                                <div className="text">
                                    {el[1][0].isCanceled && <h2 className="canceled__label">Canceled!</h2>}
                                    <Link to={'/movies/' + el[1][0].movieId}>
                                        <h3>{el[1][0].movieTitle}</h3>
                                    </Link>
                                        <p>Date: {moment(el[1][0].date).format('HH:mm DD.MM.YY')}</p>
                                        <p>Hall name: {el[1][0].hallName}</p>
                                        <p>Amount: {el[1].length}</p>
                                </div>  
                                <div className="place__item">Places: <br/><ul className="places_wrap__item"> 
                                    {
                                        el[1].map(tick => {
                                            return <li key={tick.id}>
                                                        {!deleteMode && 
                                                            <div className="place__text">
                                                                [ {tick.row} row / {tick.place} place ]
                                                            </div>
                                                        }  
                                                        {deleteMode && 
                                                            <button 
                                                                className="remove__ticket__btn" 
                                                                onClick={this.removeTicket}
                                                                value={tick.id}>
                                                                Cancel<br/>[ {tick.row} row / {tick.place} place ]
                                                            </button>
                                                        }
                                                    </li>
                                        })
                                    }</ul>
                                    </div>
                                </div>
                        </div>
                   })
               }
           </div>
        )
    }
}

export default Ticket;