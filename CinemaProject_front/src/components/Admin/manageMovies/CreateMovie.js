import React from "react";
import MovieService from "../../../services/MovieService";
import BackButton from "../../backButton/BackButton";
import YouTube from "react-youtube";
import NoPoster from "./NoPosterUrl"
import { Multiselect } from 'multiselect-react-dropdown'
import GenreService from "../../../services/GenreService";
import './CreateMovie.css'
import AccountService from "../../../services/AccountService";
import Loading from "../../Loading/Loading";

class CreateMovie extends React.Component {

    constructor(props) {
        super(props);
        this.multiselectRef = React.createRef();
        this.state={
            genres: [{
                id:Number,
                name:String
            }],
            movie:{
                actors: String,
                country: String,
                description: String,
                duration: Number,
                genres: [],
                posterPath: String,
                title: String,
                trailerPath:String
            },

            actorsError: "",
            countryError: "",
            descriptionError: "",
            durationError: "",
            posterPathError: "",
            titleError: "",
            trailerPathError:"",

            actorsBorderColor:"",
            countryBorderColor:"",
            descriptionBorderColor:"",
            durationBorderColor:"",
            posterPathBorderColor:"",
            titleBorderColor:"",
            trailerPathBorderColor:"",

            borderColorRed: "2px solid red",
            borderColorGreen: "2px solid green",
            requiredError: 'This field is required',
            test:false,
            createMovieFailed: false,
            posterPathChecked:true,
            trailerPathChecked:true,

            isLoaded: false
        }
    }
    componentDidMount = async()=> {
        AccountService.isAdmin()
        .then(() => {
            GenreService.getAll()
            .then((result) => {
                this.setState({genres: result, isLoaded: true});
            });
        })
        .catch(() => { window.location.replace('/') })
    }
    getValues() {
        return this.multiselectRef.current.getSelectedItems();
    }
    createMovie = async(event)=>{
        event.preventDefault();
        this.state.movie.genres = this.getValues();
        let test = true;
        if(this.state.trailerPathBorderColor!==this.state.borderColorGreen && this.state.trailerPathChecked) {
            this.setState({trailerPathError: 'This field is required', trailerPathBorderColor: this.state.borderColorRed});
            test=false;
        }
        if(this.state.titleBorderColor!==this.state.borderColorGreen) {
            this.setState({titleError: 'This field is required', titleBorderColor: this.state.borderColorRed});
            test=false;
        }
        if(this.state.posterPathBorderColor!==this.state.borderColorGreen && this.state.posterPathChecked) {
            this.setState({posterPathError: 'This field is required', posterPathBorderColor: this.state.borderColorRed});
            test=false;
        }
        if(this.state.durationBorderColor!==this.state.borderColorGreen) {
            this.setState({durationError: 'This field is required', durationBorderColor: this.state.borderColorRed});
            test=false;
        }
        if(this.state.descriptionBorderColor!==this.state.borderColorGreen) {
            this.setState({descriptionError: 'This field is required', descriptionBorderColor: this.state.borderColorRed});
            test=false;
        }
        if(this.state.countryBorderColor!==this.state.borderColorGreen) {
            this.setState({countryError: 'This field is required', countryBorderColor: this.state.borderColorRed});
            test=false;
        }
        if(this.state.actorsBorderColor!==this.state.borderColorGreen) {
            this.setState({actorsError: 'This field is required', actorsBorderColor: this.state.borderColorRed});
            test=false;
        }

        if(test){
            await MovieService.addMovie(this.state.movie)
                .then((response) => {
                    console.log(response)
                    if(response.ok){
                        window.location.replace("/admin/all-movies");
                    }
                    this.setState({createMovieFailed:true});
                })
                .catch((error)=>{
                    console.log(error);
                    this.setState({createMovieFailed:true});
                });
        }
    }
    changeHandler = (e) =>{
        switch (e.target.name){
            case 'actors':
                this.state.movie.actors=e.target.value;
                if(e.target.value==="")
                    this.setState({actorsError: 'This field is required', actorsBorderColor: this.state.borderColorRed})
                else
                    this.setState({actorsError: '', actorsBorderColor: this.state.borderColorGreen})
                break
            case 'country':
                this.state.movie.country=e.target.value;
                if(e.target.value==="")
                    this.setState({countryError: 'This field is required', countryBorderColor: this.state.borderColorRed})
                else
                    this.setState({countryError: '', countryBorderColor: this.state.borderColorGreen})
                break
            case 'description':
                this.state.movie.description=e.target.value;
                if(e.target.value==="")
                    this.setState({descriptionError: 'This field is required', descriptionBorderColor: this.state.borderColorRed})
                else
                    this.setState({descriptionError: '',descriptionBorderColor: this.state.borderColorGreen})
                break
            case 'duration':
                this.state.movie.duration=this.convertTime(e.target.value);
                if(e.target.value==="")
                    this.setState({durationError: 'This field is required', durationBorderColor: this.state.borderColorRed})
                else if(e.target.value==="00:00")
                    this.setState({durationError: 'The duration can`t be 00:00', durationBorderColor: this.state.borderColorRed})
                else
                    this.setState({durationError: '', durationBorderColor: this.state.borderColorGreen})
                break
            case 'posterPath':
                this.state.movie.posterPath=e.target.value;
                if(e.target.value==="")
                    this.setState({posterPathError: 'This field is required', posterPathBorderColor: this.state.borderColorRed})
                else if(!this.isValidUrl(e.target.value))
                    this.setState({posterPathError: 'This is not a path', posterPathBorderColor: this.state.borderColorRed})
                else {
                    fetch (e.target.value, {mode:'no-cors'})
                        .then((response)=>{
                            if(response.status!==404)
                                this.setState({posterPathError: '', posterPathBorderColor: this.state.borderColorGreen})
                            else
                                this.setState({posterPathError: 'This poster path not find', posterPathBorderColor: this.state.borderColorRed})
                        })
                        .catch(()=>{
                            this.setState({posterPathError: 'This poster path not find', posterPathBorderColor: this.state.borderColorRed})
                        });
                }
                break
            case 'trailerPath':
                this.state.movie.trailerPath=e.target.value;
                if(e.target.value==="")
                    this.setState({trailerPathError: 'This field is required', trailerPathBorderColor: this.state.borderColorRed})
                else {
                    fetch('http://www.youtube.com/oembed?url=http://www.youtube.com/watch?v='+e.target.value+'&format=json')
                        .then((response) => {
                            if (response.ok)
                                this.setState({
                                    trailerPathError: '',
                                    trailerPathBorderColor: this.state.borderColorGreen
                                });
                            else
                                this.setState({
                                    trailerPathError: 'This trailer path not find',
                                    trailerPathBorderColor: this.state.borderColorRed
                                })
                        })
                        .catch(() => {
                            this.setState({
                                trailerPathError: 'This trailer path not find',
                                trailerPathBorderColor: this.state.borderColorRed
                            })
                        });
                }break
            case 'title':
                this.state.movie.title=e.target.value;
                if(e.target.value==="")
                    this.setState({titleError: 'This field is required', titleBorderColor: this.state.borderColorRed})
                else
                    this.setState({titleError: '', titleBorderColor: this.state.borderColorGreen})
                break
            case 'posterPathChecked':
                if(e.target.checked) {
                    this.setState({posterPathChecked: true});
                    this.state.movie.posterPath="";
                    this.state.posterPathError="";
                    this.state.posterPathBorderColor="";
                }
                else {
                    this.setState({posterPathChecked: false});
                    this.state.movie.posterPath=NoPoster._url;
                }
                break
            case 'trailerPathChecked':
                if(e.target.checked) {
                    this.setState({trailerPathChecked: true});
                    this.state.movie.trailerPath="";
                    this.state.trailerPathError="";
                    this.state.trailerPathBorderColor="";
                }
                else {
                    this.setState({trailerPathChecked: false});
                    this.state.movie.trailerPath="";
                }
                break
        }
    };
    closeButton = () =>{
        this.setState({createMovieFailed: false});
    }
    isValidUrl(url){
        try {
            new URL(url);
            return true;
        } catch (_) {
            return false;
        }
    }
    convertTime(time){
        const h = time.substr(0,2);
        const min = time.substr(3,2);
        return (Number(h) * 360 + Number(min) * 60);
    };
    render() {
        if (!this.state.isLoaded)
            return <Loading />

        return(
            <div className="login_block row">
                <BackButton backPath={() => this.props.history.goBack()} />
                <div>
                    {(this.state.createMovieFailed) && <a style={{color: 'red'}}>An unknown error occurred
                        <button onClick={this.closeButton} className="btn-close" aria-label="Close">x</button>
                    </a>}
                </div>
                <form onSubmit={this.createMovie}>
                    <div>
                       <h3>Title:</h3>
                        <input value={this.title} onChange={e => this.changeHandler(e) }
                               type="text" name="title" className="form-control col-md-12"
                               style={{border: this.state.titleBorderColor}} onBlur={e => this.changeHandler(e)} />
                        <div className="create_movie_error__item" style={{color: 'red'}}>{this.state.titleError}</div>
                    </div>
                    <div>
                        <h3>Description:</h3>
                        <textarea className="col-md-12" value={this.description} onBlur={e => this.changeHandler(e)}
                                  onChange={e => this.changeHandler(e)} name="description"
                                  style={{border: this.state.descriptionBorderColor, width:'550px', height:'150px'}} />
                        <div className="create_movie_error__item" style={{color: 'red'}}>{this.state.descriptionError}</div>
                    </div>
                    <div>
                        <h3>Duration:</h3>
                        <input value={this.duration} onChange={e => this.changeHandler(e)} onBlur={e => this.changeHandler(e)}
                               type="time" name="duration" className="form-control col-md-12"
                               style={{border: this.state.durationBorderColor}} />
                        <div className="create_movie_error__item" style={{color: 'red'}}>{this.state.durationError}</div>
                    </div>
                    <div >
                        <h3>Genres:</h3>
                        <Multiselect ref={this.multiselectRef}
                            options={this.state.genres} displayValue="name" />
                    </div>
                    <div>
                        <h3>Actors:</h3>
                        <input value={this.actors} onChange={e => this.changeHandler(e)} onBlur={e => this.changeHandler(e)}
                               type="text" name="actors" className="form-control col-md-12"
                               style={{border: this.state.actorsBorderColor}} />
                        <div className="create_movie_error__item" style={{color: 'red'}}>{this.state.actorsError}</div>
                    </div>
                    <div>
                        <h3>Country:</h3>
                        <input value={this.country} onChange={e => this.changeHandler(e)} onBlur={e => this.changeHandler(e)}
                               type="text" name="country" className="form-control col-md-12"
                               style={{border: this.state.countryBorderColor}} />
                        <div className="create_movie_error__item"style={{color: 'red'}}>{this.state.countryError}</div>
                    </div>
                    <div>
                        <h3>Poster path:</h3>
                        <input type="checkbox" onChange={e => this.changeHandler(e)} defaultChecked="checked" name="posterPathChecked" />
                        {(this.state.posterPathChecked) && <div >
                            <input value={this.posterPath} onChange={e => this.changeHandler(e)} onBlur={e => this.changeHandler(e)}
                                   type="text" name="posterPath" className="form-control col-md-12"
                                   style={{border: this.state.posterPathBorderColor}} />
                            <div className="create_movie_error__item" style={{color: 'red'}}>{this.state.posterPathError}</div>
                            <div className="movie_poster col-md-12">
                                <img className="img_poster" alt="" src={this.state.movie.posterPath} />
                            </div>
                        </div>}


                    </div>
                    <div>
                        <h3>Trailer path id:</h3>
                        <input  className="create_movie_input__item" type="checkbox" onChange={e => this.changeHandler(e)} defaultChecked="checked" name="trailerPathChecked" />
                        {(this.state.trailerPathChecked) && <div >
                            <input value={this.trailerPath} onChange={e => this.changeHandler(e)} onBlur={e => this.changeHandler(e)}
                                   type="text" name="trailerPath" className="form-control col-md-12"
                                   style={{border: this.state.trailerPathBorderColor}} />
                            <div className="create_movie_error__item" style={{color: 'red'}}>{this.state.trailerPathError}</div>
                            <YouTube videoId={this.state.movie.trailerPath}  />
                        </div>}
                    </div>
                    <div>
                        <button id="btn_create__item" className="w-60 btn btn-lg btn-primary" type="submit">Create</button>
                    </div>
                </form>
            </div>
        );
    }
}
export default CreateMovie;