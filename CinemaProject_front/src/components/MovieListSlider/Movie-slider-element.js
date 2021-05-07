import React, {Component} from 'react'
import { Link } from 'react-router-dom'
import './Movie-slider-element.css'
import moment from 'moment'

class Movie extends Component{


    render() {
        
        const {session} = this.props


            return(
                <div className="each-fade movie container">
                    <Link className="floating-button" to={"/movies/" + session.movieId}>
                        <div className="row d-flex justify-content-between">
                            <div className="movie_body col-lg-6">
                                <div className="movie_title">{session.movieTitle}</div>
                                <div className="movie_info">{moment(session.date).format("HH:mm DD-MM-YYYY")}</div>
                                <div className="floating-button">Buy now</div>
                            </div>
                            <div className="col-lg-6">
                                <img className="img_frame__item" src={session.moviePoster} alt='img'></img>
                            </div>
                        </div>
                    </Link>
                </div>
            )
    }
}

export default Movie