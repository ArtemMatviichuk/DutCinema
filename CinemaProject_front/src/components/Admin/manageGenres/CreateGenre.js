import React from "react";
import GenreService from "../../../services/GenreService";
import './ManageGenres.css'
import {AiFillCloseCircle} from 'react-icons/ai'
import AccountService from "../../../services/AccountService";
import Loading from "../../Loading/Loading";

class CreateGenre extends React.Component {

    constructor(props) {
        super(props);
        this.state={
            newGenre:{
                name:""
            },
            nameError:"",
            borderColorName:"",
            createFailed: false,
            requiredError: 'This field is required',
            borderColorRed: "2px solid red",
            borderColorGreen: "2px solid green",
            isLoaded: false

        }
    }

    componentDidMount(){
        AccountService.isAdmin()
            .then(() => {
                this.setState({
                    isLoaded: true
                })
            })
            .catch(() => { window.location.replace('/') })
    }

    addGenre = async(event)=> {
        event.preventDefault();
        if (this.state.borderColorName === this.state.borderColorGreen) {
            console.log(this.state.newGenre)
            console.log(1)
            await GenreService.addGenre(this.state.newGenre)
                .then((response) => {
                    if (response.ok)
                        window.location.replace("/admin/genres");
                    else
                        this.setState({createFailed: true});
                })
                .catch((error) => {
                    console.log(error);
                    this.setState({createFailed: true});
                })
        } else if (this.state.newGenre.name === "")
            this.setState({nameError: this.state.requiredError, borderColorName: this.state.borderColorRed});
    };
    changeHandler = async(e) =>{
        this.setState({newGenre: {name: e.target.value}});
        const free = await GenreService.isNameFree(e.target.value);
        if(e.target.value==="")
            this.setState({nameError: this.state.requiredError, borderColorName: this.state.borderColorRed});
        else if(!free)
            this.setState({nameError: "This name is already used", borderColorName: this.state.borderColorRed});
        else
            this.setState({nameError: "", borderColorName: this.state.borderColorGreen});
    }
    closeButton = () =>{
        this.setState({createFailed: false});
    }

    render() {
        if (!this.state.isLoaded)
            return <Loading />

        return (
            <div>
                <form onSubmit={this.addGenre} className="create-genre">
                    {(this.state.createFailed) &&  <div className="error_window__item">
                        <a style={{color: 'red'}}>An unknown error occurred
                            < AiFillCloseCircle onClick={this.closeButton} className="btn_close__item"/>
                        </a>
                    </div>}
                    <div className="genre-name row">
                        <input onChange={e => this.changeHandler(e)} value={this.name} type="text" name="name" className="form-control__item" placeholder="New genre"
                               style={{border: this.state.borderColorName}}/>
                        <div id="error-text__item" style={{color: 'red', position: 'absolute', top:'120px'}}>{this.state.nameError}</div>
                        <button id="btn_add__item" type="submit">Add</button>
                    </div>
                </form>
            </div>
        )
    }
}
export default CreateGenre;