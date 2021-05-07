import React, {Component} from 'react'
import MovieSlider from '../MovieListSlider/movie-slider'
import MovieList from '../MovieList/MovieList'
import './Home.css'
import ErrorComponent from '../error/ErrorComponent';
import { Link } from 'react-router-dom';
import Loading from '../Loading/Loading'
import SessionService from '../../services/SessionService';

 class Home extends Component{
     constructor(props){
         super(props)
         this.state = {
             isLoaded: false,
             error: null,
             sessions:[]
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
        SessionService.getActual()
            .then((result) => {
                    this.setState({
                        isLoaded: true,
                        sessions: result
                    }
                );
            })
            .catch(err => {
                this.setState({
                    isLoaded: true,
                    error: err
                })
            }
        );
    }
    render(){
        const {error, isLoaded, sessions}=this.state
        if (error) {
            return <ErrorComponent error={error} />;
        } else if (!isLoaded) {
            return <Loading/>;
        } else {
            return <div>
                    <div>
                        <MovieSlider movies ={sessions.slice(0,5)}/>
                    </div>

                    <div className="col-md-12 d-flex flex-row-reverse allMovies">
                        <Link to={"/movies"} className="text-right allMovies-text">See full list of movies &#8641;</Link>
                    </div>
                    <div className="movie_list__item">
                        <MovieList movies={sessions.slice(5)}/>
                    </div>
                </div>
        }
    }
}

export default Home