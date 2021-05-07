import React from "react";
import MovieService from "../../../services/MovieService";
import './ManageMovies.css'


class ActiveMovies extends React.Component {

    constructor(props) {
        super(props);
        this.state={
            movies:{
                id: Number,
                actors: String,
                country: String,
                description: String,
                duration: Number,
                genres: {
                    id:Number,
                    name:String
                },
                posterPath: String,
                title: String,
                trailerPath:String
            }
        }
    }

    componentDidMount = async()=> {
        await MovieService.getAll()
            .then((result) => {
                this.setState({movies: result});
                console.log(result)
            });
    };

    block = async(event)=> {
        event.preventDefault();
        await MovieService.blockMovie(event.target.id).then((response)=>{
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
                    <div className="main update_block_wrap__item">
                        <img className="img_ticket__item" src={this.state.movies[i].posterPath} width="225"
                             height="275" alt=""/>
                       
                        <div className="update_block_button__item">
                            <div className="text">
                                <h3>Title: {this.state.movies[i].title}</h3>
                            
                            <button onClick={()=>window.location.replace("/admin/update-movie/"+this.state.movies[i].id)} className="update-button">Update</button>
                            <button id={this.state.movies[i].id} onClick={this.block} className="block-button">Block</button>
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
export default ActiveMovies;