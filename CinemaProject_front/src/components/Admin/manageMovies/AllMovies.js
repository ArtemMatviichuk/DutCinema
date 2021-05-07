import React from "react";
import ActiveMovies from "./ActiveMovies";
import BlockedMovies from "./BlockedMovies";
import BackButton from "../../backButton/BackButton";
import './ManageMovies.css'
import Loading from "../../Loading/Loading";
import AccountService from "../../../services/AccountService";

class AllMovies extends React.Component {

    constructor(props) {
        super(props);
        this.state ={
            isLoaded: false
        }
    }

    componentDidMount(){
        AccountService.isAdmin()
        .then(() =>{
            this.setState({
                isLoaded: true
            })
        })
        .catch(() => window.location.replace('/'))
    }

    render() {
        if (!this.state.isLoaded)
            return <Loading />

        return (
            <div className="user_list__item">
                <div className="lists_wrapper__item">
                    <div className="button-box row">
                        <div className="col-md-6"><BackButton backPath={() => this.props.history.goBack()} /></div>
                        <div className="col-md-6"><button onClick={()=>window.location.replace("/admin/create-movie/")} className="create-button">Create</button></div>
                    </div>
                    <fieldset className="movies_manage_wrap__item">
                        <input type="radio" name="sizeBy" value="weight" id="sizeWeight" defaultChecked/>
                        <label id="bookedlist_controller__item" className="radio_button__item" htmlFor="sizeWeight">
                            Active movies
                        </label>

                        <input type="radio" name="sizeBy" value="dimensions" id="sizeDimensions"/>
                        <label id="historylist_controller__item" className="radio_button__item"
                               htmlFor="sizeDimensions">Blocked movies</label>

                            <div className="movies_block_wrap__item" id="booked_list__item">
                                <ActiveMovies/>
                            </div>
                            <div className="movies_block_wrap__item" id="history_list__item">
                                <BlockedMovies/>
                            </div>
                    </fieldset>
                </div>
            </div>
        )
    }
}
export default AllMovies;