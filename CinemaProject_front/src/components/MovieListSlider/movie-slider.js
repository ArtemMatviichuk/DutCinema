import React from 'react'
import Movie from './Movie-slider-element'
import './movie-slider.css'
import { Fade } from 'react-slideshow-image';
import 'react-slideshow-image/dist/styles.css'


export default function MovieSlider({movies}){
    const movieElements = movies.map(session =>
        <li key = {session.id} className="movie-list__li"><Movie session={session}/></li>
    )
    return(
        <div className="slide-container">
            <ul className = "movies">
                <Fade>{movieElements}</Fade>
            </ul>
        </div>
    )
}