import React from 'react'
import './style.css'
import ErrorComponent from '../error/ErrorComponent';
import Loading from '../Loading/Loading'
import TicketService from '../../services/TicketService';
import SessionService from '../../services/SessionService';
import { Link } from 'react-router-dom';
import moment from 'moment';
import { FaCouch } from 'react-icons/fa';

class Booking extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            places: {
                sessionId: Number,
                rowsAmount: Number,
                place: Number,
                tickets: []
            },
            id: props.match.params.sessionId,
            selectedTickets: [],
            session: {}
        };
        this.onValueChange = this.onValueChange.bind(this);
        this.book = this.book.bind(this);
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
        TicketService.getBySession(this.state.id)
            .then((result) => {
                this.setState({
                    places: result
                });
                SessionService.getSession(this.state.id)
                    .then(res => {
                        this.setState({
                            isLoaded: true,
                            session: res
                        });
                    })
                    .catch(err => {
                        this.setState({
                            isLoaded: true,
                            error: err
                        })
                    })
            })
            .catch(err => {
                this.setState({
                    isLoaded: true,
                    error: err
                })
            }
        );
    }
  
    render() {
        const { error, isLoaded, places, session } = this.state;
        const seats = [,]

        if (places)
            for (let i = places.rowsAmount - 1; i >= 0; i--){
                let item = {key: Number, values: []}
                item.key = i
                for (let j = 0; j < places.place; j++){
                    const enabled = places.tickets.findIndex(el =>{
                        return el.row == i+1 && el.place == j+1
                    }) != -1;

                    item.values.push(
                        <div key={j}>
                            <label>
                                <input className="booking_checkbox__item"type="checkbox" onChange={this.onValueChange} value={"[" + (i+1) + "," + (j+1) + "]"} disabled={enabled}/>
                                <FaCouch title={"Row: " + (i+1) + "\nPlace: " + (j+1)} className="seat"/>
                            </label>
                        </div>
                    )
                }
                seats.push(item)
            }

        if (!isLoaded) {
            return <Loading/>;
        } else if (error && error.status != 400) {
            return <ErrorComponent error={error} />
        } else {    
            let date = moment(session.date).format('HH:mm DD-MM-YYYY')           
            return (
                <div className="hallDemo">
                    <div>
                        <Link to={'/movies/' + session.movieId}>
                            <h1>{session.movieTitle}</h1>
                        </Link>
                        <p>{date.slice(0,6)}</p>
                        <p>{date.slice(6)}</p>
                    </div>
                    <div className="demo row">
                        <div className="col-md-6">
                            <FaCouch className="seat booked"/>
                            <span> - booked</span>
                        </div>
                        <div className="col-md-6">
                        <FaCouch className="seat pick"/>
                            <span> - your pick</span>
                        </div>
                    </div>
                    <form onSubmit={this.book}>
                       <div className="row justify-content-center">

                            {seats.length != 0 &&
                                seats.map(el =>{
                                    return  <div key={el.key} className="d-flex justify-content-around col-md-12">
                                                <div className="row-number text-center">{el.key + 1}</div>
                                                {el.values}
                                                <div className="row-number text-center">{el.key + 1}</div>
                                            </div>
                                })
                            }
                        </div>
                        <div className="screen">
                            <p>SCREEN</p>
                        </div>
                        {this.state.selectedTickets.length > 0 &&
                            <div>
                                <p className="alertBook">You are booking: </p>
                                {this.state.selectedTickets.map(el => {
                                    return <p className="bookPlaces">Row : {el.row} Place: {el.place}</p>
                                })}
                            </div>
                        }
                        <p className="errorMessage">{error && error.message}</p>
                        <button className="bookTicketsBtn" type="submit">
                            Book tickets
                        </button>
                    </form>
                </div>
            );
        }
    }

    book(event) { //TODO: Add real user data in request
        event.preventDefault();

        TicketService.purchaseTickets(this.state.id, this.state.selectedTickets)
            .then(result => {
                this.props.history.push('/account')
                console.log(result)
            })
            .catch(err => {
                this.setState({
                    isLoaded: true,
                    error: err
                })
            })
    }

    onValueChange(event) {
        const { selectedTickets } = this.state
        let place = JSON.parse(event.target.value)

        if (event.target.checked)
        {
            const index = selectedTickets.findIndex(el =>{
                return el.row == place[0] && el.place == place[1]
            });

            if (index == -1){
                selectedTickets.push({
                    row: place[0],
                    place: place[1]
                })
                this.setState({})
            }
        }
        else
        {
            const index = selectedTickets.findIndex(el =>{
                return el.row == place[0] && el.place == place[1]
            });
            if (index > -1) {
                selectedTickets.splice(index, 1);
                this.setState({})
            }
        }

    }
}

export default Booking;