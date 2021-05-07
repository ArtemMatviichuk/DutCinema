import React, {Component} from 'react'
import './MovieList-element.css'
import { Link } from 'react-router-dom';
import moment from 'moment';


class Movie extends Component{
    
    render() {
        const {movie} = this.props
            return(
                <div className="movie-element__item post-wrap">
                    <div className="post-item">
                        <div className="post-item-wrap">
                            <Link to={"/movies/" + movie.movieId} className="post-link">
                                <div className="image-wrapper">
                                    <img src={movie.moviePoster} alt='img'></img>
                                </div>
                                <div className="text-wrapper">
                                    <div className="post-title">{movie.movieTitle}</div>
                                    <h3 className="post-date">{moment(movie.date).format("HH:mm DD-MM-YYYY")}</h3>
                                </div>
                                <div className="overlay"></div>
                            </Link>
                        </div>
                    </div>
                        
                </div>
            )
    }
}


export default Movie