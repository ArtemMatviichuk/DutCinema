import React from "react";
import MovieService from "../../../services/MovieService";
import './ManageMovies.css'

class BlockedMovies extends React.Component {

    constructor(props) {
        super(props);
        this.state={
            movies:{
                id: Number,
                actors: String,
                country: String,
                description: String,
                duration: Number,
                genres: String,
                posterPath: String,
                title: String,
                trailerPath:String
            }
        }
    }

    componentDidMount = async()=> {
        await MovieService.getBlocked()
            .then((result) => {
                this.setState({movies: result});
            });
    };

    block = async(event)=> {
        event.preventDefault();
        await MovieService.unblockMovie(event.target.id).then((response)=>{
            if(response.ok){
                this.componentDidMount();
            }
        });
    };

    render() {
        const movies = [];
        if(this.state.movies.length > 0) {
            let item = {values: []}
            for (let i = this.state.movies.length-1; i >= 0; i--) {
                item.values.push(
                    <div className="main">
                        <img className="img_ticket__item" src={this.state.movies[i].posterPath} width="225"
                             height="275" alt=""/>
                        
                        <div className="update_block_button__item">
                            <div className="text">
                                <h3>Title: {this.state.movies[i].title}</h3>
                                <button onClick={()=>window.location.replace("/admin/update-movie/"+this.state.movies[i].id)} className="update-button">Update</button>
                                <button id={this.state.movies[i].id} onClick={this.block} className="block-button">Unblock</button>
                            </div>
                        </div>
                    </div>
                )
            }
            movies.push(item)
        }
        return (
            <div>
                {
                    movies.map(el =>{
                        return el.values
                    })}
            </div>
        )
    }
}
export default BlockedMovies;