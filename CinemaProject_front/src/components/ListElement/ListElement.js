import React, {Component} from 'react'
import './ListElement.css'


class ListElement extends Component{
    
    render() {
        const {movie} = this.props
            return(
                <div>
                            <div className="post-title">{movie.title}</div>
                            <div className="post-date">{(new Date(movie.date)).toDateString()}</div>
                </div>    
            )
    }
}


export default ListElement